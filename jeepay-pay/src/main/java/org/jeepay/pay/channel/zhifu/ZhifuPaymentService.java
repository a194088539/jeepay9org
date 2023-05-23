package org.jeepay.pay.channel.zhifu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import org.jeepay.common.http.HttpRequestTemplate;
import org.jeepay.common.util.sign.SignUtils;
import org.jeepay.common.util.str.StringUtils;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.AmountUtil;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.BasePayment;

import java.util.Map;

@Service
public class ZhifuPaymentService extends BasePayment {
    private final static String logPrefix = "【智付】";
    @Override
    public String getChannelName() {
        return ZhifuConfig.CHANNEL_NAME;
    }

    /**
     * 支付下单
     * @param payOrder
     * @return
     */
    @Override
    public JSONObject pay(PayOrder payOrder) {
        String channelId = payOrder.getChannelId();
        JSONObject retObj;
        switch (channelId) {
            case ZhifuConfig.CHANMEL_NAME_ZHIFU_QUICK:
                retObj = doOrderReq(payOrder, "03",null);
                break;
            case ZhifuConfig.CHANMEL_NAME_JD_QR:
                retObj = doQrOrderReq(payOrder, "04","JD");
                break;
            case ZhifuConfig.CHANMEL_NAME_UNION_QR:
                retObj = doQrOrderReq(payOrder, "04","CUP");
                break;
            case ZhifuConfig.CHANMEL_NAME_WX_QR:
                retObj = doQrOrderReq(payOrder, "04","WECHAT");
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

        ZhifuConfig config = new ZhifuConfig(getPayParam(payOrder));
        String payOrderId = payOrder.getPayOrderId();
        _log.info("{}开始查询智付通道订单,payOrderId={}", logPrefix, payOrderId);
        Map<String, Object> params = Maps.newHashMap();
        setParams(payOrder,config,"","","payOrderQuery",params);
        _log.info("{}查询智付通道订单请求数据:{}", getChannelName(), JSON.toJSONString(params));
        HttpRequestTemplate requestTemplate = new HttpRequestTemplate(null);
        JSONObject origin = requestTemplate.postForObject(getReqUrl(config.getRequestUrl(),ZhifuConfig.REQ_URL), params, JSONObject.class);
        _log.info("{}智付查单同步请求响应参数：{}", logPrefix, origin.toJSONString());
        // 响应对象
        JSONObject retObj = new JSONObject();
        if(StringUtils.equals(origin.getString("status"),"0000")){
            JSONObject backData = JSONObject.parseObject(origin.getString("backData"));
            // 订单支付状态：S-为已⽀支付，其它未⽀支付
            //已支付
            if(StringUtils.equals(backData.getString("result"),"S")){
                retObj.put("status", 2);
            }else{
                // 支付中
                retObj.put("status", 1);
            }
            retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_SUCCESS);
            retObj.put("channelOrderNo", backData.getString("orderId"));
            retObj.put("channelObj", backData);
            return retObj;
        }
        retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
        retObj.put(PayConstant.RESULT_PARAM_ERRDES, origin.getString("info"));
        return retObj;
    }

