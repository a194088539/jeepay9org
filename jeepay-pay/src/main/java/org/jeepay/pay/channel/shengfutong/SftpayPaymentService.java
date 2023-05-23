package org.jeepay.pay.channel.shengfutong;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.jeepay.common.http.HttpRequestTemplate;
import org.jeepay.common.unify.AbstractPaymentConfig;
import org.jeepay.common.util.DateUtils;
import org.jeepay.common.util.HtmlUtils;
import org.jeepay.common.util.Util;
import org.jeepay.common.util.sign.SignUtils;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.BasePayment;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Package org.jeepay.pay.channel.shengfutong
 * @Class: SftpayPaymentService.java
 * @Description:
 * @Author leo
 * @Date 2019/4/8 11:36
 * @Version
 **/
@Component
public class SftpayPaymentService extends BasePayment {
    private final static String logPrefix = "【盛付通】";

    // 下单查询串
    private final static String ORDER_CREATE_STR = "/Pay_Index.html";
    // 订单查询请求串
    private final static String ORDER_QUERY_STR = "/Pay_Trade_query.html";

    @Override
    public String getChannelName() {
        return SftpayConfig.CHANNEL_NAME;
    }


    @Override
    public JSONObject pay(PayOrder payOrder) {
        String channelId = payOrder.getChannelId();
        JSONObject retObj;
        switch (channelId) {
            case SftpayConfig.CHANNEL_NAME_SFT_ALIPAY_WAP:
                retObj = doOrderReq(payOrder);
                break;
            case SftpayConfig.CHANNEL_NAME_SFT_ALIPAY_QR:
                retObj = doOrderReq(payOrder);
                break;
            case SftpayConfig.CHANNEL_NAME_SFT_WXPAY:
                retObj = doOrderReq(payOrder);
                break;
            case SftpayConfig.CHANNEL_NAME_SFT_WXPAY_SCAN:
                retObj = doOrderReq(payOrder);
                break;
            case SftpayConfig.CHANNEL_NAME_SFT_QUICK_WAP:
                retObj = doOrderReq(payOrder);
                break;

            default:
                retObj = buildRetObj(PayConstant.RETURN_VALUE_FAIL, "不支持的渠道[channelId="+channelId+"]");
                break;
        }

        return retObj;
    }

    private JSONObject doOrderReq(PayOrder payOrder) {
        SftpayConfig sftpayConfig = new SftpayConfig(getPayParam(payOrder));
        // 请求参数
        JSONObject parameters = getPublicParameters(sftpayConfig);
        parameters.put("pay_orderid", payOrder.getPayOrderId());
        parameters.put("pay_bankcode", sftpayConfig.getPayMode());
        parameters.put("pay_notifyurl", payConfig.getNotifyUrl(getChannelName()));
        parameters.put("pay_callbackurl", payOrder.getReturnUrl());
        parameters.put("pay_amount", payOrder.getAmount());
        parameters.put("pay_md5sign", getSign(parameters, sftpayConfig));
        parameters.put("pay_productname",sftpayConfig.PAY_PRODUCTNAME);
        _log.info("{}请求数据:{}", getChannelName(), JSON.toJSONString(parameters));
        // 将订单改为支付中
        rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrder.getPayOrderId(), null);

        JSONObject retObj = new JSONObject();
        String payForm = HtmlUtils.form(getReqUrl(sftpayConfig.getRequestUrl(), ORDER_CREATE_STR), HtmlUtils.POST, parameters);

        return buildPayResultOfForm(retObj, payOrder, payForm);
    }

    @Override
    public JSONObject query(PayOrder payOrder) {
        SftpayConfig sftpayConfig = new SftpayConfig(getPayParam(payOrder));
        String payOrderId = payOrder.getPayOrderId();
        _log.info("{}开始查询盛付通通道订单,payOrderId={}", logPrefix, payOrderId);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("pay_memberid", sftpayConfig.getAppId());
        parameters.put("pay_orderid", payOrder.getPayOrderId());
        parameters.put("pay_md5sign", getQuerySign(parameters, sftpayConfig));
        _log.info("{}请求参数：{}", logPrefix, parameters.toString());
        HttpRequestTemplate requestTemplate = new HttpRequestTemplate(null);
        JSONObject origin = requestTemplate.postForObject(getReqUrl(sftpayConfig.getRequestUrl(), ORDER_QUERY_STR), parameters, JSONObject.class);
        _log.info("{}盛付通查单同步请求响应参数：{}", logPrefix, origin.toJSONString());
        // 响应对象
        JSONObject retObj = new JSONObject();
        if(StringUtils.equals(origin.getString("returncode"), SftpayConfig.RETURN_VALUE_SUCCESS)) {
            // 盛付通订单支付状态：1-为已⽀支付，其它未⽀支付
            String payStatus = origin.getString("trade_state");
            // 已支付
            if(StringUtils.equals(SftpayConfig.ORDER_STATUS_SUCCESS, payStatus)) {
                retObj.put("status", 2);
            } else {
                // 支付中
                retObj.put("status", 1);
            }
            retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_SUCCESS);
            retObj.put("channelOrderNo", origin.getString("orderid"));
            retObj.put("channelObj", origin);
            return retObj;
        }
        retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
        retObj.put(PayConstant.RESULT_PARAM_ERRDES, origin.getString("msg"));
        return retObj;
    }

    /**
     * 初始化公共参数
     * @param config
     * @return
     */
    private JSONObject getPublicParameters(AbstractPaymentConfig config) {
        JSONObject parameters = new JSONObject();
        parameters.put("pay_memberid", config.getMchId());
        parameters.put("pay_applydate", DateUtils.YYYY_MM_DD_HH_MM_SS.format(new Date()));
        return parameters;
    }

    /**
     * 签名
     * @param parameters
     * @param config
     * @return
     */
    private String getSign(JSONObject parameters, AbstractPaymentConfig config) {
        String signTxt = SignUtils.parameterText(parameters);
        _log.info("{}盛付通签名字符串：{}", logPrefix, signTxt);
        String sign = SignUtils.MD5.createSign(signTxt, "&key="+config.getPrivateKey(), SignUtils.CHARSET_UTF8);
        return sign.toUpperCase();
    }
    /**
     * 签名
     * @param parameters
     * @param config
     * @return
     */
    private String getQuerySign( Map<String, Object> parameters, AbstractPaymentConfig config) {
        String signTxt = SignUtils.parameterText(parameters);
        _log.info("{}盛付通签名字符串：{}", logPrefix, signTxt);
        String sign = SignUtils.MD5.createSign(signTxt, "&key="+config.getPrivateKey(), SignUtils.CHARSET_UTF8);
        return sign.toUpperCase();
    }

}
