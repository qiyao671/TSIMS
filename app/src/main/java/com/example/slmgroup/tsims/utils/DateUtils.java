package com.example.slmgroup.tsims.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 施文正 on 2016/7/26.
 */
public class DateUtils {
    public static final String FORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss";
    /**
     * 日期型数据的格式转化
     *
     *
     * @param str 原有时间值
     * @param oldFormat 老格式 yyyy-MM-dd HH:mm:ss
     * @param newFormat 新格式 yyyy-MM-dd
     * @return yyyy-MM-dd
     */
    public static String formatDate(String str, String oldFormat, String newFormat) {
        //aaa
        if(str.isEmpty()){
            return "";
        }
        SimpleDateFormat oldSDF = new SimpleDateFormat(oldFormat);
        SimpleDateFormat newSDF = new SimpleDateFormat(newFormat.isEmpty() ? "yyyy-MM-dd" : newFormat);
        String sfstr = "";
        try {
            sfstr = newSDF.format(oldSDF.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sfstr;
    }

    /**
     * 获取当前时间
     * @param format (yyyy-MM-dd HH:mm:ss)
     * @return
     */
    public static String getCurrentTime(String format) {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);// 24小时制
        String date = sdf.format(now);
        return date;
    }

    /**
     * 把字段转化为时间
     *
     * @param dateStr
     * @param format
     * @return
     */
    public static Date getDateByStr(String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date d1 = sdf.parse(dateStr);
            return d1;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 得到两个日期间间隔的天数
     *
     * @param sj1
     * @param sj2
     * @return
     */
    public static String getIntervalDay(String sj1, String sj2) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        long day = 0;
        try {
            Date date = myFormatter.parse(sj1);
            Date mydate = myFormatter.parse(sj2);
            day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return "";
        }
        return day + "";
    }

    /**
     * 是否开始日期大于结束日期
     * @param start
     * @param end
     * @return
     */
    public static boolean isABeforeB(String a, String b) {
        // 设定时间的模板
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 得到指定模范的时间
        try {
            Date d1 = sdf.parse(a);
            Date d2 = sdf.parse(b);
            long value = ((d1.getTime() - d2.getTime()) / (24 * 3600 * 1000));
            if (value < 0) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            return true;
        }
    }

    /**
     * 时间是否在区间内
     * @param time
     * @param start
     * @param end
     * @return
     */
    public static boolean isExistDateSection(String time, int start, int end) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = sdf.parse(time);
            // Calendar cal = Calendar.getInstance();// 当前日期
            @SuppressWarnings("deprecation")
            int hour = d1.getHours();// 获取小时
            // int minute = d1.getMinutes();// 获取分钟
            // int minuteOfDay = hour * 60 + minute;// 从0:00分开是到目前为止的分钟数
            if (hour >= start && hour < end) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            return false;
        }
    }
    /**
     * 计算时间差
     * @param start
     * @param end
     * @return
     */
    public static long differDateTime(String start, String end) {
        // 设定时间的模板
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 得到指定模范的时间
        try {
            Date d1 = sdf.parse(start);
            Date d2 = sdf.parse(end);
            long value = ((d1.getTime() - d2.getTime()) / 1000);
            return value;
        } catch (ParseException e) {
            return 0;
        }
    }

}
