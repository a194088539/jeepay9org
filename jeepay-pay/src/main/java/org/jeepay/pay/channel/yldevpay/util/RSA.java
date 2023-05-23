package org.jeepay.pay.channel.yldevpay.util;

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;

/**
*
* @author: XieminQuan
* @time  : 2007-11-20 ����04:10:22
*
* DNAPAY
*/

public class RSA {
	
	private static final String ENCODING = "UTF-8";

	//˽Կǩ��
    public static String sign(String data, String pfx_path, String key_pass) {

        try {

            RSAPrivateKey pbk = getPrivateKey(pfx_path, key_pass);

            // ��˽Կ����Ϣ��������ǩ��
            Signature signet = Signature.getInstance("MD5withRSA");
            signet.initSign(pbk);
            signet.update(data.getBytes(ENCODING));
            byte[] signed = signet.sign(); // ����Ϣ������ǩ��    

            return Base64.encode(signed);

        } catch (Exception e) {
			e.printStackTrace();
            return "";
        }
    }
	
    //˽Կǩ��2048
    public static String signSHA(String data, String pfx_path, String key_pass) {
    	
    	try {
    		
    		RSAPrivateKey pbk = getPrivateKey(pfx_path, key_pass);
    		
    		// ��˽Կ����Ϣ��������ǩ��
    		Signature signet = Signature.getInstance("SHA256withRSA");
    		signet.initSign(pbk);
    		signet.update(data.getBytes(ENCODING));
    		byte[] signed = signet.sign(); // ����Ϣ������ǩ��    
    		
    		return Base64.encode(signed);
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    		return "";
    	}
    }
    
    //��Կ����
	public static String encrypt(String data,String pub_key) {
		
		try {

	        KeyFactory rsaKeyFac = KeyFactory.getInstance("RSA");
	        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decode(pub_key));
	        RSAPublicKey pbk = (RSAPublicKey) rsaKeyFac.generatePublic(keySpec);
			
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, pbk);
			
			byte[] encDate = cipher.doFinal(data.getBytes(ENCODING));
			
