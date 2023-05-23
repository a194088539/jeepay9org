package org.jeepay.pay.channel.jeepaypay;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author: aragom
 * @date: 19/2/13
 * @description: 红包支付通道
 */
@Component
public class JeepaypayConfig {

    public static final String CHANNEL_NAME = "jeepaypay";
    public static final String CHANNEL_NAME_ALIPAY_PC = CHANNEL_NAME + "_alipay_pc";
    public static final String CHANNEL_NAME_ALIPAY_H5 = CHANNEL_NAME + "_alipay_h5";
    public static final String RETURN_VALUE_SUCCESS = "success";
    public static final String RETURN_VALUE_FAIL = "fail";
    public static final Long PAY_ORDER_TIME_OUT = 30 * 60l;         // 订单超时时间，单位秒
    public static final String RESPONSE_RESULT_SUCCESS = "success"; // 返回上游成功
    public static final String RESPONSE_RESULT_FAIL = "fail";       // 返回上游失败

    // 支付宝账号
    private String mchId;
    // 支付宝用户ID
    private String mchKey;

    // H5方式是否自动跳转
    private Boolean autoJump = false;

    public JeepaypayConfig(){}

    public JeepaypayConfig(String payParam) {
        Assert.notNull(payParam, "init " + CHANNEL_NAME + " config error");
        JSONObject object = JSONObject.parseObject(payParam);
        this.mchId = object.getString("mchId");
        this.mchKey = object.getString("mchKey");

        this.autoJump = object.getBooleanValue("autoJump");
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getMchKey() {
        return mchKey;
    }

    public void setMchKey(String mchKey) {
        this.mchKey = mchKey;
    }

    public Boolean getAutoJump() {
        return autoJump;
    }

    public void setAutoJump(Boolean autoJump) {
        this.autoJump = autoJump;
    }
}
