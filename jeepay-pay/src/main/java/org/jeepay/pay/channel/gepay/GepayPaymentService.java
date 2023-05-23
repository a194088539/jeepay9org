package org.jeepay.pay.channel.gepay;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.common.util.PayDigestUtil;
import org.jeepay.core.common.util.JEEPayUtil;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.BasePayment;
import org.jeepay.pay.mq.BaseNotify4MchPay;

import java.net.URLEncoder;

/**
 * @author: aragom
 * @date: 18/3/1
 * @description: 个付通支付接口
 */
@Service
public class GepayPaymentService extends BasePayment {

    @Autowired
    public BaseNotify4MchPay baseNotify4MchPay;

    private static final MyLog _log = MyLog.getLog(GepayPaymentService.class);

    @Override
    public String getChannelName() {
        return GepayConfig.CHANNEL_NAME;
    }

    /**
     * 支付
     * @param payOrder
     * @return
     */
    @Override
    public JSONObject pay(PayOrder payOrder) {
        String channelId = payOrder.getChannelId();
        JSONObject retObj;
        switch (channelId) {
            case GepayConfig.CHANNEL_NAME_WXPAY_QR :
                retObj = doPayQrReq(payOrder, "wechat");
                break;
            case GepayConfig.CHANNEL_NAME_ALIPAY_QR :
                retObj = doPayQrReq(payOrder, "alipay");
                break;
            case GepayConfig.CHANNEL_NAME_ALIPAY_H5 :
                retObj = doAliPayReq(payOrder, "alipay", "wap");
                break;
            case GepayConfig.CHANNEL_NAME_ALIPAY_PC :
                retObj = doAliPayReq(payOrder, "alipay", "pc");
                break;
            default:
                retObj = buildRetObj(PayConstant.RETURN_VALUE_FAIL, "不支持的渠道[channelId="+channelId+"]");
                break;
        }
        return retObj;
    }

    /**
     * 查询订单
     * @param payOrder
     * @return
     */
    @Override
    public JSONObject query(PayOrder payOrder) {

        String logPrefix = "【个付通订单查询】";
        String payOrderId = payOrder.getPayOrderId();

        _log.info("{}开始查询个付通订单,payOrderId={}", logPrefix, payOrderId);
        GepayConfig gepayConfig = new GepayConfig(getPayParam(payOrder));
        JSONObject paramMap = new JSONObject();
        paramMap.put("userId", gepayConfig.getMchId());
        paramMap.put("outTradeNo", payOrder.getPayOrderId());             // 订单号
        String signValue = PayDigestUtil.getSign(paramMap, gepayConfig.getKey());
        paramMap.put("sign", signValue);
        /*public final static String PAY_STATUS_REQUEST = "1";				// 请求支付
        public final static String PAY_STATUS_RESPONSE = "2";				// 二维码获取成功
        public final static String PAY_STATUS_SUCCESS =  "3";				// 支付成功
        public final static String PAY_STATUS_QRCODE_FAIL =  "4";			// 二维码获取失败
        public final static String PAY_STATUS_NOTIFY_FAIL =  "5";			// 客户端通知服务器失败
        public final static String PAY_STATUS_NOTIFY_MERCHANT_FAIL =  "6";	// 通知商户失败*/
        JSONObject retObj = buildRetObj();
        retObj.put("status", 1);    // 支付中
        String status = "";
        try {
            String reqData = JEEPayUtil.genUrlParams(paramMap);
            _log.info("{}请求数据:{}", logPrefix, reqData);
            String url = gepayConfig.getReqUrl() + "/api/query_order?";
            String result = JEEPayUtil.call4Post(url + reqData);
            _log.info("{}返回数据:{}", logPrefix, result);
            JSONObject resObj = JSONObject.parseObject(result);
            status = resObj.getString("status");
        } catch (Exception e) {
            _log.error(e, "");
        }
        if("3".equals(status)) {
            retObj.put("status", 2);    // 成功
        }else if("1".equals(status)) {
            retObj.put("status", 1);    // 支付中
        }
        return retObj;
    }

