package org.jeepay.pay.channel.hanyinpay;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class HanyinConfig {

    public static final String CHANNEL_NAME = "hanyinpay";
    public static final String CHANNEL_NAME_QUICK_WAP = CHANNEL_NAME + "_quick_wap";
    public static final String RETURN_VALUE_SUCCESS = "0000";
    public static final String RESPONSE_RESULT_SUCCESS = "success";

    // 商户ID
    private String mchId;
    // 请求地址
    private String reqUrl;
    //私钥证书路径
    private String privateKeyPath;
    //公钥
    private String publicKey;

    public HanyinConfig() {
    }

    public HanyinConfig(String payParam) {
        Assert.notNull(payParam, "init hanyinpay config error");
        JSONObject object = JSONObject.parseObject(payParam);
        this.mchId = object.getString("mchId");
        this.reqUrl = object.getString("reqUrl");
        this.privateKeyPath = object.getString("privateKeyPath");
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }


    public String getPrivateKeyPath() {
        return privateKeyPath;
    }

    public void setPrivateKeyPath(String privateKeyPath) {
        this.privateKeyPath = privateKeyPath;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
