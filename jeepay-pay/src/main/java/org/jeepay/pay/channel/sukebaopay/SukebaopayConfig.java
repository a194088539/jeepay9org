package org.jeepay.pay.channel.sukebaopay;

import org.jeepay.pay.channel.BasePayConfig;
import org.springframework.stereotype.Component;

/**
 * 数科宝支付配置
 * <p>说明:</p>
 * <li></li>
 * @author aragom
 * @since 2018年11月1日下午3:21:34
 */
@Component
public class SukebaopayConfig extends BasePayConfig{
    public static final String CHANNEL_NAME = "sukebaopay";
    // 商户ID
    private String mchId;
    // 私钥
    private String key;
    //直连模式，根据商户传入的指定渠道编码直接跳转，依赖"bank_code"参数
    private String serviceType = "connect_service";
    //接口版本，门店聚合商户固定值：V2.0(大写)，清算聚合商户固定值：V1.0(大写)
    private String interfaceVersion = "V2.0";
    //签名类型，固定值：MD5或RSA(大写)
    private String signType = "MD5";
    //通知类型，固定值：0 或 1,1: 需要前台跳转和异步通知，某些无法前台跳转场景既设置也无效。0: 只需要底层异步通知。
    private String noticeType = "1";
    
    public SukebaopayConfig(){}

    public SukebaopayConfig(String payParam) {
    	  super(payParam);
        this.mchId = object.getString("mchId");
        this.key = object.getString("key");
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getInterfaceVersion() {
		return interfaceVersion;
	}

	public void setInterfaceVersion(String interfaceVersion) {
		this.interfaceVersion = interfaceVersion;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}
    
}
