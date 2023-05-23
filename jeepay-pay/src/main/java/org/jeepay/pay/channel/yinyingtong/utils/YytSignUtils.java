package org.jeepay.pay.channel.yinyingtong.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class YytSignUtils {

    public static String getSignature(String aid, String api_id, String key, String timestamp, String nonce)
    {
        return getSignature(aid, null, api_id, key, timestamp, nonce, null);
    }

    public static String getSignature(String aid, String tid, String api_id, String key, String timestamp, String nonce, String data_sign)
    {
        return getSignature(aid, tid, api_id, key, timestamp, nonce, data_sign, null, null);
    }

    public static String getSignature(String aid, String tid, String api_id, String key, String timestamp, String nonce, String data_sign, String forward,
                                      String error_url)
    {
        if(aid == null)
            aid = "";
        if(tid == null)
            tid = "";
        if(api_id == null)
            api_id = "";
        if(key == null)
            key = "";
        if(timestamp == null)
            timestamp = "";
        if(nonce == null)
            nonce = "";
        if(data_sign == null)
            data_sign = "";
        if(forward == null)
            forward = "";
        if(error_url == null)
            error_url = "";
        String args[] = {
                aid, tid, api_id, key, timestamp, nonce, data_sign, forward, error_url
        };
        return getSignature(args);
    }


    //用于密文转换，加解密因子加密
    public static String MD5(String str) {
        try {
            MessageDigest digist = MessageDigest.getInstance("MD5");
            byte[] rs = digist.digest(str.getBytes("UTF-8"));
            StringBuffer digestHexStr = new StringBuffer();
            for (int i = 0; i < 16; i++) {
                digestHexStr.append(byteHEX(rs[i]));
            }
            return digestHexStr.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


    public static String getSignature(String args[])
    {
        if(args == null || args.length == 0)
            return null;
        Arrays.sort(args);
        StringBuilder content = new StringBuilder();
        for(int i = 0; i < args.length; i++)
            content.append(args[i]);

        System.out.println((new StringBuilder()).append("signature >>> ").append(content.toString()).toString());
        MessageDigest md = null;
        String tmp_str = null;
        try
        {
            md = MessageDigest.getInstance("SHA-1");
            byte digest[] = md.digest(content.toString().getBytes());
            tmp_str = byteToStr(digest);
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        content = null;
        return tmp_str;
    }

    private static String byteToHexStr(byte m_byte)
    {
        char digit[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'
        };
        char temp_arr[] = new char[2];
        temp_arr[0] = digit[m_byte >>> 4 & 15];
        temp_arr[1] = digit[m_byte & 15];
        String s = new String(temp_arr);
        return s;
    }

    private static String byteHEX(byte ib) {
        char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
                'B', 'C', 'D', 'E', 'F' };
        char[] ob = new char[2];
        ob[0] = Digit[(ib >>> 4) & 0X0F];
        ob[1] = Digit[ib & 0X0F];
        String s = new String(ob);
        return s;
    }

    private static String byteToStr(byte byte_array[])
    {
        String str_digest = "";
        for(int i = 0; i < byte_array.length; i++)
            str_digest = (new StringBuilder()).append(str_digest).append(byteToHexStr(byte_array[i])).toString();

        return str_digest;
    }

    public static String httpurlconnectionpost(String req_url,  String data)  {
        String result = null;
        StringBuilder sb = new StringBuilder();
        InputStreamReader isr = null;
        InputStream is = null;
        BufferedReader br = null;
        try{
            URL url = new URL(req_url);
            HttpURLConnection http_conn = (HttpURLConnection) url.openConnection();
            //http正文内，因此需要设为true, 默认情况下是false;
            http_conn.setDoOutput(true);
            //设置是否从httpUrlConnection读入，默认情况下是true;
            http_conn.setDoInput(true);
            //Post请求不能使用缓存
            http_conn.setUseCaches(false);
            //设定请求的方法为"POST"，默认是GET
            http_conn.setRequestMethod("POST");
            //连接主机的超时时间（单位：毫秒）
            http_conn.setConnectTimeout(60 * 1000);
            //从主机读取数据的超时时间（单位：毫秒）
            http_conn.setReadTimeout(60 * 1000);
            //设置通用的请求属性
            http_conn.setRequestProperty("Accept", "*/*");
            http_conn.setRequestProperty("Connection", "Keep-Alive");
            http_conn.setRequestProperty("Cache-Control", "no-cache");
            http_conn.setRequestProperty("Content-Type", "text/xml");
            http_conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; Foxy/1; .NET CLR 2.0.50727; MEGAUPLOAD 1.0)");
            http_conn.connect();
            // 当有数据需要提交时
            if (null != data || !"".equals(data)) {
                OutputStreamWriter out = new OutputStreamWriter(http_conn.getOutputStream());
                out.write(java.net.URLEncoder.encode(data, "utf-8"));
                out.flush();
                out.close();
            }
            // 将返回的输入流转换成字符串
            is = http_conn.getInputStream();
            isr = new InputStreamReader(is, "utf-8");
            br = new BufferedReader(isr);
            String str = null;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }

            result = sb.toString() ;

        }catch (Exception e) {
            e.printStackTrace();

            result = e.getMessage( );
        }finally{
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (isr !=null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(is != null) {
                try {
                    is.close();
                    is = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

}
