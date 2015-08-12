package com.xweisoft.wx.family.logic.request;

import java.io.File;

import android.content.Context;

import com.xweisoft.wx.family.logic.download.DownloadTaskManager;
import com.xweisoft.wx.family.logic.model.DownloadItem;
import com.xweisoft.wx.family.service.http.ConnectionTask;
import com.xweisoft.wx.family.service.http.DownloadTask;
import com.xweisoft.wx.family.service.http.IDownloadCallback;
import com.xweisoft.wx.family.util.FileHelper;
import com.xweisoft.wx.family.util.LogX;

/**
 * 下载
 * <功能详细描述>
 * 
 * @author  administrator
 * @version  [版本号, 2013-11-13]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class DownloadRequest extends Request implements IDownloadCallback
{
    private static final String TAG = "===DownloadRequest===";
    
    /**
     * 下载的任务对象
     */
    private DownloadItem item;
    
    /**
     * 构造函数
     * @param context 上下文
     * @param item 下载对象
     */
    public DownloadRequest(final Context context, DownloadItem item)
    {
        super(context, null, item.downloadUrl);
        //防止快速点击暂停和继续下载导致的下载线程无法终止从而无法控制下载任务暂停的问
        if (item.connectionTask != null)
        {
            item.connectionTask.pausedConnect();
        }
        this.item = item;
        this.context = context;
        
    }
    
    /**
     * 发送请求
     * @return 连接任务
     */
    protected ConnectionTask createConnectionTask()
    {
        //将downloaditem和下载请求关联起来
        ConnectionTask downloadTask = new DownloadTask(context, this, item);
        
        //根据下载类型设置不同的下载路径
        long breakpoint = FileHelper.getLocalFileSize(item.mp3SavePath);
        
        // 判断是否需要发送断点请求头
        //增加item.status == DownloadItem.STATUS_DOWNLOAD_FAILED，解决，当下载失败后，断点续下
        if ((item.isPaused()
                || item.status == DownloadItem.STATUS_DOWNLOAD_WAITING || item.status == DownloadItem.STATUS_DOWNLOAD_FAILED)
                && breakpoint != 0 && breakpoint < item.mp3Size)
        {
            downloadTask.setBreakPoint(breakpoint);
            downloadTask.setBreakPointHeader();
        }
        
        // 给任务初始化等待状态
        item.setStatus(DownloadItem.STATUS_DOWNLOAD_WAITING);
        item.connectionTask = downloadTask;
        return downloadTask;
    }
    
    /**
     * 下载失败回调方法
     * 
     * @param code
     *            错误码
     * @param message
     *            错误信息
     */
    public void onError(int code, String message)
    {
        if (item.status == DownloadItem.STATUS_DOWNLOAD_FINISH)
        {
            downloadSuccessed();
            return;
        }
        
        item.onError(context, code, message);
    }
    
    /**
     * 取消下载回调处理
     */
    public void canceledCallback()
    {
        item.canceledCallback(context);
    }
    
    /**
     * 下载进度信息回调方法
     * 
     * @param getLength
     *            已下载的大小
     * @param totalLength
     *            需要下载的总大小
     */
    public void onProgressChanged(long getLength, long totalLength)
    {
        item.onProgressChanged(context, getLength, totalLength);
    }
    
    /**
     * 下载暂停回调处理
     */
    public void pausedCallback()
    {
        item.pausedCallback(context);
    }
    
    /**
     * 开始下载状态回调
     */
    public void startDownloadCallback()
    {
        item.startDownloadCallback();
    }
    
    /**
     * 成功下载的回调方法
     * @param context
     */
    public void successDownloadCallback(Context context)
    {
        item.successDownloadCallback(context);
        
        //start 解决当断网后，下载进入该方法后，判断，是不是真正的下载完成，否则返回。
        if (item.status != DownloadItem.STATUS_DOWNLOAD_FINISH)
        {
            return;
        }
        final File file = new File(item.mp3SavePath);
        // 通知刷新UI 下载成功，且临时文件没有删除
        if (FileHelper.isFileExist(file) && file.length() < item.mp3Size)
        {
            return;
        }
        //end
        
        downloadSuccessed();
    }
    
    /**
     * 下载成功
     * [一句话功能简述]<BR>
     * [功能详细描述]
     */
    private void downloadSuccessed()
    {
        LogX.getInstance().i(TAG,
                "download success mp3  name is " + item.mp3Name);
        
        DownloadTaskManager manager = DownloadTaskManager.getInstance();
        
        //从downloading列表删除该记录
        manager.downloadSuccessRemoveTaskFromList(context, item);
        
        //        if (null != GlobalVariable.installReceiverHandler)
        //        {
        //            GlobalVariable.installReceiverHandler.sendEmptyMessage(GlobalConstant.DOWNLOADSUCESSPUSHLIST);
        //        }
        
        //下载成功后，增加震动提示用户下载完成
        //DownloadUtil.startShock(context);
        manager.showDownNotifycation(item, true);
        //下载完成后，安装
        //        DownloadUtil.installAPK(item);
    }
    
    /**
     * 进入等待状态回调处理
     */
    public void waitingCallback()
    {
        item.waitingCallback(context);
    }
}
