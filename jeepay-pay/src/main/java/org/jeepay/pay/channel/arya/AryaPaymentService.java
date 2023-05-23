package org.jeepay.pay.channel.arya;

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
public class AryaPaymentService extends BasePayment {

    private final static String logPrefix = "【ARYA云支付】";
    @Override
    public String getChannelName() {
        return AryaConfig.CHANNEL_NAME;
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
        AryaConfig config = new AryaConfig(getPayParam(payOrder));
        Map<String,Object> params = Maps.newHashMap();
        //设置请求参数
        setParams(payOrder, config, params);
        _log.info("{}请求数据:{}", getChannelName(), JSON.toJSONString(params));
        //将订单改为支付中
        rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrder.getPayOrderId(), null);
        JSONObject retObj = new JSONObject();
        String payForm = HtmlUtils.form(config.getRequestUrl()+AryaConfig.REQ_PAY_URL_FIXX, HtmlUtils.POST, params);

        return buildPayResultOfForm(retObj, payOrder, payForm);

   
    } 

    //设置请求参数
    private void setParams(PayOrder payOrder, AryaConfig config,Map<String,Object> params){

        params.put("version", "3.0");
        params.put("method", "Gt.online.interface");
        params.put("partner", config.getAppId());
        params.put("paymoney", AmountUtil.convertCent2Dollar(String.valueOf(payOrder.getAmount())));
        params.put("banktype", config.getPayMode());
        params.put("ordernumber", payOrder.getPayOrderId());
        params.put("callbackurl", payConfig.getNotifyUrl(getChannelName()));
        params.put("hrefbackurl", payOrder.getReturnUrl());
        params.put("sign", getSign(params, config.getPrivateKey()));
    }
    //获取sign
    private String getSign(Map<String,Object> params,String key){
        String signTxt = "version=" + params.get("version").toString()//SignUtils.parameterText(params);
                + "&method=" + params.get("method").toString()
                + "&partner=" + params.get("partner")
                + "&banktype=" + params.get("banktype")
                + "&paymoney=" + params.get("paymoney")
                + "&ordernumber=" + params.get("ordernumber")
                + "&callbackurl=" + params.get("callbackurl");
        _log.info("{}待签名字符串：{}", logPrefix, signTxt+key);
        String sign = SignUtils.MD5.createSign(signTxt, key, SignUtils.CHARSET_UTF8);
        return sign;
    }
}

