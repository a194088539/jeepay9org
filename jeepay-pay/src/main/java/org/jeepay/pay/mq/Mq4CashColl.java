package org.jeepay.pay.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.activemq.ScheduledMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.core.entity.PayOrderCashCollRecord;
import org.jeepay.core.entity.PayPassageAccount;
import org.jeepay.pay.channel.CashCollInterface;
import org.jeepay.pay.service.RpcCommonService;
import org.jeepay.pay.util.SpringUtil;

import javax.jms.*;

@Component
public class Mq4CashColl {

    @Autowired
    private Queue cashCollQueue;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(Mq4CashColl.class);

    public void send(String msg) {
        _log.info("发送MQ消息:msg={}", msg);
        this.jmsTemplate.convertAndSend(this.cashCollQueue, msg);
    }

    /**
     * 发送延迟消息
     * @param msg
     * @param delay
     */
    public void send(String msg, long delay) {
        _log.info("发送MQ延时消息:msg={},delay={}", msg, delay);
        jmsTemplate.send(this.cashCollQueue, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                TextMessage tm = session.createTextMessage(msg);
                tm.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay);
                tm.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, 1*1000);
                tm.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, 1);
                return tm;
            }
        });
    }

    @JmsListener(destination = MqConfig.CASH_COLL_QUEUE_NAME)
    public void receive(String msg) {
        _log.info("处理资金归集任务.msg={}", msg);
        JSONObject msgObj = JSON.parseObject(msg);
        String payOrderId = msgObj.getString("payOrderId");

        PayOrderCashCollRecord selectCondition = new PayOrderCashCollRecord();
        selectCondition.setPayOrderId(payOrderId);
        int row = rpcCommonService.rpcPayOrderCashCollRecordService.count(selectCondition);
        if( row > 0 ){
            _log.info("处理资金归集任务已处理，本次结束.msg={}", msg);
            return ;
        }

        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(payOrderId);
        String channelId = payOrder.getChannelId();
        if(!"alipay_qr_pc".equals(channelId) && !"alipay_qr_h5".equals(channelId)){
            _log.info("当前订单不属于支付宝当面付产品订单，本次结束.msg={}", msg);
            return ;
        }

        CashCollInterface cashCollInterface = (CashCollInterface) SpringUtil.getBean("alipayCashCollService");

        JSONObject retObj = cashCollInterface.coll(payOrder);
        _log.info("资金归集渠道返回结果：" + retObj);

        String result = retObj.getString("result");
        String retMsg = retObj.getString("msg");
        boolean isSuccess = "success".equals(result);

        JSONArray records = retObj.getJSONArray("records");
        if(records != null){
            for(Object item: records){
                PayOrderCashCollRecord record = JSON.toJavaObject(((JSONObject)item), PayOrderCashCollRecord.class);
                record.setStatus(isSuccess ? MchConstant.PUB_YES : MchConstant.PUB_NO);
                record.setRemark(retMsg);
                rpcCommonService.rpcPayOrderCashCollRecordService.add(record);
            }
        }

        //是否关闭账号
        Integer closeAccount = retObj.getInteger("closeAccount");
        if(closeAccount != null){
            _log.error("cashCollCloseAccount is true , 关闭通道子账户信息 accountId = {}" , closeAccount);
            PayPassageAccount account = new PayPassageAccount();
            account.setId(closeAccount);
            account.setStatus(MchConstant.PUB_NO);
            rpcCommonService.rpcPayPassageAccountService.update(account);
        }

    }
}
