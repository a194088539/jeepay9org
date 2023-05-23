package org.jeepay.pay.channel.ysfpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.jeepay.common.unify.AbstractPaymentConfig;

/**
 * @Package org.jeepay.pay.channel.ysfpay
 * @Class: YsfpayConfig.java
 * @Description:
 * @Author leo
 * @Date 2019/6/24 12:32
 * @Version
 **/
@Component
@Data
@ToString
@NoArgsConstructor
public class YsfpayConfig extends AbstractPaymentConfig {
    public static final String CHANNEL_NAME = "ysfpay";
    public static final String CHANNEL_NAME_YSF_GEMA = CHANNEL_NAME + "_gema";
    public static final String RETURN_VALUE_SUCCESS = "success";
    public static final String RETURN_VALUE_FAIL = "fail";
    public static final String RESPONSE_RESULT_SUCCESS = "success"; // 返回上游成功
    public static final String RESPONSE_RESULT_FAIL = "fail";       // 返回上游失败

    public YsfpayConfig(String payParam) {
        Assert.notNull(payParam, "init ysfpay config error");
        JSONObject object = JSON.parseObject(payParam);
        setMchId(object.getString("mchId"));
        setPrivateKey(object.getString("privateKey"));
        setRequestUrl(object.getString("requestUrl"));
        setPayMode(object.getString("type"));
    }

}
