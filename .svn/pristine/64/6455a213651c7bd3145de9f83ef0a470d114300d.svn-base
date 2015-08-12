package com.xweisoft.wx.family.util;

import java.security.MessageDigest;

import android.content.Context;
import android.util.DisplayMetrics;

public class ScreenUtils
{
    
    private static int width = 0;
    
    private static int height = 0;
    
    private static float density = 0;
    
    /**
     * @return 初始化
     */
    public static void reInit(Context context)
    {
        if (height == 0 || width == 0 || density == 0)
        {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            height = dm.heightPixels;
            width = dm.widthPixels;
            density = dm.density;
        }
    }
    
    /**
     * @return 屏幕宽度
     */
    public static int getWidth(Context context)
    {
        reInit(context);
        return width;
    }
    
    /**
     * @return 密度
     */
    public static float getDensity(Context context)
    {
        reInit(context);
        return density;
    }
    
    /**
     * @return 宽高比
     */
    public static float WHPercent(Context context)
    {
        reInit(context);
        try
        {
            return (float) width / height;
        }
        catch (Exception e)
        {
            return 1;
        }
    }
    
    /**
     * @return 高宽比
     */
    public static float HWPercent(Context context)
    {
        reInit(context);
        try
        {
            return ((float) height) / width;
        }
        catch (Exception e)
        {
            return 1;
        }
    }
    
    /**
     * @return 屏幕高度
     */
    public static int getHeight(Context context)
    {
        reInit(context);
        return height;
    }
    
    /**
     * @return dp转px
     */
    public static int dip2px(Context context, float dpValue)
    {
        reInit(context);
        return (int) (dpValue * density + 0.5f);
    }
    
    /**
     * @return px转dp
     */
    public static int px2dip(Context context, float pxValue)
    {
        reInit(context);
        return (int) (pxValue / density + 0.5f);
    }
    
    /**
     * @return MD5
     */
    public static String getMD5String(String s)
    {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F' };
        try
        {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++)
            {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
