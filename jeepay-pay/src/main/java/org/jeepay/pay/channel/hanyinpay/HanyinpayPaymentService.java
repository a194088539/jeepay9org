package org.jeepay.pay.channel.hanyinpay;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jeepay.common.http.HttpConfigStorage;
import org.jeepay.common.http.HttpRequestTemplate;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.BasePayment;
import org.jeepay.pay.channel.hanyinpay.util.HanyinSignUtil;
import org.jeepay.pay.channel.redpay.RedpayPaymentService;
import org.jeepay.pay.mq.BaseNotify4MchPay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class HanyinpayPaymentService extends BasePayment {

    @Autowired
    public BaseNotify4MchPay baseNotify4MchPay;

    private static final MyLog _log = MyLog.getLog(RedpayPaymentService.class);

    private static AtomicLong pay_seq = new AtomicLong(0L);

    @Override
    public String getChannelName() {
        return HanyinConfig.CHANNEL_NAME;
    }

    @Override
    public String getOrderId(PayOrder payOrder) {
        SimpleDateFormat fm = new SimpleDateFormat("yyMMddHHmmss");
        return String.format("P%sR%04d", fm.format(new Date()), pay_seq.getAndIncrement() % 10000);
    }

    /**
     * 支付
     * @param payOrder
     * @return
     */
    @Override
    public JSONObject pay(PayOrder payOrder) {
//        String channelId = payOrder.getChannelId();
//        JSONObject retObj;
//        switch (channelId) {
//            case HanyinConfig.CHANNEL_NAME_QUICK_WAP :
//                retObj = doQuickWappay(payOrder, "0001");
//                break;
//            default:
//                retObj = buildRetObj(PayConstant.RETURN_VALUE_FAIL, "不支持的渠道[channelId="+channelId+"]");
//                break;
//        }
        return doQuickWappay(payOrder, "0003");
    }

    /**
     * 查询订单
     * @param payOrder
     * @return
     */
    @Override
    public JSONObject query(PayOrder payOrder) {
        // 上游没有查询接口，直接返回
        JSONObject retObj = buildRetObj();
        retObj.put("status", 1);    // 支付中
        return retObj;
    }

    /**
     * 快捷支付
     * @param payOrder
     * @param type
     * @return
     */
    public JSONObject doQuickWappay(PayOrder payOrder, String type) {
        HanyinConfig hanyinConfig = new HanyinConfig(getPayParam(payOrder));
        JSONObject retObj = new JSONObject();
        Map<String,Object> map = new TreeMap();
        map.put("version","1.0.0");
        map.put("transType","SALES");
        map.put("productId",type);
        map.put("merNo",hanyinConfig.getMchId());
        SimpleDateFormat fm = new SimpleDateFormat("yyyyMMdd");
        map.put("orderDate", fm.format(new Date()));
        map.put("orderNo",payOrder.getPayOrderId());
        map.put("notifyUrl",payConfig.getNotifyUrl(getChannelName()));
        map.put("returnUrl",payOrder.getReturnUrl());
        //分为单位如 100 代表  1.00元
        map.put("transAmt",String.valueOf(payOrder.getAmount()));
        map.put("signature", HanyinSignUtil.getSign(map,hanyinConfig.getPrivateKeyPath()));

        _log.info("hanyinPay请求参数:{}", JSONObject.toJSONString(map));
        HttpConfigStorage httpConfigStorage = new HttpConfigStorage();
        httpConfigStorage.setMaxTotal(5);
        httpConfigStorage.setDefaultMaxPerRoute(3);
        HttpRequestTemplate httpRequestTemplate = new HttpRequestTemplate(httpConfigStorage);
        String url = hanyinConfig.getReqUrl() + "/trans-api/trans/api/back.json";
        JSONObject resData = httpRequestTemplate.postForObject(url, map, JSONObject.class);
        _log.info("hanyinPay请求结果:{}", JSONObject.toJSONString(resData ));
        String respCode = resData.getString("respCode");
        String respDesc = resData.getString("respDesc");
        if(!HanyinConfig.RETURN_VALUE_SUCCESS.equals(respCode)) {
            retObj.put("errDes", respDesc);
            retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
            return retObj;
        }

            // 将订单更改为支付中
            int result = rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrder.getPayOrderId(), null);
            _log.info("[{}]更新订单状态为支付中:payOrderId={},channelOrderNo={},result={}", getChannelName(), payOrder.getPayOrderId(), null, result);
            // 支付链接地址
            return buildPayResultOfForm(retObj, payOrder, respDesc);
    }
}
