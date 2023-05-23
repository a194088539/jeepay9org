package org.jeepay.pay.channel.sukebaopay.util;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
/**
 * 工具类
 * <p>说明:</p>
 * <li></li>
 * @author aragom
 * @since 2018年11月1日上午11:10:44
 */
public class PayUtil {
	
	/**
	 * 生成随机数
	 * <p>说明:</p>
	 * <li></li>
	 * @author aragom
	 * @return
	 * @since 2018年10月31日下午2:59:56
	 */
	public static String createNonceStr() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	/**
	 * 生成签名
	 * <p>说明:</p>
	 * <li></li>
	 * @author aragom
	 * @param map
	 * @param secKey 密钥
	 * @return 签名字符串
	 * @since 2018年10月31日下午3:12:29
	 */
	public static String getSignature(Map<String, String> map, String secKey) {
		 StringBuilder sb = new StringBuilder((map.size() +1) * 10);
		 List<String> keys = new ArrayList<String>(map.keySet());
	        Collections.sort(keys);
	        for(String key : keys){
	        	if(StringUtils.isBlank(map.get(key))){
	        		continue;
	        	}
	            sb.append(key).append("=");
                sb.append(map.get(key));
	            sb.append("&");
	        }
	        sb.append("key=").append(secKey);
	        String signature = "";
			try {
				MessageDigest crypt = MessageDigest.getInstance("MD5");
				crypt.reset();
				crypt.update(sb.toString().getBytes("UTF-8"));
				signature = byteToHex(crypt.digest());
			} catch (Exception e) {
				e.printStackTrace();
			}
	        return signature.toUpperCase();
	}
	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
}
