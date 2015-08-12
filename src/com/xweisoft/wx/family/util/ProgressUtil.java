/*
 * 文 件 名:  ProgressUtil.java
 * 描    述:  进度条工具类
 * 创 建 人:  李晨光
 * 创建时间:  2013年7月31日
 */
package com.xweisoft.wx.family.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

/**
 * 进度条工具类
 * 
 * @author  李晨光
 * @version  [版本号, 2013年7月31日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ProgressUtil
{
    /**
     * 是否取消了上传图片，视频请求进度条
     */
    public static boolean isUploadCancel = false;
    
    public static ProgressDialog progressDialog = null;
    
    /**
     * 显示进度条
     * @param context
     * @param message
     * @see [类、类#方法、类#成员]
     */
    public static void showProgressDialog(Context context, String message)
    {
        if (null != progressDialog && progressDialog.isShowing())
        {
            return;
        }
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        if (context instanceof Activity && ((Activity) context).isFinishing())
        {
            return;
        }
        progressDialog.show();
    }
    
    /**
     * 显示进度条,增加是否点击back取消或者点击对话框之外取消对话框
     * @param context
     * @param message
     * @see [类、类#方法、类#成员]
     */
    public static void showProgressDialog(Context context, String message,
            boolean isDismiss)
    {
        if (null != progressDialog && progressDialog.isShowing())
        {
            return;
        }
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(isDismiss);
        if (context instanceof Activity && ((Activity) context).isFinishing())
        {
            return;
        }
        progressDialog.show();
    }
    
    /**
     * 上传图片或者视频时显示的进度条
     * @param context
     * @param message
     * @see [类、类#方法、类#成员]
     */
    public static void showUploadProgressDialog(Context context, String message)
    {
        if (null != progressDialog && progressDialog.isShowing())
        {
            return;
        }
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnCancelListener(new OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                isUploadCancel = true;
            }
        });
        if (context instanceof Activity && ((Activity) context).isFinishing())
        {
            return;
        }
        progressDialog.show();
    }
    
    /**
     * 隐藏进度条
     * @see [类、类#方法、类#成员]
     */
    public static void dismissProgressDialog()
    {
        if (null != progressDialog && progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
    }
}
