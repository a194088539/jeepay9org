package org.jeepay.pay.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Http请求工具类
 * <p>说明:</p>
 * <li>post请求</li>
 * <li>get请求</li>
 * @author DuanYong
 * @since 2018年12月19日上午10:49:03
 */
public class HttpUtils {
    protected static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    private static HttpHandler httpHandler = null;
    
    private static class HttpClientHolder {
		/**
		 * 静态初始化器，由JVM来保证线程安全
		 */
		private static HttpUtils instance = new HttpUtils();
	}
    private HttpUtils() {
    	httpHandler = new HttpClientHandler();
    }
    public static HttpUtils getInstance() {
		return HttpClientHolder.instance;
	}
    /**
     * post请求
     * <p>说明:</p>
     * <li></li>
     * @author DuanYong
     * @param address 请求地址
     * @param data 参数
     * @return 响应数据
     * @since 2018年12月19日上午10:47:28
     */
    public String post(String address, Map<String, String> data){
    	return httpHandler.post(address, data);
    }
    public String post(String address, Map<String, String> data, Map<String, String> header){
        return httpHandler.post(address, data,header);
    }
    public String post(String address, String json, Map<String, String> header){
        return httpHandler.post(address, json,header);
    }
    public String post(String address, String json){
        return httpHandler.post(address, json);
    }
    /**
     * get请求
     * <p>说明:</p>
     * <li></li>
     * @author DuanYong
     * @param address 地址
     * @return 响应数据
     * @since 2018年12月19日上午10:48:07
     */
    public String get(String address){
    	return httpHandler.get(address);
    }
    public String get(String address, Map<String, String> header){
        return httpHandler.get(address,header);
    }
}
