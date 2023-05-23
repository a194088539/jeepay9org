package org.jeepay.pay.channel.zhongfu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.Assert;
import org.jeepay.common.unify.AbstractPaymentConfig;

public class ZhongfuConfig extends AbstractPaymentConfig {

    public static final String CHANNEL_NAME = "zhongfu";

    public static final String REQ_PAY_URL_FIXX = "/customer.pay";
    ZhongfuConfig(String payParam){
        Assert.notNull(payParam, "init zhongfu config error");
        JSONObject object = JSON.parseObject(payParam);
        setAppId(object.getString("appId"));
        setPayMode(object.getString("payType"));
        setPrivateKey(object.getString("privateKey"));
        setPublicKey(object.getString("publicKey"));
        setRequestUrl(object.getString("requestUrl"));
        setExtInfo(object.getString("serviceIp"));

    }
}
