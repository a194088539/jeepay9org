package org.jeepay.pay.channel.gomepay;

import org.jeepay.pay.channel.BasePayConfig;
import org.springframework.stereotype.Component;

/**
 * @author: aragom
 * @date: 18/3/20
 * @description: 银盈通配置
 */
@Component
public class GomepayConfig extends BasePayConfig{

    // 商户号
    private String merchno;
    // 商户名称
    private String merchname;
    // 电商钱包ID
    private String mediumno;
    // 钱包密钥
    private String mediumkey;

    public GomepayConfig(){}

    public GomepayConfig(String payParam) {
  	    super(payParam);
        this.merchno = object.getString("merchno");
        this.merchname = object.getString("merchname");
        this.mediumno = object.getString("mediumno");
        this.mediumkey = object.getString("mediumkey");
    }

    public String getMerchno() {
        return merchno;
    }

    public void setMerchno(String merchno) {
        this.merchno = merchno;
    }

    public String getMerchname() {
        return merchname;
    }

    public void setMerchname(String merchname) {
        this.merchname = merchname;
    }

    public String getMediumno() {
        return mediumno;
    }

    public void setMediumno(String mediumno) {
        this.mediumno = mediumno;
    }

    public String getMediumkey() {
        return mediumkey;
    }

    public void setMediumkey(String mediumkey) {
        this.mediumkey = mediumkey;
    }
}
