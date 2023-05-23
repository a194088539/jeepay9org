package org.jeepay.manage.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="config")
public class MainConfig {

	private String mchQrUrl;
	private String downMchBillUrl;
	private String loginMchUrl;
	private String loginAgentUrl;
	private String settNotifyUrl;
	private String payUrl;

	public String getMchQrUrl() {
		return mchQrUrl;
	}

	public void setMchQrUrl(String mchQrUrl) {
		this.mchQrUrl = mchQrUrl;
	}

	public String getDownMchBillUrl() {
		return downMchBillUrl;
	}

	public void setDownMchBillUrl(String downMchBillUrl) {
		this.downMchBillUrl = downMchBillUrl;
	}

	public String getLoginMchUrl() {
		return loginMchUrl;
	}

	public void setLoginMchUrl(String loginMchUrl) {
		this.loginMchUrl = loginMchUrl;
	}

	public String getLoginAgentUrl() {
		return loginAgentUrl;
	}

	public void setLoginAgentUrl(String loginAgentUrl) {
		this.loginAgentUrl = loginAgentUrl;
	}

	public String getSettNotifyUrl() {
		return settNotifyUrl;
	}

	public void setSettNotifyUrl(String settNotifyUrl) {
		this.settNotifyUrl = settNotifyUrl;
	}

	public String getPayUrl() {
		return payUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}
}