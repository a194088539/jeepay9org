package org.jeepay.task.reconciliation.util;

/**
 * @author: aragom
 * @date: 17/12/24
 * @description:
 */
public class StrUtil {

    public static String getBillClass(String channel) {
        channel = channel.toLowerCase();
        return "org.jeepay.reconciliation.channel." + channel + "." + toUpperCaseFirstOne(channel) + "BillService";
    }

    public static String toUpperCaseFirstOne(String s){
        if(Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }

}
