package org.jeepay.pay.channel.ylpay.util;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;


/**
*
* @author: XieminQuan
* @time  : 2007-11-20 ����04:10:22
*
* DNAPAY
*/

public class TripleDes {

	private static final String Algorithm = "DESede"; //���� �����㷨,���� DES,DESede,Blowfish
	private static final String ENCODING = "UTF-8";


	//���ܣ�ʹ��base64����
    public static String encrypt(String keybyte, String src) {
    	
        try {
        	
            SecretKey deskey = new SecretKeySpec(Base64.decode(keybyte), Algorithm);

            Cipher c1 = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            byte[] bts = c1.doFinal(src.getBytes(ENCODING));

            return Base64.encode(bts);
            
        } catch (Exception e) {
			e.printStackTrace();
        }
        return "";
    }

	public static String decrypt(String keybyte, String src) {
		
		try {

			SecretKey deskey = new SecretKeySpec(Base64.decode(keybyte), Algorithm);
			
			Cipher c1 = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			c1.init(Cipher.DECRYPT_MODE, deskey);
			
			byte[] bts = c1.doFinal(Base64.decode(src));
			
			return new String(bts,ENCODING);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getPubKeyDER(String derPath){
		String pub_key = "" ;
		try {
			InputStream streamCert = new java.io.FileInputStream( derPath);
			CertificateFactory factory = CertificateFactory.getInstance("X.509");
			Certificate cert = factory.generateCertificate(streamCert);
			pub_key = Base64.encode(cert.getPublicKey().getEncoded()) ;
			streamCert.close();
			System.out.println( pub_key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return pub_key;
	}
	
	public static String getPubKeyCER(String cerPath){
		String pub_key = "" ;
		try {
			InputStream streamCert = new java.io.FileInputStream( cerPath);
			CertificateFactory factory = CertificateFactory.getInstance("X.509");
			Certificate cert = factory.generateCertificate(streamCert);
			pub_key = Base64.encode(cert.getPublicKey().getEncoded()) ;
			streamCert.close();
			System.out.println( pub_key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return pub_key;
	}
	
	public static void main(String[] args) throws Exception {
//		String dna_pub_key =      "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCqWSfUW3fSyoOYzOG8joy3xldpBanLVg8gEDcvm9KxVjqvA/qJI7y0Rmkc1I7l9vAfWtNzphMC+wlulpaAsa/4PbfVj+WhoNQyhG+m4sP27BA8xuevNT9/W7/2ZVk4324NSowwWkaqo1yuZe1wQMcVhROz2h+g7j/uZD0fiCokWwIDAQAB";
		String merchant_pub_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDS5jLUcw1gYy0uvrEpNbmKuSD0M27jQ3RfHV4DimH1KpIXokqcRd/oN+sEscvBogl+7HDBWQaa6rvV5MLXvjKX5EiLOdNBLGd8+OPCLoTgcfEtowPQPY4S3M2pQlghBBOBCfr2wEQi8L5v55APZh3ryy8r1HK0j3y7AqBXDBYiSQIDAQAB";
								   
		String merchant_pfx_key = "C:\\Users\\Allen\\Desktop\\104000000126968-Signature.pfx";
		String key_pass = "02012698";
	
//		String merchant_pfx_key = "C:\\yilian.pfx";

//		String merchant_pfx_key = "C:\\server.pfx";
//		String merchant_pfx_key = "C:\\yilian2048.pfx";
//		String key_pass = "11111111";

//		String merchant_pub_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCw9rSVy5hqxo7PvlZeTCrmSBq2wLSG2NZ+h2d0sLPXdbsOFp1EXpONWWu8kzbodlYpuPqcPznw+SQweVPNJGZpW81ZUUcnmANfbctGbcKYODKeSFtGoyGg87hO7ncC/nLqirMt+DM9+qV074iT/baonnH6rOgaeEz6d7JYNLUrTQIDAQAB";
	
		System.out.println(Base64.encode(RSA.getPublicKey(merchant_pfx_key, key_pass).getEncoded()));
		
//		getPubKeyCER("C:\\dnapay\\gzdsf.cer");
//		getPubKeyCER("E:\\easypay\\project\\���ո�\\���ո�ϵͳ\\�̻�����\\���Ѱ�����������Ϣ�����Ϻ���\\104000000071272-Signature.der");
//		System.out.println("=public=="+getPubKeyCER("E:\\��Ŀ\\������ά��\\����Ҫ��\\����\\acp_test_enc.cer"));
		
		
//		getPubKeyCER("D:\\easypay\\project\\���ո�\\���ո�ϵͳ\\�̻�����\\���ݲ��׿Ƽ����޹�˾\\���ݲ��׿Ƽ����޹�˾\\312000100999263-Signature.cer");
		getPubKeyCER("C:\\Users\\Allen\\Desktop\\104000000247802-Signature.der");
		
/*		if(true)
			return;*/
//        System.out.println(Base64.encode(RSA.getPublicKey("D:\\gdyilian_merchant_signature.pfx", "80115864").getEncoded()));
//        System.out.println(Base64.encode(RSA.getPublicKey("E:\\easypay\\project\\���ո�\\���ո�ϵͳ\\�̻�����\\���ݲ��׿Ƽ����޹�˾\\���ݲ��׿Ƽ����޹�˾\\312000100999263-Signature.pfx", "08696511").getEncoded()));
//        System.out.println(Base64.encode(RSA.getPublicKey("D:\\easypay\\project\\���ո�\\���ո�ϵͳ\\�ӿڽ��뷢���̻�����\\librsa\\cert\\RSA_merchant_test.pfx", "000000").getEncoded()));
//        System.out.println(Base64.encode(RSA.getPublicKey("C:\\Users\\Allen\\Desktop\\yilian_dsf.pfx", "11111111").getEncoded()));
       /* System.out.println(Base64.encode(RSA.getPublicKey("C:\\Users\\Allen\\Desktop\\104000000125584-Signature.pfx", "06411230").getEncoded()));
        if(true)
			return;*/
        
		//24key����
				/*String key = "0F91F44FB331EB39779F9605";
				String key_f = RSA.encrypt(Base64.encode(key.getBytes()), dna_pub_key);
				System.out.println("key����="+key_f);
				//24key����
				String key_enc = "QhX+0f8JPqicd2HjJbM3p/A5aQw/xeAI/5G0bCirhy8zKCTNlUAAdF/mWx+lgvxOoBTJdGB30VZGZ+PGUN2elU1B8BVPgie9qEe/5WTfN4Ze9UEPtHdqFoaIWxE6vUcX7oiMUjn+veYJB2tRFtBrx9HKNvoo/coap4wMxq6KG0o=";
//				String key_enc = key_f;
				String key_g = RSA.decrypt(key_enc, merchant_pfx_key, key_pass);
				System.out.println("key="+key_g);	
		
		if(true)
			return;
		 
		*/
//		String a = "<MSGBEAN><MSG_TYPE>200002</MSG_TYPE><BATCH_NO>99EE936559D776</BATCH_NO><USER_NAME>13760136514</USER_NAME><TRANS_STATE></TRANS_STATE><MSG_SIGN></MSG_SIGN><TRANS_DETAILS></TRANS_DETAILS></MSGBEAN>";
		String a = "201910161513070492 18210098595 100002 CNY";
		System.out.println("����="+a);
		
		
		//ǩ��
		String b = RSA.sign(a,merchant_pfx_key,key_pass);
		System.out.println("ssssǩ��="+b);
		
		
		//��ǩ
		b = "ITtIRjHZdOlm90TMIS6uRTNq3woR2euD9CU0hxwX3ndnU22ErBK50a46j5snsottT7FHD4UgVNf4u0c2YSbikDx5LCPD071tXDhKv3JlWXAhcfnShhvGmCclHRhbnwUJbrKyPFIpIGQKQS8cB+yujNGH36GKCwir9y4J4fruheg=";
//		boolean c = RSA.verify(b,merchant_pub_key,a);
		boolean c = RSA.verify(b,merchant_pub_key,a);
		System.out.println("sssss��ǩ="+c);
		
		//����
//    	String d = Util.generateKey(9999,24);
    	String d = "M0ZDOUE1MkVFMEJDMzU2MDFGMzlGQjVF";
		System.out.println("key="+d);
        String e = TripleDes.encrypt(d, a);
		System.out.println("��������="+e);
        String e1 = TripleDes.encrypt(d, b);
		System.out.println("ǩ������="+e1);
        
		d = "201910161513070492 18210098595 100002 CNY";
        String f = RSA.encrypt(d, merchant_pub_key);
        System.out.println("key����="+d);
		System.out.println("key����="+f);
		
		//����
//		f="julla2HwxAGQjAIekO8JbKNYhmIVNv4urfuSoDxBVRGogRN29IDmu1HbLd1Rwua85vkdrGeilmT9BU1Wn0MKnd1eg4wf5FQX3zA1UVxzlJmfQjvjsic8C/96e1Sq/V8VvOnYJX5O+s0i4pD0JVX2KuOicmL6xzBZhTFYbRS1VH8=";
		String g = RSA.decrypt(f, merchant_pfx_key, key_pass);
		System.out.println("key="+g);
		
		String h = TripleDes.decrypt(g, e);
		System.out.println("����="+h);
		String h1 = TripleDes.decrypt(g, e1);
		System.out.println("ǩ��="+h1);

		System.out.println(a.equals(h));
		System.out.println(b.equals(h1));
	}
}