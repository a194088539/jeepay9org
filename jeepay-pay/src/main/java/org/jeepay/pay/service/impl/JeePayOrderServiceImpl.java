package org.jeepay.pay.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.common.util.JEEPayUtil;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.core.service.IJeePayOrderService;
import org.jeepay.pay.channel.PaymentInterface;
import org.jeepay.pay.mq.BaseNotify4MchPay;
import org.jeepay.pay.service.RpcCommonService;
import org.jeepay.pay.util.SpringUtil;

/**
 * @Package org.jeepay.pay.service.impl
 * @Class: JeePayOrderServiceImpl.java
 * @Description:
 * @Author leo
 * @Date 2019/4/12 17:49
 * @Version
 **/
@Service(version = "1.0.0")
public class JeePayOrderServiceImpl implements IJeePayOrderService {

    private final MyLog _log = MyLog.getLog(JeePayOrderServiceImpl.class);

    @Autowired
    public BaseNotify4MchPay baseNotify4MchPay;

    @Autowired
    private RpcCommonService rpcCommonService;

    @Override
    public JSONObject channelOrderQuery(PayOrder payOrder) {
        JSONObject retObj = null;
        if(payOrder == null) return retObj;
        String channelType = payOrder.getChannelType();
        try {
            PaymentInterface payInterface = (PaymentInterface) SpringUtil.getBean(channelType + "PaymentService");
            retObj = payInterface.query(payOrder);

            // 订单为成功
            // 渠道查询接口返回的状态为2表示订单支付成功  1-支付中 2-成功 3-失败
            if(JEEPayUtil.isSuccess(retObj) && "2".equals(retObj.get("status"))) {
                String channelOrderNo = retObj.getString("channelOrderNo");
                int updatePayOrderRows = rpcCommonService.rpcPayOrderService.updateStatus4Success(payOrder.getPayOrderId(), channelOrderNo, retObj.getString("channelAttach"));
                _log.error("将payOrderId={}订单状态更新为支付成功,result={}", payOrder.getPayOrderId(), updatePayOrderRows);
                if (updatePayOrderRows == 1) {
                    // 通知业务系统
                    baseNotify4MchPay.doNotifys(payOrder, true);
                    return retObj;
                }
            }
        } catch (BeansException e) {
            _log.error(e, "不支持的订单查询渠道.channelType={}", channelType);
        }

        return retObj;
    }
}
