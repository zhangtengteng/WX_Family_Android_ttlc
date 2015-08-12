package com.xweisoft.wx.family.logic.model;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import android.content.Context;
import android.os.Handler;
import android.view.View;

import com.xweisoft.wx.family.logic.download.DownloadObservable;
import com.xweisoft.wx.family.logic.download.DownloadTaskManager;
import com.xweisoft.wx.family.logic.global.GlobalVariable;
import com.xweisoft.wx.family.service.http.ConnectionLogic;
import com.xweisoft.wx.family.service.http.ConnectionTask;
import com.xweisoft.wx.family.util.FileHelper;
import com.xweisoft.wx.family.util.LogX;

/**
 * 下载管理中针对每个下载任务的逻辑数据结构
 * <功能详细描述>
 * 
 * @author  administrator
 * @version  [版本号, 2013-11-13]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class DownloadItem
{
    
    /**
     * 类标志
     */
    public static final String TAG = "===DownLoadItem===";
    
    /**
     * 下载的downloadurl不合法
     */
    public static final int MSG_DOWNLOAD_URL_INVALID = 308;
    
    // -------------------------------------------任务下载时候的状态集----------------------------------------------------
    /**
     * 未知状态
     */
    public static final int STATUS_UNKNOWN = 0x000001;
    
    /**
     * 下载等待状态
     */
    public static final int STATUS_DOWNLOAD_WAITING = 0x000002;
    
    /**
     * 正在下载
     */
    public static final int STATUS_DOWNLOADING = 0x000003;
    
    /**
     * 下载完成
     */
    public static final int STATUS_DOWNLOAD_FINISH = 0x000004;
    
    /**
     * 下载失败
     */
    public static final int STATUS_DOWNLOAD_FAILED = 0x000005;
    
    /**
     * 暂停中
     */
    public static final int STATUS_PAUSEING = 0x000006;
    
    /**
     * 暂停
     */
    public static final int STATUS_PAUSE = 0x000007;
    
    /**
     * 正在安装
     */
    public static final int STATUS_INSTALLING = 0x000008;
    
    /**
     * 安装成功
     */
    public static final int STATUS_INSTALL_SUCCESS = 0x000009;
    
    /**
     * 安装失败
     */
    public static final int STATUS_INSTALL_FAIL = 0x000010;
    
    /**
     * 下载取消
     */
    public static final int STATUS_DOWNLOAD_CANCEL = 0x000011;
    
    // -------------------------------------------------------------------------------------------------------------------------
    
    /**
     * 等待更新
     */
    public static final int WAIT_UPDATE = 0x000011;
    
    /**
     * 满百分比值
     */
    public static final int FULL_PERCENT_VALUE = 100;
    
    /**
     * 零百分比值
     */
    public static final int ZERO_PERCENT_VALUE = 0;
    
    /**
     * 是否为更新重新下载
     */
    public boolean isUpdate = false;
    
    /**
     * 唯一标示id
     */
    public Long id;
    
    /**
     * mp3 id
     */
    public String mp3Id;
    
    /**
     * mp3所在栏目id
     */
    public String categoryId;
    
    /**
     * mp3所在栏目标题
     */
    public String categoryTitle = "";
    
    /**
     * mp3名称
     */
    public String mp3Name = "";
    
    /**
     * mp3歌唱者
     */
    public String mp3Singer = "";
    
    /**
     * mp3下载地址
     */
    public String downloadUrl;
    
    /**
     * 下载管理器中业务的当前状态
     */
    public int status;
    
    /**
     * 该下载文件mp3总长度
     */
    public long mp3Size;
    
    /**
     * 该下载文件本地保存路径
     */
    public String mp3SavePath = "";
    
    /**
     * mp3专辑icon图片
     */
    public String mp3AlbumUrl = "";
    
    /**
     * 下载时间
     */
    public long downloadedTime;
    
    /**
     * 错误信息
     */
    public String errMsg;
    
    /**
     * 下载错误详细信息
     */
    public String errDownloadMsg;
    
    /**
     * 下载任务的当前下载进度
     */
    public int downloadPercent;
    
    /**
     * 下载管理器中业务的安装进度
     */
    public int progress;
    
    /**
     * 文件后缀类型
     */
    public String filePostfixName;
    
    /**
     * 用来接收下载任务下载状态的消息回调Handler对象
     */
    public Handler downloadHandler = null;
    
    /**
     * 下载对象对应的连接任务
     */
    public ConnectionTask connectionTask;
    
    /**
     * 下载任务状态控制同步对象
     */
    public Object syncStateCtrl = new Object();
    
    /**
     * view对象
     */
    public View view;
    
    /**
     * ConnectionTask读取前后的时间戳
     */
    public long downloadTime = 0L;
    
    /**
     * 已经下载文件的字节数
     */
    public long readedBytes = 0L;
    
    /**
     * 下载显示剩余时间戳
     */
    public Double dMillisecond = 0.0;
    
    /**
     * 获取头域中文件的大小
     */
    public long contentLength = 0L;
    
    /**
     * 增加一个标志来区分是否是重新下载此任务，默认为true
     */
    public boolean isRestart = true;
    
    /**
     * 取消正在运行的任务<BR>
     * @param context 上下文
     */
    public void cancelTask(final Context context)
    {
        final boolean isDownloading = isDownloading();
        
        // 如果正在下载，取消网络连接
        if (connectionTask != null && isDownloading)
        {
            connectionTask.pausedConnect();
            canceledCallback(context);
        }
        // 如果没有正在下载从数据库和本地文件中删除
        else
        {
            canceledCallback(context);
        }
    }
    
    /**
     * 暂停正在执行的下载任务
     */
    public void pausedTask()
    {
        // 获取是否正在下载标志
        final boolean isDownloading = isDownloading();
        
        //增加暂停时，针对等待任务的处理
        final boolean isWaiting = isWaiting();
        
        if (connectionTask != null && (isDownloading || isWaiting))
        {
            connectionTask.pausedConnect();
            if (status != STATUS_PAUSE && status != STATUS_DOWNLOAD_FINISH)
            {
                // 将状态修改为暂停中
                setStatus(STATUS_PAUSE);
            }
            
            // 如果暂停按钮按下时已经下载完成，则返回下载完成
            if (status == STATUS_DOWNLOAD_FINISH)
            {
                // 通知UI层更新界面
                sendMsgToUI(STATUS_DOWNLOAD_FINISH);
            }
            else
            {
                // 通知UI层更新界面
                sendMsgToUI(STATUS_PAUSE);
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
        ConnectionLogic.getInstance().removeTaskThreadpool(this);
        
        // 从数据库中更新数据库中的记录
        DownloadTaskManager.getInstance()
                .updateTaskToPaused(GlobalVariable.currentActivity, this);
    }
    
    /**
     * 暂停正在等待的下载任务
     */
    public void pausedWaitingTask()
    {
        // 获取是否正在下载标志
        final boolean isWaiting = isWaiting();
        
        if (connectionTask != null && isWaiting)
        {
            connectionTask.pausedConnect();
            if (status != STATUS_PAUSE && status != STATUS_DOWNLOAD_FINISH)
            {
                // 将状态修改为暂停中
                setStatus(STATUS_PAUSE);
            }
            
            // 如果暂停按钮按下时已经下载完成，则返回下载完成
            if (status == STATUS_DOWNLOAD_FINISH)
            {
                // 通知UI层更新界面
                sendMsgToUI(STATUS_DOWNLOAD_FINISH);
            }
            else
            {
                // 通知UI层更新界面
                sendMsgToUI(STATUS_PAUSE);
            }
            pauseConnection();
        }
    }
    
    /**
     * 刷新UI<BR>
     * [功能详细描述]
     */
    public void refreshUI()
    {
        sendMsgToUI(this.status);
    }
    
    /**
     * 任务加入下载等待队列
     */
    public void waitingTask()
    {
        // 如果已经是等待中状态，则不做处理
        if (status == STATUS_DOWNLOAD_WAITING)
        {
            return;
        }
        
        setStatus(STATUS_DOWNLOAD_WAITING);
        
        if (connectionTask != null)
        {
            connectionTask.waitingConnect();
        }
        
    }
    
    /**
     * 任务加入下载等待队列
     */
    public void waitingRunTask()
    {
        // 如果已经是等待中状态，则不做处理
        if (status == STATUS_DOWNLOAD_WAITING)
        {
            return;
        }
        
        setStatus(STATUS_DOWNLOAD_WAITING);
        
        if (connectionTask != null)
        {
            connectionTask.waitingConnect();
            ConnectionLogic.getInstance().removeRunTaskThreadpool(this);
        }
        
    }
    
    /**
     * 下载失败回调方法
     * 
     * @param context
     *            上下文
     * @param code
     *            错误码
     * @param message
     *            错误信息
     */
    public void onError(final Context context, final int code,
            final String message)
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
        
        // 将状态修改为下载失败
        setStatus(STATUS_DOWNLOAD_FAILED);
        
        // 更新数据库
        DownloadTaskManager.getInstance().updateTaskToDB(context, this);
        
        // 通知UI层更新界面
        sendMsgToUI(STATUS_DOWNLOAD_FAILED);
    }
    
    /**
     * 取消下载回调处理
     * 
     * @param context
     *            上下文
     */
    public void canceledCallback(final Context context)
    {
        try
        {
            // 删除的时候要删除TaskQueue队列，这样保存数据显示的一致性
            ConnectionLogic.getInstance().removeTaskThreadpool(this);
            DownloadTaskManager.getInstance().removeTaskFromDB(context, this);
            
            if (GlobalVariable.sdCardIsExist)
            {
                FileHelper.delFileByAbsolutepath(this.mp3SavePath);
            }
            
            // 从缓存中删除下载项
            DownloadTaskManager.getInstance().removeTaskFromList(context, this);
        }
        catch (Exception e)
        {
            LogX.getInstance().d("exception", "canceledCallback");
        }
        
        //取消下载将状态设置为----> 取消
        setStatus(STATUS_DOWNLOAD_CANCEL);
        
        // 通知UI刷新界面
        sendMsgToUI(STATUS_DOWNLOAD_CANCEL);
    }
    
    /**
     * 下载暂停回调处理
     * 
     * @param context
     *            上下文
     */
    public void pausedCallback(final Context context)
    {
        // 修改状态为暂停
        setStatus(STATUS_PAUSE);
        
        // 更新数据库
        DownloadTaskManager.getInstance().updateTaskToDB(context, this);
        
        // 通知UI刷新界面
        sendMsgToUI(STATUS_PAUSE);
    }
    
    /**
     * 下载等待回调处理
     * 
     * @param context
     *            上下文
     */
    public void waitingCallback(final Context context)
    {
        // 改变状态
        setStatus(STATUS_DOWNLOAD_WAITING);
        
        // 通知UI刷新界面
        sendMsgToUI(STATUS_DOWNLOAD_WAITING);
        
    }
    
    /**
     * 开始下载状态回调
     */
    public void startDownloadCallback()
    {
        if (status == STATUS_PAUSE)
        {
            return;
        }
        
        // 修改状态为正在下载
        setStatus(STATUS_DOWNLOADING);
        
        // 通知UI刷新界面
        sendMsgToUI(STATUS_DOWNLOADING);
    }
    
    /**
     * 下载进度信息回调方法
     * 
     * @param context
     *            上下文
     * @param getLength
     *            已下载的大小
     * @param totalLength
     *            需要下载的总大小
     */
    public void onProgressChanged(final Context context, final long getLength,
            final long totalLength)
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
        
        if (downloadPercent < precent)
        {
            // 把当前下载进度赋给当前下载对象
            downloadPercent = precent;
            
            // 更新状态为下载中
            setStatus(STATUS_DOWNLOADING);
            
            // 通知UI刷新界面
            sendMsgToUI(STATUS_DOWNLOADING);
        }
        
        // 当文件总长度变化时更显数据库
        if (mp3Size != totalLength)
        {
            // 获取文件总长度
            mp3Size = totalLength;
            // 更新数据库
            DownloadTaskManager.getInstance().updateTaskToDB(context, this);
        }
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
    
    public void setDownTime(final long time)
    {
        synchronized (syncStateCtrl)
        {
            this.downloadedTime = time;
        }
    }
    
    /**
     * 设置下载URL
     * 
     * @param downloadUrl
     *            下载URL地址
     */
    public void setDownloadUrl(final String downloadUrl)
    {
        this.downloadUrl = downloadUrl;
    }
    
    /**
     * 设置文件后缀名
     * 
     * @param suffixName
     *            文件的后缀名
     */
    public void setFileSuffixName(final String suffixName)
    {
        filePostfixName = suffixName;
    }
    
    /**
     * 设置下载进度条<BR>
     * 
     * @param progress
     *            进度值
     */
    public void setProgress(final int progress)
    {
        this.progress = progress;
    }
    
    /**
     * 下载完成回调通知接口
     * 
     * @param context
     *            下载上下文
     */
    public void successDownloadCallback(final Context context)
    {
        // 判断是否真正下载完成
        final File file = new File(this.mp3SavePath);
        if (FileHelper.isFileExist(file) && file.length() < this.mp3Size)
        {
            // 如果下载长度小于文件的大小，则证明下载失败。故将状态设置为失败
            LogX.getInstance().i(TAG,
                    mp3Name + " file.length() == " + file.length()
                            + " this.fileSize " + this.mp3Size);
            setStatus(STATUS_DOWNLOAD_FAILED);
        }
        else
        {
            if (null != this.mp3SavePath)
            {
                //                String pcgName = Util.getApkPackageName(context, this.savePath);
                //                if (null != pcgName && !"".equals(pcgName)
                //                        && !pcgName.equals(this.packageName))
                //                {
                //                    this.packageName = pcgName;
                //                }
            }
            // 获取apk包名
            //            LogX.getInstance().i(TAG,
            //                    appName + " of packageName ===== " + this.packageName);
            
            // 修改状态为下载完成
            // 先改变状态，再存入数据库
            setStatus(STATUS_DOWNLOAD_FINISH);
        }
        
        // 通知刷新UI 下载成功，且临时文件没有删除
        sendMsgToUI(STATUS_DOWNLOAD_FINISH);
        
        DownloadTaskManager.getInstance().updateTaskToDB(context, this);
    }
    
    /**
     * 给下载管理activity发送信息
     * 
     * @param downloadmgractivity
     *            下载管理activity
     * @param type
     *            类型
     */
    private void sendMsgToUI(final int type)
    {
        //通知进行变更观察者
        DownloadObservable.getInstance().notifyObservers(this);
        if (downloadHandler != null)
        {
            try
            {
                downloadHandler.sendMessage(downloadHandler.obtainMessage(type,
                        this));
            }
            catch (Exception e)
            {
                LogX.getInstance().d("exception", "sendMsgToUI delete");
            }
        }
    }
    
    /**
     * 是否正在下载
     * 
     * @return true: 正在下载, false: 没有下载
     */
    public boolean isDownloading()
    {
        boolean isDownloadflag = false;
        synchronized (syncStateCtrl)
        {
            // 判断状态是否为正在下载
            isDownloadflag = status == STATUS_DOWNLOADING ? true : false;
        }
        
        return isDownloadflag;
    }
    
    /**
     * 是否正在等待
     * 
     * @return true: 正在等待, false: 没有等待
     */
    public boolean isWaiting()
    {
        boolean isWaitflag = false;
        synchronized (syncStateCtrl)
        {
            // 判断状态是否为正在等待
            isWaitflag = status == STATUS_DOWNLOAD_WAITING ? true : false;
        }
        
        return isWaitflag;
    }
    
    /**
     * 是否正在安装
     * 
     * @return true: 正在安装, false: 没有安装
     */
    public boolean isInstalling()
    {
        boolean isInstallingflag = false;
        synchronized (syncStateCtrl)
        {
            // 判断状态是否为正在安装
            isInstallingflag = status == STATUS_INSTALLING ? true : false;
        }
        
        return isInstallingflag;
    }
    
    /**
     * 是否已被暂停
     * 
     * @return true: 处于暂停状态, false: 非暂停态
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
    
    /**
     * 格式化时间戳的方法，将时间戳显示为：
     * 
     * @param millisecond
     *            格式化的毫秒数
     * @author zhangbing
     * @return String 格式化后的时间
     */
    public String dateFormat(long millisecond)
    {
        if (millisecond >= 0)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            TimeZone timeZone = TimeZone.getDefault();
            timeZone.setRawOffset(0);
            sdf.setTimeZone(timeZone);
            return sdf.format(millisecond);
        }
        return "--:--:--";
    }
    
    /**
     * 获取下载时的速度
     * 
     * @return 下载时速度
     */
    public double getSpeed()
    {
        if (this.downloadTime <= 0)
        {
            return 0;
        }
        final double mySpeed = this.readedBytes / this.downloadTime;
        return mySpeed;
    }
    
    /**
     * 获取剩余时间
     * 
     * @return String 获取剩余时间，显示于页面
     */
    public String getLeaveTime()
    {
        // 获取下载速度
        final double downloadSpeed = getSpeed();
        if (downloadSpeed <= 0.0d)
        {
            return "--:--:--";
        }
        dMillisecond = (this.mp3Size - this.readedBytes) / getSpeed();
        final long leaveMillisecond = dMillisecond.longValue();
        final String leaveTime = dateFormat(leaveMillisecond);
        return leaveTime;
    }
    
}
