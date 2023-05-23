package org.jeepay.pay.channel.arya;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.jeepay.common.util.Util;
import org.jeepay.common.util.sign.SignUtils;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.BasePayNotify;

@Service
public class AryaPayNotifyService extends BasePayNotify {
    private final static String logPrefix = "【ARYA云回调】";
    @Override
    public String getChannelName() {
        return AryaConfig.CHANNEL_NAME;
    }
    @Override
    public JSONObject doNotify(Object notifyData) {
        _log.info("====== 开始处理ARYA云支付回调通知 ======");
        JSONObject bizContext = getRequestParameters(notifyData);
        _log.info("{}回调请求响应参数：{}", logPrefix, bizContext.toJSONString());
        JSONObject retObj = buildFailRetObj();
        try {

            if (!verifyPayParams(bizContext,bizContext.getString("sign"))) {
                retObj.put(PayConstant.RESPONSE_RESULT, "fail");
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
            _log.info("====== 完成处理ARYA云支付回调通知 ======");
            retObj.put(PayConstant.RESPONSE_RESULT, "ok");
        } catch (Exception e) {
            _log.error(e, logPrefix + "处理异常");
        }
        return retObj;
    }

    public boolean verifyPayParams(JSONObject payContext,String backSign) {
        // 查询payOrder记录
        String payOrderId = payContext.getString("ordernumber");
        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(payOrderId);
        if (payOrder == null) {
            _log.error("Can't found payOrder form db. payOrderid={}, ", payOrderId);
            payContext.put("retMsg", "Can't found payOrder");
            return false;
        }
        if(!payContext.getString("orderstatus").equals("1")){
            _log.error("订单{}交易失败，回调内容", payOrderId,payContext.toJSONString());
            payContext.put("retMsg", "订单{}交易失败");
            return false;
        }
        AryaConfig config = new AryaConfig(getPayParam(payOrder));

        String signValue = getSign(payContext,config.getPrivateKey());

        if(!backSign.equals(signValue)) {
            _log.error("验证签名失败. payOrderId={}, ", payOrderId);
            payContext.put("retMsg", "验证签名失败");
            return false;
        }

        // 核对金额
        long outPayAmt = Util.conversionCentAmount(payContext.getBigDecimal("paymoney"));
        long dbPayAmt = payOrder.getAmount().longValue();
        if (dbPayAmt != outPayAmt) {
            _log.error("金额不一致. outPayAmt={},payOrderId={}", outPayAmt, payOrderId);
            payContext.put("retMsg", "金额不一致");
            return false;
        }
        payContext.put("payOrder", payOrder);
        return true;
    }
    /**
     * 签名
     * @param parameters
     * @param key
     * @return
     */
    private String getSign(JSONObject parameters,String key) {
        String signTxt = "partner="+parameters.getString("partner")//SignUtils.parameterText(parameters);
                         +"&ordernumber="+parameters.getString("ordernumber")
                         +"&orderstatus="+parameters.getString("orderstatus")
                         +"&paymoney="+parameters.getString("paymoney");
        _log.info("{}待签名字符串：{}", logPrefix, signTxt+key);
        String sign = SignUtils.MD5.createSign(signTxt, key, SignUtils.CHARSET_UTF8);
        return sign;
    }
}

