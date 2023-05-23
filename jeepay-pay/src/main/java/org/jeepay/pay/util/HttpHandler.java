package org.jeepay.pay.util;

import java.util.Map;

/**
 * Http处理接口
 * <p>说明:</p>
 * <li></li>
 * @since 2018年12月19日上午9:33:11
 */
public interface HttpHandler {
   /**
	 * 同步post请求
	 * <p>说明:</p>
	 * <li></li>
     * @param address 请求地址
     * @param data 请求数据
	 * @return 响应数据
    * @since 2018年12月19日上午9:33:30
    */
	String post(String address, Map<String, String> data);
	String post(String address, Map<String, String> data,Map<String,String> header);
	String post(String address, String json,Map<String,String> header);
	/**
	 * 同步post请求
	 * <p>说明:</p>
	 * <li></li>
	 * @param address 请求地址
	 * @param json json请求数据
	 * @return 响应数据
	 */
	String post(String address, String json);
	/**
	 *  同步get请求
	 * <p>说明:</p>
	 * <li></li>
     * @param address 请求地址
	 * @return 响应数据
	 * @since 2018年12月19日上午9:34:15
	 */
	String get(String address);
	String get(String address,Map<String,String> header);
}
