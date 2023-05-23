package org.jeepay.pay.channel.arya;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.Assert;
import org.jeepay.common.unify.AbstractPaymentConfig;

public class AryaConfig extends AbstractPaymentConfig {

    public static final String CHANNEL_NAME = "arya";

    public static final String REQ_PAY_URL_FIXX = "/api/v1/getway";
    AryaConfig(String payParam){
        Assert.notNull(payParam, "init arya config error");
        JSONObject object = JSON.parseObject(payParam);
        setAppId(object.getString("appId"));
        setPayMode(object.getString("payType"));
        setPrivateKey(object.getString("privateKey"));
        setPublicKey(object.getString("publicKey"));
        setRequestUrl(object.getString("requestUrl"));

    }
}
