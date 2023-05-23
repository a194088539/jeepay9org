package org.jeepay.common.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @Package com.egzosn.pay.common.util
 * @Class: ConvertUtils.java
 * @Description:
 * @Author leo
 * @Date 2018/12/11 20:30
 * @Version
 **/
public class ConvertUtils {

    /**
     * Map转实体类
     * @param type 实体类Class
     * @param map
     * @return
     */
    public static Object convertMap2Bean(Class type, Map map) throws IntrospectionException, IllegalAccessException, InstantiationException, InvocationTargetException {
        BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
        Object obj = type.newInstance(); // 创建 JavaBean 对象

        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();

            if (map.containsKey(propertyName)) {
                // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                Object value = map.get(propertyName);

                Object[] args = new Object[1];
                args[0] = value;

                descriptor.getWriteMethod().invoke(obj, args);
            }
        }
        return obj;
    }

    /**
     * 实体类转Map
     * @param bean bean实体
     * @return
     * @throws Exception
     */
    public static Map convertBean2Map(Object bean) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        Class type = bean.getClass();
        Map returnMap = new HashMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        PropertyDescriptor [] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for(PropertyDescriptor descriptor : propertyDescriptors) {
            String propertyName = descriptor.getName();
            if(!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean);
                if(null != result) {
                    returnMap.put(propertyName, result);
                } else {
                    returnMap.put(propertyName, "");
                }
            }
        }
        return returnMap;
    }

    public static String convertInputStream(InputStream is) {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            String str = result.toString(StandardCharsets.UTF_8.name());
            return str;
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (null != result) {
                try {
                    result.close();
                    is.close();
                } catch (IOException ex) {
                }
            }
        }
        return null;
    }
}
