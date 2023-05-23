package org.jeepay.pay.channel.wxpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.config.WxPayConfig;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

/**
 * @author: aragom
 * @date: 17/8/25
 * @description:
 */
public class WxPayUtil {
	/**
	 * 域名正则表达式
	 */
	private static final String DOMAIN_REGEX = "(\\w+):\\/\\/([^/:]+)(:\\d*)?";

    /**
     * 获取微信支付配置
     * @param configParam
     * @param tradeType
     * @param certRootPath
     * @param notifyUrl
     * @return
     */
    public static WxPayConfig getWxPayConfig(String configParam, String tradeType, String certRootPath, String notifyUrl) {
        WxPayConfig wxPayConfig = new WxPayConfig();
        JSONObject paramObj = JSON.parseObject(configParam);
        wxPayConfig.setMchId(paramObj.getString("mchId"));
        wxPayConfig.setAppId(paramObj.getString("appId"));
        wxPayConfig.setKeyPath(certRootPath + File.separator + paramObj.getString("certLocalPath"));
        wxPayConfig.setMchKey(paramObj.getString("key"));
        wxPayConfig.setNotifyUrl(transformUrl(notifyUrl,paramObj.getString("agentUrl")));
        wxPayConfig.setTradeType(tradeType);
        return wxPayConfig;
    }

    /**
     * 获取微信支付配置
     * @param configParam
     * @return
     */
    public static WxPayConfig getWxPayConfig(String configParam) {
        WxPayConfig wxPayConfig = new WxPayConfig();
        JSONObject paramObj = JSON.parseObject(configParam);
        wxPayConfig.setMchId(paramObj.getString("mchId"));
        wxPayConfig.setAppId(paramObj.getString("appId"));
        wxPayConfig.setMchKey(paramObj.getString("key"));
        return wxPayConfig;
    }

    /**
	 * url转换
	 * <p>说明:</p>
	 * <li>如果agentUrl不为空，则将原始url替换为agentUrl，参数不变</li>
	 * @author DuanYong
	 * @param originalUrl 原始请求url
	 * @return  转换后的URL
	 * @since 2018年12月15日下午10:51:33
	 */
	public static  String transformUrl(String originalUrl,String agentUrl){
		if(StringUtils.isNotBlank(originalUrl) && StringUtils.isNotBlank(agentUrl)){
			return originalUrl.replaceAll(DOMAIN_REGEX, agentUrl);
		}
		return originalUrl;
	}

}
