package com.henry.common.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * 
 */
public class DateUtil extends org.apache.commons.lang3.time.DateUtils {
    public static final String DATE_DIVISION = "-";
    public static final String TIME_PATTERN_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN_DEFAULT = "yyyy-MM-dd";
    public static final String DATE_PATTERN_YYYYMMDD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String DATA_PATTERN_YYYYMMDD = "yyyyMMdd";
    public static final String TIME_PATTERN_HHMMSS = "HH:mm:ss";

    public static final int ONE_SECOND = 1000;
    public static final int ONE_MINUTE = 60 * ONE_SECOND;
    public static final int ONE_HOUR = 60 * ONE_MINUTE;
    public static final long ONE_DAY = 24 * ONE_HOUR;
    public static final long THREE_DAY = 3 * ONE_DAY / 1000;
    
    private static String[] parsePatterns = {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd HH:mm", "yyyy-MM", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
            "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * Return the current date
     * 
     * @return － DATE<br>
     */
    public static Date getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        Date currDate = cal.getTime();

        return currDate;
    }
    
    /**
	 * 获取当前系统时间戳
	 * 
	 * @return
	 */
	public static long getNowTimeStamp() {
		return System.currentTimeMillis();
	}
    
    /**
     * Return the current date string
     * 
     * @return － 产生的日期字符串<br>
     */
    public static String getCurrentDateStr() {
        Calendar cal = Calendar.getInstance();
        Date currDate = cal.getTime();

        return format(currDate);
    }

    /**
     * Return the current date in the specified format
     * 
     * @param strFormat
     * @return
     */
    public static String getCurrentDateStr(String strFormat) {
        Calendar cal = Calendar.getInstance();
        Date currDate = cal.getTime();

        return format(currDate, strFormat);
    }


    /**
     * 将Timestamp类型的日期转换为系统参数定义的格式的字符串。
     * 
     * @param aTs_Datetime 需要转换的日期。
     * @return 转换后符合给定格式的日期字符串
     */
    public static String format(Date aTs_Datetime) {
        return format(aTs_Datetime, DATE_PATTERN_DEFAULT);
    }

    /**
     * 将Timestamp类型的日期转换为系统参数定义的格式的字符串。
     * 
     * @param aTs_Datetime 需要转换的日期。
     * @return 转换后符合给定格式的日期字符串
     */
    public static String formatTime(Date aTs_Datetime) {
        return format(aTs_Datetime, TIME_PATTERN_DEFAULT);
    }

    /**
     * 将Date类型的日期转换为系统参数定义的格式的字符串。
     * 
     * @param aTs_Datetime
     * @param as_Pattern
     * @return
     */
    public static String format(Date aTs_Datetime, String as_Pattern) {
        if (aTs_Datetime == null || as_Pattern == null) {
            return null;
        }
        SimpleDateFormat dateFromat = new SimpleDateFormat();
        dateFromat.applyPattern(as_Pattern);

        return dateFromat.format(aTs_Datetime);
    }

    /**
     * @param aTs_Datetime
     * @param as_Format
     * @return
     */
    public static String formatTime(Date aTs_Datetime, String as_Format) {
        if (aTs_Datetime == null || as_Format == null) {
            return null;
        }
        SimpleDateFormat dateFromat = new SimpleDateFormat();
        dateFromat.applyPattern(as_Format);

        return dateFromat.format(aTs_Datetime);
    }

    public static String getFormatTime(Date dateTime) {
        return formatTime(dateTime, TIME_PATTERN_HHMMSS);
    }

    /**
     * @param aTs_Datetime
     * @param as_Pattern
     * @return
     */
    public static String format(Timestamp aTs_Datetime, String as_Pattern) {
        if (aTs_Datetime == null || as_Pattern == null) {
            return null;
        }
        SimpleDateFormat dateFromat = new SimpleDateFormat();
        dateFromat.applyPattern(as_Pattern);

        return dateFromat.format(aTs_Datetime);
    }

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd）.
     * 
     * @return date String
     */
    public static String getDate() {
        return getDate("yyyy-MM-dd");
    }

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E".
     * 
     * @param pattern String
     * @return date String
     */
    public static String getDate(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    /**
     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E".
     * 
     * @param pattern String
     * @param date Date
     * @return date String
     */
    public static String formatDate(Date date, Object... pattern) {
        String formatDate = null;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }

    /**
     * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）.
     * 
     * @param date Date
     * @return date String
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 转换为时间（天,时:分:秒.毫秒）.
     * 
     * @param timeMillis Long
     * @return datetime String
     */
    public static String formatDateTime(long timeMillis) {
        long day = timeMillis / (24 * 60 * 60 * 1000);
        long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
        long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000
                - s * 1000);
        return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
    }

    /**
     * 得到当前时间字符串 格式（HH:mm:ss）.
     * 
     * @return time String
     */
    public static String getTime() {
        return formatDate(new Date(), "HH:mm:ss");
    }

