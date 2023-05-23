package org.jeepay.pay.channel.zhongfu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import org.jeepay.common.http.HttpRequestTemplate;
import org.jeepay.common.util.HtmlUtils;
import org.jeepay.common.util.sign.SignUtils;
import org.jeepay.common.util.str.StringUtils;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.AmountUtil;
import org.jeepay.core.common.util.DateUtil;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.BasePayment;

import java.util.Date;
import java.util.Map;
@Service
public class ZhongfuPaymentService extends BasePayment {

    private final static String logPrefix = "【中付支付】";
    @Override
    public String getChannelName() {
        return ZhongfuConfig.CHANNEL_NAME;
    }

    /**
     * 支付下单
     * @param payOrder
     * @return
     */
    @Override
    public JSONObject pay(PayOrder payOrder){
        return doOrderReq(payOrder);
    }

    /**
     * 查询订单
     * @param payOrder
     * @return
     */
    @Override
    public JSONObject query(PayOrder payOrder){
        JSONObject retObj = buildRetObj();
        retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
        retObj.put(PayConstant.RESULT_PARAM_ERRDES, "没有订单查询接口");
        return retObj;
    }

    //下单具体实现
    private JSONObject doOrderReq(PayOrder payOrder){
        ZhongfuConfig config = new ZhongfuConfig(getPayParam(payOrder));
        Map<String,Object> params = Maps.newHashMap();
        //设置请求参数
        setParams(payOrder, config, params);
        _log.info("{}请求数据:{}", getChannelName(), JSON.toJSONString(params));
        //将订单改为支付中
        rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrder.getPayOrderId(), null);
        HttpRequestTemplate requestTemplate = new HttpRequestTemplate(null);
        JSONObject origin = requestTemplate.postForObject(config.getRequestUrl()+ZhongfuConfig.REQ_PAY_URL_FIXX, params, JSONObject.class);
        _log.info("{}响应数据:{}", getChannelName(), JSON.toJSONString(origin));
        // 响应对象
        JSONObject retObj = new JSONObject();
        if(StringUtils.equals("success",origin.getString("result"))){
            JSONObject data = origin.getJSONObject("data");
                return  buildPayResultOfForm(retObj, payOrder,"<script type=\"text/javascript\">\n" +
                        "　　window.location.href=\""+ data.getString("trade_qrcode")+"\";\n" +
                        "　　</script>" );

        }
        retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
        retObj.put(PayConstant.RESULT_PARAM_ERRDES, origin.toJSONString());
        return retObj;

   
    } 

    //设置请求参数
    private void setParams(PayOrder payOrder, ZhongfuConfig config,Map<String,Object> params){

        params.put("mer_id", config.getAppId());
        params.put("businessnumber", payOrder.getPayOrderId());
        params.put("amount", payOrder.getAmount());
        params.put("terminal", config.getPayMode());
        params.put("version", "01");
        params.put("goodsName", payOrder.getPayOrderId());
        params.put("timestamp", DateUtil.date2Str(new Date()));
        params.put("ServerUrl", payConfig.getNotifyUrl(getChannelName()));
        if(StringUtils.equals(null,payOrder.getReturnUrl())||payOrder.getReturnUrl().length()==0){
            params.put("backurl", "http://erro.test.com/erro.html");
        }else {
            params.put("backurl", payOrder.getReturnUrl());
        }

        params.put("failUrl", "http://erro.test.com/erro.html");
        params.put("sign", getSign(params, config.getPrivateKey()));
        params.put("sign_type", "md5");
    }
    //获取sign
    private String getSign(Map<String,Object> params,String key){
        String signTxt = SignUtils.parameterText(params);
        _log.info("{}待签名字符串：{}", logPrefix, signTxt+"&"+key);
        String sign = SignUtils.MD5.createSign(signTxt, "&"+key, SignUtils.CHARSET_UTF8).toUpperCase();
        return sign;
    }
}

