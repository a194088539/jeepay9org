package org.jeepay.pay.channel.wxpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.github.binarywang.wxpay.util.SignUtils;
import org.springframework.stereotype.Service;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.BasePayment;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: aragom
 * @date: 17/12/24
 * @description:
 */
@Service
public class WxpayPaymentService extends BasePayment {

    private static final MyLog _log = MyLog.getLog(WxpayPaymentService.class);

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_WXPAY;
    }

    @Override
    public JSONObject pay(PayOrder payOrder) {
        String logPrefix = "【微信支付统一下单】";
        JSONObject map = new JSONObject();
        map.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_SUCCESS);
        try {
            String channelId = payOrder.getChannelId();
            String tradeType = channelId.substring(channelId.indexOf("_") + 1).toUpperCase();   // 转大写,与微信一致
            WxPayConfig wxPayConfig = WxPayUtil.getWxPayConfig(getPayParam(payOrder), tradeType, payConfig.getCertRootPath() + File.separator + getChannelName(), payConfig.getNotifyUrl(getChannelName()));
            WxPayService wxPayService = new WxPayServiceImpl();
            wxPayService.setConfig(wxPayConfig);
            WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = buildUnifiedOrderRequest(payOrder, wxPayConfig);

            String payOrderId = payOrder.getPayOrderId();
            WxPayUnifiedOrderResult wxPayUnifiedOrderResult;
            try {

                wxPayUnifiedOrderResult = wxPayService.unifiedOrder(wxPayUnifiedOrderRequest);
                _log.info("{} >>> 下单成功", logPrefix);
                map.put("payOrderId", payOrderId);
                int result = rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrderId, null);
                _log.info("更新第三方支付订单号:payOrderId={},prepayId={},result={}", payOrderId, wxPayUnifiedOrderResult.getPrepayId(), result);
                switch (tradeType) {
                    case PayConstant.WxConstant.TRADE_TYPE_NATIVE: {
                        JSONObject payInfo = new JSONObject();
                        payInfo.put("prepayId", wxPayUnifiedOrderResult.getPrepayId());
                        payInfo.put("codeUrl", wxPayUnifiedOrderResult.getCodeURL()); // 二维码支付链接
                        payInfo.put("codeImgUrl", payConfig.getPayUrl() + "/qrcode_img_get?url=" + wxPayUnifiedOrderResult.getCodeURL() + "&widht=200&height=200");
                        payInfo.put("payMethod", PayConstant.PAY_METHOD_CODE_IMG);
                        map.put("payParams", payInfo);
                        break;
                    }
                    case PayConstant.WxConstant.TRADE_TYPE_APP: {
                        Map<String, String> payInfo = new HashMap<>();
                        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
                        String nonceStr = String.valueOf(System.currentTimeMillis());
                        // APP支付绑定的是微信开放平台上的账号，APPID为开放平台上绑定APP后发放的参数
                        String wxAppId = wxPayConfig.getAppId();
                        Map<String, String> configMap = new HashMap<>();
                        // 此map用于参与调起sdk支付的二次签名,格式全小写，timestamp只能是10位,格式固定，切勿修改
                        String partnerId = wxPayConfig.getMchId();
                        configMap.put("prepayid", wxPayUnifiedOrderResult.getPrepayId());
                        configMap.put("partnerid", partnerId);
                        String packageValue = "Sign=WXPay";
                        configMap.put("package", packageValue);
                        configMap.put("timestamp", timestamp);
                        configMap.put("noncestr", nonceStr);
                        configMap.put("appid", wxAppId);
                        // 此map用于客户端与微信服务器交互
                        payInfo.put("sign", SignUtils.createSign(configMap, wxPayConfig.getMchKey()));
                        payInfo.put("prepayId", wxPayUnifiedOrderResult.getPrepayId());
                        payInfo.put("partnerId", partnerId);
                        payInfo.put("appId", wxAppId);
                        payInfo.put("package", packageValue);
                        payInfo.put("timeStamp", timestamp);
                        payInfo.put("nonceStr", nonceStr);
                        map.put("payParams", JSONObject.parseObject(JSON.toJSONString(payInfo)));
                        break;
                    }
                    case PayConstant.WxConstant.TRADE_TYPE_JSPAI: {
                        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
                        String nonceStr = String.valueOf(System.currentTimeMillis());
                        Map<String, String> payInfo = new HashMap<>(); // 如果用JsonObject会出现签名错误
                        payInfo.put("appId", wxPayUnifiedOrderResult.getAppid());
                        // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
                        payInfo.put("timeStamp", timestamp);
                        payInfo.put("nonceStr", nonceStr);
                        payInfo.put("package", "prepay_id=" + wxPayUnifiedOrderResult.getPrepayId());
                        payInfo.put("signType", WxPayConstants.SignType.MD5);
                        payInfo.put("paySign", SignUtils.createSign(payInfo, wxPayConfig.getMchKey()));
                        // 签名以后在增加prepayId参数
                        payInfo.put("prepayId", wxPayUnifiedOrderResult.getPrepayId());
                        map.put("payParams", JSONObject.parseObject(JSON.toJSONString(payInfo)));
                        break;
                    }
                    case PayConstant.WxConstant.TRADE_TYPE_MWEB: {
                        JSONObject payInfo = new JSONObject();
                        payInfo.put("prepayId", wxPayUnifiedOrderResult.getPrepayId());
                        payInfo.put("payUrl", wxPayUnifiedOrderResult.getMwebUrl()); // h5支付链接地址
                        map.put("payParams", payInfo);
                        break;
                    }
                }
            } catch (WxPayException e) {
                _log.error(e, "下单失败");
                //出现业务错误
                _log.info("{}下单返回失败", logPrefix);
                _log.info("err_code:{}", e.getErrCode());
                _log.info("err_code_des:{}", e.getErrCodeDes());
                map.put("errDes", e.getErrCodeDes());
                map.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
            }
        } catch (Exception e) {
            _log.error(e, "微信支付统一下单异常");
            map.put("errDes", "微信支付统一下单异常");
            map.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
        }
        return map;
    }

    @Override
    public JSONObject query(PayOrder payOrder) {
        return null;
    }

    @Override
    public JSONObject close(PayOrder payOrder) {
        return null;
    }

    /**
     * 构建微信统一下单请求数据
     *
     * @param payOrder
     * @param wxPayConfig
     * @return
     */
    WxPayUnifiedOrderRequest buildUnifiedOrderRequest(PayOrder payOrder, WxPayConfig wxPayConfig) {
        String tradeType = wxPayConfig.getTradeType();
        String payOrderId = payOrder.getPayOrderId();
        Integer totalFee = payOrder.getAmount().intValue();// 支付金额,单位分
        String deviceInfo = payOrder.getDevice();
        String body = payOrder.getBody();
        String detail = null;
        String attach = null;
        String outTradeNo = payOrderId;
        String feeType = "CNY";
        String spBillCreateIP = payOrder.getClientIp();
        String timeStart = null;
        String timeExpire = null;
        String goodsTag = null;
        String notifyUrl = payConfig.getNotifyUrl(getChannelName());
        String productId = null;
        if (tradeType.equals(PayConstant.WxConstant.TRADE_TYPE_NATIVE)) productId = System.currentTimeMillis() + "";
        String limitPay = null;
        String openId = null;
        if (tradeType.equals(PayConstant.WxConstant.TRADE_TYPE_JSPAI))
            openId = JSON.parseObject(payOrder.getExtra()).getString("openId");
        String sceneInfo = null;
        if (tradeType.equals(PayConstant.WxConstant.TRADE_TYPE_MWEB))
            sceneInfo = JSON.parseObject(payOrder.getExtra()).getString("sceneInfo");
        // 微信统一下单请求对象
        WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
        request.setDeviceInfo(deviceInfo);
        request.setBody(body);
        request.setDetail(detail);
        request.setAttach(attach);
        request.setOutTradeNo(outTradeNo);
        request.setFeeType(feeType);
        request.setTotalFee(totalFee);
        request.setSpbillCreateIp(spBillCreateIP);
        request.setTimeStart(timeStart);
        request.setTimeExpire(timeExpire);
        request.setGoodsTag(goodsTag);
        request.setNotifyUrl(notifyUrl);
        request.setTradeType(tradeType);
        request.setProductId(productId);
        request.setLimitPay(limitPay);
        request.setOpenid(openId);
        request.setSceneInfo(sceneInfo);
        //request.setProfitSharing() //是否分账
        return request;
    }


}