			return Base64.encode(encDate);
		} catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	 //��Կ����
		public static byte[] encrypt64(String data,String pub_key) {
			
			try {

		        KeyFactory rsaKeyFac = KeyFactory.getInstance("RSA");
		        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decode(pub_key));
		        RSAPublicKey pbk = (RSAPublicKey) rsaKeyFac.generatePublic(keySpec);
				
				Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
				cipher.init(Cipher.ENCRYPT_MODE, pbk);
				
				byte[] encDate = cipher.doFinal(data.getBytes(ENCODING));
				
				return encDate;
			} catch(Exception e) {
				e.printStackTrace();
				return null;
			}
		}

	//˽Կ����
    public static String decrypt(String sign_msg, String pfx_path, String pfx_pass) {

        try {
			
            RSAPrivateKey pbk = getPrivateKey(pfx_path, pfx_pass);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
            cipher.init(Cipher.DECRYPT_MODE, pbk);

            byte[] btSrc = cipher.doFinal(Base64.decode(sign_msg));
            
            return new String(btSrc,ENCODING);

        } catch (Exception e) {
			e.printStackTrace();
            return "";
        }
    }
    
    //��Կ��ǩ
    public static boolean verify(String data, String pub_key, String value) {
    	
        try {
        	byte[] bts_data = Base64.decode(data);
        	byte[] bts_key = Base64.decode(pub_key);

            KeyFactory rsaKeyFac = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bts_key);
            RSAPublicKey pbk = (RSAPublicKey) rsaKeyFac.generatePublic(keySpec);
            
            Signature signetcheck = Signature.getInstance("MD5withRSA");
            signetcheck.initVerify(pbk);
            signetcheck.update(value.getBytes(ENCODING));
            
            return signetcheck.verify(bts_data);
            
        } catch (Exception e) {
			e.printStackTrace();
            return false;
        }
    }
    //��Կ��ǩ
    public static boolean verifySHA(String data, String pub_key, String value) {
    	
    	try {
    		byte[] bts_data = Base64.decode(data);
    		byte[] bts_key = Base64.decode(pub_key);
    		
    		KeyFactory rsaKeyFac = KeyFactory.getInstance("RSA");
    		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bts_key);
    		RSAPublicKey pbk = (RSAPublicKey) rsaKeyFac.generatePublic(keySpec);
    		
    		Signature signetcheck = Signature.getInstance("SHA256withRSA");
    		signetcheck.initVerify(pbk);
    		signetcheck.update(value.getBytes(ENCODING));
    		
    		return signetcheck.verify(bts_data);
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    		return false;
    	}
    }

    public static RSAPrivateKey getPrivateKey(String keyPath, String passwd) throws Exception {

        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            FileInputStream fis = new FileInputStream(keyPath);
            
            char[] nPassword = null;
            if ((passwd == null) || passwd.trim().equals("")) {
                nPassword = null;
            } else {
                nPassword = passwd.toCharArray();
            }
            ks.load(fis, nPassword);
            fis.close();
            
            Enumeration enumq = ks.aliases();
            String keyAlias = null;
            if (enumq.hasMoreElements()) 
            {
                keyAlias = (String) enumq.nextElement();
            }

            PrivateKey prikey = (PrivateKey) ks.getKey(keyAlias, nPassword);

            return (RSAPrivateKey) prikey;
        } catch (Exception e) {
			e.printStackTrace();
            return null;
        }
    }

    
    public static RSAPublicKey getPublicKey(String keyPath, String passwd) throws Exception {

        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");
        	
            FileInputStream fis = new FileInputStream(keyPath);
            
            char[] nPassword = null;
            if ((passwd == null) || passwd.trim().equals("")) {
                nPassword = null;
            } else {
                nPassword = passwd.toCharArray();
            }
            ks.load(fis, nPassword);
            fis.close();

            Enumeration enumq = ks.aliases();
            String keyAlias = null;
            if (enumq.hasMoreElements()) 
            {
                keyAlias = (String) enumq.nextElement();
            }

            Certificate cert = ks.getCertificate(keyAlias);
            
            PublicKey pubkey = cert.getPublicKey();
  
            return (RSAPublicKey) pubkey;
        } catch (Exception e) {
			e.printStackTrace();
            return null;
        }
    }
