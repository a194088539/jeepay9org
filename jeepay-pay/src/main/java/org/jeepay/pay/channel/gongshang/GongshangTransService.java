package org.jeepay.pay.channel.gongshang;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.sun.javafx.collections.MappingChange;
import org.springframework.stereotype.Component;
import org.jeepay.common.http.HttpRequestTemplate;
import org.jeepay.common.util.Util;
import org.jeepay.common.util.sign.SignUtils;
import org.jeepay.common.util.sign.encrypt.Base64;
import org.jeepay.common.util.str.StringUtils;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.AmountUtil;
import org.jeepay.core.entity.TransOrder;
import org.jeepay.pay.channel.BaseTrans;
import org.jeepay.pay.channel.zhifu.ZhifuConfig;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Component
public class GongshangTransService extends BaseTrans {
    private final static String logPrefix = "【工商代付】";

    @Override
    public String getChannelName() {
        return GsConfig.CHANNEL_NAME;
    }

    @Override
    public JSONObject balance(String payParam) {
        JSONObject retObj = buildRetObj();
        retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
        retObj.put(PayConstant.RESULT_PARAM_ERRDES, "没有余额查询接口");
        return retObj;
    }
    /**
     * 代付订单
     *
     * @param transOrder
     * @return
     */
    @Override
    public JSONObject trans(TransOrder transOrder) {
        GsConfig config = new GsConfig(getTransParam(transOrder));
        JSONObject req = new JSONObject();
        req.put("mchId",config.getMchId());
        req.put("userId",config.getAppId());
        req.put("pch",transOrder.getMchTransNo());
        req.put("payAcntNo",config.getPartnerId());
        req.put("payAcntName",config.getPayMode());
        JSONObject data = new JSONObject();
        data.put("rid",transOrder.getTransOrderId());
        data.put("recAcntNo",transOrder.getAccountNo());
        data.put("recAcntName",transOrder.getAccountName());
        data.put("recBankName",transOrder.getBankName());
        data.put("je", AmountUtil.convertCent2Dollar(String.valueOf(transOrder.getAmount())));
        JSONArray dataArray = new JSONArray();
        dataArray.add(data);
        req.put("data",dataArray);
        String payent = req.toJSONString();
        _log.info("{}申请代付请求参数：{}", logPrefix, req.toJSONString());
        _log.info("{}申请代付异步回调地址：{}", logPrefix, payConfig.getNotifyTransUrl(getChannelName()));
        String payentb = null;
        try {
            String key = Base64.encode(payent.getBytes(SignUtils.CHARSET_UTF8));
            payentb = Base64.encode(key.getBytes(SignUtils.CHARSET_UTF8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        _log.info("{}申请代付请求加密参数：{}", logPrefix, payentb);
        HttpRequestTemplate requestTemplate = new HttpRequestTemplate(null);
        JSONObject origin = requestTemplate.postForObject(config.getRequestUrl(), payentb, JSONObject.class);
        _log.info("{}申请代付响应结果：{}", logPrefix, origin.toString());
        JSONObject retObj = buildRetObj();
        retObj.put("isSuccess", false);
        retObj.put("transOrderId", transOrder.getTransOrderId());
        if (StringUtils.equals(origin.getString("result"),"7")){
                retObj.put("channelOrderNo", "");
                retObj.put("isSuccess", true);
                // 1. 处理中 2. 成功 3. 失败
                retObj.put("status", 1);
                return retObj;
            }
        // 1. 处理中 2. 成功 3. 失败
        retObj.put("status", 3);
        retObj.put(PayConstant.RESULT_PARAM_ERRDES, "下单失败[" + origin.getString("retMsg") + "]");
        retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
        return retObj;
    }

    /**
     * 订单查询接口
     *
     * @param transOrder
     * @return
     */
    @Override
    public JSONObject query(TransOrder transOrder) {
        JSONObject retObj = buildRetObj();
        retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
        retObj.put(PayConstant.RESULT_PARAM_ERRDES, "没有查询接口");
        return retObj;
    }
}
