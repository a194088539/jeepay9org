package org.jeepay.pay.channel.kqpay;

import org.jeepay.pay.channel.BasePayConfig;
import org.springframework.stereotype.Component;

/**
 * @author: aragom
 * @date: 18/1/29
 * @description: 快钱配置
 */
@Component
public class KqpayConfig extends BasePayConfig{

    // 商户ID
    private String merchantAcctId;
    // 商户私钥证书路径
    private String privateCertPath;
    // 快钱公钥证书路径
    private String publicCertPath;
    // 证书密码
    private String keyPwd;

    private String showUrl;

    public KqpayConfig(){}

    public KqpayConfig (String payParam) {
    	  super(payParam);
        this.merchantAcctId = object.getString("merchantAcctId");
        this.privateCertPath = object.getString("privateCertPath");
        this.publicCertPath = object.getString("publicCertPath");
        this.keyPwd = object.getString("keyPwd");
        this.showUrl = object.getString("showUrl");
    }

    public String getMerchantAcctId() {
        return merchantAcctId;
    }
    public void setMerchantAcctId(String merchantAcctId) {
        this.merchantAcctId = merchantAcctId;
    }
    public String getPrivateCertPath() {
        return privateCertPath;
    }

    public void setPrivateCertPath(String privateCertPath) {
        this.privateCertPath = privateCertPath;
    }

    public String getPublicCertPath() {
        return publicCertPath;
    }

    public void setPublicCertPath(String publicCertPath) {
        this.publicCertPath = publicCertPath;
    }

    public String getKeyPwd() {
        return keyPwd;
    }

    public void setKeyPwd(String keyPwd) {
        this.keyPwd = keyPwd;
    }

    public String getShowUrl() {
        return showUrl;
    }

    public void setShowUrl(String showUrl) {
        this.showUrl = showUrl;
    }
}
