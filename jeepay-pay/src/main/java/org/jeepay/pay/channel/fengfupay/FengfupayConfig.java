package org.jeepay.pay.channel.fengfupay;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;


@Component
public class FengfupayConfig {
    public static final String CHANNEL_NAME = "fengfupay";
    public static final String CHANNEL_NAME_DMF = CHANNEL_NAME + "_dmf";
    public static final String CHANNEL_NAME_WAP = CHANNEL_NAME + "_wap";
    public static final String CHANNEL_NAME_TRADE = CHANNEL_NAME + "_trade";
    public static final String CHANNEL_NAME_FF = CHANNEL_NAME + "_ff";
    public static final String RETURN_VALUE_SUCCESS = "200";
    // 返回上游成功
    public static final String RESPONSE_RESULT_SUCCESS = "success";
    public static final String RETURN_VALUE_FAIL = "fail";
    // 商户ID
    private String mchId;
    // 商户Key
    private String key;
    // 版本：1.0用户版  2.0商户版
    private String version;
    // 请求地址
    private String reqUrl;

    public FengfupayConfig(){}

    public FengfupayConfig(String payParam) {
        Assert.notNull(payParam, "init Fengfupay config error");
        JSONObject object = JSONObject.parseObject(payParam);
        this.mchId = object.getString("mchId");
        this.key = object.getString("key");
        this.version = object.getString("version");
        this.reqUrl = object.getString("reqUrl");
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }
}
