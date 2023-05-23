package org.jeepay.pay.channel.sand;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.jeepay.common.http.HttpRequestTemplate;
import org.jeepay.common.unify.AbstractPaymentConfig;
import org.jeepay.common.util.DateUtils;
import org.jeepay.common.util.FormUrlDecoder;
import org.jeepay.common.util.HtmlUtils;
import org.jeepay.common.util.sign.CertDescriptor;
import org.jeepay.common.util.sign.SignUtils;
import org.jeepay.common.util.sign.encrypt.Base64;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.BasePayment;
import org.jeepay.pay.channel.sand.response.SandResBody;
import org.jeepay.pay.channel.sand.response.SandResData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Package org.jeepay.pay.channel.sand
 * @Class: SandPaymentService.java
 * @Description:
 * @Author leo
 * @Date 2019/4/11 11:44
 * @Version
 **/
@Component
public class SandPaymentService extends BasePayment {

    private final static String logPrefix = "【杉德支付】";

    @Override
    public String getChannelName() {
        return SandConfig.CHANNEL_NAME;
    }

    @Override
    public JSONObject pay(PayOrder payOrder) {
        String channelId = payOrder.getChannelId();
        JSONObject retObj;
        switch (channelId) {
            case SandConfig.CHANNEL_NAME_SAND_QR:
                retObj = doOrderQrReq(payOrder);
                break;
            case SandConfig.CHANNEL_NAME_SAND_JDQR:
                retObj = doOrderQrReq(payOrder);
                break;
            case SandConfig.CHANNEL_NAME_SAND_QUICK:
                retObj = doOrderQuickReq(payOrder);
                break;
            default:
                retObj = buildRetObj(PayConstant.RETURN_VALUE_FAIL, "不支持的渠道[channelId="+channelId+"]");
                break;
        }

        return retObj;
    }


