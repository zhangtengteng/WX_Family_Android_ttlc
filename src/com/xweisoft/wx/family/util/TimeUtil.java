/*
 * 文 件 名:  TimeUtil.java
 * 描    述:   时间工具类
 * 创 建 人:  李晨光
 * 创建时间:  2014年7月9日
 */
package com.xweisoft.wx.family.util;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 * 
 * @author  李晨光
 * @version  [版本号, 2014年7月9日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class TimeUtil
{
    /**
     * 格式化PHP时间
     * @param time
     * @return
     * @see [类、类#方法、类#成员]
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatPHPTime(String time)
    {
        try
        {
            StringBuffer buffer = new StringBuffer();
            buffer.append(time);
            buffer.append("000");
            long timeLong = Long.valueOf(buffer.toString());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return sdf.format(new Date(timeLong));
        }
        catch (Exception e)
        {
            return time;
        }
    }
    
    /**
     * 格式化PHP时间 去掉分钟毫秒
     * @param time
     * @return
     * @see [类、类#方法、类#成员]
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatTime(String time)
    {
        try
        {
            StringBuffer buffer = new StringBuffer();
            buffer.append(time);
            buffer.append("000");
            long timeLong = Long.valueOf(buffer.toString());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(new Date(timeLong));
        }
        catch (Exception e)
        {
            return time;
        }
    }
    
    /**
     * 格式化PHP时间 具体到分钟毫秒
     * @param time
     * @return
     * @see [类、类#方法、类#成员]
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatTimeMMSS(String time)
    {
        try
        {
            // 设置日期输出的格式
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            // 格式化输出
            return df.format(time);
        }
        catch (Exception e)
        {
        }
        return "";
        
    }
}
