package org.jeepay.pay.channel.zhifu;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;
import org.jeepay.common.http.HttpRequestTemplate;
import org.jeepay.common.util.sign.SignUtils;
import org.jeepay.common.util.str.StringUtils;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.AmountUtil;

import org.jeepay.core.entity.TransOrder;
import org.jeepay.pay.channel.BaseTrans;



import java.util.Map;

@Component
public class ZhifuTransService extends BaseTrans {
    private final static String logPrefix = "【智付】";

    @Override
    public String getChannelName() {
        return ZhifuConfig.CHANNEL_NAME;
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
        JSONObject retObj = buildRetObj();
        ZhifuConfig config = new ZhifuConfig(getTransParam(transOrder));
        Map<String, Object> params = Maps.newHashMap();
        setParams(transOrder,config,"cashGateway",params,"pay");
        _log.info("{}申请代付请求参数：{}", logPrefix, params.toString());
        HttpRequestTemplate requestTemplate = new HttpRequestTemplate(null);
        JSONObject origin = requestTemplate.postForObject(getReqUrl(config.getRequestUrl(),ZhifuConfig.REQ_URL), params, JSONObject.class);
        retObj.put("isSuccess", false);
        retObj.put("transOrderId", transOrder.getTransOrderId());
        if(StringUtils.equals(origin.getString("status"),"0000")){
            retObj.put("channelOrderNo", "");
            retObj.put("isSuccess", true);
            // 1. 处理中 2. 成功 3. 失败
            retObj.put("status", 1);
            return retObj;
        }
        // 1. 处理中 2. 成功 3. 失败
        retObj.put("status", 3);
        retObj.put(PayConstant.RESULT_PARAM_ERRDES, "下单失败[" + origin.getString("info") + "]");
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
        ZhifuConfig config = new ZhifuConfig(getTransParam(transOrder));
        String payOrderId = transOrder.getTransOrderId();
        _log.info("{}开始查询代付通道订单,payOrderId={}", logPrefix, payOrderId);
        Map<String, Object> params = Maps.newHashMap();
        setParams(transOrder,config,"cashOrderQuery",params,"");
        _log.info("{}代付查询请求参数：{}", logPrefix, params.toString());
        HttpRequestTemplate requestTemplate = new HttpRequestTemplate(null);
        JSONObject origin = requestTemplate.postForObject(getReqUrl(config.getRequestUrl(),ZhifuConfig.REQ_URL), params, JSONObject.class);
        _log.info("{}代付查询响应参数：{}", logPrefix, origin.toJSONString());
        JSONObject retObj = buildRetObj();
        if(StringUtils.equals(origin.getString("status"),"0000")){
            retObj.put("isSuccess", true);
            retObj.put("transOrderId", transOrder.getTransOrderId());
            retObj.putAll(origin);

            JSONObject backData = JSONObject.parseObject(origin.getString("backData"));
            /**
             *  status状态说明 S-成功;F-失败;U-未知
             */
            String status = backData.getString("result");
            if (StringUtils.equals(status,"S")) {
                retObj.put("status", 2); // 2表示成功
            } else if (StringUtils.equals(status,"F")) {
                retObj.put("status", 3); // 3表示失败
            } else {
                retObj.put("status", 1); // 1处理中
            }
            retObj.put("channelOrderNo", origin.getString("cashOrderId"));
            retObj.put("channelObj", origin);
            return retObj;
        }
        retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
        retObj.put(PayConstant.RESULT_PARAM_ERRDES, origin.getString("info"));
        retObj.put("channelErrCode", origin.getString("status"));
        retObj.put("isSuccess", false);
        return retObj;
    }

    //设置请求主体参数
    private void setParams(TransOrder transOrder, ZhifuConfig config,  String tradeId, Map<String,Object> map,String type){

        map.put("tradeId", tradeId);
        map.put("ver", "1.0");
        String tradeData;
        if(StringUtils.equals(type,"pay")){
            //代付
            tradeData = getTradeData(transOrder, config);
        }else{
            //代付查询
            tradeData = getTradeData_query(transOrder, config);
        }
        map.put("tradeData", tradeData);
        map.put("tradeSign", getSign(tradeData, config.getPrivateKey()));

    }

    //获取代付下单报文
    private String getTradeData(TransOrder transOrder,ZhifuConfig config){

        JSONObject jsondata = new JSONObject();

        jsondata.put("merId", config.getAppId());
        jsondata.put("orderId", transOrder.getTransOrderId());
        jsondata.put("amount", AmountUtil.convertCent2Dollar(String.valueOf(transOrder.getAmount())));
        jsondata.put("toPublic", "0");
        jsondata.put("bankAcc", transOrder.getAccountNo());
        jsondata.put("bankAccName", transOrder.getAccountName());
        jsondata.put("bankBranch",transOrder.getBankName() );
        jsondata.put("bankProvince", transOrder.getProvince());
        jsondata.put("bankCity", transOrder.getCity());
        jsondata.put("notifyUrl", payConfig.getNotifyTransUrl(getChannelName()));
        jsondata.put("cnapsCode","011881001009");
        return jsondata.toJSONString();
    }

    //获取代付订单查询报文
    private String getTradeData_query(TransOrder transOrder,ZhifuConfig config){

        JSONObject jsonData = new JSONObject();

        jsonData.put("merId", config.getAppId());
        jsonData.put("orderId", transOrder.getTransOrderId());

        return jsonData.toJSONString();
    }

    //获取签名字符串
    private String getSign(String tradeData,String key){
        _log.info("{}智付签名字符串：{}", logPrefix, tradeData);
        String sign = SignUtils.MD5.createSign(tradeData, "&"+key, SignUtils.CHARSET_UTF8);
        return sign.toUpperCase();
    }
}
