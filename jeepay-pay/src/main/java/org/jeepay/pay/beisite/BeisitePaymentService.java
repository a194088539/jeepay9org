package org.jeepay.pay.beisite;

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

import java.util.Map;

@Service
public class BeisitePaymentService extends BasePayment {
    private final static String logPrefix = "【贝斯特支付】";
    @Override
    public String getChannelName() {
        return BeisiteConfig.CHANNEL_NAME;
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
        BeisiteConfig config = new BeisiteConfig(getPayParam(payOrder));
        Map<String,Object> params = Maps.newHashMap();
        //设置请求参数
        setParams(payOrder, config, params);
        _log.info("{}请求数据:{}", getChannelName(), JSON.toJSONString(params));
        //将订单改为支付中
        rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrder.getPayOrderId(), null);

//        JSONObject retObj = new JSONObject();
//        String payFrom = HtmlUtils.form(config.getRequestUrl() + BeisiteConfig.REQ_PAY_URL_FIXX, HtmlUtils.POST, params);
//        return buildPayResultOfForm(retObj,payOrder,payFrom);
        HttpRequestTemplate requestTemplate = new HttpRequestTemplate(null);
        JSONObject origin = requestTemplate.postForObject(config.getRequestUrl()+BeisiteConfig.REQ_PAY_URL_FIXX, params, JSONObject.class);
        _log.info("{}响应数据:{}", getChannelName(), JSON.toJSONString(origin));
        // 响应对象
        JSONObject retObj = new JSONObject();
        if(StringUtils.equals("1",origin.getString("code"))){
            String pay_url = origin.getJSONObject("data").getString("pay_url");
            return  buildPayResultOfForm(retObj, payOrder,"<script type=\"text/javascript\">\n" +
                    "　　window.location.href=\""+pay_url+"\";\n" +
                    "　　</script>" );

        }
        retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
        retObj.put(PayConstant.RESULT_PARAM_ERRDES, origin.toJSONString());
        return retObj;
    }

    //设置请求参数
    private void setParams(PayOrder payOrder, BeisiteConfig config,Map<String,Object> params){

        params.put("mch_id", config.getAppId());
        params.put("out_order_id", payOrder.getPayOrderId());
        params.put("price", AmountUtil.convertCent2Dollar(String.valueOf(payOrder.getAmount())));
        params.put("type", config.getPayMode());
        params.put("notifyurl", payConfig.getNotifyUrl(getChannelName()));
        params.put("returnurl", payOrder.getReturnUrl());
        params.put("sign", getSign(params, config.getPrivateKey()));
    }
    //获取sign
    private String getSign(Map<String,Object> params,String key){
        String signTxt = SignUtils.parameterText(params);
        _log.info("{}待签名字符串：{}", logPrefix, signTxt+"&key="+key);
        String sign = SignUtils.MD5.createSign(signTxt, "&key="+key, SignUtils.CHARSET_UTF8).toUpperCase();
        return sign;
    }
}
