package org.jeepay.pay.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.util.StreamUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * HTTP连接器
 * <p>
 * 说明:
 * </p>
 * <li>基于HttpClient</li>
 *
 * @since 2017年3月3日上午10:17:31
 */
public class HttpClientHandler  implements HttpHandler{
	protected static final Log logger = LogFactory.getLog(HttpClientHandler.class);
	private static final String UTF8 = "GBK";
	private static final String CONTENT_TYPE = "contentType";
	/** HttpClient */
	public static CloseableHttpClient httpClient = null;

	public HttpClientHandler() {
		httpClient = getHttpClient();
	}

	@Override
	public String post(String address, Map<String, String> data) {
		return doPost(address, data,null);
	}
	@Override
	public String post(String address, Map<String, String> data,Map<String,String> header) {
		return doPost(address, data,header);
	}
	@Override
	public String post(String address, String json,Map<String,String> header) {
		return doPost(address, json,header);
	}
	@Override
	public String post(String address, String json) {
		return doPost(address, json,null);
	}
	@Override
	public String get(String address) {
		return doGet(address);
	}
	@Override
	public String get(String address,Map<String,String> header) {
		return doGet(address,header);
	}
	/**
	 * 执行post请求
	 * <p>
	 * 说明:
	 * </p>
	 * <li></li>
	 *
	 * @param url
	 *            请求地址
	 * @param data
	 *            参数
	 * @return 响应结果
	 * @since 2018年12月19日上午10:09:17
	 */
	private String doPost(String url, Object data,Map<String,String> header) {
		HttpPost post = new HttpPost(url);
		CloseableHttpResponse httpResponse = null;
		String jsonStr = "";
		Long startTime = System.currentTimeMillis();
		logger.info("POST参数:"+data);
		try {
			try {
				post.setHeader(CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
				if(header != null){
					header.forEach((k,v)->post.setHeader(k,v));
				}
				if(data instanceof Map){
					Map<String,String> dataMap = (Map<String,String>)data;
					List<NameValuePair> list = new ArrayList<NameValuePair>();
					for(Entry<String,String> entry : dataMap.entrySet()){
						list.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
					}
					if(list.size() > 0){
						UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,UTF8);
						post.setEntity(entity);
					}
				}else{
					post.setEntity(new StringEntity(data.toString(),UTF8));
				}
				httpResponse = httpClient.execute(post);
			} catch (Exception e) {
				logger.error("发送远程服务调用请求时发生异常,请求URL:" + url + ",POST参数:" + jsonStr + ",信息：" + e);
				e.printStackTrace();
			}
			if (logger.isErrorEnabled()) {
				logger.error(url + ",(参数:" + jsonStr + "),请求完成,耗时->" + (System.currentTimeMillis() - startTime) / 1000.0
						+ "秒");
			}
			// 读取响应结果
			try {
				return getResponse(httpResponse);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} finally {
			try {
				if (null != httpResponse) {
					httpResponse.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	/**
	 * 执行get请求
	 * <p>
	 * 说明:
	 * </p>
	 * <li></li>
	 * @param url 请求地址
	 * @return 响应数据
	 * @since 2018年12月19日上午10:20:44
	 */
	private String doGet(String url) {
		HttpGet get = new HttpGet(url);
		CloseableHttpResponse httpResponse = null;
		Long startTime = System.currentTimeMillis();
		try {
			try {
				get.setHeader(CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
				httpResponse = httpClient.execute(get);
			} catch (Exception e) {
				logger.error("发送远程服务调用请求时发生异常,请求URL:" + url + ",信息：" + e);
				e.printStackTrace();
			}
			if (logger.isErrorEnabled()) {
				logger.error(url + ",请求完成,耗时->" + (System.currentTimeMillis() - startTime) / 1000.0 + "秒");
			}
			/**
			 * 读取响应结果
			 */
			try {
				return getResponse(httpResponse);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} finally {
			try {
				if (null != httpResponse) {
					httpResponse.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	private String doGet(String url,Map<String,String> header) {
		HttpGet get = new HttpGet(url);
		CloseableHttpResponse httpResponse = null;
		Long startTime = System.currentTimeMillis();
		try {
			try {
				if(header != null){
					header.forEach((k,v)->get.setHeader(k,v));
				}else{
					get.setHeader(CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
				}
				httpResponse = httpClient.execute(get);
			} catch (Exception e) {
				logger.error("发送远程服务调用请求时发生异常,请求URL:" + url + ",信息：" + e);
				e.printStackTrace();
			}
			if (logger.isErrorEnabled()) {
				logger.error(url + ",请求完成,耗时->" + (System.currentTimeMillis() - startTime) / 1000.0 + "秒");
			}
			/**
			 * 读取响应结果
			 */
			try {
				return getResponse(httpResponse);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} finally {
			try {
				if (null != httpResponse) {
					httpResponse.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	/**
	 * 获取HttpClient
	 * <p>
	 * 说明:
	 * </p>
	 * <li></li>
	 *
	 * @return
	 * @since 2017年6月21日上午9:43:20
	 */
	private CloseableHttpClient getHttpClient() {
		CloseableHttpClient client;
		// 设置协议http和https对应的处理socket链接工厂的对象
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", getSSLConnectionSocketFactory()).build();
		// 初始化线程池
		PoolingHttpClientConnectionManager pccm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		pccm.setMaxTotal(300);
		// 单路由最大并发数
		pccm.setDefaultMaxPerRoute(50);
		// 配置请求的超时设置
		RequestConfig requestConfig = RequestConfig.custom().setExpectContinueEnabled(true)
				.setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
				.setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
				.setSocketTimeout(4 * 1000)
				.setConnectTimeout(3 * 1000)
				.setConnectionRequestTimeout(1 * 1000).build();
		// 构建
		client = HttpClients.custom().setConnectionManager(pccm).setDefaultRequestConfig(requestConfig)
				.setRetryHandler(new DefaultHttpRequestRetryHandler()).build();// 默认失败后重发3次
		return client;
	}

	/**
	 * 获取SSLConnectionSocketFactory
	 * <p>
	 * 说明:
	 * </p>
	 * <li></li>
	 * 
	 * @author DuanYong
	 * @return
	 * @since 2017年8月31日上午11:40:30
	 */
	private SSLConnectionSocketFactory getSSLConnectionSocketFactory() {
		try {
			return new SSLConnectionSocketFactory(createIgnoreVerifySSL());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 绕过验证
	 * <p>
	 * 说明:
	 * </p>
	 * <li></li>
	 *
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @since 2017年8月31日下午3:40:59
	 */
	private static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
		SSLContext sc = SSLContext.getInstance("TLS");
		// 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
		X509TrustManager trustManager = new X509TrustManager() {
			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
					String paramString) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
					String paramString) throws CertificateException {
			}

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};

		sc.init(null, new TrustManager[] { trustManager }, null);
		return sc;
	}

	/**
	 * 获取响应结果
	 * <p>
	 * 说明:
	 * </p>
	 * <li></li>
	 *
	 * @param httpResponse
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * @since 2017年6月21日上午10:26:11
	 */
	private String getResponse(HttpResponse httpResponse) throws IOException, UnsupportedEncodingException {
		HttpEntity entity = httpResponse.getEntity();
		InputStream is = entity.getContent();
		ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
		StreamUtils.copy(is, out);
		String responseCharset = ((entity.getContentEncoding() == null
				|| entity.getContentEncoding().getValue() == null) ? UTF8 : entity.getContentEncoding().getValue());
		String result = new String(out.toByteArray(), responseCharset);
		if (logger.isInfoEnabled()) {
			logger.info("响应结果:" + result);
		}
		return result;
	}

}
