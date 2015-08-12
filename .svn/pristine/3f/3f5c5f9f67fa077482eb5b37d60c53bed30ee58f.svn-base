package com.xweisoft.wx.family.service.download;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.xweisoft.wx.family.service.upload.IHttpListener;
import com.xweisoft.wx.family.util.LogX;
import com.xweisoft.wx.family.util.Util;

/**
 * 下载文件实体类
 * <功能详细描述>
 * 
 * @author  administrator
 * @version  [版本号, 2013-5-16]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class DownloadItem implements IHttpListener
{
    private static final String TAG = Util.makeLogTag(DownloadItem.class);
    
    /**
     * 满百分比值
     */
    public static final int FULL_PERCENT_VALUE = 100;
    
    public static final int DOWNLOAD_UNKNOWN = -200;
    
    public static final int DOWNLOAD_START = 201;
    
    public static final int DOWNLOAD_ERROR = 202;
    
    public static final int DOWNLOAD_PROGRESS = 203;
    
    public static final int DOWNLOAD_CANCEL = 204;
    
    public static final int DOWNLOAD_COMPLETE = 205;
    
    /**
     * 下载暂停
     */
    public static final int DOWNLOAD_PAUSE = 206;
    
    public static final int SEND_DOWNLOAD_DONE_REQUEST = 203;
    
    public static final int FXZ_DOWNLOAD_SCUUCSS = 2008;
    
    public String downloadServer = null;
    
    /**
     * 标题
     */
    public String title;
    
    /**
     * 摘要
     * 视频的描述
     */
    public String summary;
    
    /**
     * 是否正在下载
     */
    public boolean isDownloading = false;
    
    /**
     * 下载地址
     * 文件存储路径
     */
    public String filePath;
    
    /**
     * 下载文件时，需要针对消息的文件下载进行特殊处理
     * 
     * 1. 下载消息文件， 要取数据流的前四个字节。作为错误码来进行判断
     * 2. 其他的直接下载。
     */
    public int fileModeType = 1;
    
    /**
     * 文件类型，主要针对下载时，消息中存在其他下载。
     * 断点下载只针对视频的下载。
     */
    public String fileType = "";
    
    /**
     * 文件总长度
     */
    public long fileSize;
    
    /**
     * 下载断点值
     */
    public long breakpoint = 0l;
    
    /**
     * 下载任务的当前进度
     */
    public int downloadPercent;
    
    public Handler handler;
    
    public DownloadFileTask downloadTask;
    
    /**
     * 增加一个标志来区分是否是重新下载此任务，默认为true
     */
    public boolean isRestart = true;
    
    ///////////////////////////////////////////
    
    /**
     * 下载等待
     */
    public static final int STATUS_WAITING = 1;
    
    /**
     * 下载暂停
     */
    public static final int STATUS_PAUSE = 2;
    
    /**
     * 下载中
     */
    public static final int STATUS_DOWNLOADING = 3;
    
    /**
     * 下载失败
     */
    public static final int STATUS_FAILED = 4;
    
    /**
     * 下载结束
     */
    public static final int STATUS_FINISHED = 5;
    
    /**
     * view对象
     */
    public View view;
    
    /**
     * 数据库中的记录的id
     */
    //    public long id;
    public String id;
    
    /**
     * 用户id
     */
    public String jid;
    
    /**
     * 视频参加的活动名称
     */
    public String eventName;
    
    /**
     * 父活动id
     */
    public String parentEventId;
    
    /**
     * 子活动id
     */
    public String childEventId;
    
    /**
     * 下载视频的状态 见 STATUS_WAITING .....等状态
     */
    public int status;
    
    /**
     * 已经下载的的大小
     */
    public long size;
    
    /**
     * 下载视频的日期 yyyy-MM-dd
     */
    public long uploadTime;
    
    /**
     * 下载成功，是否发布成功 0 成功， 其他不成功
     */
    public int pubStatus = 0;
    
    /**
     * 下载成功后，服务器返回的文件名，需要在发布时携带
     */
    public String fileName;
    
    /**
     * 下载成功后，服务器返回的url，需要在发布时携带
     */
    public String fileUrl;
    
    /////////////////////////////////////////////
    
    /**
     * 错误信息
     */
    public String errMsg;
    
    /**
     * 上一次遍历下载任务集合的索引位置
     */
    public int prePosition;
    
    /**
     * 上下文
     */
    public Context context;
    
    /**
     * 下载任务状态控制同步对象
     */
    public Object syncStateCtrl = new Object();
    
    public DownloadItem()
    {
    }
    
    public void onError(int code, String message)
    {
        // 保存错误信息
        if (code == 404)
        {
            errMsg = "SC_NOT_FOUND";
        }
        else
        {
            errMsg = message;
        }
        
        // 百分比 置为0
        downloadPercent = 0;
        
        setStatus(STATUS_FAILED);
        
        sendMsgToUI(DOWNLOAD_ERROR, 0);
    }
    
    public void onProgressChanged(long getLength, long totalLength)
    {
        
        // 计算下载进度
        int precent = 0;
        if (getLength >= totalLength)
        {
            precent = FULL_PERCENT_VALUE;
        }
        else
        {
            precent = (int) (FULL_PERCENT_VALUE * getLength / (totalLength + 1));
        }
        
        size = getLength;
        
        if (fileSize != totalLength)
        {
            // 获取文件总长度
            fileSize = totalLength;
        }
        
        if (downloadPercent < precent)
        {
            downloadPercent = precent;
            isDownloading = true;
            sendMsgToUI(DOWNLOAD_PROGRESS, downloadPercent);
            
        }
    }
    
    /**
     * 增量下载视频后的转码以及入库
     */
    private void sendDownLoadRequest()
    {
        Message msg = handler.obtainMessage();
        //modify by gac 2013-5-27 文件下载id
        msg.obj = this.id;
        msg.what = FXZ_DOWNLOAD_SCUUCSS;
        handler.sendMessage(msg);
        
    }
    
    public void sendMsgToUI(int type, int progress)
    {
        LogX.getInstance().i(TAG, "progress : " + progress);
        if (handler != null)
        {
            handler.sendMessage(handler.obtainMessage(type, this));
        }
    }
    
    @Override
    public void taskStartCallback()
    {
        if (status == STATUS_PAUSE)
        {
            return;
        }
        
        isDownloading = true;
        
        setStatus(STATUS_DOWNLOADING);
        
        sendMsgToUI(DOWNLOAD_START, 0);
    }
    
    @Override
    public void taskSuccessCallback(Object obj)
    {
        isDownloading = false;
        
        setStatus(STATUS_FINISHED);
        
        sendDownLoadRequest();
    }
    
    @Override
    public void canceledCallback()
    {
        isDownloading = false;
        
        sendMsgToUI(DOWNLOAD_CANCEL, 0);
        
        pauseConnection();
    }
    
    @Override
    public void pausedCallback()
    {
        isDownloading = false;
        
        setStatus(STATUS_PAUSE);
        
        sendMsgToUI(DOWNLOAD_PAUSE, 0);
        
    }
    
    /**
     * 取消正在运行的任务
     */
    public void cancelTask()
    {
        // 如果正在下载，取消网络连接
        if (downloadTask != null && isDownloading)
        {
            downloadTask.cancelConnect();
            downloadTask.cancelTask();
        }
        // 如果没有正在下载从数据库和本地文件中删除
        else
        {
            canceledCallback();
        }
    }
    
    /**
     * 暂停正在执行的下载任务
     */
    public void pausedTask()
    {
        // 获取是否正在下载标志
        final boolean isDownloading = isDownloading();
        
        if (downloadTask != null && isDownloading)
        {
            downloadTask.pausedConnect();
            if (status != STATUS_PAUSE && status != STATUS_FINISHED)
            {
                // 将状态修改为暂停中
                setStatus(STATUS_PAUSE);
            }
            
            // 如果暂停按钮按下时已经下载完成，则返回下载完成
            if (status == STATUS_FINISHED)
            {
                // 通知UI层更新界面
                sendDownLoadRequest();
            }
            else
            {
                // 通知UI层更新界面
                sendMsgToUI(STATUS_PAUSE, downloadPercent);
            }
            pauseConnection();
        }
    }
    
    /**
     * 暂停时将删除线程池中的线程
     */
    public void pauseConnection()
    {
        // 暂停时要删除TaskQueue队列，这样保存数据显示的一致性
        FileDownloadManager.getInstance().removeTast(this);
    }
    
    /**
     * 设置状态
     * 
     * @param status
     *            状态
     */
    public void setStatus(final int status)
    {
        synchronized (syncStateCtrl)
        {
            this.status = status;
        }
    }
    
    /**
     * 是否在下载中
     * <功能详细描述>
     * @return [参数说明]
     * 
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public boolean isDownloading()
    {
        return isDownloading;
    }
    
    /**
     * 是否暂停
     * <功能详细描述>
     * @return [参数说明]
     * 
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public boolean isPaused()
    {
        boolean isPauseflag = false;
        synchronized (syncStateCtrl)
        {
            // 判断状态是否为暂停
            isPauseflag = status == STATUS_PAUSE ? true : false;
        }
        
        return isPauseflag;
    }
}