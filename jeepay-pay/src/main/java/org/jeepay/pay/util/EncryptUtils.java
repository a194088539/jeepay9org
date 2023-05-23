package org.jeepay.pay.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 加密操作类
 * 
 * @version 1.0
 */
public class EncryptUtils {

	public static final String MD5 = "MD5";
	public static final String SHA = "SHA";
	public static final String SHA256 = "SHA-256";
	public static final String SHA348 = "SHA-348";
	public static final String SHA512 = "SHA-512";

	public static final String HMACMD5 = "HmacMD5";
	public static final String HMACSHA = "HmacSHA1";
	public static final String HMACSHA256 = "HmacSHA256";
	public static final String HMACSHA348 = "HmacSHA384";
	public static final String HMACSHA512 = "HmacSHA512";

	/**
	 * BASE64加密
	 * 
	 * @param data
	 *            要加密的字符
	 * @return
	 */
	public static String encryptBASE64(String data) {
		try {
			return (new BASE64Encoder()).encodeBuffer(data.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return data;
		}
	}

	/**
	 * BASE64解密
	 * 
	 * @param data
	 *            要解密的字符
	 * @return
	 */
	public static String decryptBASE64(String data) {
		try {
			return new String((new BASE64Decoder()).decodeBuffer(data), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * MessageDigest加密
	 * 
	 * @param data
	 * @return
	 */
	public static String encrypt(String data, String encryptType) {
		return encrypt(data, encryptType, "utf-8");
	}

	/**
	 * MessageDigest加密
	 * 
	 * @param data
	 * @return
	 */
	public static String encrypt(String data, String encryptType,
			String encondeType) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance(encryptType);
			try {
				md.update(data.getBytes(encondeType));
			} catch (UnsupportedEncodingException e) {
				md.update(data.getBytes());
			}
			byte[] b = md.digest();
			StringBuffer buf = new StringBuffer("");
			int i = 0;
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			return data;
		}
	}

	/**
	 * 默认MD5加密的时候
	 * 
	 * @param data
	 * @return
	 */
	public static String encrypt(String data) {
		return encrypt(data, MD5);
	}

	/**
	 * MAC加密
	 * 
	 * @param data
	 * @param macType
	 * @return
	 */
	public static String encryptMac(String data, String macType) {
		SecretKey secretKey = new SecretKeySpec(decryptBASE64(getMacKey())
				.getBytes(), macType);
		try {
			Mac mac = Mac.getInstance(secretKey.getAlgorithm());
			mac.init(secretKey);
			return new BigInteger(mac.doFinal(data.getBytes())).toString(16);
		} catch (Exception e) {
			return data;
		}
	}

	/**
	 * MAC默认加密
	 * 
	 * @param data
	 * @param macType
	 * @return
	 */
	public static String encryptMac(String data) {
		return encryptMac(data, HMACMD5);
	}

	/**
	 * MAC密匙
	 * 
	 * @return
	 */
	public static String getMacKey(String macType) {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance(macType);
			SecretKey secretKey = keyGenerator.generateKey();
			return (new BASE64Encoder()).encodeBuffer(secretKey.getEncoded())
					.trim();
		} catch (NoSuchAlgorithmException e) {
			return "";
		}
	}

	/**
	 * MAC默认密匙
	 * 
	 * @return
	 */
	public static String getMacKey() {
		return getMacKey(HMACMD5);
	}

	public static String getUniqueKey() {
		return encrypt(
				RandomStringUtils.randomNumeric(10)
						+ System.currentTimeMillis(), SHA);
	}

	/**
	 * 字符串加密
	 * 
	 * @param input
	 *            要加密的字符串
	 * @param strkey
	 *            密码
	 * @return
	 * @throws Exception
	 */
	public static String desEncrypt(String input, String strkey)
			throws Exception {
		DESedeKeySpec dks = new DESedeKeySpec(encrypt(strkey).getBytes("UTF-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
		SecretKey securekey = keyFactory.generateSecret(dks);

		Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, securekey);
		byte[] b = cipher.doFinal(encryptBASE64(input).getBytes("UTF-8"));
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(b).replaceAll("\r", "").replaceAll("\n", "");
	}

	/**
	 * 字符串解密
	 * 
	 * @param cipherText
	 *            密串
	 * @param strkey
	 *            解密密码
	 * @return
	 * @throws Exception
	 */
	public static String desDecrypt(String cipherText, String strkey)
			throws Exception {
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] bytesrc = decoder.decodeBuffer(cipherText);
		// --解密的key
		DESedeKeySpec dks = new DESedeKeySpec(encrypt(strkey).getBytes("UTF-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
		SecretKey securekey = keyFactory.generateSecret(dks);

		// --Chipher对象解密
		Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, securekey);
		byte[] retByte = cipher.doFinal(bytesrc);
		return decryptBASE64(new String(retByte));
	}

	//转化为16进制
	public static String toHexString(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			String s4 = Integer.toHexString(ch);
			str = str + s4;
		}
		return str;
	}
	
	
	private static final String COMPANY_PASSWORDKEY = "28lCF89V6kaPFT8jneviQjW17OO4nwtW";
	public static String passwordCompany(String pwd) {
		try {
			String key = encrypt(COMPANY_PASSWORDKEY);
			for (int i = 0; i < md5count; i++) {
				key = encrypt(key);
			}
			return StringUtils.reverse(desEncrypt(pwd, key));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String restoreCompany(String pwd) {
		try {
			String key = encrypt(COMPANY_PASSWORDKEY);
			for (int i = 0; i < md5count; i++) {
				key = encrypt(key);
			}
			return desDecrypt(StringUtils.reverse(pwd), key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static final String PASSWORDKEY = "super_pay_20181113";
	private static final int md5count = 16;
	public static String password(String pwd) {
		try {
			String key = encrypt(PASSWORDKEY);
			for (int i = 0; i < md5count; i++) {
				key = encrypt(key);
			}
			return StringUtils.reverse(desEncrypt(pwd, key));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String restore(String pwd) {
		try {
			String key = encrypt(PASSWORDKEY);
			for (int i = 0; i < md5count; i++) {
				key = encrypt(key);
			}
			return desDecrypt(StringUtils.reverse(pwd), key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(password("admin123"));
		System.out.println(restore("==QHARTpWSxxG06fhC7Tqe6U"));
	}
	
}