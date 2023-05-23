package org.jeepay.pay.channel.heepay.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * 
 * @ClassName: Disguiser
 * @Description: 哈希算法的工具类，提供SHA MD5 HMAC等算法
 *
 */
public class Disguiser {

	public static final String ENCODE = "UTF-8";
	private static final String KEY = "8data998mnwepxugnk03-2zirb";
	
	public static void main(String[] args) {
		System.out.println(KEY.length());
		System.out.println(disguise("12345678"));
		System.out.println(Disguiser.disguiseMD5("#OnlineKuaiPay#B100490594#20180109150929880#100.0#一条裤子#221.223.46.229#rw2bWJKSrFBjkQHFmfiXy9i+t6lHVLjB#n2np1TaFblNnXNhhjG6cQY0m79aBWUPp#WTskAXb57dDIN8WEmcrOYg==#13889201757#PAY#T0#eQfDDwkDRUBriv7DxT5sUBbGFvVQGwLM"));
	}

	public static String disguise(String message) {
		return disguise(message + KEY, ENCODE);

	}

	public static String disguise(String message, String encoding) {
		message = message.trim();
		byte value[];
		try {
			value = message.getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			value = message.getBytes();
		}
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		return ConvertUtils.toHex(md.digest(value));
	}

	public static String disguiseMD5(String message) {

		if (null == message) {

			return null;
		}

		return disguiseMD5(message, ENCODE);
	}

	public static String disguiseMD5(String message, String encoding) {

		if (null == message || null == encoding) {

			return null;
		}

		message = message.trim();
		byte value[];
		try {
			value = message.getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			value = message.getBytes();
		}
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		return ConvertUtils.toHex(md.digest(value));
	}

	public static String string2MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();

	}

	/**
	 * 对报文进行采用MD5进行hmac签名
	 * 
	 * @param aValue
	 *            - 字符串
	 * @param aKey
	 *            - 密钥
	 * @param encoding
	 *            - 字符串编码方式
	 * @return - 签名结果，hex字符串
	 */
	public static String hmacSign(String aValue, String aKey, String encoding) {
		byte k_ipad[] = new byte[64];
		byte k_opad[] = new byte[64];
		byte keyb[];
		byte value[];
		try {
			keyb = aKey.getBytes(encoding);
			value = aValue.getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			keyb = aKey.getBytes();
			value = aValue.getBytes();
		}
		// System.out.println(ConvertUtils.toHex(k_ipad));
		// System.out.println(ConvertUtils.toHex(keyb));
		Arrays.fill(k_ipad, keyb.length, 64, (byte) 54);
		Arrays.fill(k_opad, keyb.length, 64, (byte) 92);
		// System.out.println(ConvertUtils.toHex(k_ipad));
		for (int i = 0; i < keyb.length; i++) {
			k_ipad[i] = (byte) (keyb[i] ^ 0x36);
			k_opad[i] = (byte) (keyb[i] ^ 0x5c);
		}
		// System.out.println(ConvertUtils.toHex(k_ipad));
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		md.update(k_ipad);
		md.update(value);
		byte dg[] = md.digest();
		md.reset();
		md.update(k_opad);
		md.update(dg, 0, 16);
		dg = md.digest();
		return ConvertUtils.toHex(dg);
	}

	/**
	 * 对报文进行hmac签名，字符串按照UTF-8编码
	 * 
	 * @param aValue
	 *            - 字符串
	 * @param aKey
	 *            - 密钥
	 * @return - 签名结果，hex字符串
	 */
	public static String hmacSign(String aValue, String aKey) {
		return hmacSign(aValue, aKey, ENCODE);
	}

}
