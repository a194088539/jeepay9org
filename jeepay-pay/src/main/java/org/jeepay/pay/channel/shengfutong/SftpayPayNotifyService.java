package org.jeepay.pay.channel.shengfutong;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.jeepay.common.util.Util;
import org.jeepay.common.util.sign.SignUtils;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.BasePayNotify;

/**
 * @Package org.jeepay.pay.channel.shengfutong
 * @Class: SftpayPayNotifyService.java
 * @Description:
 * @Author leo
 * @Date 2019/4/8 15:01
 * @Version
 **/
@Component
public class SftpayPayNotifyService extends BasePayNotify {
    private final static String logPrefix = "【盛付通支付回调】";

    @Override
    public String getChannelName() {
        return SftpayConfig.CHANNEL_NAME;
    }


    @Override
    public JSONObject doNotify(Object notifyData) {
        _log.info("====== 开始处理sftpay支付回调通知 ======");
        JSONObject bizContext = getRequestParameters(notifyData);
        _log.info("{}盛付通回调请求响应参数：{}", logPrefix, bizContext.toJSONString());
        JSONObject retObj = buildFailRetObj();
        try {
            if (!verifyPayParams(bizContext)) {
                retObj.put(PayConstant.RESPONSE_RESULT, SftpayConfig.RESPONSE_RESULT_FAIL);
                return retObj;
            }
            PayOrder payOrder = (PayOrder) bizContext.get("payOrder");
            // 处理订单
            byte payStatus = payOrder.getStatus(); // 0：订单生成，1：支付中，-1：支付失败，2：支付成功，3：业务处理完成，-2：订单过期
            if (payStatus != PayConstant.PAY_STATUS_SUCCESS && payStatus != PayConstant.PAY_STATUS_COMPLETE) {
                int updatePayOrderRows = rpcCommonService.rpcPayOrderService.updateStatus4Success(payOrder.getPayOrderId());
                if (updatePayOrderRows != 1) {
                    _log.error("{}更新支付状态失败,将payOrderId={},更新payStatus={}失败", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
                    retObj.put(PayConstant.RESPONSE_RESULT, "处理订单失败");
                    return retObj;
                }
                _log.error("{}更新支付状态成功,将payOrderId={},更新payStatus={}成功", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
                payOrder.setStatus(PayConstant.PAY_STATUS_SUCCESS);
            }
            // 业务系统后端通知
            baseNotify4MchPay.doNotify(payOrder, true);
            _log.info("====== 完成处理sftpay支付回调通知 ======");
            retObj.put(PayConstant.RESPONSE_RESULT, SftpayConfig.RESPONSE_RESULT_OK);
        } catch (Exception e) {
            _log.error(e, logPrefix + "处理异常");
        }
        return retObj;
    }

    public boolean verifyPayParams(JSONObject payContext) {
        // 查询payOrder记录
        String payOrderId = payContext.getString("orderid");
        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(payOrderId);
        if (payOrder == null) {
            _log.error("Can't found payOrder form db. payOrderId={}, ", payOrderId);
            payContext.put("retMsg", "Can't found payOrder");
            return false;
        }
        if(!payContext.getString("returncode").equals(SftpayConfig.RETURN_VALUE_SUCCESS)){
            _log.error("订单{}交易失败，回调内容", payOrderId,payContext.toJSONString());
            payContext.put("retMsg", "订单{}交易失败");
            return false;
        }
        SftpayConfig sftpayConfig = new SftpayConfig(getPayParam(payOrder));

        String signTxt = SignUtils.parameterText(payContext);
        String signValue = SignUtils.MD5.createSign(signTxt, "&key="+sftpayConfig.getPrivateKey(), SignUtils.CHARSET_UTF8);

        signValue = signValue.toUpperCase();

        if(!payContext.getString("sign").equals(signValue)) {
            _log.error("验证签名失败. payOrderId={}, ", payOrderId);
            payContext.put("retMsg", "验证签名失败");
            return false;
        }

        // 核对金额
        long outPayAmt = Util.conversionCentAmount(payContext.getBigDecimal("amount"));
        long dbPayAmt = payOrder.getAmount().longValue();
        if (dbPayAmt != outPayAmt) {
            _log.error("金额不一致. outPayAmt={},payOrderId={}", outPayAmt, payOrderId);
            payContext.put("retMsg", "金额不一致");
            return false;
        }
        payContext.put("payOrder", payOrder);
        return true;
    }

}
