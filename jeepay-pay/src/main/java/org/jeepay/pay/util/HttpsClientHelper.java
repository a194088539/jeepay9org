package org.jeepay.pay.util;
import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

public class HttpsClientHelper {

	public static String sendGet(String url) {
		ProtocolSocketFactory fcty = new MySecureProtocolSocketFactory();  
		Protocol.registerProtocol("https", new Protocol("https", fcty, 443));
		GetMethod getMethod = new GetMethod(url);
		String charset = "utf-8";
		getMethod.getParams().setHttpElementCharset(charset);
		getMethod.getParams().setContentCharset(charset);
		getMethod.getParams().setCredentialCharset(charset);
	
		String token=random();
		
		String result = null;
		try {
			HttpClient httpClient = new HttpClient();
			int status = httpClient.executeMethod(getMethod);
			if (status != 200) {
				return null;
			}
			result = getMethod.getResponseBodyAsString();
		} catch (HttpException e) {			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 释放连接
			getMethod.releaseConnection();
		}
		return result;
	}
	
	public static String sendPost(String url, Map<String, String> params) {
		ProtocolSocketFactory fcty = new MySecureProtocolSocketFactory();  
		Protocol.registerProtocol("https", new Protocol("https", fcty, 443));
		PostMethod postMethod = new PostMethod(url);
		String charset = "utf-8";
		//postMethod.setParameter("Content-Type", "application/x-www-form-urlencoded");
		postMethod.getParams().setHttpElementCharset(charset);
		postMethod.getParams().setContentCharset(charset);
		postMethod.getParams().setCredentialCharset(charset);
		if (MapUtils.isNotEmpty(params)) {
			Set<String> keys = params.keySet();
			for (String key : keys) {
				postMethod.addParameter(key, params.get(key));
			}
		}
		String result = null;
		try {
			HttpClient httpClient = new HttpClient();
			int status = httpClient.executeMethod(postMethod);
			if (status != 200) {
				return "http.error." + status;
			}
			result = postMethod.getResponseBodyAsString();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 释放连接
			postMethod.releaseConnection();
		}
		return result;
	}
	
	
	public static String   random() {
		
		String [] randmoArray= {"UDH5OY3G6YY4IJGUROGBNHRNGQ6POLRKXSFD3C2ZM6RBP5ADK6HA111ec8b",
								"UWRGDWRFN23KY54NZYJENQ5NXGHPDAPIHICCII4PA7CUNDYUFUCA1136210",
								"OKWQSIOFBLVLPGMPLH4E7IXFQAYASTRK7VHA53KH66P2DXES5CAA11267c2",
								"2Q4MA3DA2JX6BSYRCZBYSJN5TXUUK767HTOE7STUAL5RUUNQSPGA1121b48",
								"S2FJUMRFRASNGLU4YK7JK6WZOR5WQFINMK3R3BN5WC4PUN7CYKIQ1121210",
								"JIHDOEQXFDDFZOQGTQONBI7CJY6A24N5EKTPOO7JNPO4VXOXS53Q111aa13",
								"JXZG55GAC26LT7YSWDM2BH4QWD5RD6XH2ZKLYOKWFR6YONQPW26A113cc55",
								"WTJC3EWXFQPN35R4YIYUG32L5D5INDXL2FSKTDRKS2ZIXINUP4BA112f295",
								"HFWQPQ7UIAJKOE67GNEDLVY5BU42NXEEAYTLUB7XN4BTQDTMUCJQ111100e",
								"LG4G45D3L4SRGNVZDGT7AEQHYHUQZXYAV2CXW3F2BNID4MRNU2TQ111709e"};
		Random random=new Random();
		
		int  number=random.nextInt(10);	
		
		return randmoArray[number];
		
		
	}

	
}