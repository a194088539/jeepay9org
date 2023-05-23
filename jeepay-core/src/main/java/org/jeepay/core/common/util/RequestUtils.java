package org.jeepay.core.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class RequestUtils {

    private static final Set<String> sensitiveKeySet = new HashSet<>(); //敏感参数key集合
    static{
        sensitiveKeySet.add("access_token");
        sensitiveKeySet.add("pwd");
        sensitiveKeySet.add("password");
        sensitiveKeySet.add("oldPassWord");
        sensitiveKeySet.add("payPassword");
        sensitiveKeySet.add("oldPayPassword");
        sensitiveKeySet.add("vercode");
    }

    /**
     * 将请求参数转换json对象
     * @param request
     * @param ignoreSensitive 是否将敏感信息忽略
     * @return
     */
    public static JSONObject getJsonParam(HttpServletRequest request, boolean ignoreSensitive) {
        String params = request.getParameter("params");
        if(StringUtils.isNotBlank(params)) {
            return JSON.parseObject(params);
        }
        // 参数Map
        Map properties = request.getParameterMap();
        // 返回值Map
        JSONObject returnObject = new JSONObject();
        Iterator entries = properties.entrySet().iterator();
        Map.Entry entry;
        String name;
        String value = "";
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            name = (String) entry.getKey();

            if(ignoreSensitive && sensitiveKeySet.contains(name)){
                continue;
            }

            Object valueObj = entry.getValue();
            if(null == valueObj){
                value = "";
            }else if(valueObj instanceof String[]){
                String[] values = (String[])valueObj;
                for(int i=0;i<values.length;i++){
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length()-1);
            }else{
                value = valueObj.toString();
            }
            returnObject.put(name, value);
        }
        return returnObject;
    }
}
