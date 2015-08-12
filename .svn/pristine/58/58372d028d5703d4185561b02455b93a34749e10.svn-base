/**
 * 
 */
package com.xweisoft.wx.family.logic.download;

import java.text.DecimalFormat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.text.TextUtils;

import com.xweisoft.wx.family.util.LogX;

/**
 * 下载管理的工具类
 * <功能详细描述>
 * 
 * @author  administrator
 * @version  [版本号, 2013-11-13]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class DownloadUtil
{
    private static final String TAG = "===DownloadUtil===";
    
    /**
     * 大小单位为k时，除数
     */
    private static final float SIZE_F_K = 1024.0f;
    
    /**
     * 大小单位为m时，除数
     */
    private static final float SIZE_F_M = 1048576.0f;
    
    /**
     * 大小单位为g时，除数
     */
    private static final float SIZE_F_G = 1073741824.0f;
    
    /**
     * 大小单位为k时，区分界限
     */
    private static final long SIZE_L_K = 1024l;
    
    /**
     * 大小单位为m时，区分界限
     */
    private static final long SIZE_L_M = 1048576l;
    
    /**
     * 大小单位为g时，区分界限
     */
    private static final long SIZE_L_G = 1073741824l;
    
    /**
     * 打开apk
     * 
     * @param packageName 包名
     * @param context 上下文
     */
    public static void openApk(String packageName, Context context)
    {
        LogX.getInstance().d(TAG, "open Apk of packageName " + packageName);
        Intent intent = null;
        // 捕捉异常，防止有的apk本身的bug，导致我们打开时出错
        try
        {
            PackageManager packageManager = context.getPackageManager();
            intent = packageManager.getLaunchIntentForPackage(packageName);
            context.startActivity(intent);
        }
        catch (Exception e)
        {
            LogX.getInstance().e(TAG, "open apk exception");
        }
    }
    
    /**
     * 震动
     * @param context 上下文
     */
    public static void startShock(Context context)
    {
        Vibrator vibrator = null;
        if (null == vibrator)
        {
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }
        
        // 停止 开启 停止 开启
        long[] pattern = { 0, 1000 };
        
        // 重复两次上面的pattern 如果只想震动一次，index设为-1
        vibrator.vibrate(pattern, -1);
    }
    
    /**
     * 获取AppItem业务包大小
     * 
     * @param appSize appSize
     * @return 业务包大小
     */
    public static String getAppSize(long appSize)
    {
        String appSizeStr = "";
        DecimalFormat df = null;
        if (df == null)
        {
            df = new DecimalFormat("#0.0");
        }
        if (appSize <= 0)
        {
            appSizeStr = "0 KB";
        }
        else if (appSize < SIZE_L_K)
        {
            appSizeStr = df.format(appSize / SIZE_F_K) + " " + "KB";
        }
        else if (appSize < SIZE_L_M)
        {
            appSizeStr = df.format(appSize / SIZE_F_K) + " " + "KB";
        }
        else if (appSize < SIZE_L_G)
        {
            appSizeStr = df.format(appSize / SIZE_F_M) + " " + "MB";
        }
        else
        {
            appSizeStr = df.format(appSize / SIZE_F_G) + " " + "GB";
        }
        return appSizeStr;
    }
    
    /**
     * 获取AppItem业务包大小
     * 
     * @param appSize appSize
     * @return 业务包大小
     */
    public static String getDownloadSpeed(long appSize)
    {
        String appSizeStr = "";
        DecimalFormat df = null;
        if (df == null)
        {
            df = new DecimalFormat("#0.0");
        }
        if (appSize <= 0)
        {
            appSizeStr = "0 kb/s";
        }
        else if (appSize < SIZE_L_K)
        {
            appSizeStr = df.format(appSize / SIZE_F_K) + " " + "kb/s";
        }
        else if (appSize < SIZE_L_M)
        {
            appSizeStr = df.format(appSize / SIZE_F_K) + " " + "kb/s";
        }
        else if (appSize < SIZE_L_G)
        {
            appSizeStr = df.format(appSize / SIZE_F_M) + " " + "mb/s";
        }
        else
        {
            appSizeStr = df.format(appSize / SIZE_F_G) + " " + "gb/s";
        }
        return appSizeStr;
    }
    
    /**
     * <一句话功能简述>
     * 将字符串转换为long型
     * @param size
     * @return [参数说明]
     * 
     * @return long [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static long parserAppSizeStr2Long(String size)
    {
        if (TextUtils.isEmpty(size))
        {
            return 0l;
        }
        try
        {
            return Long.parseLong(size);
        }
        catch (Exception e)
        {
            return 0l;
        }
    }
    
    /**
     * 获取下载百分比的字符数据
     * <功能详细描述>
     * @param percent 百分数
     * @return [参数说明] eg. 20% ---下载百分比
     * 
     * @return String [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static String getDownloadedPercent(int percent)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(percent);
        sb.append("%");
        return sb.toString();
    }
}
