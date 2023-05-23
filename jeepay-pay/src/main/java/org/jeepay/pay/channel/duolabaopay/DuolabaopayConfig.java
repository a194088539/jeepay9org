package org.jeepay.pay.channel.duolabaopay;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;


@Component
public class DuolabaopayConfig {
    public static final String CHANNEL_NAME = "duolabaopay";
    public static final String CHANNEL_NAME_JD = CHANNEL_NAME + "_JD";
    public static final String RETURN_VALUE_SUCCESS = "1";
    // 返回上游成功
    public static final String RESPONSE_RESULT_SUCCESS = "success";
    public static final String RETURN_VALUE_FAIL = "fail";
    // 商户ID
    private String mchId;
    // 商户ID
    private String shopId;
    // 微信(或支付宝)openId
    private String openId;
    // 虚拟机具序列号
    private String machineNum;
    // 商户Key
    private String publicKey;
    // 商户Key
    private String privateKey;
    // 请求地址
    private String reqUrl;

    public DuolabaopayConfig(){}

    public DuolabaopayConfig(String payParam) {
        Assert.notNull(payParam, "init duolabaopay config error");
        JSONObject object = JSONObject.parseObject(payParam);
        this.mchId = object.getString("mchId");
        this.shopId = object.getString("shopId");
        this.openId = object.getString("openId");
        this.machineNum = object.getString("machineNum");
        this.publicKey = object.getString("publicKey");
        this.privateKey = object.getString("privateKey");
        this.reqUrl = object.getString("reqUrl");
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getMachineNum() {
        return machineNum;
    }

    public void setMachineNum(String machineNum) {
        this.machineNum = machineNum;
    }
}