package org.jeepay.pay.channel.yinyingtong;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.jeepay.common.unify.AbstractPaymentConfig;

@Component
@Data
@ToString
@NoArgsConstructor
public class YytpayConfig extends AbstractPaymentConfig {
    public static final String CHANNEL_NAME = "yytpay";
    //应用版本
    public static final String APP_VERSION = "1.0.0";
    //服务号
    public static final String SERVICE_CODE = "sne_00000000002";
    //平台
    public static final String PLAT_FORM = "01";
    //付款方密码类型
    public static final String PASS_TYPE = "02";
    //币种
    public static final String CURRENCY = "CNY";

    //请求返回成功
    public static final String REP_SUCCESS= "000";


    public YytpayConfig(String payParam) {
        Assert.notNull(payParam, "init sft config error");
        JSONObject object = JSON.parseObject(payParam);
        //商户号
        setMchId(object.getString("mchId"));
        //应用号
        setPartnerId(object.getString("appCode"));
        //服务号
        setPublicKey(object.getString("serviceCode"));
        //支付密码
        setPrivateKey(object.getString("payPassword"));
        //请求地址
        setRequestUrl(object.getString("requestUrl"));
        //登陆令牌
        setAppId(object.getString("loginToken"));
        //付款钱包id
        setPayProduct(object.getString("walletId"));
        //付款资产id
        setPayMode(object.getString("assetId"));
        //
        setPrivateStorePathPwd(object.getString("payPass"));
    }
}
