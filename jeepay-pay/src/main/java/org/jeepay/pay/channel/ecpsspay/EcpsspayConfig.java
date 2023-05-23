package org.jeepay.pay.channel.ecpsspay;

import org.jeepay.core.common.constant.PayConstant;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;

@Component
public class EcpsspayConfig {
    //汇潮支付
    public static final String CHANNEL_NAME = "ecpsspay";
    public static final String PAY_CHANNEL_ECPSSPAY_B2CCREDIT = CHANNEL_NAME + "_B2CCredit";						// 汇潮支付B2C信用卡
    public static final String PAY_CHANNEL_ECPSSPAY_B2CDEBIT = CHANNEL_NAME + "_B2CDebit";							// 汇潮支付B2C借记卡
    public static final String PAY_CHANNEL_ECPSSPAY_NOCARD= CHANNEL_NAME + "_noCard";								// 汇潮支付银联快捷支付
    public static final String PAY_CHANNEL_ECPSSPAY_UNIONSCANPAY_H5= CHANNEL_NAME + "_UnionScanPay_H5";			// 汇潮支付云闪付H5借记卡支付
    public static final String PAY_CHANNEL_ECPSSPAY_UNIONSCANCREDITPAY_H5= CHANNEL_NAME + "_UnionScanCreditPay_H5";// 汇潮支付云闪付H5贷记卡支付
    public static final String PAY_CHANNEL_ECPSSPAY_MANUALPAY= CHANNEL_NAME + "_manualPay";						// 汇潮支付B2B转账
    public static final String PAY_CHANNEL_ECPSSPAY_B2B= CHANNEL_NAME + "_B2B";									// 汇潮支付企业网银支付
    public static final String PAY_CHANNEL_ECPSSPAY_INSTALLMENT= CHANNEL_NAME + "_Installment";					// 汇潮支付分期支付
	public final static String PAY_CHANNEL_ECPSSPAY_GATEWAY = CHANNEL_NAME + "_gateway"; 							// 汇潮支付(跳转网关快捷)
	public final static String PAY_CHANNEL_ECPSSPAY_AGENTPAY = CHANNEL_NAME + "_agentpay"; 						// 汇潮代付
    // 返回上游成功
    public static final String RESPONSE_RESULT_SUCCESS = "success";
    // 返回上游失败
    public static final String RESPONSE_RESULT_FAIL = "fail";
    // 商户ID
    private String mchId;
    //私钥
    private String mchKey;
    // 请求地址
    private String reqUrl;

    public EcpsspayConfig(){}

    public EcpsspayConfig(String payParam) {
        Assert.notNull(payParam, "init Ecpsspay config error");
        JSONObject object = JSONObject.parseObject(payParam);
        this.mchId = object.getString("mchId");
        this.mchKey = object.getString("mchKey");
        this.reqUrl = object.getString("reqUrl");
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

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }

    @Override
    public String toString() {
        return "EcpsspayConfig{" + "mchId='" + mchId + '\'' + ", mchKey='" + mchKey + '\'' + ", reqUrl='" + reqUrl
            + '\'' + '}';
    }
}
