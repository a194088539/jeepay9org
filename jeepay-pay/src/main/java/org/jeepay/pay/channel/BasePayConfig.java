package org.jeepay.pay.channel;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;

/**
 * 支付渠道配置基类
 * <p>说明:</p>
 * <li></li>
 * @author aragom
 * @since 2018年12月15日下午8:10:44
 */
public class BasePayConfig {
	public static final String ORIGINAL_REQ_URL_KEY = "originalReqUrl";
	/**
	 * 域名正则表达式
	 */
	private static final String DOMAIN_REGEX = "(\\w+):\\/\\/([^/:]+)(:\\d*)?";
	/**
	 * 支付配置参数json对象
	 */
	 protected JSONObject object;
    /**
     * 请求地址
     */
    private String reqUrl;
    /**
     * 代理URL
     */
    private String agentUrl;
    /**
     * 原始请求地址
     */
    private String originalReqUrl;
	/**
	 * 当面付
	 */
    public BasePayConfig(){}
    
    public BasePayConfig(String payParam){
        Assert.notNull(payParam, "支付参配置为空");
        object = JSONObject.parseObject(payParam);
    	this.agentUrl = object.getString("agentUrl");
    	this.originalReqUrl = object.getString("reqUrl");
        this.reqUrl = transformReqUrl(object.getString("reqUrl"));
    }
    
	public String getReqUrl() {
		return reqUrl;
	}

	public void setReqUrl(String reqUrl) {
		this.reqUrl = reqUrl;
	}

	
	public String getAgentUrl() {
		return agentUrl;
	}

	public void setAgentUrl(String agentUrl) {
		this.agentUrl = agentUrl;
	}

	public String getOriginalReqUrl() {
		return originalReqUrl;
	}

	public void setOriginalReqUrl(String originalReqUrl) {
		this.originalReqUrl = originalReqUrl;
	}
	


	/**
	 * url转换
	 * <p>说明:</p>
	 * <li>如果agentUrl不为空，则将原始url替换为agentUrl，参数不变</li>
	 * @author aragom
	 * @param originalUrl 原始请求url
	 * @return  转换后的URL
	 * @since 2018年12月15日下午10:51:33
	 */
	public String transformUrl(String originalUrl){
		if(StringUtils.isNotBlank(originalUrl) && StringUtils.isNotBlank(this.agentUrl)){
			return originalUrl.replaceAll(DOMAIN_REGEX, this.agentUrl);
		}
		return originalUrl;
	}
	
	private String transformReqUrl(String originalUrl){
		if(StringUtils.isNotBlank(originalUrl) && StringUtils.isNotBlank(this.agentUrl)){
			return this.agentUrl+"/agent";
		}
		return originalUrl;
	}
}