/*    
    public static void main(String[] args) throws Exception {
    	
//    	String pfx_path = "F:\\doc\\个人\\活路\\104000000255482\\104000000255482-Signature.pfx" ;
//    	String pfx_pass = "00610581";
        String pfx_path = "D:\\yilian.pfx" ;
        String pfx_pass = "11111111";
    	System.out.println(Base64.encode(getPublicKey(pfx_path, pfx_pass).getEncoded()));
    	String res= "lWhhV9oHTSyPEJZnUYoAv48mTlWGq5QP2Wdig69FFCg6/Wju0ek2gCca12jHfiCF6wNi8jUActvP8NTDScsc3ATh0zxgqwnmB1YW6bxLVCbpDCdXKPHrtJZgqxfGSYfPIlpdrS2P6aRbk5LusApfy0d7utStEpue45MqK9OvaHZIdP/JJ2rmzaSC6oXXNlzgpk6QaZUKHaTMm4Mxpn9qHKAoY3nKmh/6A7GbUEK195F9vuNHIB+wHsZOs4gMa53ZAsAxFgyNAxGZlNEWGlKq6JwMAgQ/F4Lj+QqbwYOVPbpK67Ju7sfogfX+QH2PW7reWO/PaFylXqL9raZELt3Dx43Yw1bbfkmd2EercMFdv3/IpeK/Q+bhsY8ZNi577+qaMsiSuuNhrtHlCh1fUvPAlv+PbBkQ0IuooxwPP/eTJ/DNe3ksX91j+H4jB1gXqL0I8hGOWFMOPwjnGXbLnpL46xqeOFx/rP86pILqhdc2XOCgiWZFuMdXOUSYc0HZOaYbZyy8WZTikFqiU//99i7h4tmxnvsUOf8+pwiEg2GywPmKF9NP2CA7tWJE8GQ3O8r8A8uGnGscNqEu+LkajVqyQXnYuVlJJxiSTjUuqW5+gmF/9hcewAdzmaBnMJsYrFMD49/6sFUlII8NtdDfS1OeQNq9s0jbJrbvXjbPyhZEKvzBP+J1YE+YgZad4hr51phhCfvMdsu8SFOqSr0ETAYN87hT9sM2QYEuTJhkSnBSyRhqdldVxuld4SRgVp89gxap8H2DBKFBMMGAl96kXAftAnejMspL7QYIXLxY/pVHXWgrEW4BTgs624w0aU67jHcGH/q64M8py4rx0nghyBf6T6cDP1VC8LLbJtabGytqa//EHYmGvB8KatsLRHQ4+1vZoDv09JkV09lBJucaXLw7GVTi2Y1WGHKWua+Eur76r0JT6zqfHDio2RT1HuTaxRB1+UdoLjLtalesxq+dsNoKLQqjcLUjfWcOJ3iD6/tlXoE4OGlYKcdDErjpU5aYWsGVPp7fwrbsVpzEwG+ocdLDyj6e38K27FacnHeSfRE9JxTZUuN4+QTl5hkxpwPl4rmx6zScB8h1/XHCrkg+Z+YZyEtolXZIHMcE8dVC6SCD1dW+ZDoBmg7/5wEG9tGU0gxQ34ztn5EBfLdmfEWr1uhRrqzFgkiHQj7OKfOeYgzapdRIrC/A6QmMsPlgavKHuR01Sy2hyjLRcNXVdIvHLwGInHw5sUeucWQzxDC5ozkA7fSn/0i8IVV+jcW1T0p1wpKFB1/H+HGgooZyWNKs8TMp6d8f1R0EWNZniCW2FfkRVaGoG1IlBg9i9iqqVWLHDvBZiPeVme0qyaHMx67n9rxY5fWTe5mMOpSD8rp4UiXPK2/fYDH2pDiDdOWPf2uZf6rwam1jeOgOigioUc2b4JQSYaYRHuorVYefOKiwYKtULHYiWIq5L7+RD2x4p/0d3AuR9dM/c7n/TYhZie2IQJ/KLl7zZJVIqrvrKhVDi5B83rlR2Q4afumO0CoVQ4uQfN65jzuxcYo/tOIYLQ3jjQYrDK0jh3GZIo/dGAE/29X7/Bj1ThHufcL4zFQBE9gl4mgyO2/xzHOVvZrVFhNNHBMAz79e5zyN+LLrUuoXv8I/G716BeJ0RoeLLOS16xFnQ5oLzAzrWc9Wx8U=|SAvPNBvWKlzCSLWyQd1U1TSP/JQidRGuQdnN8BHMeWKn7mU6tSgsn2n3cuScaKBOP9U8c/t6X2IUiNIuNOj43rKGFf4W9/8olppQ1WQaMCOM8ItN+g+ufp3fgmrt/Of88mD3wk62+/+JvCeLzU29L5MURzlikK+Roj1ZpmEpLx0=";
        res= "wl9aM5d2Xa5lpavmkL1rPoPb6HOgOfhZGLJDCGPM0b2vGlOJ/o4WBwTgVzPTkiL+ieoECn7MnIv90OkMY42dHfv5M0bkuk3jMncH7wSr6dpUMYnumlO4bfM2tNP8JT5yKzCOn2I7/GFneBRExUNapc1+pM1nIsNFwGpncb19SUpheCcKWJgpE2kuaR+GznJzp7oDzcjJU75P544eUQcZ8lTGyRBloXtMyZDOgePnfNmVg8qyeCXMNQ0gPtgAX6alXcAUe6CIUMwt++avroyM/XRrYTTLIphUdK6ItAQNUnI3bTrpfUTz6QSZj5RQ+d/XmLJUKi4tdPvtbGvFOXxMQEtRClUg4gl2Yp+pIieNQcM0d0hkOz/bCUy/nLva60cScysD7oGIsYltIOil8UvcwcWYjqKCq0Kivk8NfvGfdfut+vEMtFwfrWd4FETFQ1ql7qsdydQIgTV3e9vE9Ksgp1AnreWl0WFNyNO6DYxVAzM/nz+au1+QD+nZl92c+eM4RfBehhsiOGZ8QgBQNWKqfSq0hfujIJoVT/5OavJfoFj1tFPM/XDohUokrbSId1NziEqilog+lRnZ+St89Gb7Jw5IypgatUf+8hnF/H/rP/rrNizqESOvIfbuR9QHwJUmTkAX422ICBxdf1ga2aj+/0pb/QlleBwCB4j4EAVWLg8WHtTWHSCI0wLfdMFSuji1RVeAd4tHqij/ZNgbc6p0GL53ujHrZp+SP7pBksAIXGThrZXfIB7uAhVVrGzVSFfj+Z3hAJVeFyH6cQTXsbWsAZ+G2/MWXbB/+YX4alWAJNspUpT040euoNp+YQAdtgfM+UJGIKi3HjNtgjJ9OZlWbQACqhWr+FeHSKCri2ehGH+d7YtXtuM7YWBWNqSRv89VQDsmYdrN/wIUy9MVWUq8rvhz10EPQZZmTUUlG1geP3kQYncz21LFMzpmjkzuVBKOQwPNx3eq8iDFI+ax1g//yC1e1HnXfut0veFWUQjQvUu6CAteZQAea/Aex0iVKrWozBynZjGQphaJo9jKc7Atfg==|Yr2aonI8sN74h4lQA7eiFJXUAGXorTnHrF1mjapNmu14Wf61MGPsTCQwjAFaGhYtGerQwq0jXM7QU+8RrnZx2+TXGfZwPXtOFgcDtzIm0StT0oISNwBckkrej822hk0flR6vgGelzkszOUHpczxRHmquBxxMvTfsiqj2/Wd+tKM=";
    	//MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAv8jzSQ0smn0h2O7923M0rIXAulrKyzOWd1UhDkUGax+vQXhUl7SrTqOL12DR55wNkpcrxE8Kgho2xIiLyMEIAz9yr4Y2F2GIdmUfHAX2Os6vHE9/p18OixL0DA/j1WRSQMV98290aGqy7eATEnZNWxOyUMrSvt4zsfyEIB7a3Mm0nzNNA/qqEyXw904vpIqNmjFyT4qr1jyVZoVldraHaDxjhBe7fY+rJBgNW8GRBpkt5GaPF/1BKLyY2aX1fAOmGWpqgZzKO36M7kEevidlgwLOlqe+wbYUcKqROkQbncD4wjacifcBKFeYY4OYrlKxc0+HmRS7ac34sGpv5gXrLQIDAQAB
//MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzWw3Qni0EBEwlb7VoEMIFCGZ4Cc+4+a9aDEVPI29WnyYVduBZ1Qo2cNUWvhVayVpWfZIvKMJfkrSr8/eFUkIakulqQNdMUIUkST7lrlivalhEvxxClYvMr1OAmuQKemKrHrdDUlG+h7oiMP9bJJpcNh+8NCjSeQEimkDJ7ESOvtuGUemgr8L2JKowPKtgvHJOmOLTMv55XUFZJn35BSQq6Bzn0osIwdU7peZrdfSTxa5p11NHRFVR84J//85Uc4Uk8Mb54JDw+kEwZSK8WOJcwRjDlbcRx0H463D4b64FYDm44F/Hx7IDJf1TqS1N7kV7TqnwZs24V4aaDOg2OmUJQIDAQAB
//MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAv8jzSQ0smn0h2O7923M0rIXAulrKyzOWd1UhDkUGax+vQXhUl7SrTqOL12DR55wNkpcrxE8Kgho2xIiLyMEIAz9yr4Y2F2GIdmUfHAX2Os6vHE9/p18OixL0DA/j1WRSQMV98290aGqy7eATEnZNWxOyUMrSvt4zsfyEIB7a3Mm0nzNNA/qqEyXw904vpIqNmjFyT4qr1jyVZoVldraHaDxjhBe7fY+rJBgNW8GRBpkt5GaPF/1BKLyY2aX1fAOmGWpqgZzKO36M7kEevidlgwLOlqe+wbYUcKqROkQbncD4wjacifcBKFeYY4OYrlKxc0+HmRS7ac34sGpv5gXrLQIDAQAB
//MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAv8jzSQ0smn0h2O7923M0rIXAulrKyzOWd1UhDkUGax+vQXhUl7SrTqOL12DR55wNkpcrxE8Kgho2xIiLyMEIAz9yr4Y2F2GIdmUfHAX2Os6vHE9/p18OixL0DA/j1WRSQMV98290aGqy7eATEnZNWxOyUMrSvt4zsfyEIB7a3Mm0nzNNA/qqEyXw904vpIqNmjFyT4qr1jyVZoVldraHaDxjhBe7fY+rJBgNW8GRBpkt5GaPF/1BKLyY2aX1fAOmGWpqgZzKO36M7kEevidlgwLOlqe+wbYUcKqROkQbncD4wjacifcBKFeYY4OYrlKxc0+HmRS7ac34sGpv5gXrLQIDAQAB
//MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAv8jzSQ0smn0h2O7923M0rIXAulrKyzOWd1UhDkUGax+vQXhUl7SrTqOL12DR55wNkpcrxE8Kgho2xIiLyMEIAz9yr4Y2F2GIdmUfHAX2Os6vHE9/p18OixL0DA/j1WRSQMV98290aGqy7eATEnZNWxOyUMrSvt4zsfyEIB7a3Mm0nzNNA/qqEyXw904vpIqNmjFyT4qr1jyVZoVldraHaDxjhBe7fY+rJBgNW8GRBpkt5GaPF/1BKLyY2aX1fAOmGWpqgZzKO36M7kEevidlgwLOlqe+wbYUcKqROkQbncD4wjacifcBKFeYY4OYrlKxc0+HmRS7ac34sGpv5gXrLQIDAQAB
    	//String pfx_path = "C:\\yilian.pfx" ;
    	//String pfx_pass = "11111111";

//    	String xml = "<MSGBEAN><MSG_TYPE>200002</MSG_TYPE><BATCH_NO>99EE936559D864</BATCH_NO><USER_NAME>13760136514</USER_NAME><TRANS_STATE></TRANS_STATE><MSG_SIGN>";
//    	String sign = RSA.sign(xml, pfx_path, pfx_pass);
//    	System.out.println("sign==="+sign);
//		if(true)
//			return;

		String msg_sign_enc = res.split("\\|")[0];
		System.out.println("msg_sign_enc" + msg_sign_enc);
		String key_3des_enc = res.split("\\|")[1];
		System.out.println("key_3des_enc"+key_3des_enc);
		byte[] bs = Base64.decode(key_3des_enc);
		System.out.println("key_3des_enc  decode ="+Base64.encode(bs)) ;
		System.out.println(decrypt(key_3des_enc, pfx_path, pfx_pass));

		//������Կ
		String key_3des = RSA.decrypt(key_3des_enc,pfx_path,pfx_pass);

//		key_3des = Base64.encode(key_3des.getBytes("UTF-8"));


		//���ܱ���
		String msg_sign = TripleDes.decrypt(key_3des, msg_sign_enc);
		System.out.println(msg_sign);
	}
*/
}
