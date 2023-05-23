package org.jeepay.core.common.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @Package com.fudian.common.utils
 * @Class: RequestParamsContextHolder.java
 * @Description: 请求参数获取工具类
 * @Author xxx
 * @Date 2018/5/25 12:22
 * @Version v1.0
 **/
public class RequestParamsContextHolder {
    private static final Logger LOGGER = Logger.getLogger(RequestParamsContextHolder.class.getName());
    /**
     * 将request查询参数封装至Map
     *
     * @param request  请求
     * @param printLog 是否打印日志
     * @return 参数Map
     */
    public static Map<String, Object> getParameters(HttpServletRequest request,
                                                    boolean printLog) {
        Enumeration<String> enume = request.getParameterNames();
        Map<String, Object> map = new HashMap<String, Object>();
        while (enume.hasMoreElements()) {
            String key = enume.nextElement();
            String value = request.getParameter(key);
            map.put(key, value);
            if (printLog) {
                LOGGER.info(key + "==>" + value);
            }
        }
        if (map.get("sort") != null) {
            map.put("sort", "asc");
        }
        return map;
    }

    /**
     * 将request查询参数封装至Map
     *
     * @param request 请求
     * @return 参数Map
     */
    public static Map<String, Object> getParameters(HttpServletRequest request) {

        return getParameters(request, false);
    }

    /**
     * 获取请求方IP
     *
     * @param request 请求
     * @return 客户端Ip
     */
    public static String getClientIp(HttpServletRequest request) {
        String xff = request.getHeader("x-forwarded-for");
        if (xff == null) {
            return request.getRemoteAddr();
        }
        return xff;
    }

    /**
     * 主要功能:获取请求方IP
     * 注意事项:无
     *
     * @param request 请求
     * @return String IP
     */
    public static String getIpAddrByRequest(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取完整的请求URL
     *
     * @param request 请求
     * @return URL
     */
    public static String getRequestUrl(HttpServletRequest request) {
        return request.getRequestURL().toString();
    }

    /**
     * 主要功能:获取request
     * 注意事项:无
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 主要功能:获取前端请求并转换map
     * 注意事项:无
     *
     * @param request 请求
     * @return 参数map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getBody(HttpServletRequest request) {
        // 获取前台参数
        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(
                        inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                }
            }
        }
        body = stringBuilder.toString();
        // 转换成map
        Map<String, Object> paramers = JSONObject.parseObject(body);
        return paramers;
    }


    public static String getXmlParameters(HttpServletRequest request) {

        InputStream is = null;
        try {
            //取得通知信息
            StringBuffer sbuff = new StringBuffer("");
            is = request.getInputStream();
            byte[] b = new byte[1024];
            int len = -1;
            while((len = is.read(b)) != -1){
                sbuff.append(new String(b,0,len,"utf-8"));
            }
            return sbuff.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(is != null){
                try {
                    is.close();
                } catch (Exception e) {}
            }
        }
        return null;
        // 读取xml
//        InputStream inputStream = null;
//        BufferedReader in = null;
//        try {
//            inputStream = request.getInputStream();
//
//            StringBuffer sb = new StringBuffer();
//
//            String s;
//            in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
//            while ((s = in.readLine()) != null) {
//                sb.append(s);
//            }
//            return sb.toString();
//        } catch(Exception e) {
//            e.printStackTrace();
//        } finally {
//            if(null != in) {
//                try {
//                    in.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            if(null != inputStream) {
//                try {
//                    inputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return null;
    }

}
