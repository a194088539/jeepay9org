package org.jeepay.pay.channel.heepay;

import org.jeepay.pay.channel.BasePayConfig;
import org.springframework.stereotype.Component;

/**
 * 汇付宝支付配置
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2018年11月30日下午2:40:40
 */
@Component
public class HeepayConfig extends BasePayConfig{
    // 商户ID
    private String mchId;
    // 签名密钥
    private String singKey;
   
    public HeepayConfig(){}

    public HeepayConfig(String payParam) {
  	  super(payParam);
        this.mchId = object.getString("mchId");
        this.singKey = object.getString("singKey");
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    

    public String getSingKey() {
		return singKey;
	}

	public void setSingKey(String singKey) {
		this.singKey = singKey;
	}

}
