package com.xweisoft.wx.family.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

/**
 * <一句话功能简述>
 * 日期格式处理类
 * 
 * @author  administrator
 * @version  [版本号, 2013-10-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class DateTools
{
    /**
     * 日期格式yyyy-MM-dd HH:mm:ss
     */
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * 日期格式yyyy-MM-dd HH:mm
     */
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    
    /**
     * 日期格式MM-dd HH:mm
     */
    public static final String MM_DD_HH_MM = "MM-dd HH:mm";
    
    /**
     * 日期格式HH:mm
     */
    public static final String HH_MM = "HH:mm";
    
    /**
     * 日期格式yyyy-MM-dd HH:mm
     */
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    
    /**
     * 日期格式MMMM d yyyy
     */
    public static final String MMMM_D_YYYY = "MMMM d yyyy";
    
    /**
     * 日期格式MMMM d yyyy
     */
    public static final String MM_DD_YYYY = "MM/dd/yyyy";
    
    /**
     * 日期格式yyyy_MM
     */
    public static final String YYYY_MM = "yyyy-MM";
    
    /**
     * 日期格式MM月dd日
     */
    public static final String MMdd = "MM月dd日";
    
    /**
     * 将字符串日期转换为Date类型
     * @param tempDate 字符串日期
     * @param format 需要转换的格式
     * @return 转换后的日期
     */
    public static Date parseStr2Date(String tempDate, String format)
    {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date date = null;
        try
        {
            date = df.parse(tempDate);
            return date;
        }
        catch (ParseException e)
        {
            Log.e("===DateTools===",
                    "parseStr2Date(tempDate): " + e.getMessage());
        }
        return date;
    }
    
    /**
     * 将Date日期转换为字符串日期
     * @param date 转换日期
     * @param format 转换格式
     * @return 字符串日期
     */
    public static String parseDate2Str(Date date, String format)
    {
        if (null == date)
        {
            return "";
        }
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }
    
    /**
     * 将Date日期转换为字符串日期
     * @param date 转换日期
     * @param format 转换格式
     * @return 字符串日期
     */
    public static String parseTimeMillis2Str(String timeMillis, String format)
    {
        String time = "";
        try
        {
            if (!TextUtils.isEmpty(timeMillis))
            {
                Date date = new Date(Long.parseLong(timeMillis));
                SimpleDateFormat df = new SimpleDateFormat(format);
                time = df.format(date);
            }
        }
        catch (Exception e)
        {
        }
        return time;
    }
    
    /**
     * 将Date日期转换为字符串日期
     * @param date 转换日期
     * @param format 转换格式
     * @return 字符串日期
     */
    public static Calendar parseTimeMillis2Calendar(String timeMillis)
    {
        Calendar calendar = Calendar.getInstance();
        try
        {
            if (!TextUtils.isEmpty(timeMillis))
            {
                Date date = new Date(Long.parseLong(timeMillis));
                calendar.setTime(date);
            }
        }
        catch (Exception e)
        {
        }
        return calendar;
    }
    
    /**
     * 将Date日期转换为字符串日期
     * @param date 转换日期
     * @param format 转换格式
     * @return 字符串日期
     */
    public static String[] parseTimeMillis2StrArray(String timeMillis)
    {
        String time = "";
        try
        {
            if (!TextUtils.isEmpty(timeMillis))
            {
                Date date = new Date(Long.parseLong(timeMillis));
                SimpleDateFormat df = new SimpleDateFormat(MM_DD_YYYY);
                time = df.format(date);
            }
        }
        catch (Exception e)
        {
        }
        if (time.split("[/]").length != 3)
        {
            return new String[3];
        }
        else
        {
            return time.split("[/]");
        }
    }
    
    /**
     * 将string日期转换为指定格式的字符串日期
     * @param date 转换日期
     * @param format 转换格式
     * @return 字符串日期
     */
    public static String parseStr2Str(String date, String format)
    {
        if (null == date || "".equals(date))
        {
            return "";
        }
        return parseDate2Str(parseStr2Date(date, format), format);
    }
    
    /**
     * 将string日期转换为指定格式的字符串日期 
     * @param date
     * @return
     */
    public static String parseStr2Str(String date, Context context)
    {
        return parseStr2Str_MM_dd_yyyy(date);
    }
    
    /**
     * 将string日期转换为指定格式的字符串日期  : dd/MM/yyyy
     * @param date
     * @return
     */
    public static String parseStr2Str_dd_MM_yyyy(String date)
    {
        if (TextUtils.isEmpty(date))
        {
            return "";
        }
        
        String date2 = "";
        
        try
        {
            date2 = date.substring(6, 8) + "/" + date.substring(4, 6) + "/"
                    + date.substring(0, 4);
        }
        catch (Exception e)
        {
            
        }
        
        return date2;
    }
    
    /**
     * 将string日期转换为指定格式的字符串日期  : MM/dd/yyyy
     * @param date
     * @return
     */
    public static String parseStr2Str_MM_dd_yyyy(String date)
    {
        if (TextUtils.isEmpty(date))
        {
            return "";
        }
        
        String date2 = "";
        
        try
        {
            date2 = date.substring(4, 6) + "/" + date.substring(6, 8) + "/"
                    + date.substring(0, 4);
        }
        catch (Exception e)
        {
            
        }
        
        return date2;
    }
    
    /**
     * 将string日期转换为指定格式的字符串日期 
     * @param date
     * @return
     */
    public static String parseUTC2Str(String date, Context context)
    {
        return parseUTC2Str_MM_dd_yyyy(date);
    }
    
    /**
     * 将string日期转换为指定格式的字符串日期  : dd/MM/yyyy
     * @param date
     * @return
     */
    public static String parseUTC2Str_dd_MM_yyyy(String date)
    {
        if (TextUtils.isEmpty(date))
        {
            return "";
        }
        
        String[] s = date.split("-");
        
        String date2 = "";
        
        if (s.length == 3)
        {
            
            try
            {
                date2 = s[2].substring(0, 2) + "/" + s[1] + "/" + s[0];
            }
            catch (Exception e)
            {
                
            }
        }
        
        return date2;
    }
    
    /**
     * 将string日期转换为指定格式的字符串日期  : MM/dd/yyyy
     * @param date
     * @return
     */
    public static String parseUTC2Str_MM_dd_yyyy(String date)
    {
        if (TextUtils.isEmpty(date))
        {
            return "";
        }
        
        String[] s = date.split("-");
        
        String date2 = "";
        
        if (s.length == 3)
        {
            
            try
            {
                date2 = s[1] + "/" + s[2].substring(0, 2) + "/" + s[0];
            }
            catch (Exception e)
            {
                
            }
        }
        
        return date2;
    }
    
    /**
     * 计算两个日期型的时间相差多少时间
     * @param startDate  开始日期
     * @param endDate    结束日期
     * @return
     */
    public static String twoDateDistance(long startDate, long endDate)
    {
        
        if (startDate == 0 || endDate == 0)
        {
            return "刚刚";
        }
        long timeLong = endDate - startDate;
        if (timeLong < 2 * 1000)
        {
            return "刚刚";
        }
        else if (timeLong < 60 * 1000)
        {
            return timeLong / 1000 + "秒前";
        }
        else if (timeLong < 60 * 60 * 1000)
        {
            timeLong = timeLong / 1000 / 60;
            return timeLong + "分钟前";
        }
        else if (timeLong < 60 * 60 * 24 * 1000)
        {
            timeLong = timeLong / 60 / 60 / 1000;
            return timeLong + "小时前";
        }
        else if (timeLong < 60 * 60 * 24 * 1000 * 7)
        {
            timeLong = timeLong / 1000 / 60 / 60 / 24;
            return timeLong + "天前";
        }
        else if (timeLong < 60 * 60 * 24 * 1000 * 7 * 4)
        {
            timeLong = timeLong / 1000 / 60 / 60 / 24 / 7;
            return timeLong + "周前";
        }
        else
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
            return sdf.format(startDate);
        }
    }
    
    /**
     * 计算两个日期型的时间相差多少时间
     * @param startDate  开始日期
     * @param endDate    结束日期
     * @return
     */
    public static String getChatTime(String longTime)
    {
        long time = 0;
        String timeStr = null;
        Calendar calendar = Calendar.getInstance();
        try
        {
            if (!TextUtils.isEmpty(longTime))
            {
                time = Long.parseLong(longTime);
                Date date = new Date(time);
                calendar.setTime(date);
                int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                int dayServer = calendar.get(Calendar.DAY_OF_MONTH);
                
                int year = Calendar.getInstance().get(Calendar.YEAR);
                int yearServer = calendar.get(Calendar.YEAR);
                
                if (Math.abs(dayServer - day) == 0)
                {
                    SimpleDateFormat df = new SimpleDateFormat(HH_MM);
                    timeStr = "今天 " + df.format(date);
                }
                else if (Math.abs(dayServer - day) == 1)
                {
                    SimpleDateFormat df = new SimpleDateFormat(HH_MM);
                    timeStr = "昨天 " + df.format(date);
                }
                else if (Math.abs(dayServer - day) > 1
                        && Math.abs(dayServer - day) < 7)
                {
                    SimpleDateFormat df = new SimpleDateFormat(HH_MM);
                    int i = calendar.get(Calendar.DAY_OF_WEEK);
                    String s = "星期";
                    switch (i)
                    {
                        case 1:
                            s = s + "日";
                            break;
                        case 2:
                            s = s + "一";
                            break;
                        case 3:
                            s = s + "二";
                            break;
                        case 4:
                            s = s + "三";
                            break;
                        case 5:
                            s = s + "四";
                            break;
                        case 6:
                            s = s + "五";
                            break;
                        case 7:
                            s = s + "六";
                            break;
                        default:
                            break;
                    }
                    timeStr = s + " " + df.format(date);
                }
                else
                {
                    if (Math.abs(yearServer - year) != 0)
                    {
                        SimpleDateFormat df = new SimpleDateFormat(YYYY_MM_DD);
                        timeStr = df.format(date);
                    }
                    else
                    {
                        SimpleDateFormat df = new SimpleDateFormat(MM_DD_HH_MM);
                        timeStr = df.format(date);
                    }
                }
            }
        }
        catch (Exception e)
        {
        }
        return timeStr;
    }
    
    public static long timeDifference(String preTime, String time)
    {
        long t = 0;
        long pt = 0;
        try
        {
            if (!TextUtils.isEmpty(preTime))
            {
                pt = Long.parseLong(preTime);
            }
            if (TextUtils.isEmpty(time))
            {
                t = Long.parseLong(time);
            }
        }
        catch (Exception e)
        {
        }
        return t - pt;
    }
}
