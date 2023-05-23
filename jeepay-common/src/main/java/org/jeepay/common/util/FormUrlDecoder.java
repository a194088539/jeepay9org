package org.jeepay.common.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Package com.fudian.common.utils
 * @Class: FormUrlDecoder.java
 * @Description:
 * @Author leo
 * @Date 2018/12/28 14:48
 * @Version
 **/
public class FormUrlDecoder {

    private Map<String, String> parameters;

    public FormUrlDecoder(String str) {
        this.parameters = new HashMap<>();
        parse(this.parameters, str);
    }

    public Map<String, String> getParameters() {
        return this.parameters;
    }

    public static Map<String, String> getMap(String params) {
        HashMap<String, String> map = new HashMap<>();

        int start = 0, len = params.length();

        while (start < len) {
            int i = params.indexOf('&', start);

            if (i == -1) {
                i = params.length(); // 此时处理最后的键值对
            }

            String keyValue = params.substring(start, i);

            int j = keyValue.indexOf('=');
            String key = keyValue.substring(0, j);
            String value = keyValue.substring(j + 1, keyValue.length());

            map.put(key, value);

            if (i == params.length()) {
                break;
            }

            start = i + 1; // index+1 为下一个键值对的起始位置
        }

        return map;
    }

    public String get(String key) {
        return this.get(key, null);
    }

    public String get(String key, String defaultValue) {
        String value = this.parameters.get(key);
        return Objects.isNull(value) ? defaultValue : value;
    }

    private int getInt32(String key) {
        return this.getInt32(key, 0);
    }

    private int getInt32(String key, int defaultValue) {
        String value = this.parameters.get(key);
        return Objects.isNull(value) || value.isEmpty() ? defaultValue : Integer.parseInt(value);
    }

    public long getInt64(String key) {
        return this.getInt64(key, 0L);
    }

    private long getInt64(String key, long defaultValue) {
        String value = this.parameters.get(key);
        return Objects.isNull(value) || value.isEmpty() ? defaultValue : Long.parseLong(value);
    }

    private float getFloat32(String key) {
        return this.getFloat32(key, 0F);
    }

    private float getFloat32(String key, float defaultValue) {
        String value = this.parameters.get(key);
        return Objects.isNull(value) || value.isEmpty() ? defaultValue : Float.parseFloat(value);
    }

    public double getFloat64(String key) {
        return this.getFloat64(key, 0D);
    }

    private double getFloat64(String key, double defaultValue) {
        String value = this.parameters.get(key);
        return Objects.isNull(value) || value.isEmpty() ? defaultValue : Double.parseDouble(value);
    }

    private boolean getBool(String key) {
        return this.getBool(key, false);
    }

    private boolean getBool(String key, boolean defaultValue) {
        String value = this.parameters.get(key);
        return Objects.isNull(value) || value.isEmpty() ? defaultValue : Boolean.parseBoolean(value);
    }

    private boolean contains(String key) {
        return this.parameters.containsKey(key);
    }

    private static void parse(Map<String, String> map, String str) {
        if(Objects.isNull(str) || str.isEmpty()) return ;
        Arrays.stream(str.split("&"))
                .filter(kv -> kv.contains("="))
                .map(kv -> kv.split("="))
                .forEach(array -> map.put(array[0], array[1]));
    }

}
