package org.jeepay.pay.channel.sand;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.jeepay.common.unify.AbstractPaymentConfig;
import org.jeepay.common.util.sign.CertDescriptor;
import org.jeepay.common.util.sign.SignUtils;
import org.jeepay.common.util.sign.encrypt.RSA;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.BasePayNotify;
import org.jeepay.pay.channel.sand.response.SandResData;

import java.security.PublicKey;

/**
 * @Package org.jeepay.pay.channel.sand
 * @Class: SandPaymentNotifyService.java
 * @Description:
 * @Author leo
 * @Date 2019/4/11 17:58
 * @Version
 **/
@Component
public class SandPayNotifyService extends BasePayNotify {
    private final static String logPrefix = "【杉德支付回调】";

    @Override
    public String getChannelName() {
        return SandConfig.CHANNEL_NAME;
    }

    @Override
    public JSONObject doNotify(Object notifyData) {
        JSONObject bizContext = getRequestParameters(notifyData);
        _log.info("{}请求参数：{}", logPrefix, bizContext.toJSONString());
        SandResData sandResData = bizContext.getJSONObject("data").toJavaObject(SandResData.class);
        JSONObject retObj = buildFailRetObj();
        try {
            if (!verifyPayParams(bizContext, sandResData)) {
                retObj.put(PayConstant.RESPONSE_RESULT, SandConfig.RESPONSE_RESULT_FAIL);
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
            retObj.put(PayConstant.RESPONSE_RESULT, SandConfig.RESPONSE_RESULT_SUCCESS);
        } catch (Exception e) {
            _log.error(e, logPrefix + "处理异常");
        }
        return retObj;
    }

    private boolean verifyPayParams(JSONObject payContext, SandResData sandResData) {
        // 查询payOrder记录
        String payOrderId = sandResData.getBody().getOrderCode();
        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(payOrderId);
        if (payOrder == null) {
            _log.error("Can't found payOrder form db. payOrderId={}, ", payOrderId);
            payContext.put("retMsg", "Can't found payOrder");
            return false;
        }

        if(!StringUtils.equals(sandResData.getBody().getOrderStatus(), SandConfig.ASYNC_NOTIFY_STATUS_OK)) {
            payContext.put("retMsg", "notify status not ok");
            return false;
        }

        SandConfig sandConfig = new SandConfig(getPayParam(payOrder));

        boolean isVerify = signVerify(JSONObject.toJSONString(sandResData), payContext.getString("sign"), sandConfig);
        if(isVerify) {
            _log.error("验证签名失败. payOrderId={}", payOrderId);
            payContext.put("retMsg", "验证签名失败");
            return false;
        }

        // 核对金额
        long outPayAmt = Long.valueOf(sandResData.getBody().getTotalAmount());
        long dbPayAmt = payOrder.getAmount().longValue();
        if (dbPayAmt != outPayAmt) {
            _log.error("金额不一致. outPayAmt={},payOrderId={}", outPayAmt, payOrderId);
            payContext.put("retMsg", "金额不一致");
            return false;
        }
        payContext.put("payOrder", payOrder);
        return true;
    }

    public boolean signVerify(String body, String sign, AbstractPaymentConfig config) {
        CertDescriptor certDescriptor = new CertDescriptor();
        certDescriptor.initPublicCert(config.getPublicStorePath());
        PublicKey publicKey = certDescriptor.getPublicCert().getPublicKey();
        return RSA.verify(body, sign, publicKey, SignUtils.CHARSET_UTF8);
    }

}
