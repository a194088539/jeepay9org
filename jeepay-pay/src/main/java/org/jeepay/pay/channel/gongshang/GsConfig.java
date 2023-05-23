package org.jeepay.pay.channel.gongshang;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.Assert;
import org.jeepay.common.unify.AbstractPaymentConfig;

public class GsConfig extends AbstractPaymentConfig {

    public static final String CHANNEL_NAME = "gongshang";


    GsConfig(String payParam ){
        Assert.notNull(payParam, "init sft config error");
        JSONObject object = JSON.parseObject(payParam);
        //商户机构ID
        setMchId(object.getString("mchId"));
        //商户用户ID
        setAppId(object.getString("appId"));

        setRequestUrl(object.getString("requestUrl"));
        //付款方账号
        setPartnerId(object.getString("accountNo"));
        //付款方户名
        setPayMode(object.getString("accountNanme"));


    }
}
