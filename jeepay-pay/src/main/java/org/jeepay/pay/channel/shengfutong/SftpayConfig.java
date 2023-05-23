package org.jeepay.pay.channel.shengfutong;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.jeepay.common.unify.AbstractPaymentConfig;

/**
 * @Package org.jeepay.pay.channel.shengfutong
 * @Class: SftpayConfig.java
 * @Description:
 * @Author leo
 * @Date 2019/4/8 11:34
 * @Version
 **/
@Component
@Data
@ToString
@NoArgsConstructor
public class SftpayConfig extends AbstractPaymentConfig {
    public static final String CHANNEL_NAME = "sftpay";
    public static final String CHANNEL_NAME_SFT_ALIPAY_WAP = CHANNEL_NAME + "_aliwap";
    public static final String CHANNEL_NAME_SFT_ALIPAY_QR = CHANNEL_NAME + "_aliqr";
    public static final String CHANNEL_NAME_SFT_WXPAY = CHANNEL_NAME + "_wxpay";
    public static final String CHANNEL_NAME_SFT_WXPAY_SCAN = CHANNEL_NAME + "_wxpay_scan";
    public static final String CHANNEL_NAME_SFT_QUICK_WAP = CHANNEL_NAME + "_quick_wap";
    public static final String PAY_PRODUCTNAME = "皮鞋";
    public static final String RETURN_VALUE_SUCCESS = "00";
    public static final String RETURN_VALUE_FAIL = "0"; // 失败返回码
    public static final String RESPONSE_RESULT_SUCCESS = "success"; // 返回上游成功
    public static final String RESPONSE_RESULT_OK = "OK"; // 返回上游成功
    public static final String RESPONSE_RESULT_FAIL = "fail";       // 返回上游失败

    public static final String ORDER_STATUS_SUCCESS = "SUCCESS";
    public static final String ORDER_PAY_STATUS_SUCCESS = "1";

    public SftpayConfig(String payParam) {
        Assert.notNull(payParam, "init sft config error");
        JSONObject object = JSON.parseObject(payParam);
        setMchId(object.getString("mchId"));
        setPrivateKey(object.getString("privateKey"));
        setRequestUrl(object.getString("requestUrl"));
        // 支付方式1支付宝2微信
        setPayMode(object.getString("payMode"));
        setAppId(object.getString("token"));
        setResponseType(object.getString("dataType"));
    }


}
