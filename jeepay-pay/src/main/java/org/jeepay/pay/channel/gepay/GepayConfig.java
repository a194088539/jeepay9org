package org.jeepay.pay.channel.gepay;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author: aragom
 * @date: 18/3/1
 * @description: 个付通配置
 */
@Component
public class GepayConfig {

    public static final String CHANNEL_NAME = "gepay";
    public static final String CHANNEL_NAME_WXPAY_QR = CHANNEL_NAME + "_wxpay_qr";
    public static final String CHANNEL_NAME_ALIPAY_QR = CHANNEL_NAME + "_alipay_qr";
    public static final String CHANNEL_NAME_ALIPAY_H5 = CHANNEL_NAME + "_alipay_h5";
    public static final String CHANNEL_NAME_ALIPAY_PC = CHANNEL_NAME + "_alipay_pc";

    public static final String RETURN_VALUE_SUCCESS = "success";
    public static final String RETURN_VALUE_FAIL = "fail";

    // 商户ID
    private String mchId;
    // 商户Key
    private String key;
    // 请求地址
    private String reqUrl;

    public GepayConfig(){}

    public GepayConfig(String payParam) {
        Assert.notNull(payParam, "init gepay config error");
        JSONObject object = JSONObject.parseObject(payParam);
        this.mchId = object.getString("mchId");
        this.key = object.getString("key");
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

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }
}
