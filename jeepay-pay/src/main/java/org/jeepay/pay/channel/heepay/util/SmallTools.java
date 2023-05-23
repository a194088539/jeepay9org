package org.jeepay.pay.channel.heepay.util;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.jeepay.core.common.util.JsonUtil;
import org.jeepay.core.common.util.MyLog;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by Administrator on 2017/5/27.
 */
public class SmallTools {
	private static final MyLog _log = MyLog.getLog(SmallTools.class);
    /**
     * MD5加密
     * @param str 需要加密的值
     * @return 加密完成的值(小写)
     */
    public static String MD5en(String str){
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;
    }


    /**
     * 获取当前时间（格式自传）
     * @param dateFormat 要返回的时间格式，例如yyyy/MM/dd HH:mm:ss
     * @return
     */
    public static String getDate(String dateFormat){
        Date date = new Date();
        SimpleDateFormat dateF = new SimpleDateFormat(dateFormat);//可以方便地修改日期格式
        String retu = dateF.format(date);
        return retu;
    }

    /**
     * Json格式数据验签
     * 传入返回数据和KEY，返回验签结果（验签成功返回true，失败返回false）
     * 如果请求报错，然后的参数中不会有sign字段也就不存在验签问题，直接提示参数中无sign字段无需验签，方法返回false
     */
    public static Boolean checkSign(String parameter, String key){
        if (!parameter.contains("sign")){
        	_log.info("参数中无sign字段无需验签");
            return false;
        }
        Map<String, String> map = JsonUtil.getObjectFromJson(parameter,Map.class);
        Iterator<String> a = map.keySet().iterator();
        a.toString();
        ArrayList<String> keys = new ArrayList<String>();
        while (true){
            String k = a.next();
            if (!k.equals("sign")){
                keys.add(k);
            }
            if (!a.hasNext()){
                break;
            }
        }
        // 进行升序排列
        Collections.sort(keys, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        StringBuilder mSign = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            mSign.append(keys.get(i));
            mSign.append("=");
            mSign.append(map.get(keys.get(i)));
            mSign.append("&");
        }
        mSign.append("key="+key);
        _log.info("返回签名参数{}：",mSign.toString());
        String msign = SmallTools.MD5en(mSign.toString());
        _log.info("本地签名结果：{}：",msign);
        String rsign = map.get("sign");
        _log.info("返回签名结果：{}：",rsign);
        if (msign.equals(rsign)){
        	 _log.info("签名验证成功");
            return true;
        }else {
        	 _log.info("签名验证失败");
            return false;
        }
    }


    /**
     * aa=11&bb=22 格式数据转 Json格式数据
     */
    public static String toJson(String parameter){
        JSONObject jsobj = new JSONObject();
        if (!parameter.contains("&")){
            if (!parameter.contains("=")){
            	 _log.info("参数格式不正确，无法转换");
            }else{
                String[] list = parameter.split("=");
                jsobj.put(list[0],list[1]);
                return jsobj.toString();
            }
        }else {
            String[] strings = parameter.split("&");
            for (int i=0;i<strings.length;i++){
                String[] list = strings[i].split("=");
                jsobj.put(list[0],list[1]);
            }
            return jsobj.toString();
        }
        return "";
    }

}
