package org.jeepay.pay.channel.yldevpay;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;

@Component
public class YldevpayConfig {
    public static final String CHANNEL_NAME = "yldevpay";
    public static final String CHANNEL_NAME_PAY = CHANNEL_NAME + "_pay";
    public static final String CHANNEL_NAME_BATCH_PAY = CHANNEL_NAME + "_batchPay";
    public static final String RETURN_VALUE_SUCCESS = "200";
    // 返回上游成功
    public static final String RESPONSE_RESULT_SUCCESS = "success";
    // 返回上游失败
    public static final String RESPONSE_RESULT_FAIL = "fail";
    // 商户ID
    private String mchId;
    // 用户ID
    private String userName;
    //私钥
    private String privateKey;
    //私钥密码
    private String privateKeyPass;
    //公钥
    private String publicKey;
    // 请求地址
    private String reqUrl;

    public YldevpayConfig(){}

    public YldevpayConfig(String payParam) {
        Assert.notNull(payParam, "init ylpay config error");
        JSONObject object = JSONObject.parseObject(payParam);
        this.mchId = object.getString("mchId");
        this.userName = object.getString("userName");
        this.privateKey = object.getString("privateKey");
        this.privateKeyPass = object.getString("privateKeyPass");
        this.publicKey = object.getString("publicKey");
        this.reqUrl = object.getString("reqUrl");
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPrivateKeyPass() {
        return privateKeyPass;
    }

    public void setPrivateKeyPass(String privateKeyPass) {
        this.privateKeyPass = privateKeyPass;
    }
}
