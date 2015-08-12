package com.xweisoft.wx.family.service.http;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

import org.json.JSONException;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.logic.download.DownloadTaskManager;
import com.xweisoft.wx.family.logic.global.GlobalConstant;
import com.xweisoft.wx.family.logic.global.GlobalVariable;
import com.xweisoft.wx.family.logic.global.NetWorkCodes.CommontErrorCodes;
import com.xweisoft.wx.family.logic.model.DownloadItem;
import com.xweisoft.wx.family.service.exception.SDNotEnouchSpaceException;
import com.xweisoft.wx.family.service.exception.SDUnavailableException;
import com.xweisoft.wx.family.service.httpClient.NetworkUtils;
import com.xweisoft.wx.family.service.threadpool.TaskQueue;
import com.xweisoft.wx.family.util.FileHelper;
import com.xweisoft.wx.family.util.LogX;
import com.xweisoft.wx.family.util.Util;

/**
 * 下载任务类
 * <功能详细描述>
 * 
 * @author  administrator
 * @version  [版本号, 2013-11-15]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class DownloadTask extends ConnectionTask
{
    /**
     * 用来执行下载任务的线程池
     */
    protected static TaskQueue downloadQueue;
    
    /**
     * 下载任务的下载实例参数 下载资源包要初始化
     */
    public DownloadItem downloadItem = new DownloadItem();
    
    /**
     * 当为下载任务时，标志任务下载是否完成
     */
    protected boolean isDownloadSuccess = false;
    
    /**
     * 下载构造方法
     * @param context 上下文
     * @param callback 联网回调接口
     * @param httpUrl 下载地址
     */
    public DownloadTask(final Context context, IHttpCallback callback,
            String httpUrl)
    {
        super(context, httpUrl, callback);
        requestType = GET;
        if (downloadQueue == null)
        {
            // 初始化下载任务队列
            downloadQueue = new TaskQueue(GlobalVariable.maxDownloadCount);
        }
    }
    
    /**
     * 下载构造方法 
     * 
     * @param context 上下文
     * @param callback 联网回调接口
     * @param item 下载对象
     */
    public DownloadTask(final Context context, IHttpCallback callback,
            DownloadItem item)
    {
        super(context, item.downloadUrl, callback);
        this.downloadItem = item;
        this.context = context;
        requestType = GET;
        if (downloadQueue == null)
        {
            // 初始化下载任务队列
            downloadQueue = new TaskQueue(GlobalVariable.maxDownloadCount);
        }
    }
    
    /**
     * 处理下载任务<BR>
     * @throws Exception 上抛异常
     * @throws Error 上抛错误
     * @see ConnectionTask#doTask()
     */
    protected void doTask() throws Exception, Error, InterruptedException
    {
        // 通知任务开始下载
        isDownloadSuccess = false;
        
        startDownloadCallback();
        
        // 执行网络连接操作（发送HTTP请求，并处理网络返回数据）
        connetionProcess();
        
        // 如果下载成功，回调到上层处理
        if (null != downloadItem
                && downloadItem.status == DownloadItem.STATUS_DOWNLOADING)
        {
            successDownloadCallback();
        }
        //end
    }
    
    /**
     * 读网络数据
     * 
     * @throws Exception             异常类
     * @throws IOException             IO异常类
     * @throws InterruptedException    中断异常类
     * @throws UnsupportedEncodingException 编码的异常
     * @throws JSONException json的异常
     */
    protected void readData() throws Exception, IOException,
            InterruptedException, UnsupportedEncodingException, JSONException
    {
        super.readData();
        
        // 是否是OMA下载的标志，初始为否
        Boolean bOmaOrNot = false;
        
        long fileLenght = 0;
        if (isSDKAbove9())
        {
            fileLenght = (long) conn.getContentLength();
        }
        else
        {
            fileLenght = (long) entity.getContentLength();
        }
        // 将头域中的文件大小保存到downloadItem中 
        downloadItem.contentLength = fileLenght;
        
        // 获取服务器返回的content type
//        String contentType = conn.getHeaderField("Content-Type");
//        String contentType = entity.
//        if (contentType != null)
//        {
//            // 如果有Content-Type字段，从中获取OMA标志 // 获取是否是OMA下载方式
//            bOmaOrNot = contentType.contains("application/vnd.oma.dd+xml");
//        }
        
        String downloadURL = "";
        
        // 如果是OMA下载方式的话，先获取OMA描述符，如果不是直接走
        if (bOmaOrNot)
        {
            // 如果是OMA下载方式
           // AppItem appItem = new AppItem();
           // downloadURL = appItem.downloadUrl;
            // 2011-03-27增加文件的后缀名，由于文件名现在不是从URL中截取，而是从DD文件中读取
            //downloadItem.downloadNotifyURI = "";
            
            this.httpUrl = "";
            
            if (DownloadTaskManager.getInstance() != null)
            {
                DownloadTaskManager.getInstance()
                        .updateDownloadItemFromList(this.context,
                                this.downloadItem);
            }
            
            // 如果是oma下载，调用doTask会导致重复回调successDownloadCallback，所以这里只调用connetionProcess()
            connetionProcess();
        }
        else
        {
            // 网络连接被暂停或取消，需要抛出中断异常，并关闭写文件线程
            if (canceled || paused || isTimeOut || bWaiting)
            {
                throw new InterruptedException();
            }
            
            // Http下载时，如果存在Content-Disposition字段，文件名和扩展名以此为准
            StringBuffer sbFileName = new StringBuffer("");
            StringBuffer sbFileExt = new StringBuffer("");
            String strFileExt = "";
            // 获取服务器返回的Content-Disposition
            String contentDisposition = "";
            if (isSDKAbove9())
            {
                contentDisposition = conn.getHeaderField("Content-Disposition");
            }
            else
            {
                contentDisposition = NetworkUtils.getFileName(response, "Content-Disposition");
            }
            if (!TextUtils.isEmpty(contentDisposition))
            {
                int startPos = contentDisposition.indexOf("filename=");
                String fileServerName = contentDisposition.substring(startPos + 9);
                
                // 优先从Content-Disposition取文件名和扩展名
                getFileNameFromUrl(fileServerName, sbFileName, sbFileExt);
                strFileExt = sbFileExt.toString();
            }
            
            // 没有从Content-Disposition取到值时，才从Url中截取文件名和扩展名
            if (strFileExt.equals("") && null != httpUrl)
            {
                // 添加文件类型filePostfixName的获取
                getFileNameFromUrl(httpUrl, sbFileName, sbFileExt);
                strFileExt = sbFileExt.toString();
            }
            
            if ("".equals(sbFileExt.toString()))
            {
                setError(GlobalConstant.URL_ILLAGELE, "URLNotValidException");
                throw new InterruptedException("URLNotValidException");
            }
            
            // 网络连接被暂停或取消，需要抛出中断异常，并关闭写文件线程
            if (canceled || paused || isTimeOut || bWaiting)
            {
                throw new InterruptedException();
            }
            
            // 根据扩展名决定下载类型
            int appType = GlobalConstant.INSTALL_OTHER;
            
            if (strFileExt.equals(".apk"))
            {
                appType = GlobalConstant.INSTALL_APP;
            }
            
            // 只有当保存路径为空时，才更新下载保存路径。否则OMA下载每次都会修改文件名，导致保存的文件名不同，不能断点续传
            if ("".equals(downloadItem.mp3SavePath))
            {
                // 先决定下载类型
                //downloadItem.appKind = appType;
                
                // 通过appKind来设置保存路径
                String folderPath = FileHelper.selectFileSavaPath(context,
                        0,
                        FileHelper.DOWNLOADFILE,
                        downloadItem);
                
                // 只修改保存路径，不修改应用名称
                //因为是多线程请求，而服务器返回的名称是当前的时间YYYYMMDDHHMMSS所以就会产生同名的情况，
                //所以客户端是以应用的ContentCode作为文件名
                //文件后缀名以当前时间命名，防止服务器返回的url相同导致的重名现象  by  hff 2014.4.16
                //文件后缀名以时间戳命名，防止某些手机无法创建文件  by  gac 2014.4.25
                downloadItem.mp3SavePath = folderPath
                        + sbFileName.append(Util.getSystemTime()).toString()
                        + strFileExt;
                downloadItem.filePostfixName = strFileExt;
            }
            
            // 网络连接被暂停或取消，需要抛出中断异常，并关闭写文件线程
            if (canceled || paused || isTimeOut || bWaiting)
            {
                throw new InterruptedException();
            }
            
            if (DownloadTaskManager.getInstance() != null)
            {
                DownloadTaskManager.getInstance()
                        .updateDownloadItemFromList(this.context,
                                this.downloadItem);
            }
            
            readDownloadData(context, fileLenght);
        }
    }
    
    private boolean getFileNameFromUrl(final String strUrl,
            StringBuffer fileName, StringBuffer fileExt)
    {
        String strTmp = strUrl.substring(strUrl.lastIndexOf("/") + 1,
                strUrl.length());
        int endPos = strTmp.indexOf("?");
        if (endPos != -1)
        {
            strTmp = strTmp.substring(0, endPos);
        }
        
        int dotPos = strTmp.indexOf(".");
        if (dotPos == -1)
        {
            return false;
        }
        
        fileName.append(strTmp.substring(0, dotPos));
        fileExt.append(strTmp.substring(dotPos));
        
        return true;
    }
    
    /**
     * 读取下载业务的业务包数据
     * 
     * @param context 上下文
     * @param dataLen 数据长度
     * @throws IOException 抛出IO异常供调用者捕捉
     * @throws InterruptedException 抛出中断异常供调用者捕捉
     */
    protected void readDownloadData(final Context context, long dataLen)
            throws IOException, InterruptedException
    {
        
        // 超时时间未到，连接成功停止超时任务
        stopTimeoutTimer();
        
        long contentLen = 0L;
        
        if (isSDKAbove9())
        {
            // 从返回头中获取返回长度
            String contentLength = conn.getHeaderField("Content-Length");
            if (contentLength != null && contentLength.length() > 0)
            {
                try
                {
                    contentLen = Long.parseLong(contentLength.trim());
                }
                catch (Exception e)
                {
                    LogX.getInstance().i(TAG, e.toString());
                }
            }
            else
            {
                String contentRange = conn.getHeaderField("content-range");
                if (contentRange != null)
                {
                    contentLen = Long.parseLong(Util.split2(contentRange, "/")[1])
                            - breakpoint;
                }
            }
        }
        else
        {
            contentLen = (long) entity.getContentLength();
        }
        
        try
        {
            // 从流中获取返回长度
            long isLen = (long) (is.available());
            
            // 取3种返回长度的最大值作为真正的返回长度
            dataLen = dataLen > contentLen ? dataLen : contentLen;
            dataLen = dataLen > isLen ? dataLen : isLen;
            
            setDataLength(breakpoint, dataLen + breakpoint);
            
            // 存储已经下载的长度
            long getLen = 0;
            // 存储每次从网络层读取到的数据
            // 临时数据缓冲区
            int buffLen = 0;
            byte[] buff = new byte[DATA_BUFFER_LEN];
            byte[] tempBuff = null;
            
            // 创建文件
            if (!createFile(context, dataLen))
            {
                return;
            }
            
            // 下载一定字节开始时间  
            long startMillisecond = 0L;
            
            // 下载一定字节后时间 
            long endMillisecond = 0L;
            long fileLength = 0;
            while (buffLen != -1)
            {
                // 网络连接被暂停或取消，需要抛出中断异常，并关闭写文件线程
                if (canceled || paused || isTimeOut || bWaiting)
                {
                    throw new InterruptedException();
                }
                
                fileLength = FileHelper.getLocalFileSize(downloadItem.mp3SavePath);
                if (fileLength == 0 && getLen != 0)
                {
                    downloadItem.downloadPercent = 0;
                    downloadItem.isRestart = true;
                    throw new InterruptedException();
                }
                
                // 网络连接被暂停或取消，需要抛出中断异常，并关闭写文件线程
                if (canceled || paused || isTimeOut || bWaiting)
                {
                    throw new InterruptedException();
                }
                
                startMillisecond = System.currentTimeMillis();
                buffLen = is.read(buff);
                
                // 网络连接被暂停或取消，需要抛出中断异常，并关闭写文件线程
                if (canceled || paused || isTimeOut || bWaiting)
                {
                    throw new InterruptedException();
                }
                
                // 如果没有读到数据就不需要写文件了
                if (buffLen <= 0)
                {
                    continue;
                }
                
                // sd卡存在时将下载的数据写入文件
                if (GlobalVariable.sdCardIsExist)
                {
                    // 网络连接被暂停或取消，需要抛出中断异常，并关闭写文件线程
                    if (canceled || paused || isTimeOut || bWaiting)
                    {
                        throw new InterruptedException();
                    }
                    
                    // 将读到的数据写入文件，写文件成功后通知上层回调下载进度
                    tempBuff = new byte[buffLen];
                    
                    System.arraycopy(buff, 0, tempBuff, 0, buffLen);
                    int len = FileHelper.writeFile(context, file, tempBuff);
                    if (len == FileHelper.ERROR)
                    {
                        throw new InterruptedException();
                    }
                    else
                    {
                        getLen += len;
                        // 设置下载量（百分比）
                        setDataLength(breakpoint + getLen, breakpoint + dataLen);
                        // 增加已经下载字节数 
                        downloadItem.readedBytes = breakpoint + getLen;
                    }
                    
                    // 网络连接被暂停或取消，需要抛出中断异常，并关闭写文件线程
                    if (canceled || paused || isTimeOut || bWaiting)
                    {
                        throw new InterruptedException();
                    }
                    
                    // 获取读取一定字节数的时间  
                    endMillisecond = System.currentTimeMillis();
                    // 将已经下载的时间累加  
                    downloadItem.downloadTime += (endMillisecond - startMillisecond);
                }
                // sd卡不存在直接抛出异常
                else
                {
                    // SDCard不存在直接弹出提示
                    Toast.makeText(GlobalVariable.currentActivity,
                            GlobalVariable.currentActivity.getResources()
                                    .getString(R.string.sdcard_unload),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                
                // 网络连接被暂停或取消，需要抛出中断异常，并关闭写文件线程
                if (canceled || paused || isTimeOut || bWaiting)
                {
                    throw new InterruptedException();
                    
                }
            }
            
            // 保证下载完了文本进度显示100%
            if (dataLen <= getLen)
            {
                dataLen = getLen;
                setDataLength(breakpoint + getLen, breakpoint + dataLen);
            }
        }
        catch (IOException e)
        {
            throw new IOException();
        }
        catch (SDUnavailableException e)
        {
            throw new InterruptedException("SDUnavailableException");
        }
        catch (SDNotEnouchSpaceException e)
        {
            throw new InterruptedException("SDNotEnouchSpaceException");
        }
        
        // 网络连接被暂停或取消，需要抛出中断异常，并关闭写文件线程
        if (paused || canceled || isTimeOut || bWaiting)
        {
            throw new InterruptedException();
        }
        
        // 标志下载完成
        isDownloadSuccess = true;
    }
    
    /**
     * 创建下载文件目录  
     * @param context 上下文
     * @param fileLength 文件长度
     * @throws SDUnavailableException SD卡不可用的异常
     * @throws SDNotEnouchSpaceException SD卡空间不足的异常
     * @return 创建下载文件目录 是否成功
     */
    protected boolean createFile(final Context context, long fileLength)
            throws SDUnavailableException, SDNotEnouchSpaceException
    {
        boolean success = true;
        File tempFile = null;
        // 判断路径是否存在
        FileHelper.makeDir();
        try
        {
            LogX.getInstance().i(TAG, "fileName = " + downloadItem.mp3Name);
            LogX.getInstance().i(TAG,
                    "downloadItem.savePath = " + downloadItem.mp3SavePath);
            tempFile = FileHelper.createDownloadFile(downloadItem.mp3SavePath, "");
            
            // 注册observer到广播，加在此处为了防止存储在data目录下拔掉存储卡也关闭文件
            //            DownloadObservable.getInstance().addObserver(this);
            
            file = new RandomAccessFile(tempFile, "rw");
        }
        catch (IOException e)
        {
            success = false;
            //            DownloadObservable.getInstance().deleteObserver(this);
        }
        catch (SDNotEnouchSpaceException e)
        {
            success = false;
            if (null == downloadItem.filePostfixName
                    || downloadItem.filePostfixName.equals(""))
            {
                setError(GlobalConstant.URL_ILLAGELE, "URLNotValidException");
            }
            else
            {
                setError(GlobalConstant.LOW_MEMORY, "NotEnoughSpaceException");
            }
        }
        return success;
    }
    
    /**
     * 设置下载回调
     */
    private void startDownloadCallback()
    {
        if (httpCallback != null)
        {
            ((IDownloadCallback) httpCallback).startDownloadCallback();
        }
    }
    
    /**
     * 下载完成回调通知接口
     */
    protected void successDownloadCallback()
    {
        if (httpCallback != null)
        {
            ((IDownloadCallback) httpCallback).successDownloadCallback(context);
        }
    }
    
    /**
     * 设置取消回调
     */
    private void canceledCallback()
    {
        if (httpCallback != null)
        {
            ((IDownloadCallback) httpCallback).canceledCallback();
        }
    }
    
    /**
     * 设暂停回调
     */
    private void pausedCallback()
    {
        if (httpCallback != null)
        {
            ((IDownloadCallback) httpCallback).pausedCallback();
        }
    }
    
    /**
     * 进入等待状态回调
     */
    private void waitingCallback()
    {
        if (httpCallback != null)
        {
            ((IDownloadCallback) httpCallback).waitingCallback();
        }
    }
    
    /**
     * 回调网络接收的及时数据长度
     * 
     * @param getLength 已下载文件大小
     * @param totalLength 文件的总大小
     */
    public void setDataLength(long getLength, long totalLength)
    {
        if (httpCallback != null)
        {
            ((IDownloadCallback) httpCallback).onProgressChanged(getLength,
                    totalLength);
        }
    }
    
    /**
     * 异常处理
     * 
     * @param exception 异常对象
     */
    protected void hanlderException(Exception exception)
    {
        setError(responseCode, exception.toString());
    }
    
    /**
     * 处理InterruptedIOException异常
     * 
     * @param e 异常对象
     */
    protected void handlerInterruptedIOException(Exception e)
    {
        // 在connetionProcess()方法执行中或读写流中或打开连接时，打断会抛此异常
        if (canceled)
        {
            canceledCallback();
        }
        else if (paused)
        {
            pausedCallback();
        }
        else if (isTimeOut)
        {
            setError(CommontErrorCodes.NETWORK_TIMEOUT_ERROR, "TIMEOUT");
        }
        else if (bWaiting)
        {
            waitingCallback();
        }
        else
        {
            super.handlerInterruptedIOException(e);
        }
    }
    
    /**
     * 处理中断异常
     * 
     * @param e 中断异常
     */
    protected void handlerInterruptedException(InterruptedException e)
    {
        // 暂停取消时的打断异常
        if (canceled)
        {
            canceledCallback();
        }
        else if (paused)
        {
            pausedCallback();
        }
        else if (bWaiting)
        {
            waitingCallback();
        }
        else
        {
            super.handlerInterruptedException(e);
        }
    }
    
    /**
     * 处理Error异常
     * 
     * @param e 异常对象
     */
    protected void handlerError(Error e)
    {
        // 错误处理
        if (!isTimeOut)
        {
            setError(responseCode, e.toString());
        }
    }
    
    /**
     * 发送请求
     */
    @Override
    public void sendTaskReq()
    {
        if (downloadQueue != null)
        {
            downloadQueue.addTask(this,
                    GlobalVariable.downloadTaskStartPriority);
        }
    }
    
    /**
     * 更新最大同时下载任务数
     * 
     * @param newMaxCount 新的最大同时下载任务数 备注: 该方法在设置界面中更改最大同时下载任务数时会调用
     */
    public static void updateMaxCount(int newMaxCount)
    {
        if (downloadQueue == null)
        {
            downloadQueue = new TaskQueue(GlobalVariable.maxDownloadCount);
        }
        downloadQueue.updateDownloadMaxCount(newMaxCount);
    }
    
    /**
     * 
     * 删除等待队列下载项<BR>
     * 
     * @param item 下载项
     */
    public static void deleteWaitingItem(DownloadItem item)
    {
        if (downloadQueue != null && item != null)
        {
            //使用这个接口，TaskQueue里提供了
            downloadQueue.removeTask(item.connectionTask);
        }
    }
    
    /**
     * 
     * 删除运行队列下载项<BR>
     * 
     * @param item 下载项
     */
    public static void deleteRunItem(DownloadItem item)
    {
        if (downloadQueue != null && item != null)
        {
            //使用这个接口，TaskQueue里提供了
            downloadQueue.removeRunTask(item.connectionTask);
        }
    }
}