    /**
     * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）.
     * 
     * @return datetime String
     */
    public static String getDateTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到当前年份字符串 格式（yyyy）.
     * 
     * @return year String
     */
    public static String getYear() {
        return formatDate(new Date(), "yyyy");
    }

    /**
     * 得到当前月份字符串 格式（MM）.
     * 
     * @return month String
     */
    public static String getMonth() {
        return formatDate(new Date(), "MM");
    }

    /**
     * 得到当天字符串 格式（dd）.
     * 
     * @return day String
     */
    public static String getDay() {
        return formatDate(new Date(), "dd");
    }

    /**
     * 得到当前星期字符串 格式（E）星期几.
     * 
     * @return week String
     */
    public static String getWeek() {
        return formatDate(new Date(), "E");
    }

    /**
     * 日期型字符串转化为日期 格式 { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy/MM/dd",
     * "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd
     * HH:mm" }.
     * 
     * @param str Object
     * @return date Date
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取过去的天数.
     * 
     * @param date Date
     * @return time long
     */
    public static long pastDays(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / (24 * 60 * 60 * 1000);
    }

    /**
     * 获取过去的小时.
     * 
     * @param date Date
     * @return hour Long
     */
    public static long pastHour(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / (60 * 60 * 1000);
    }

    /**
     * 获取过去的分钟.
     * 
     * @param date Date
     * @return minutes Long
     */
    public static long pastMinutes(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / (60 * 1000);
    }

    /**
     * 获取两个日期之间的天数.
     * 
     * @param before Date
     * @param after Date
     * @return distance of two date Double
     */
    public static double getDistanceOfTwoDate(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
    }

    /**
     * 计算年龄
     * 
     * @return
     */
    public static int getAge(Date birthDay, Date cureDate) {
        if (birthDay == null) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        if (cureDate != null) {
            cal.setTime(cureDate);
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                age--;
            }
        }
        return age;
    }

   public static Date parseDayDateByAuto(String dataValue) throws ParseException {
        if ((dataValue != null) && (dataValue.length() > 0)) {
            if (dataValue.indexOf("/") > 0) {
                return parseDate("yyyy/MM/dd", dataValue);
            }
            if (dataValue.indexOf("-") > 0) {
                return parseDate("yyyy-MM-dd", dataValue);
            }
        }
        return null;
    }
   
   // 获得某天最大时间 2017-10-15 23:59:59  
   public static Date getEndOfDay(Date date) {  
       LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());;  
       LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);  
       return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());  
   } 
   
 
   	/**
   	 *  获得当天24点时间戳
   	 * @return
   	 */
	public static long getTimesMorningTwelve() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.MILLISECOND, 0);
		return  (cal.getTimeInMillis());
	}
   
   /**
    * 获取距离今日凌晨多少秒
    * @param date
    * @return
    */
   public static long getEndOfDaySecond() {  
       return (getTimesMorningTwelve() - getNowTimeStamp())/1000;
   } 
     
   // 获得某天最小时间 2017-10-15 00:00:00  
   public static Date getStartOfDay(Date date) {  
       LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());  
       LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);  
       return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());  
   } 
   
   
   /**
    * 判断当前日期是否在某段日期时间内
    * @param start   开始日期
    * @param end     结束日期
    * @param date    校验日期,为空时取当前时间
    * @return
    */
	public static boolean isNotDate(String start, String end ,String date){
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		boolean flag = false;
		try {
			
			Date startDate = sdf.parse(start);
			Date endDate = sdf.parse(end);
			Date now = null;
			if (date == null) {
				now = new Date();
			}else {
				now = sdf.parse(date);
			}
			return isNotDate(startDate, endDate, now);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return flag;
	}
   
	 /**
	    * 判断当前日期是否在某段日期时间内
	    * @param start   开始日期
	    * @param end     结束日期
	    * @param date    校验日期,为空时取当前时间
	    * @return
	    */
		public static boolean isNotDate(Date startDate, Date endDate ,Date date){
			
			boolean flag = false;
			try {
				Date now = null;
				if (date == null) {
					now = new Date();
				}else {
					now = date;
				}
				long time = now.getTime()+10;
				if(time > startDate.getTime() && time < endDate.getTime()){
					flag = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return flag;
		}
	
	
    /**
     * Test purpose.
     */
    public static void main(String[] args) throws ParseException {
        // System.out.println(formatDate(parseDate("2010/3/6")));
        // System.out.println(getDate("yyyy年MM月dd日 E"));
        // long time = new Date().getTime()-parseDate("2012-11-19").getTime();
        // System.out.println(time/(24*60*60*1000));
        System.out.println(isNotDate("2012-11-19", "2018-08-14", null));
        Date date = new Date();  
        System.out.println("今天开始时间：" + getStartOfDay(date));  
        System.out.println("今天结束时间：" + getEndOfDay(date));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
        System.out.println(sdf.format(getStartOfDay(date)));  
        System.out.println(sdf.format(getEndOfDay(date)));  
    }
}
