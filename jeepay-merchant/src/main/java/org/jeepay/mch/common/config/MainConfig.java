package org.jeepay.mch.common.config;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="config")
public class MainConfig {

	private String appId;

	private String appSecret;

	private String token;

	private String aesKey;

	private String mchApiUrl;

	private String redirectWxCodeUrl;

	private String payUrl;

	private String notifyUrl;
	
	private String downloadDemoPath;

	@Bean
	public WxMpConfigStorage wxMpConfigStorage() {
		WxMpInMemoryConfigStorage configStorage = new WxMpInMemoryConfigStorage();
		configStorage.setAppId(this.appId);
		configStorage.setSecret(this.appSecret);
		configStorage.setToken(this.token);
		configStorage.setAesKey(this.aesKey);
		return configStorage;
	}

	@Bean
	public WxMpService wxMpService() {
		WxMpService wxMpService = new WxMpServiceImpl();
		wxMpService.setWxMpConfigStorage(wxMpConfigStorage());
		return wxMpService;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAesKey() {
		return aesKey;
	}

	public void setAesKey(String aesKey) {
		this.aesKey = aesKey;
	}

	public String getMchApiUrl() {
		return mchApiUrl;
	}

	public void setMchApiUrl(String mchApiUrl) {
		this.mchApiUrl = mchApiUrl;
	}

	public String getRedirectWxCodeUrl() {
		return redirectWxCodeUrl;
	}

	public void setRedirectWxCodeUrl(String redirectWxCodeUrl) {
		this.redirectWxCodeUrl = redirectWxCodeUrl;
	}

	public String getPayUrl() {
		return payUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getDownloadDemoPath() {
		return downloadDemoPath;
	}

	public void setDownloadDemoPath(String downloadDemoPath) {
		this.downloadDemoPath = downloadDemoPath;
	}

}