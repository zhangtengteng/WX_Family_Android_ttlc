package com.xweisoft.wx.family.service.download;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;

import com.xweisoft.wx.family.service.threadpool.TaskQueue;
import com.xweisoft.wx.family.util.FileHelper;
import com.xweisoft.wx.family.util.LogX;

/**
 * 网络连接请求逻辑处理类
 * <功能详细描述>
 * 
 * @author  administrator
 * @version  [版本号, 2013-5-16]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public final class FileDownloadManager
{
    private static final String TAG = "===FileDownloadManager===";
    
    private final static int MAXCOUNT = 3;
    
    private static FileDownloadManager instance;
    
    private TaskQueue requestQueue;
    
    /**
     * 下载管理器中下载中任务的列表
     */
    private ArrayList<DownloadItem> lstDownloadTask;
    
    private FileDownloadManager()
    {
        requestQueue = new TaskQueue(MAXCOUNT);
        lstDownloadTask = new ArrayList<DownloadItem>();
    }
    
    public synchronized static FileDownloadManager getInstance()
    {
        if (instance == null)
        {
            instance = new FileDownloadManager();
        }
        return instance;
    }
    
    /**
     * 下载任务
     * <功能详细描述>
     * @param item [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void removeTast(DownloadItem item)
    {
        if (null != item && null != item.downloadTask)
        {
            requestQueue.removeTask(item.downloadTask);
        }
    }
    
    /**
     * 个人资料页面下载添加任务
     * <功能详细描述>
     * @param item [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void addDownloadTask(DownloadItem item)
    {
        lstDownloadTask.add(0, item);
        
        DownloadFileTask downloadTask = new DownloadFileTask(item,
                item.downloadServer, item.filePath);
        
        downloadTask.setContext(item.context);
        downloadTask.fileModeType = item.fileModeType;
        downloadTask.handler = item.handler;
        
        //下载任务
        downloadTask(item, downloadTask);
    }
    
    /**
     * 个人资料页面下载添加任务
     * <功能详细描述>
     * @param item [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void addDownloadTask(DownloadItem item, int type)
    {
        lstDownloadTask.add(0, item);
        
        DownloadFileTask downloadTask = new DownloadFileTask(item,
                item.downloadServer, item.filePath);
        
        downloadTask.setContext(item.context);
        downloadTask.handler = item.handler;
        downloadTask.requestType = type;
        //下载任务
        downloadTask(item, downloadTask);
    }
    
    /**
     * 执行下载操作
     * @param downloadTask 要下载的业务信息
     */
    public void downloadTask(DownloadItem item, DownloadFileTask downloadTask)
    {
        /**
         * 正在下载和暂停的都不用去管了
         */
        if (null != item && item.status == DownloadItem.STATUS_DOWNLOADING)
        {
            return;
        }
        final File file = new File(item.filePath);
        /**
         * 已经下载完成了的，也就不用管了
         */
        if (FileHelper.isFileExist(file) && file.length() == item.fileSize
                || item.status == DownloadItem.STATUS_FINISHED)
        {
            return;
        }
        
        //检查现在已经下载了多少
        if (FileHelper.isFileExist(file))
        {
            item.breakpoint = file.length();
        }
        
        if ((item.isPaused() || item.status == DownloadItem.STATUS_WAITING || item.status == DownloadItem.STATUS_FAILED)
                && item.breakpoint != 0 && item.breakpoint < item.fileSize)
        {
            downloadTask.setBreakPoint(item.breakpoint);
            downloadTask.setBreakPointHeader();
        }
        
        item.setStatus(DownloadItem.STATUS_WAITING);
        item.downloadTask = downloadTask;
        
        /**
         * 下载任务
         */
        addRequest(downloadTask);
    }
    
    /**
     * <一句话功能简述>
     * 由于消息的下载需要传请求体，且是post请求。
     * @param item 
     * @param reqBody [参数说明] 下载请求体
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void addDownloadMsgTask(DownloadItem item, String reqBody)
    {
        addDownloadMsgNoVideosTask(item, reqBody);
    }
    
    /**
     * <一句话功能简述>
     * 由于消息的下载需要传请求体，且是post请求。
     * @param item 
     * @param reqBody [参数说明] 下载请求体
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void addDownloadMsgNoVideosTask(DownloadItem item, String reqBody)
    {
        byte[] data = null;
        if (null != reqBody)
        {
            try
            {
                LogX.getInstance().i(TAG, "post Data: " + reqBody);
                data = reqBody.getBytes("utf-8");
            }
            catch (Exception e)
            {
                LogX.getInstance().e(TAG, e.toString());
            }
        }
        DownloadFileTask downloadTask = new DownloadFileTask(item,
                item.downloadServer, item.filePath);
        
        downloadTask.requestType = DownloadFileTask.POST;
        downloadTask.dataBuf = data;
        
        downloadTask.setContext(item.context);
        downloadTask.fileModeType = item.fileModeType;
        
        item.downloadTask = downloadTask;
        addRequest(downloadTask);
    }
    
    /**
     * 增加请求任务
     */
    private void addRequest(DownloadFileTask req)
    {
        if (requestQueue != null)
        {
            requestQueue.addTask(req);
        }
    }
    
    /**
     * 暂停任务
     * <功能详细描述>
     * @param item [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void pauseDownload(DownloadItem item)
    {
        if (null != item && item.isDownloading)
        {
            item.pausedTask();
        }
    }
    
    /**
     * 取消任务
     * <功能详细描述>
     * @param item [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void cancelDownload(DownloadItem item)
    {
        if (null != item && item.isDownloading)
        {
            item.cancelTask();
        }
    }
}