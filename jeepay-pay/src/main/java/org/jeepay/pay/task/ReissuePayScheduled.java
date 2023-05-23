package org.jeepay.pay.task;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.DateUtils;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.common.util.JEEPayUtil;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.PaymentInterface;
import org.jeepay.pay.mq.BaseNotify4CashColl;
import org.jeepay.pay.mq.BaseNotify4MchPay;
import org.jeepay.pay.service.RpcCommonService;
import org.jeepay.pay.util.SpringUtil;

import java.util.Date;
import java.util.List;

/**
 * @author: aragom
 * @date: 18/11/05
 * @description: 支付补单任务
 */
@Component
public class ReissuePayScheduled extends ReissuceBase {

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(ReissuePayScheduled.class);

    @Autowired
    public BaseNotify4MchPay baseNotify4MchPay;

    @Autowired
    public BaseNotify4CashColl baseNotify4CashColl;

    /**
     * 支付订单批量补单任务
     */
    @Scheduled(cron="0 0/1 * * * ?") //每分钟执行一次
    public void payReissueTask() {
        String logPrefix = "【支付补单】";
        // 支付补单开关
        if(!isExcuteReissueTask(reissuePayTaskSwitch, reissuePayTaskIp)) {
            _log.info("{}当前机器不执行支付补单任务", logPrefix);
            return;
        }
        int pageIndex = 1;
        int limit = 100;
        PayOrder queryPayOrder = new PayOrder();
        queryPayOrder.setStatus(PayConstant.PAY_STATUS_PAYING);   // 支付中
        // 查询需要处理的订单
        Date createTimeStart = new Date(System.currentTimeMillis() - 2 * 60 * 60 * 1000);  // 2小时
        Date createTimeEnd = new Date(System.currentTimeMillis() - 1 * 60 * 1000);          // 1分钟
        boolean flag = true;
        _log.info("{}开始查询需要处理的支付订单,查询订单{}<=创建时间<={}", logPrefix, DateUtils.getTimeStr(createTimeStart, "yyyy-MM-dd HH:mm:ss"), DateUtils.getTimeStr(createTimeEnd, "yyyy-MM-dd HH:mm:ss"));
        // 循环查询所有
        while (flag) {
            List<PayOrder> payOrderList = rpcCommonService.rpcPayOrderService.select((pageIndex - 1) * limit, limit, queryPayOrder, createTimeStart, createTimeEnd);
            _log.info("{}查询需要处理的支付订单,共:{}条", logPrefix, payOrderList == null ? 0 : payOrderList.size());
            for(PayOrder payOrder : payOrderList) {
                long startTime = System.currentTimeMillis();
                String payOrderId = payOrder.getPayOrderId();
                _log.info("{}开始处理payOrderId={}", logPrefix, payOrderId);
                // 渠道类型
                String channelType = payOrder.getChannelType();
                JSONObject retObj;
                try{
                    PaymentInterface paymentInterface = (PaymentInterface) SpringUtil.getBean(channelType + "PaymentService");
                    retObj = paymentInterface.query(payOrder);
                }catch (BeansException e) {
                    _log.warn("{}不支持的支付渠道,停止处理.payOrderId={},channelType={}", logPrefix, payOrderId, channelType);
                    continue;
                }catch (Exception e) {
                    _log.warn("{}查询上游订单异常", logPrefix, payOrderId);
                    _log.error(e, "");
                    continue;
                }
                if(retObj == null) {
                    _log.warn("{}查询上游返回空,停止处理.payOrderId={},channelType={}", logPrefix, payOrderId, channelType);
                    continue;
                }
                // 查询成功
                if(JEEPayUtil.isSuccess(retObj)) {
                    // 1-支付中 2-成功 3-失败
                    int status = retObj.getInteger("status");
                    if(status == 2) {
                        String channelOrderNo = retObj.getString("channelOrderNo");
                        int updatePayOrderRows = rpcCommonService.rpcPayOrderService.updateStatus4Success(payOrderId, channelOrderNo);
                        _log.info("{}更新支付订单状态为成功({}),payOrderId={},返回结果:{}", logPrefix, PayConstant.PAY_STATUS_SUCCESS, payOrderId, updatePayOrderRows);
                        if (updatePayOrderRows == 1) {
                            payOrder.setStatus(PayConstant.TRANS_STATUS_SUCCESS);
                            // 通知业务系统
                            payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(payOrderId);
                            baseNotify4MchPay.doNotifys(payOrder, true);

                            //资金归集
                            if("alipay_qr_pc".equals(payOrder.getChannelId()) || "alipay_qr_h5".equals(payOrder.getChannelId())){
                                baseNotify4CashColl.doNotify(payOrderId);
                            }
                        }
                    }
                }
                _log.info("{}处理完毕payOrderId={}.耗时:{} ms", logPrefix, payOrderId, System.currentTimeMillis() - startTime);
            }
            pageIndex++;
            if(CollectionUtils.isEmpty(payOrderList) || payOrderList.size() < limit) {
                flag = false;
            }
        }
        _log.info("{}本次查询处理完成,", logPrefix);
    }

}
