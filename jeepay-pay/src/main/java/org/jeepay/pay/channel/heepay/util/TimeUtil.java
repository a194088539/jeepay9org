package org.jeepay.pay.channel.heepay.util;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
public class TimeUtil {
	public static String getPreYear(String today, int n)
	  {
	    String result = null;
	    try {
	      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	      Calendar c = Calendar.getInstance();
	      c.setTime(sdf.parse(today));
	      c.add(1, -1);
	      Date y = c.getTime();
	      result = sdf.format(y);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return result;
	  }

	  public static String getPreMonth(String today, String format, int n)
	  {
	    String result = null;
	    try {
	      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
	      String tmp = changeStrTimeFormat(today, format, "yyyyMMdd");
	      int year = Integer.parseInt(tmp.substring(0, 4));
	      int month = Integer.parseInt(tmp.substring(4, 6));
	      Calendar calendar = Calendar.getInstance();
	      month = month - n - 1;
	      if (month < 0) {
	        year -= 1;
	        month += 12;
	      }
	      calendar.set(year, month, 1, 0, 0, 0);
	      result = sdf.format(new Date(calendar.getTime().getTime()));
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return result;
	  }

	  public static String getMonthLastDate(String today, String format)
	  {
	    String result = null;
	    try {
	      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	      String tmp = changeStrTimeFormat(today, format, "yyyyMMdd");
	      int year = Integer.parseInt(tmp.substring(0, 4));
	      int month = Integer.parseInt(tmp.substring(4, 6));
	      if (month == 12) {
	        year += 1;
	        month = 0;
	      }
	      Calendar calendar = Calendar.getInstance();
	      calendar.set(year, month, 1, 0, 0, 0);
	      result = sdf.format(new Date(calendar
	        .getTime().getTime() - 3600000L));
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return result;
	  }

	  public static String getPreMonthLastDate(String today, String format)
	  {
	    String result = null;
	    try {
	      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	      String tmp = changeStrTimeFormat(today, format, "yyyyMMdd");
	      int year = Integer.parseInt(tmp.substring(0, 4));
	      int month = Integer.parseInt(tmp.substring(4, 6));
	      Calendar calendar = Calendar.getInstance();
	      calendar.set(year, month - 1, 1, 0, 0, 0);
	      result = sdf.format(new Date(calendar
	        .getTime().getTime() - 3600000L));
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return result;
	  }

	  public static String getNextMonthFirstDate(String today, String format)
	  {
	    String result = null;
	    try {
	      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	      String tmp = changeStrTimeFormat(today, format, "yyyyMMdd");
	      int year = Integer.parseInt(tmp.substring(0, 4));
	      int month = Integer.parseInt(tmp.substring(4, 6));
	      if (month == 12) {
	        year += 1;
	        month = 0;
	      }
	      Calendar calendar = Calendar.getInstance();
	      calendar.set(year, month, 1, 0, 0, 0);
	      result = sdf.format(calendar.getTime());
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return result;
	  }

	  public static String getFirstDateOfTheMonth(String today, String format)
	  {
	    String result = null;
	    try {
	      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	      String tmp = changeStrTimeFormat(today, format, "yyyyMMdd");
	      int year = Integer.parseInt(tmp.substring(0, 4));
	      int month = Integer.parseInt(tmp.substring(4, 6));
	      Calendar calendar = Calendar.getInstance();
	      calendar.set(year, month - 1, 1, 0, 0, 0);
	      result = sdf.format(calendar.getTime());
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return result;
	  }

	  public static String dateFormat(Date date, String format)
	  {
	    String result = null;
	    try {
	      if (date == null) {
	        result = "";
	      } else {
	        SimpleDateFormat sdf = new SimpleDateFormat(format);
	        result = sdf.format(date);
	      }
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return result;
	  }

	  public static Date strTimeToDate(String date, String format)
	  {
	    Date result = null;
	    try {
	      SimpleDateFormat sdf = new SimpleDateFormat(format);
	      result = sdf.parse(date);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return result;
	  }

	  public static String changeStrTimeFormat(String date, String oldFormat, String newFormat)
	  {
	    String result = null;
	    try {
	      if ((date == null) || (date.equals(""))) {
	        return "";
	      }
	      SimpleDateFormat sdf = new SimpleDateFormat(oldFormat);
	      Date tmp = sdf.parse(date);
	      sdf.applyPattern(newFormat);
	      result = sdf.format(tmp);
	    }
	    catch (ParseException e)
	    {
	      e.printStackTrace();
	    }
	    if (result == null) {
	      return "";
	    }
	    return result;
	  }

	  public static String getCurDate(String dateFormat)
	  {
	    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

	    Calendar c1 = Calendar.getInstance();
	    return sdf.format(c1.getTime());
	  }

	  public static Date getDateRelateToDate(Date date, int dateCnt)
	  {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.add(5, dateCnt);
	    return calendar.getTime();
	  }

	  public static Date getDateRelateToMonth(Date date, int monthCnt)
	  {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.add(2, monthCnt);
	    return calendar.getTime();
	  }

	  public static Date changeStrToDate(String date, String format)
	    throws Exception
	  {
	    SimpleDateFormat sf = new SimpleDateFormat(format);
	    Date dt = null;
	    try {
	      dt = sf.parse(date);
	    } catch (ParseException e) {
	      e.printStackTrace();
	      throw e;
	    }
	    return dt;
	  }

	  public static Date getLastWorkday(Date date)
	  {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    int today = calendar.get(7);
	    if (today == calendar.getFirstDayOfWeek())
	      calendar.roll(6, -3);
	    else {
	      calendar.roll(6, -1);
	    }
	    return calendar.getTime();
	  }

	  public static String getWeekday(Date date)
	  {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    String weekday = "";
	    int today = calendar.get(7);
	    switch (today) {
	    case 1:
	      weekday = "SUNDAY";
	      break;
	    case 2:
	      weekday = "MONDAY";
	      break;
	    case 3:
	      weekday = "TUESDAY";
	      break;
	    case 4:
	      weekday = "WEDNESDAY";
	      break;
	    case 5:
	      weekday = "THURSDAY";
	      break;
	    case 6:
	      weekday = "FRIDAY";
	      break;
	    case 7:
	      weekday = "SATURDAY";
	      break;
	    }

	    return weekday;
	  }

	  public static int getMonth(Date date) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    int currentMonth = calendar.get(2);
	    return currentMonth;
	  }

	  public static String getMonthOfStringVal(Date date)
	  {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    String month = "";
	    int currentMonth = calendar.get(2);
	    switch (currentMonth) {
	    case 0:
	      month = "JANUARY";
	      break;
	    case 1:
	      month = "FEBRUARY";
	      break;
	    case 2:
	      month = "MARCH";
	      break;
	    case 3:
	      month = "APRIL";
	      break;
	    case 4:
	      month = "MAY";
	      break;
	    case 5:
	      month = "JUNE";
	      break;
	    case 6:
	      month = "JULY";
	      break;
	    case 7:
	      month = "AUGUST";
	      break;
	    case 8:
	      month = "SEPTEMBER";
	      break;
	    case 9:
	      month = "OCTOBER";
	      break;
	    case 10:
	      month = "NOVEMBER";
	      break;
	    case 11:
	      month = "DECEMBER";
	      break;
	    }

	    return month;
	  }

	  public static String getPreMonth(Date date) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    String month = "";
	    int currentMonth = calendar.get(2);
	    if (currentMonth == 0)
	      currentMonth = 11;
	    else {
	      currentMonth -= 1;
	    }
	    switch (currentMonth) {
	    case 0:
	      month = "JANUARY";
	      break;
	    case 1:
	      month = "FEBRUARY";
	      break;
	    case 2:
	      month = "MARCH";
	      break;
	    case 3:
	      month = "APRIL";
	      break;
	    case 4:
	      month = "MAY";
	      break;
	    case 5:
	      month = "JUNE";
	      break;
	    case 6:
	      month = "JULY";
	      break;
	    case 7:
	      month = "AUGUST";
	      break;
	    case 8:
	      month = "SEPTEMBER";
	      break;
	    case 9:
	      month = "OCTOBER";
	      break;
	    case 10:
	      month = "NOVEMBER";
	      break;
	    case 11:
	      month = "DECEMBER";
	      break;
	    }

	    return month;
	  }

	  public static boolean checkDate(String date, String format)
	  {
	    if ((null == format) || (null == date)) {
	      return false;
	    }

	    DateFormat dateFormat = new SimpleDateFormat(format);
	    try {
	      Date formatDate = dateFormat.parse(date);
	      return date.equals(dateFormat.format(formatDate)); } catch (ParseException e) {
	    }
	    return false;
	  }

	  public static long fromTimeNow(Date begin)
	  {
	    Date end = new Date();
	    long between = (end.getTime() - begin.getTime()) / 1000L;
	    return between;
	  }

	  public static long compare(Date date1, Date date2)
	  {
	    return date1.getTime() - date2.getTime();
	  }

	  public static long compare(String date1, String date2, String format) throws ParseException {
	    DateFormat dateFormat = new SimpleDateFormat(format);
	    Date formatDate1 = dateFormat.parse(date1);
	    Date formatDate2 = dateFormat.parse(date2);
	    return formatDate1.getTime() - formatDate2.getTime();
	  }

	  public static int daysBetween(Date startdate, Date enddate) throws ParseException {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    startdate = sdf.parse(sdf.format(startdate));
	    enddate = sdf.parse(sdf.format(enddate));
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(startdate);
	    long time1 = cal.getTimeInMillis();
	    cal.setTime(enddate);
	    long time2 = cal.getTimeInMillis();
	    long between_days = (time2 - time1) / 86400000L;
	    return Integer.parseInt(String.valueOf(between_days));
	  }

	  public static int daysBetween(String startdate, String enddate)
	    throws ParseException
	  {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    Date start = sdf.parse(startdate);
	    Date end = sdf.parse(enddate);
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(start);
	    long time1 = cal.getTimeInMillis();
	    cal.setTime(end);
	    long time2 = cal.getTimeInMillis();
	    long between_days = (time2 - time1) / 86400000L;
	    return Integer.parseInt(String.valueOf(between_days));
	  }
}
