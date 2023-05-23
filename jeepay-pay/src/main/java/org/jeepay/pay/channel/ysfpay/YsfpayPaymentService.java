package org.jeepay.pay.channel.ysfpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;
import org.jeepay.common.http.HttpRequestTemplate;
import org.jeepay.common.unify.AbstractPaymentConfig;
import org.jeepay.common.util.HtmlUtils;
import org.jeepay.common.util.Util;
import org.jeepay.common.util.sign.SignUtils;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.BasePayment;

import java.util.Map;

/**
 * @Package org.jeepay.pay.channel.ysfpay
 * @Class: YsfpayPaymentService.java
 * @Description:
 * @Author leo
 * @Date 2019/6/24 12:34
 * @Version
 **/
@Component
public class YsfpayPaymentService extends BasePayment {
    private final static String logPrefix = "【云闪付个码】";

    @Override
    public String getChannelName() {
        return YsfpayConfig.CHANNEL_NAME;
    }

    @Override
    public JSONObject pay(PayOrder payOrder) {
        String channelId = payOrder.getChannelId();
        JSONObject retObj;
        switch (channelId) {
            case YsfpayConfig.CHANNEL_NAME_YSF_GEMA:
                retObj = doOrderReq(payOrder);
                break;
            default:
                retObj = buildRetObj(PayConstant.RETURN_VALUE_FAIL, "不支持的渠道[channelId="+channelId+"]");
                break;
        }

        return retObj;
    }

    private JSONObject doOrderReq(PayOrder payOrder) {
        YsfpayConfig ysfpayConfig = new YsfpayConfig(getPayParam(payOrder));
        Map<String, Object> parameters = getRequestParameters(ysfpayConfig, payOrder);
        _log.info("{}请求数据:{}", logPrefix, JSON.toJSONString(parameters));

        JSONObject retObj = new JSONObject();
        // 将订单更改为支付中
        int result = rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrder.getPayOrderId(), null);
        _log.info("[{}]更新订单状态为支付中:payOrderId={},channelOrderNo={},result={}", getChannelName(), payOrder.getPayOrderId(), null, result);

        String formTxt = HtmlUtils.form(ysfpayConfig.getRequestUrl(), HtmlUtils.GET, parameters);

        return buildPayResultOfForm(retObj, payOrder, formTxt);
    }

    private Map<String, Object> getRequestParameters(AbstractPaymentConfig config, PayOrder order) {
        Map<String, Object> parameters = Maps.newHashMap();
        parameters.put("orderNo", order.getPayOrderId());
        parameters.put("tradeAmount", Util.conversionYuanAmount(order.getAmount()).toString());
        parameters.put("payType", config.getPayMode());
        parameters.put("apiAccount", config.getMchId());
        parameters.put("backUrl", HtmlUtils.encodeURI(order.getReturnUrl()));
        parameters.put("token", getSign(config, parameters));
        return parameters;
    }

    private String getSign(AbstractPaymentConfig config, Map<String, Object> parameters) {
        StringBuffer sbuffer = new StringBuffer()
                .append(parameters.get("orderNo"))
                .append(parameters.get("tradeAmount"))
                .append(parameters.get("payType"))
                .append(parameters.get("apiAccount"));
        String firstMd5 = SignUtils.MD5.createSign(sbuffer.toString(), "", SignUtils.CHARSET_UTF8);
        String finalMd5 = SignUtils.MD5.createSign(firstMd5, config.getPrivateKey(), SignUtils.CHARSET_UTF8);

        return finalMd5;
    }

}