    //下单实现
    private JSONObject doOrderReq(PayOrder payOrder,String payMode,String payBankId){

        ZhifuConfig config = new ZhifuConfig(getPayParam(payOrder));
        Map<String, Object> params = Maps.newHashMap();
        setParams(payOrder,config,payMode,payBankId,"payGateway",params);
        _log.info("{}请求数据:{}", getChannelName(), JSON.toJSONString(params));
        // 将订单改为支付中
        rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrder.getPayOrderId(), null);
        HttpRequestTemplate requestTemplate = new HttpRequestTemplate(null);
        JSONObject origin = requestTemplate.postForObject(getReqUrl(config.getRequestUrl(),ZhifuConfig.REQ_URL), params, JSONObject.class);
        _log.info("{}响应参数：{}", logPrefix, origin.toJSONString());
        JSONObject retObj = new JSONObject();
        if(!origin.get("status").equals("0000")){
            retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
            retObj.put(PayConstant.RESULT_PARAM_ERRDES, origin.getString("info"));
            return retObj;
        }
        JSONObject backData = JSONObject.parseObject(origin.getString("backData"));
        return buildPayResultOfForm(retObj, payOrder,getHtml(backData.getString("payUrl")));
    }

    private JSONObject doQrOrderReq(PayOrder payOrder,String payMode,String payBankId){

        ZhifuConfig config = new ZhifuConfig(getPayParam(payOrder));
        Map<String, Object> params = Maps.newHashMap();
        setParams(payOrder,config,payMode,payBankId,"payGateway",params);
        _log.info("{}请求数据:{}", getChannelName(), JSON.toJSONString(params));
        // 将订单改为支付中
        rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrder.getPayOrderId(), null);
        HttpRequestTemplate requestTemplate = new HttpRequestTemplate(null);
        JSONObject origin = requestTemplate.postForObject(getReqUrl(config.getRequestUrl(),ZhifuConfig.REQ_URL), params, JSONObject.class);
        _log.info("{}响应参数：{}", logPrefix, origin.toJSONString());
        JSONObject retObj = new JSONObject();
        if(!origin.get("status").equals("0000")){
            retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
            retObj.put(PayConstant.RESULT_PARAM_ERRDES, origin.getString("info"));
            return retObj;
        }
        JSONObject backData = JSONObject.parseObject(origin.getString("backData"));

        String codeUrl = backData.getString("payUrl");
        return buildPayResultOfCodeURL(retObj, payOrder, codeUrl);
    }

    //设置请求主体参数
    private void setParams(PayOrder payOrder, ZhifuConfig config,String payMode,String payBankId,String tradeId, Map<String,Object> map){

        map.put("tradeId", tradeId);
        map.put("ver", "1.0");
        String tradeData;
        if(payMode.length()>0){
             tradeData = getTradeData(payOrder, payMode,payBankId, config);
        }else{
             tradeData = getTradeData(payOrder, config);
        }
        map.put("tradeData", tradeData);
        map.put("tradeSign", getSign(tradeData, config.getPrivateKey()));

    }

    //获取tradeData数据报文
    private String getTradeData(PayOrder payOrder,String payMode ,String payBankId,ZhifuConfig config){

        JSONObject jsonData = new JSONObject();

        jsonData.put("merId", config.getAppId());
        jsonData.put("orderId", payOrder.getPayOrderId());
        jsonData.put("goods", payOrder.getPayOrderId());
        jsonData.put("amount", AmountUtil.convertCent2Dollar(String.valueOf(payOrder.getAmount())));
        jsonData.put("notifyUrl", payConfig.getNotifyUrl(getChannelName()));
        if(null!=payOrder.getReturnUrl()&&payOrder.getReturnUrl().length()>0){
            jsonData.put("pageUrl", payOrder.getReturnUrl());
        }
        jsonData.put("payMode", payMode);
        if(StringUtils.equals(payMode,"03")&&!StringUtils.equals(null,payOrder.getExtra())&&payOrder.getExtra().length()>0&&isJson(payOrder.getExtra())){
            String phone =  JSONObject.parseObject(payOrder.getExtra()).getString("phone");
            jsonData.put("cardMobile",phone);
            jsonData.put("userId", phone);
        }else if (StringUtils.equals(payMode,"04")){
            jsonData.put("payBankId", payBankId);
            jsonData.put("userId", payOrder.getPayOrderId());
        }else {
            jsonData.put("userId", payOrder.getPayOrderId());
        }
        jsonData.put("clientIp", payOrder.getClientIp());
        jsonData.put("creditType","2");

        return jsonData.toJSONString();
    }

    private String getTradeData(PayOrder payOrder,ZhifuConfig config){

        JSONObject jsonData = new JSONObject();

        jsonData.put("merId", config.getAppId());
        jsonData.put("orderId", payOrder.getPayOrderId());

        return jsonData.toJSONString();
    }

    //获取签名字符串
    private String getSign(String tradeData,String key){
        _log.info("{}智付签名字符串：{}", logPrefix, tradeData);
        String sign = SignUtils.MD5.createSign(tradeData, "&"+key, SignUtils.CHARSET_UTF8);
        return sign.toUpperCase();
    }

    //判断字符串是否为json格式
    private static boolean isJson(String content) {
        try {
            JSONObject.parseObject(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //获取请求html
    private String getHtml(String payUrl){
        StringBuffer buffer = new StringBuffer();
        buffer.append("<script type=\"text/javascript\">");
        buffer.append("window.location.href=\""+payUrl+"\"");
        buffer.append("</script>");
        return buffer.toString();
    }
}
