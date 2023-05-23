package org.jeepay.pay.beisite;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.Assert;
import org.jeepay.common.unify.AbstractPaymentConfig;

public class BeisiteConfig extends AbstractPaymentConfig {
    public static final String CHANNEL_NAME = "beisite";

    public static final String REQ_PAY_URL_FIXX = "/api/orders/index.html";
    BeisiteConfig(String payParam){
        Assert.notNull(payParam, "init beisite config error");
        JSONObject object = JSON.parseObject(payParam);
        setAppId(object.getString("appId"));
        setPayMode(object.getString("payType"));
        setPrivateKey(object.getString("privateKey"));
        setPublicKey(object.getString("publicKey"));
        setRequestUrl(object.getString("requestUrl"));

    }
}
