package org.jeepay.pay.channel.alipay;

import org.jeepay.pay.channel.BasePayConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import org.apache.commons.lang3.StringUtils;
/**
 * @author: aragom
 * @date: 17/8/21
 * @description:
 */
@Component
public class AlipayConfig extends BasePayConfig {
    private String pid;             // 合作伙伴身份partner
    private String appId;           // 应用App ID
    private String privateKey;      // 应用私钥
    private String alipayPublicKey; // 支付宝公钥
    private String alipayAccount;   // 支付宝账号
    private String reqUrl;          // 请求网关地址

    private String certPath;	    // 应用公钥证书地址
    private String alipayPublicCertPath;    // 支付宝公钥证书地址
    private String rootCertPath;    // 支付宝根证书地址

    public static String SIGNTYPERSA = "RSA";    // RSA2
    public static String SIGNTYPE = "RSA2";    // RSA2
    public static String CHARSET = "UTF-8";    // 编码
    public static String FORMAT = "json";    // 返回格式
    
    public AlipayConfig(){}

    public AlipayConfig(String payParam) {
        Assert.notNull(payParam, "init alipay config error");
        JSONObject object = JSON.parseObject(payParam);
        this.pid = object.getString("pid");
        this.appId = object.getString("appId");
        this.privateKey = object.getString("privateKey");
        this.alipayPublicKey = object.getString("alipayPublicKey");
        this.alipayAccount = object.getString("alipayAccount");
        this.reqUrl = object.getString("reqUrl");
        this.certPath = object.getString("certPath");
        this.alipayPublicCertPath = object.getString("alipayPublicCertPath");
        this.rootCertPath = object.getString("rootCertPath");
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getAlipayPublicKey() {
        return alipayPublicKey;
    }

    public void setAlipayPublicKey(String alipayPublicKey) {
        this.alipayPublicKey = alipayPublicKey;
    }

    public String getAlipayAccount() {
        return alipayAccount;
    }

    public void setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount;
    }

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }

    public String getCertPath() {
        return certPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }

    public String getAlipayPublicCertPath() {
        return alipayPublicCertPath;
    }

    public void setAlipayPublicCertPath(String alipayPublicCertPath) {
        this.alipayPublicCertPath = alipayPublicCertPath;
    }

    public String getRootCertPath() {
        return rootCertPath;
    }

    public void setRootCertPath(String rootCertPath) {
        this.rootCertPath = rootCertPath;
    }
    
}

