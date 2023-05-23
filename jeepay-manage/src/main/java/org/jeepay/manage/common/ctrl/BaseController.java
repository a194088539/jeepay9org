package org.jeepay.manage.common.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.jeepay.core.common.util.DateUtil;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.manage.common.config.MainConfig;
import org.jeepay.manage.secruity.JwtUser;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author: aragom
 * @date: 17/12/6
 * @description:
 */
@Controller
public class BaseController {

    protected static final MyLog _log = MyLog.getLog(BaseController.class);

    @Autowired
    protected MainConfig mainConfig;

    private static final int DEFAULT_PAGE_INDEX = 1;
    private static final int DEFAULT_PAGE_SIZE = 20;
    protected static final String PAGE_COMMON_ERROR = "common/error";

    protected JwtUser getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        return jwtUser;
    }

    protected int getPageIndex(JSONObject object) {
        if(object == null) return DEFAULT_PAGE_INDEX;
        Integer pageIndex = object.getInteger("page");
        if(pageIndex == null) return DEFAULT_PAGE_INDEX;
        return pageIndex;
    }

    protected int getPageSize(JSONObject object) {
        if(object == null) return DEFAULT_PAGE_SIZE;
        Integer pageSize = object.getInteger("limit");
        if(pageSize == null) return DEFAULT_PAGE_SIZE;
        return pageSize;
    }

    protected int getPageIndex(Integer page) {
        if(page == null) return DEFAULT_PAGE_INDEX;
        return page;
    }

    protected int getPageSize(Integer limit) {
        if(limit == null) return DEFAULT_PAGE_SIZE;
        return limit;
    }

    protected JSONObject getJsonParam(HttpServletRequest request) {
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

    protected String getString(JSONObject param, String key) {
        if(param == null) return null;
        return param.getString(key);
    }

    protected String getStringRequired(JSONObject param, String key) {
        if(param == null ) {
            throw new RuntimeException(getErrMsg(key));
        }
        String value = param.getString(key);
        if(StringUtils.isBlank(value)) {
            throw new RuntimeException(getErrMsg(key));
        }
        return value;
    }

    protected String getStringDefault(JSONObject param, String key, String defaultValue) {
        String value = getString(param, key);
        if(value == null) return defaultValue;
        return value;
    }

    protected Byte getByte(JSONObject param, String key) {
        if(param == null) return null;
        return param.getByte(key);
    }

    protected Byte getByteRequired(JSONObject param, String key) {
        if(param == null ) throw new RuntimeException(getErrMsg(key));
        Byte value = param.getByte(key);
        if(value == null) throw new RuntimeException(getErrMsg(key));
        return value;
    }

    protected int getByteDefault(JSONObject param, String key, byte defaultValue) {
        Byte value = getByte(param, key);
        if(value == null) return defaultValue;
        return value.byteValue();
    }

    protected Integer getInteger(JSONObject param, String key) {
        if(param == null) return null;
        return param.getInteger(key);
    }

    protected Integer getIntegerRequired(JSONObject param, String key) {
        if(param == null ) throw new RuntimeException(getErrMsg(key));
        Integer value = param.getInteger(key);
        if(value == null) throw new RuntimeException(getErrMsg(key));
        return value;
    }

    protected int getIntegerDefault(JSONObject param, String key, int defaultValue) {
        Integer value = getInteger(param, key);
        if(value == null) return defaultValue;
        return value.intValue();
    }

    protected Long getLong(JSONObject param, String key) {
        if(param == null) return null;
        return param.getLong(key);
    }

    protected Long getLongRequired(JSONObject param, String key) {
        if(param == null ) throw new RuntimeException(getErrMsg(key));
        Long value = param.getLong(key);
        if(value == null) throw new RuntimeException(getErrMsg(key));
        return value;
    }

    protected long getLongDefault(JSONObject param, String key, long defaultValue) {
        Long value = getLong(param, key);
        if(value == null) return defaultValue;
        return value.longValue();
    }

    protected JSONObject getJSONObject(JSONObject param, String key) {
        if(param == null) return null;
        return param.getJSONObject(key);
    }

    protected <T> T getObject(JSONObject param, String key, Class<T> clazz) {
        JSONObject object = getJSONObject(param, key);
        if(object == null) return null;
        return JSON.toJavaObject(object, clazz);
    }

    protected <T> T getObject(JSONObject param, Class<T> clazz) {
        if(param == null) return null;
        return JSON.toJavaObject(param, clazz);
    }

    private String getErrMsg(String key) {
        return "参数" + key + "必填";
    }

    /**
     * 参数值不能为空
     * @param param
     * @param keys
     */
    protected void checkRequired(JSONObject param, String... keys) {
        if(param == null ) throw new RuntimeException(getErrMsg(keys[0]));
        for(String key : keys) {
            String value = param.getString(key);
            if(value == null) throw new RuntimeException(getErrMsg(key));
        }
    }

    /**
     * 参数值不能为空,不能以"-"开头
     * @param param
     * @param keys
     */
    public void checkRequired2(JSONObject param, String... keys) {
        if(param == null ) throw new RuntimeException(getErrMsg(keys[0]));
        for(String key : keys) {
            String value = param.getString(key);
            if(value == null) throw new RuntimeException(getErrMsg(key));
            if(value.startsWith("-")) throw new RuntimeException("参数" + key + "不能小于0");
        }
    }

    /**
     * 得到前端传入的金额元,转换成长整型分
     * @param param
     * @param name
     * @return
     */
    public Long getRequiredAmountL(JSONObject param, String name) {
        String amountStr = getStringRequired(param, name);  // 前端填写的为元,可以为小数点2位
        Long amountL = new BigDecimal(amountStr.trim()).multiply(new BigDecimal(100)).longValue(); // // 转成分
        return amountL;
    }

    /**
     * 处理参数中的金额(将前端传入金额元转成分)
     * @param param
     * @param names
     */
    public void handleParamAmount(JSONObject param, String... names) {
        for(String name : names) {
            String amountStr = getString(param, name);  // 前端填写的为元,可以为小数点2位
            if(StringUtils.isNotBlank(amountStr)) {
                Long amountL = new BigDecimal(amountStr.trim()).multiply(new BigDecimal(100)).longValue(); // // 转成分
                param.put(name, amountL);
            }
        }
    }

    public Map<String, Object> request2payResponseMap(HttpServletRequest request, String[] paramArray) {
        Map<String, Object> responseMap = new HashMap<>();
        for (int i = 0;i < paramArray.length; i++) {
            String key = paramArray[i];
            String v = request.getParameter(key);
            if (v != null) {
                responseMap.put(key, v);
            }
        }
        return responseMap;
    }

    /**
     * 生成公共查询参训
     * @param param
     * @return
     */
    public JSONObject getQueryObj(JSONObject param) {
        // 订单起止时间
        Date createTimeStart = null;
        Date createTimeEnd = null;
        String createTimeStartStr = getString(param, "createTimeStart");
        if(StringUtils.isNotBlank(createTimeStartStr)) createTimeStart = DateUtil.str2date(createTimeStartStr);
        String createTimeEndStr = getString(param, "createTimeEnd");
        if(StringUtils.isNotBlank(createTimeEndStr)) createTimeEnd = DateUtil.str2date(createTimeEndStr);
        JSONObject queryObj = new JSONObject();
        queryObj.put("createTimeStart", createTimeStart);
        queryObj.put("createTimeEnd", createTimeEnd);
        return queryObj;
    }

}
