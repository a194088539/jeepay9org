package org.jeepay.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期转换运算工具
 * @author  egan
 * <pre>
 * email egzosn@gmail.com
 * date 2018-11-21 16:43:20
 * </pre>
 */
public final class DateUtils {
    public static final DateFormat  YYYY_MM_DD_HH_MM_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final DateFormat  YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateFormat YYYYMMDD = new SimpleDateFormat("yyyyMMdd");
    public static final DateFormat YYYYMMDDHHMMSS = new SimpleDateFormat("yyyyMMddHHmmss");
    public static final DateFormat YYYYMMDDHHMMSSSSS = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    public static final DateFormat MMDD = new SimpleDateFormat("MMdd");

    static {
        TimeZone timeZone = TimeZone.getTimeZone("GMT+8");
        YYYY_MM_DD_HH_MM_SS.setTimeZone(timeZone);
        YYYY_MM_DD.setTimeZone(timeZone);
        YYYYMMDD.setTimeZone(timeZone);
        YYYYMMDDHHMMSS.setTimeZone(timeZone);
        MMDD.setTimeZone(timeZone);
    }

    public static final String format(Date date){
        return YYYY_MM_DD_HH_MM_SS.format(date);
    }

    public static final String formatDay(Date date){
        return YYYY_MM_DD.format(date);
    }

    /**
     * 剩余分钟数
     * @param date 结束点日期
     * @return 分钟数
     */
    public static final long minutesRemaining(Date date){
        return (date.getTime() - System.currentTimeMillis()) / 1000 / 60 ;
    }

    /**
     * 剩余小时
     * @param date 结束点日期
     * @return 小时数
     */
    public static final long remainingHours(Date date){
        return minutesRemaining(date) / 60 ;
    }
    /**
     * 剩余天数
     * @param date 结束点日期
     * @return 天数
     */
    public static final long remainingDays(Date date){
        return remainingHours(date) / 24 ;
    }

    /**
     * 10位时间戳
     * @param date
     * @return
     */
    public static final String timestamp(Date date) {
        long timeStampSec = System.currentTimeMillis()/1000;
        return String.format("%010d", timeStampSec);
    }

}
