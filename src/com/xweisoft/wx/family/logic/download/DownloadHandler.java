package com.xweisoft.wx.family.logic.download;

import java.io.File;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.logic.global.GlobalVariable;
import com.xweisoft.wx.family.logic.model.DownloadItem;
import com.xweisoft.wx.family.util.FileHelper;
import com.xweisoft.wx.family.util.LogX;
import com.xweisoft.wx.family.widget.MarketToast;


/**
 * 下载返回消息的详细处理
 * 
 * @author  administrator
 * @version  [版本号, 2014年1月16日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public final class DownloadHandler extends Handler
{
    /**
     * 日志标示
     */
    private static final String TAG = "===DownloadHandler===";
    
    /**
     * 下载失败提示标志
     */
    public static boolean isError = true;

    /**
     * 声明私有变量instance
     */
    private static DownloadHandler instance;
    
    /**
     * 下载handler
     * @param looper 循环
     */
    private DownloadHandler(Looper looper)
    {
        super(looper);
    }

    /**
     * 单例模式获取下载对象<BR>
     * @param looper 循环
     * @return 实例
     */
    public static DownloadHandler getInstance(Looper looper)
    {
        if (instance == null)
        {
            instance = new DownloadHandler(looper);
        }
        return instance;
    }

    /**
     * 处理下载返回的消息队列
     * 
     * @param msg
     *            消息对象
     */
    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what)
        {
        // 删除下载任务
            case DownloadItem.STATUS_DOWNLOAD_CANCEL:
                DownloadItem deleteItem = (DownloadItem) msg.obj;
                // 显示删除成功信息
                showToast(GlobalVariable.currentActivity.getResources()
                        .getString(R.string.l00_downloading_delete_success), deleteItem.mp3Name);
                break;
            // 下载失败
            case DownloadItem.STATUS_DOWNLOAD_FAILED:
                DownloadItem failedItem = (DownloadItem) msg.obj;
                final String errMsg = failedItem.errMsg;
                errToast(errMsg, failedItem.mp3Name, failedItem);
                break;
            // 下载成功
            case DownloadItem.STATUS_DOWNLOAD_FINISH:
                // Toast下载完成 且临时文件没有删除
                DownloadItem saveItem = (DownloadItem) msg.obj;
                downloadSucessed(msg, saveItem);
                break;
            default:

                break;
        }
        super.handleMessage(msg);
    }

    /**
     * 
     * 显示错误提示框Toast<BR>
     * 网络超时等错误提示给用户
     * @param errMsg 错误信息
     * @param appName 应用名称
     * @param item 下载对象
     */
    private void errToast(String errMsg, String appName, DownloadItem item)
    {
        if (errMsg.indexOf("SDUnavailableException") >= 0 || errMsg
                .indexOf("SDNotEnouchSpaceException") >= 0)
        {
            showToast(
                    GlobalVariable.currentActivity.getResources().getString(
                            R.string.sdcard_not_enough_space), "");
        }
        else if (errMsg.indexOf("SC_NOT_FOUND") >= 0)
        {
            showToast(
                    GlobalVariable.currentActivity.getResources().getString(
                            R.string.l00_app_download_sc_not_found), appName);
        }
        else if (errMsg.indexOf("DOWNLOAD_302_ERROR") >= 0)
        {
            showToast(item.errDownloadMsg, item.mp3Name);
        }
        else if (errMsg.indexOf("URLNotValidException") >= 0)
        {

            showToast(
                    GlobalVariable.currentActivity.getResources().getString(
                            R.string.l00_app_download_url_not_valid), appName);
        }
        else if (errMsg.indexOf("IOException") >= 0)
        {
            showToast(GlobalVariable.currentActivity.getResources().getString(R.string.l00_app_download_failed), appName);
        }
        else if (errMsg.indexOf("!") >= 0)
        {

            showToast(errMsg.substring(0, errMsg.length() - 1), appName);
        }
        else if (errMsg.indexOf("TIMEOUT") >= 0 || errMsg
                .indexOf("SocketTimeoutException") >= 0)
        {

            showToast(
                    GlobalVariable.currentActivity.getResources().getString(
                            R.string.network_timeout), appName);
        }
        else
        {
            showToast(GlobalVariable.currentActivity.getResources()
                .getString(R.string.l00_app_download_failed), appName);
        }
        
        //notification下载失败提示
        if (null != item && item.mp3Id != null)
        {
            DownloadTaskManager.getInstance().cancelNotification();
            DownloadTaskManager.getInstance().downloadFailNotification(item);
        }
    }

    /**
     * 显示提示框
     * @param appName 应用名称
     * @param res 资源值
     */
    public void showToast(String res, String appName)
    {
        MarketToast.showToast(GlobalVariable.currentActivity, appName + " " + res);
    }
    
    /**
     * 
     * Handler发送发送过来下载成功的message，这边进行判断
     * 
     * @param msg
     *            提示Message
     * @param saveItem
     *            下载项
     */
    protected void downloadSucessed(final Message msg,
            final DownloadItem saveItem)
    {
        /**
         * 下载的文件会放到savePath里面，建立一个file
         */
        final File file = new File(saveItem.mp3SavePath);
        /**
         * 要是有这个文件存在，并且这个文件的大小和下载文件的大小一样
         * 就说明下载成功了
         */
        if (FileHelper.isFileExist(file) && file.length() >= saveItem.mp3Size)
        {
            //下载完成了，会跳转到安装页面，所以，可以不用调用
            LogX.getInstance().d(TAG,"[ manager ] [ App download SUCCESS ]");
            showToast(
                    GlobalVariable.currentActivity.getResources().getString(
                            R.string.l00_status_download_finish),
                    saveItem.mp3Name);      
        } 
        else
        {
            DownloadTaskManager.getInstance().updateTaskToDB(GlobalVariable.currentActivity,
                    saveItem);
            errToast("IOException", saveItem.mp3Name, saveItem);
            LogX.getInstance().d(TAG,"[ manager ] [ App download FAILURE ]");
        }
    }
}