    @Override
        public JSONObject query(PayOrder payOrder) {
        SandConfig sandConfig = new SandConfig(getPayParam(payOrder));
        String channelId = payOrder.getChannelId();
        JSONObject retObj;
        SandTransactionType transactionType = null;
        String urlRequestQuery = null;
        switch (channelId) {
            case SandConfig.CHANNEL_NAME_SAND_QR:
                transactionType = SandTransactionType.SAND_QR_QUERY;
                urlRequestQuery = SandConfig.TRADE_QR_QUERY_URL;
                break;
            case SandConfig.CHANNEL_NAME_SAND_JDQR:
                transactionType = SandTransactionType.SAND_QR_QUERY;
                urlRequestQuery = SandConfig.TRADE_QR_QUERY_URL;
                break;
            case SandConfig.CHANNEL_NAME_SAND_QUICK:
                transactionType = SandTransactionType.SAND_H5_QUERY;
                urlRequestQuery = SandConfig.TRADE_ORDER_QUERY_URL;
                break;
            default:
                retObj = buildRetObj(PayConstant.RETURN_VALUE_FAIL, "不支持的渠道[channelId="+channelId+"]");
                return retObj;
        }
        String requestBody = getRequestBody(getOrderQueryParameters(payOrder), getPublicHeads(sandConfig, payOrder, transactionType));
        Map<String, Object> parameters = getPublicParameters(requestBody, getSign(sandConfig, requestBody));
        _log.info("{}查询订单下单请求参数：{}", logPrefix, JSONObject.toJSONString(parameters));
        HttpRequestTemplate requestTemplate = new HttpRequestTemplate(null);
        String origin = requestTemplate.postForObject(getReqUrl(sandConfig.getRequestUrl(), urlRequestQuery), parameters, String.class);
        origin = HtmlUtils.decodeURI(origin);
        _log.info("{}二维码下单请求原始响应参数：{}", logPrefix, origin);
        Map<String, String> responseInfo = FormUrlDecoder.getMap(origin);
        JSONObject orderInfo = JSONObject.parseObject(JSON.toJSONString(responseInfo));
        _log.info("{}请求响应参数：{}", logPrefix, orderInfo.toJSONString());
        SandResData resData = orderInfo.getJSONObject("data").toJavaObject(SandResData.class);
        // 响应对象
        JSONObject finalRetObj = new JSONObject();
        if(StringUtils.equals(resData.getHead().getRespCode(), SandConfig.RETURN_VALUE_SUCCESS)) {
            SandResBody sandResBody = resData.getBody();
            // sand订单状态：00 成功 01 订单生成未支付 02 失败 04 已退货 05 退款处理中
            if(StringUtils.equals(sandResBody.getOrderStatus(), "00")) {
                finalRetObj.put("status", 2); // 已支付
            } else if(StringUtils.equals(sandResBody.getOrderStatus(), "01")) {
                finalRetObj.put("status", 1); // 支付中
            }
            finalRetObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_SUCCESS);
            finalRetObj.put("channelOrderNo", sandResBody.getTradeNo());
            finalRetObj.put("channelObj", orderInfo);
            return finalRetObj;
        }
        finalRetObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
        finalRetObj.put(PayConstant.RESULT_PARAM_ERRDES, String.format("code:%s,desc:%s", resData.getHead().getRespCode(), resData.getHead().getRespMsg()));
        return finalRetObj;
    }

    private JSONObject doOrderQrReq(PayOrder payOrder) {
        SandConfig sandConfig = new SandConfig(getPayParam(payOrder));
        SandTransactionType transactionType = SandTransactionType.valueOf(sandConfig.getPayMode());
        String requestBody = getRequestBody(getQrParameOrder(sandConfig, payOrder), getPublicHeads(sandConfig, payOrder, transactionType));
        Map<String, Object> parameters = getPublicParameters(requestBody, getSign(sandConfig, requestBody));
        _log.info("{}二维码下单请求参数：{}", logPrefix, JSONObject.toJSONString(parameters));
        HttpRequestTemplate requestTemplate = new HttpRequestTemplate(null);
        String origin = requestTemplate.postForObject(getReqUrl(sandConfig.getRequestUrl(), SandConfig.ORDER_QR_CREATE), parameters, String.class);
        origin = HtmlUtils.decodeURI(origin);
        _log.info("{}二维码下单请求原始响应参数：{}", logPrefix, origin);
        FormUrlDecoder formUrlDecoder = new FormUrlDecoder(origin);
        JSONObject orderInfo = JSONObject.parseObject(JSON.toJSONString(formUrlDecoder.getParameters()));
        _log.info("{}二维码请求响应参数：{}", logPrefix, orderInfo.toJSONString());
        SandResData resData = orderInfo.getJSONObject("data").toJavaObject(SandResData.class);
        // 响应对象
        JSONObject retObj = new JSONObject();
        if(StringUtils.equals(resData.getHead().getRespCode(), SandConfig.RETURN_VALUE_SUCCESS)) {
            // 将订单更改为支付中
            int result = rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrder.getPayOrderId(), null);
            _log.info("[{}]更新订单状态为支付中:payOrderId={},channelOrderNo={},result={}", getChannelName(), payOrder.getPayOrderId(), null, result);
            // 支付链接地址
            String codeUrl = resData.getBody().getQrCode();
            return buildPayResultOfCodeURL(retObj, payOrder, codeUrl);
        }
        retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
        retObj.put(PayConstant.RESULT_PARAM_ERRDES, resData.getHead().getRespMsg());

        return retObj;
    }

    /**
     * 快捷下单
     * @param payOrder
     * @return
     */
    private JSONObject doOrderQuickReq(PayOrder payOrder) {
        SandConfig sandConfig = new SandConfig(getPayParam(payOrder));
        SandTransactionType transactionType = SandTransactionType.valueOf(sandConfig.getPayMode());
        String requestBody = getRequestBody(getQuickParameOrder(sandConfig, payOrder, transactionType), getPublicHeads(sandConfig, payOrder, transactionType));
        Map<String, Object> parameters = getPublicParameters(requestBody, getSign(sandConfig, requestBody));
        _log.info("{}下单请求参数：{}", logPrefix, JSONObject.toJSONString(parameters));
        HttpRequestTemplate requestTemplate = new HttpRequestTemplate(null);
        String origin = requestTemplate.postForObject(getReqUrl(sandConfig.getRequestUrl(), SandConfig.ORDER_QUICK_CREATE), parameters, String.class);
        origin = HtmlUtils.decodeURI(origin);
        _log.info("{}下单请求原始响应参数：{}", logPrefix, origin);
        Map<String, String> responseInfo = FormUrlDecoder.getMap(origin);
        JSONObject orderInfo = JSONObject.parseObject(JSON.toJSONString(responseInfo));
        _log.info("{}请求响应参数：{}", logPrefix, orderInfo.toJSONString());
        SandResData resData = orderInfo.getJSONObject("data").toJavaObject(SandResData.class);
        // 响应对象
        JSONObject retObj = new JSONObject();
        if(StringUtils.equals(resData.getHead().getRespCode(), SandConfig.RETURN_VALUE_SUCCESS)) {
            // 将订单更改为支付中
            int result = rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrder.getPayOrderId(), null);
            _log.info("[{}]更新订单状态为支付中:payOrderId={},channelOrderNo={},result={}", getChannelName(), payOrder.getPayOrderId(), null, result);
            // 支付链接地址
            String formTxt = resData.getBody().getCredential();
            return buildPayResultOfForm(retObj, payOrder, formTxt);
        }
        retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
        retObj.put(PayConstant.RESULT_PARAM_ERRDES, resData.getHead().getRespMsg());

        return retObj;
    }



    /**
     * 设置公共请求头
     * @return
     */
    private Map<String,Object> getPublicHeads(AbstractPaymentConfig config, PayOrder payOrder, SandTransactionType transactionType) {
        Map<String, Object> head = new HashMap<String, Object>();
        head.put("version", SandConfig.PARAM_VERSION_VAL);
        head.put("method", transactionType.getMethod());
        head.put("productId", transactionType.getProductId());
        head.put("accessType", SandConfig.PARAM_ACCESSTYPE_VAL);
        head.put("mid", config.getMchId());
        // 渠道类型：07-互联网   08-移动端
        head.put("channelType", StringUtils.equals(payOrder.getDevice(), "pc") ? "08" : "07");
        head.put("reqTime", DateUtils.YYYYMMDDHHMMSS.format(new Date()));
        return  head;
    }
    /**
     * 设置公共请求参数
     *
     * @return
     */
    private Map<String, Object> getPublicParameters(String strData, String sign) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("charset", SandConfig.PARAM_CHARSET_VAL);
        param.put("data", strData);
        param.put("signType", SandConfig.PARAM_SIGN_TYPE_VAL);
        param.put("sign", sign);
        param.put("extend", null);
        return param;
    }

    /**
     * 设置请求参数Data
     *
     * @param paramOrder
     * @return
     */
    private String getRequestBody(Map<String, Object> paramOrder,Map<String,Object> head) {
        String strData = null;
        JSONObject reqJson=new JSONObject();
        reqJson.put("head",head);
        reqJson.put("body",paramOrder);
        strData = reqJson.toJSONString();
        return strData;
    }

    private String getSign(AbstractPaymentConfig config, String requestBody) {
        CertDescriptor certDescriptor = new CertDescriptor();
        certDescriptor.initPrivateSignCert(config.getPrivateStorePath(), config.getPrivateStorePathPwd(), "PKCS12");
        String key = Base64.encode(certDescriptor.getSignCertPrivateKey(config.getPrivateStorePathPwd()).getEncoded());
        String signValue = SignUtils.RSA.createSign(requestBody, key, SignUtils.CHARSET_UTF8);
        return signValue;
    }

    /**
     * 格式化金额让其符合Sand上传格式
     * @param price
     * @return
     */
    private String foramtPrice(long price) {
        return String.format("%012d",price);
    }

    /**
     * 设置二维码下单请求参数
     * @param config
     * @param orderInfo
     * @return
     */
    private  Map<String, Object> getQrParameOrder(AbstractPaymentConfig config, PayOrder orderInfo){
        Map<String, Object> paramOrder = new HashMap<String,Object>();
        paramOrder.put("payTool", "0403");
        paramOrder.put("orderCode", orderInfo.getPayOrderId());
        paramOrder.put("totalAmount", foramtPrice(orderInfo.getAmount()));
        paramOrder.put("subject", orderInfo.getSubject());
        paramOrder.put("notifyUrl", payConfig.getNotifyUrl(getChannelName()));
        paramOrder.put("clearCycle", config.getTradeRule());
        return  paramOrder;
    }

    private Map<String, Object> getQuickParameOrder(AbstractPaymentConfig config, PayOrder payOrder, SandTransactionType transactionType) {
        Map<String, Object> paramOrder = Maps.newHashMap();

        paramOrder.put("orderCode", payOrder.getPayOrderId());
        paramOrder.put("totalAmount", foramtPrice(payOrder.getAmount()));
        paramOrder.put("subject", payOrder.getSubject());
        paramOrder.put("body", payOrder.getBody());
        paramOrder.put("payMode", transactionType.getPayMode() );
        paramOrder.put("clientIp", payOrder.getClientIp());
        paramOrder.put("notifyUrl", payConfig.getNotifyUrl(getChannelName()));
        paramOrder.put("frontUrl", payOrder.getReturnUrl());
        paramOrder.put("clearCycle", config.getTradeRule());
        return paramOrder;
    }

    /**
     *
     * @param payOrder
     * @return
     */
    private Map<String, Object> getOrderQueryParameters(PayOrder payOrder) {
        Map<String, Object> paramOrder = Maps.newHashMap();
        paramOrder.put("orderCode", payOrder.getPayOrderId());
        return paramOrder;
    }

}
