package org.jeepay.common.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;

/**
 * @Package com.fudian.utils
 * @Class: HtmlUtils.java
 * @Description:
 * @Author leo
 * @Date 2018/12/26 0:11
 * @Version
 **/
public class HtmlUtils {

    private final static String CHARSET = "UTF-8";

    private final static String MONEY_TEMPLATE = "###amt###";
    private final static String TRADE_NO_TEMPLATE = "###tno###";
    private final static String UID_TEMPLATE = "###uid###";

    public final static String POST = "POST";
    public final static String GET = "GET";

    // alipays://platformapi/startapp?appId=20000123&actionType=scan&biz_data={'s': 'money','u': '2088332165930310','a': '1','m': '2330'}
    private final static String ALIPAY_URL_SCHEME_COLLECT = "alipays://platformapi/startapp?appId=20000123&actionType=scan&biz_data=";

    // https://ds.alipay.com/?from=mobilecodec&scheme=alipays://...
    public static String alipayScheme(Map<String, String> params) {
        JSONObject bizData = new JSONObject();
        bizData.put("s", "money");
        bizData.put("u", params.get("uid"));
        bizData.put("a", params.get("amount"));
        bizData.put("m", params.get("outTradeNo"));
        StringBuilder builder = new StringBuilder()
                .append("<script>")
                .append("window.location.href='")
                .append(ALIPAY_URL_SCHEME_COLLECT)
                .append(bizData.toJSONString())
                .append("'</script>");
        return builder.toString();
    }

    public static String form(String url, Map<String, Object> params) {
        return form(url, POST, params);
    }

    public static String form(String url, String method, Map<String, Object> params) {
        StringBuilder form = new StringBuilder();
        String inputTemplate = "    <input type='hidden' name='%s' value='%s' />";

        form.append("<form id='form_submit' name='form_submit' action='"+url+"' method='"+method+"'>");
        String input = "";
        for(Object key : params.keySet()) {
            input = inputTemplate;
            Object value = params.get(key);
            form.append(String.format(input, key, value));
        }
        form.append("   <input type='submit' value='POST' style='display:none;'>");
        form.append("</form>");
        form.append("<script>");
        form.append("        document.forms['form_submit'].submit();");
        form.append("</script>");
        return form.toString();
    }



    public static String createHtml(String fileName, Map<String, String> params) {

        String html = read(fileName);

        String money = params.get("amount");
        String tradeNo = params.get("outTradeNo");
        String uid = params.get("uid");

        html = html.replaceAll(MONEY_TEMPLATE, money);
        html = html.replaceAll(TRADE_NO_TEMPLATE, tradeNo);
        html = html.replaceAll(UID_TEMPLATE, uid);

        return html;
    }

    public static String read(String fileName) {
        Resource resource = new ClassPathResource(fileName);
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        try {
            inputStream = resource.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String b ;
        StringBuffer stringBuffer=new StringBuffer();
        try {
            while ((b=bufferedReader.readLine())!=null){
                stringBuffer.append(b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuffer.toString();
    }

    public static String encodeURI(String url) {
        try {
            return URLEncoder.encode(url, CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String encodeURI(StringBuilder sb) {
        return encodeURI(sb.toString());
    }

    public static String decodeURI(String url) {
        try {
            return URLDecoder.decode(url, CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }


    public static String toJS(String url) {
        StringBuilder form = new StringBuilder("<script>")
                .append("window.location.href='")
                .append(url)
                .append("'</script>");
        return form.toString();
    }

    public static String toJS(StringBuilder sb) {
        return toJS(sb.toString());
    }

    public static String map2Url(Map<String, String> paramers) {
        boolean hasParams = false;
        StringBuilder builder = new StringBuilder();
        Set<Map.Entry<String, String>> params = paramers.entrySet();
        for (Map.Entry<String, String> entry : params) {
            if (hasParams) {
                builder.append("&");
            } else {
                hasParams = true;
            }
            String key = entry.getKey().trim();
            String value = entry.getValue(); // .trim()
            builder.append(key).append("=").append(value);
        }

        return builder.toString();
    }

    public static String doPathUrl(String url, Map<String, String> paramers) {
        if (url.endsWith("?")) {
            url += map2Url(paramers);
        } else {
            url = url + "?" + map2Url(paramers);
        }
        return url;
    }

    public static String notifyUrlHandle(String notifyUrl, Integer accId) {
        if(notifyUrl.endsWith("notify") || notifyUrl.endsWith("notify/")) {
            return new StringBuilder(notifyUrl)
                    .append("/")
                    .append(accId)
                    .toString();
        }
        return notifyUrl;
    }

    /**
     * 发送application/json请求
     * @param url
     * @param body
     * @return
     * @throws Exception
     */
    public static String sendHttpPost(String url, String body) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json");
        //httpPost.addHeader("X-Auth-Signature",Signature);
        httpPost.setEntity(new StringEntity(body,"utf-8"));
        CloseableHttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        String responseContent = EntityUtils.toString(entity, "UTF-8");
        response.close();
        httpClient.close();
        return responseContent;
    }
    public static String sendHttpGet(String url,String Signature) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Content-Type", "application/json charset=utf-8");
        httpGet.addHeader("X-Auth-Signature",Signature);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String responseContent = EntityUtils.toString(entity, "UTF-8");
        response.close();
        httpClient.close();
        return responseContent;
    }

    public static String sendHttpPostForm(String url, Map<String,Object> postParam) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result=null;
        try{
            //把一个普通参数和文件上传给下面这个地址    是一个servlet
            HttpPost httpPost = new HttpPost(url);
            //设置传输参数
            MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
            //设计文件以外的参数
            Set<String> keySet = postParam.keySet();
            for (String key : keySet) {
                //相当于<input type="text" name="name" value=name>
                multipartEntity.addPart(key, new StringBody((String) postParam.get(key), ContentType.create("text/plain", Consts.UTF_8)));
            }

            HttpEntity reqEntity =  multipartEntity.build();
            httpPost.setEntity(reqEntity);


            //发起请求   并返回请求的响应
            CloseableHttpResponse response = httpClient.execute(httpPost);
            try {

                //打印响应状态
                //log.info(response.getStatusLine());
                // resultMap.put("statusCode", response.getStatusLine().getStatusCode());
                //获取响应对象
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    //打印响应长度

                    //打印响应内容
                    //resultMap.put("data", EntityUtils.toString(resEntity,Charset.forName("UTF-8")));
                    result=EntityUtils.toString(resEntity, Charset.forName("UTF-8"));
                }
                //销毁
                EntityUtils.consume(resEntity);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally{
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