    /**
     * 扫码支付
     * @param payOrder
     * @return
     */
    public JSONObject doPayQrReq(PayOrder payOrder, String channel) {
        GepayConfig gepayConfig = new GepayConfig(getPayParam(payOrder));
        JSONObject retObj = new JSONObject();
        JSONObject paramMap = new JSONObject();
        paramMap.put("userId", gepayConfig.getMchId());
        paramMap.put("type", channel);                                      // 支付方式,微信和支付宝
        paramMap.put("money", payOrder.getAmount().doubleValue()/100);      // 单位元
        paramMap.put("outTradeNo", payOrder.getPayOrderId());               // 订单号
        String signValue = PayDigestUtil.getSign(paramMap, gepayConfig.getKey());
        paramMap.put("sign", signValue);
        try {
            String reqData = JEEPayUtil.genUrlParams(paramMap);
            _log.info("{}请求数据:{}", getChannelName(), reqData);
            String url = gepayConfig.getReqUrl() + "/api/create_order?";
            String result = JEEPayUtil.call4Post(url + reqData);
            _log.info("{}返回数据:{}", getChannelName(), result);
            if(StringUtils.isBlank(result)) {
                retObj.put("errDes", "上游通道返回空!");
                retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
                return retObj;
            }
            /*{
                    "money": 2,
                    "remark": "",
                    "type": "wechat",
                    "userId": "test01",
                    "payUrl": "wxp://f2f1DC1PGYHXBHOQhfgS38gQCbH5imBifJJ_",
                    "outTradeNo": "P12312321123",
                    "code": 1,
                    "define": "SUCCESS",
                    "codeImgUrl": "http://47.92.134.173:8070/api/qrcode_img_get?url=wxp://f2f1DC1PGYHXBHOQhfgS38gQCbH5imBifJJ_&width=200&height=200"
            }*/

            JSONObject resObj = JSONObject.parseObject(result);
            Integer code = resObj.getInteger("code");
            if(code == 1) {
                String payUrl = resObj.getString("payUrl");     // 二维码URL
                String payId = resObj.getString("payId");       // 上游通道订单号
                int updateCount = rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrder.getPayOrderId(), payId);
                _log.info("[{}]更新订单状态为支付中:payOrderId={},payId={},result={}", getChannelName(), payOrder.getPayOrderId(), payId, updateCount);
                JSONObject payParams = new JSONObject();
                payParams.put("codeUrl", payUrl); // 二维码支付链接
                payParams.put("codeImgUrl", payConfig.getPayUrl() + "/qrcode_img_get?url=" + URLEncoder.encode(payUrl) + "&widht=200&height=200");
                payParams.put("payMethod", PayConstant.PAY_METHOD_CODE_IMG);
                retObj.put("payParams", payParams);
                retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_SUCCESS);
                return retObj;
            }else {
                retObj.put("errDes", "操作失败!");
                retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
                return retObj;
            }
        } catch (Exception e) {
            _log.error(e, "");
            retObj.put("errDes", "操作失败!");
            retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
            return retObj;
        }
    }

    /**
     * 支付宝支付(跳转页面,显示二维码)
     * @param payOrder
     * @param channel
     * @param type
     * @return
     */
    public JSONObject doAliPayReq(PayOrder payOrder, String channel, String type) {
        GepayConfig gepayConfig = new GepayConfig(getPayParam(payOrder));
        JSONObject retObj = new JSONObject();
        JSONObject paramMap = new JSONObject();
        paramMap.put("userId", gepayConfig.getMchId());
        paramMap.put("type", channel);                                     // 支付方式,微信和支付宝
        paramMap.put("money", payOrder.getAmount().doubleValue()/100);      // 单位元
        paramMap.put("outTradeNo", payOrder.getPayOrderId());               // 订单号
        String signValue = PayDigestUtil.getSign(paramMap, gepayConfig.getKey());
        paramMap.put("sign", signValue);
        try {
            String reqData = JEEPayUtil.genUrlParams(paramMap);
            _log.info("[{}]请求数据:{}", getChannelName(), reqData);
            String url = gepayConfig.getReqUrl() + "/api/create_qr?";
            String result = JEEPayUtil.call4Post(url + reqData);
            _log.info("[{}]返回数据:{}", getChannelName(), result);
            if(StringUtils.isBlank(result)) {
                retObj.put("errDes", "上游通道返回空!");
                retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
                return retObj;
            }
            JSONObject resObj = JSONObject.parseObject(result);
            Integer code = resObj.getInteger("code");
            String define = resObj.getString("define");
            if(code == 1) {

                String payId = resObj.getString("payId");                       // 上游通道订单号
                String sessionKey = resObj.getString("sessionKey");             // 上游获取二维码标识
                String collectionName = resObj.getString("collectionName");     // 收款姓名
                String collectionType = resObj.getString("collectionType");     // 收款类型
                int updateCount = rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrder.getPayOrderId(), payId);
                _log.info("[{}]更新订单状态为支付中:payOrderId={},payId={},result={}", getChannelName(), payOrder.getPayOrderId(), payId, updateCount);
                StringBuffer payForm = new StringBuffer();
                String toPayUrl = payConfig.getPayUrl() + "/gepay/pay_"+type+".htm";
                payForm.append("<form style=\"display: none\" action=\""+toPayUrl+"\" method=\"post\">");
                payForm.append("<input name=\"mchOrderNo\" value=\""+payOrder.getMchOrderNo()+"\" >");
                payForm.append("<input name=\"payOrderId\" value=\""+payOrder.getPayOrderId()+"\" >");
                payForm.append("<input name=\"amount\" value=\""+payOrder.getAmount()+"\" >");
                payForm.append("<input name=\"payId\" value=\""+payId+"\" >");
                payForm.append("<input name=\"sk\" value=\""+sessionKey+"\" >");
                payForm.append("<input name=\"collectionName\" value=\""+collectionName+"\" >");
                payForm.append("<input name=\"collectionType\" value=\""+collectionType+"\" >");
                payForm.append("<input type=\"submit\" value=\"立即支付\" style=\"display:none\" >");
                payForm.append("</form>");
                payForm.append("<script>document.forms[0].submit();</script>");

                // 支付链接地址
                retObj.put("payOrderId", payOrder.getPayOrderId()); // 设置支付订单ID
                JSONObject payParams = new JSONObject();
                payParams.put("payUrl", payForm);
                String payJumpUrl = toPayUrl + "?mchOrderNo=" + payOrder.getMchOrderNo() + "&payOrderId=" + payOrder.getPayOrderId() +
                        "&amount=" + payOrder.getAmount() + "&payId=" + payId + "&sk=" + sessionKey + "&collectionName=" + URLEncoder.encode(collectionName);
                payParams.put("payJumpUrl", payJumpUrl);
                payParams.put("payMethod", PayConstant.PAY_METHOD_FORM_JUMP);
                retObj.put("payParams", payParams);
                retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_SUCCESS);
                return retObj;
            }else {
                retObj.put("errDes", "下单失败[" + define + "]");
                retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
                return retObj;
            }
        } catch (Exception e) {
            _log.error(e, "");
            retObj.put("errDes", "操作失败!");
            retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
            return retObj;
        }
    }

}
