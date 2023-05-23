
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

class DataTransUtil {
	/**
	 * 取得一个信任任何证书的Https链接
	 * @param url
	 * @return
	 * @throws Exception
	 */
	private static HttpsURLConnection getInstance(String url) throws Exception{
		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String urlHostName, SSLSession session) {
				return true;
			}
		};
		trustAllHttpsCertificates();
		HttpsURLConnection.setDefaultHostnameVerifier(hv);
		URL urlTmp = new URL(url);
		return (HttpsURLConnection) urlTmp.openConnection();
	}
	/**
	 * 信任所有的证书
	 * 
	 * @throws Exception
	 */
	private static void trustAllHttpsCertificates() throws Exception {
		javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
		javax.net.ssl.TrustManager tm = new miTM();
		trustAllCerts[0] = tm;
		javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, null);
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}
	static class miTM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}
		public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
			return true;
		}
		public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
			return true;
		}
		public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}
		public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}
	}
	/**
	 * http/https post
	 * @param url
	 * @param data
	 * @return
	 * @throws Exception
	 */
	protected byte [] doPost(String url,byte [] data) throws Exception {
		URLConnection con = null;
		OutputStream os = null;
		InputStream is = null;
		ByteArrayOutputStream bos = null;
		DataOutputStream dos = null;
		try {
			URL dataUrl = new URL(url);
			if (url.startsWith("https")) {
				con = getInstance(url);
			} else {
				con = (HttpURLConnection) dataUrl.openConnection();
			}
			con.setDoInput(true);
			con.setDoOutput(true);
			os = con.getOutputStream();
			os.write(data);
			os.close();
			os = null;
			is = con.getInputStream();
			byte[] b = new byte[1024];
			int len = -1;
			bos = new ByteArrayOutputStream();
			dos = new DataOutputStream(bos);
			while ((len = is.read(b)) != -1) {
				dos.write(b, 0, len);
			}
			b = bos.toByteArray();
			return b;
		} catch (Exception e) {
			throw e;
		} finally {
			try {if (is != null)is.close();} catch (Exception e) {}
			try {if (os != null){os.flush();os.close();}} catch (Exception e) {}
			try {if (dos != null)dos.close();} catch (Exception e) {}
			try {if (bos != null)bos.close();} catch (Exception e) {}
			try {closeURLConnection(con);} catch (Exception e) {}
		}
	}
	/**
	 * http/https post
	 * @param url
	 * @param data
	 * @param respDataStream 输出流
	 * @throws Exception
	 */
	protected void doPost(String url,byte [] data,OutputStream respDataStream) throws Exception {
		URLConnection con = null;
		OutputStream os = null;
		InputStream is = null;
		try {
			URL dataUrl = new URL(url);
			if (url.startsWith("https")) {
				con = getInstance(url);
			} else {
				con = (HttpURLConnection) dataUrl.openConnection();
			}
			con.setDoInput(true);
			con.setDoOutput(true);
			os = con.getOutputStream();
			os.write(data);
			os.close();
			os = null;
			is = con.getInputStream();
			byte[] b = new byte[1024];
			int len = -1;
			while ((len = is.read(b)) != -1) {
				respDataStream.write(b, 0, len);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {if (is != null)is.close();} catch (Exception e) {}
			try {if (os != null){os.flush();os.close();}} catch (Exception e) {}
			try {if (respDataStream != null){respDataStream.flush();respDataStream.close();}} catch (Exception e) {}
			try {closeURLConnection(con);} catch (Exception e) {}
		}
	}
	private void closeURLConnection(URLConnection con) {
		if (con instanceof HttpURLConnection) {
			((HttpURLConnection) con).disconnect();
		} else if (con instanceof HttpsURLConnection) {
			((HttpsURLConnection) con).disconnect();
		}
	}
	public static void main(String [] args) throws Exception{
		// PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0idXRmLTgiIHN0YW5kYWxvbmU9Im5vIj8+DQo8bWVzc2FnZSBhcHBsaWNhdGlvbj0iTm90aWZ5T3JkZXIiIG1lcmNoYW50SWQ9IjEwMDAwMDAiIG1lcmNoYW50T3JkZXJJZD0iMTQ4ODQyMDM2MjIwOCIgdmVyc2lvbj0iMS4wLjEiPg0KPGRlZHVjdExpc3Q+DQo8aXRlbSBwYXlBbXQ9IjIwMCIgcGF5RGVzYz0i5LuY5qy+5oiQ5YqfIiBwYXlPcmRlcklkPSJxY0ZaVkx0MlEzcktUT00iIHBheVN0YXR1cz0iMDEiIHBheVRpbWU9IjIwMTcwMzAyMTAwNjIwIi8+DQo8L2RlZHVjdExpc3Q+DQo8cmVmdW5kTGlzdC8+DQo8L21lc3NhZ2U+DQo=|zasaCrdzPvYZrAEA6GPp26EHOGssDdjy2WZ46+OJT7SO3h2V3WH3vkGTHj5Xg9/6HwTmul859Yo6DZioWV8TGFVo6tWFDoIlg83ZE5uIegMISGV07RZkvFummrQzxsIjyXAQyv1xjxRfzPtwd+Nf4D/0ST3sSd404X2MKyLDpcM=
//		String url="http://hb.frps.heyu168888.com:9070/notify/suile/notify_res.htm";
//		String data = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0idXRmLTgiIHN0YW5kYWxvbmU9Im5vIj8+DQo8bWVzc2FnZSBhcHBsaWNhdGlvbj0iTm90aWZ5T3JkZXIiIG1lcmNoYW50SWQ9IjEwMDAwMzkiIG1lcmNoYW50T3JkZXJJZD0iUDAxMjAxOTA2MDMyMzM0MjEyNDMwMDAxIiB2ZXJzaW9uPSIxLjAuMSI+DQo8ZGVkdWN0TGlzdD4NCjxpdGVtIHBheUFtdD0iNTAwMCIgcGF5RGVzYz0i5LuY5qy+5oiQ5YqfIiBwYXlPcmRlcklkPSJKV0MzNkdaSjNKTFhQVERNTCIgcGF5U3RhdHVzPSIwMSIgcGF5VGltZT0iMjAxOTA2MDQxNzU4NTgiLz4NCjwvZGVkdWN0TGlzdD4NCjxyZWZ1bmRMaXN0Lz4NCjwvbWVzc2FnZT4NCg=|tZj8Z3/gSrR54VxbmDmJk5IYRdhkgu/XSM0PWevuxbVBR20JDwv+8LnDWTXR5tXPegGIZ45rvY3tfQaW/GfqPhh03tPlA9pZX4J5vSkf+sI9cowpWWR8b01OZtNLa5tKnPBH6TfkAjiF+kZVNVd9jYAfPcSU2JErTJUd1f54yG0=";
//		System.out.println(new String(new DataTransUtil().doPost(url,data.getBytes()),"utf-8"));

		testBase64();
	}


	public static void testBase64() {
		String s = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0idXRmLTgiIHN0YW5kYWxvbmU9Im5vIj8+DQo8bWVzc2FnZSBhcHBsaWNhdGlvbj0iTm90aWZ5T3JkZXIiIG1lcmNoYW50SWQ9IjEwMDAwMzkiIG1lcmNoYW50T3JkZXJJZD0iUDAxMjAxOTA2MDMyMzM0MjEyNDMwMDAxIiB2ZXJzaW9uPSIxLjAuMSI+DQo8ZGVkdWN0TGlzdD4NCjxpdGVtIHBheUFtdD0iNTAwMCIgcGF5RGVzYz0i5LuY5qy+5oiQ5YqfIiBwYXlPcmRlcklkPSJKV0MzNkdaSjNKTFhQVERNTCIgcGF5U3RhdHVzPSIwMSIgcGF5VGltZT0iMjAxOTA2MDQxNzU4NTgiLz4NCjwvZGVkdWN0TGlzdD4NCjxyZWZ1bmRMaXN0Lz4NCjwvbWVzc2FnZT4NCg=";
		System.out.println(new String(org.apache.commons.codec.binary.Base64.decodeBase64(s)));
	}

}
