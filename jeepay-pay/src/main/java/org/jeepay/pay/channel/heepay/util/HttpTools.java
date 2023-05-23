package org.jeepay.pay.channel.heepay.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpTools {
//请求获取json信息
@SuppressWarnings({ "rawtypes", "unchecked" })
public static String httpPostToJsonStr(String url,Map map)throws Exception {
	CloseableHttpResponse response;
    CloseableHttpClient httpClient = null;
	HttpPost httpPost = null;
	String result = null;
	try{
		httpClient = HttpClients.createDefault();
		httpPost = new HttpPost(url);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		Iterator iterator = map.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String,String> elem = (Entry<String, String>) iterator.next();
			list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));
		}
		if(list.size() > 0){
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,"utf-8");
			httpPost.setEntity(entity);
		}
		response = httpClient.execute(httpPost);
		if(response != null){
			HttpEntity resEntity = response.getEntity();
			if(resEntity != null){
				result = EntityUtils.toString(resEntity,"utf-8");
			}
		}
	}catch(Exception ex){
		ex.printStackTrace();
	}finally {
        if(httpClient != null){
            try {
            	httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	return result;
 }
}
