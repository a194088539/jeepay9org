package org.jeepay.pay.channel.zhifu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.Assert;
import org.jeepay.common.unify.AbstractPaymentConfig;

public class ZhifuConfig extends AbstractPaymentConfig {
    public static final String CHANNEL_NAME = "zhifu";
    public static final String CHANMEL_NAME_ZHIFU_QUICK = CHANNEL_NAME + "_quick";
    public static final String CHANMEL_NAME_JD_QR = CHANNEL_NAME + "_jd_qr";
    public static final String CHANMEL_NAME_UNION_QR = CHANNEL_NAME + "_union_qr";
    public static final String CHANMEL_NAME_WX_QR = CHANNEL_NAME + "_wx_qr";

    public static final String RETURN_VALUE_SUCCESS = "S";
    public static final String RESPONSE_RESULT_FAIL = "fail";
    public static final String RESPONSE_RESULT_OK = "SUCCESS"; // 返回上游成功
    public static final String REQ_URL = "/sdk/json.do";
    public static final String RESPONSE_RESULT_SUCCESS = "SUCCESS"; // 返回上游成功

    ZhifuConfig(String payParam ){
        Assert.notNull(payParam, "init sft config error");
        JSONObject object = JSON.parseObject(payParam);
        setAppId(object.getString("appId"));
        setPrivateKey(object.getString("privatekey"));
        setRequestUrl(object.getString("requestUrl"));
    }

    public String getUrl(String domain,String Suffix){
        return domain+Suffix;
    }
}
