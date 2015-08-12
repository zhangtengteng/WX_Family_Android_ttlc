package com.xweisoft.wx.family.logic.download;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.WXApplication;
import com.xweisoft.wx.family.logic.global.GlobalVariable;
import com.xweisoft.wx.family.logic.model.DownloadItem;
import com.xweisoft.wx.family.logic.request.DownloadRequest;
import com.xweisoft.wx.family.service.database.DownloadDBHelper;
import com.xweisoft.wx.family.ui.sliding.WXMainFragmentActivity;
import com.xweisoft.wx.family.util.FileHelper;
import com.xweisoft.wx.family.util.LogX;

/**
 * 下载管理器的任务管理类
 * <功能详细描述>
 * 
 * @author  administrator
 * @version  [版本号, 2013-11-13]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public final class DownloadTaskManager implements IDownloadTaskManager
{
    /**
     * 类标志
     */
    private static final String TAG = "===DownloadTaskManager===";
    
    /**
     * 状态通知栏对象
     */
    private static NotificationManager notificationManager;
    
    /**
     * 单实例对象
     */
    private static DownloadTaskManager instance;
    
    /**
     * 下载管理器中下载中任务的列表
     */
    private ArrayList<DownloadItem> lstDownloadTask;
    
    /**
     * 下载管理器中数据库处理对象
     */
    private DownloadDBHelper downloadDBHelper;
    
    /**
     * 构造方法
     */
    private DownloadTaskManager()
    {
        downloadDBHelper = new DownloadDBHelper();
    }
    
    public List<DownloadItem> getLstDownloadTask()
    {
        return lstDownloadTask;
    }
    
    public void setLstDownloadTask(ArrayList<DownloadItem> lstDownloadTask)
    {
        this.lstDownloadTask = lstDownloadTask;
    }
    
    /**
     * 获取下载管理类单实例对象的接口
     * 
     * @return 单实例对象
     */
    public static DownloadTaskManager getInstance()
    {
        if (instance == null)
        {
            instance = new DownloadTaskManager();
        }
        
        if (notificationManager == null)
        {
            notificationManager = (NotificationManager) WXApplication.getInstance()
                    .getSystemService(Activity.NOTIFICATION_SERVICE);
        }
        
        return instance;
    }
    
    /**
     * 初始化下载管理
     * 
     * @param context
     *            初始化数据库的上下文
     */
    public void initDownLoadMgr(final Context context)
    {
        
        if (downloadDBHelper == null)
        {
            downloadDBHelper = new DownloadDBHelper();
        }
        
        downloadDBHelper.initDownloadDBHelper(context);
        
        /**
         * 下载队列管理
         */
        if (lstDownloadTask == null)
        {
            lstDownloadTask = downloadDBHelper.getAllTask(context);
        }
    }
    
    /**
     * 创建一个下载任务到下载管理器中
     * 
     * @param context 上下文
     * @param item 为从ODP中传过来的结构对象（先临时用AppItem代替） 备注: 向下载管理器中添加下载任务时会调用该方法
     */
    public void createTask(final Context context, DownloadItem item)
    {
        if (downloadDBHelper == null)
        {
            downloadDBHelper = new DownloadDBHelper();
        }
        
        if (lstDownloadTask == null)
        {
            lstDownloadTask = downloadDBHelper.getAllTask(context);
        }
        
        /**
         * 设置下载开始的时间
         */
        item.downloadedTime = new Date().getTime();
        
        downloadDBHelper.addToDB(context, item);
        
        if (null == lstDownloadTask)
        {
            return;
        }
        
        /**
         * 添加到这个队列的首部还是尾部，因为这个里面可能有更新app
         */
        synchronized (lstDownloadTask)
        {
            switch (GlobalVariable.downloadTaskInsertType)
            {
                case 0:
                    lstDownloadTask.add(item);
                    break;
                case 1:
                    lstDownloadTask.add(0, item);
                    break;
                default:
                    lstDownloadTask.add(item);
                    break;
            }
        }
        
        DownloadTaskManager.getInstance().showDownNotifycation(item, false);
        
        /**
         * 开始执行
         */
        downloadTask(context, item);
    }
    
    /**
     * 删除原文件
     * @param fileName 文件名
     */
    public void deleteDownloadFile(String fileName)
    {
        FileHelper.deleteDownloadFile(fileName);
    }
    
    /**
     * 从缓存中删除下载项
     * @param context 上下文
     * @param downloadingItem 要删除的下载对象
     */
    public void reDownloadPuaseDownloading(final Context context,
            DownloadItem downloadingItem)
    {
        if (null == lstDownloadTask)
        {
            return;
        }
        synchronized (lstDownloadTask)
        {
            Iterator<DownloadItem> items = lstDownloadTask.iterator();
            DownloadItem item = null;
            while (items.hasNext())
            {
                item = items.next();
                if (null != downloadingItem.mp3Id
                        && downloadingItem.mp3Id.compareTo(item.mp3Id) == 0)
                {
                    if (item.isWaiting() || item.isDownloading())
                    {
                        item.pausedTask();
                    }
                    return;
                }
            }
        }
    }
    
    /**
     * 重新下载任务
     *      将原来的任务和下载下来的文件删除
     *      从0开始，下载新得
     * @param context 上下文
     * @param item 下载项
     */
    public void redownloadTask(final Context context, DownloadItem item)
    {
        if (item != null && item.downloadUrl != null
                && item.status != DownloadItem.STATUS_PAUSEING)
        {
            /**
             * 把Item的状态变成waiting，并且将
             * connectionTask也设置成等待
             */
            item.waitingTask();
            FileHelper.delFileByAbsolutepath(item.mp3SavePath);
            item.downloadPercent = 0;
            item.mp3Size = 0;
            item.setStatus(DownloadItem.STATUS_DOWNLOAD_WAITING);
            if (downloadDBHelper != null)
            {
                downloadDBHelper.updateTaskToDB(context, item);
            }
            
            if (null == lstDownloadTask)
            {
                return;
            }
            
            /**
             * 添加到这个队列的首部还是尾部，因为这个里面可能有更新app
             */
            synchronized (lstDownloadTask)
            {
                if (lstDownloadTask.isEmpty())
                {
                    lstDownloadTask.add(item);
                }
                else
                {
                    Iterator<DownloadItem> items = lstDownloadTask.iterator();
                    DownloadItem temp = null;
                    while (items.hasNext())
                    {
                        temp = items.next();
                        if (null != temp.mp3Id
                                && temp.mp3Id.compareTo(item.mp3Id) == 0)
                        {
                            lstDownloadTask.remove(temp);
                            break;
                        }
                    }
                    lstDownloadTask.add(item);
                }
            }
            downloadTask(context, item);
        }
    }
    
    /**
     * 执行下载操作
     * @param context 上下文
     * @param item 要下载的业务信息
     */
    public void downloadTask(final Context context, DownloadItem item)
    {
        /**
         * 正在下载和暂停的都不用去管了
         */
        if (null != item
                && (item.status == DownloadItem.STATUS_DOWNLOADING || item.status == DownloadItem.STATUS_PAUSEING))
        {
            return;
        }
        final File file = new File(item.mp3SavePath);
        /**
         * 已经下载完成了的，也就不用管了
         */
        if (FileHelper.isFileExist(file) && file.length() == item.mp3Size
                || item.status == DownloadItem.STATUS_DOWNLOAD_FINISH)
        {
            return;
        }
        /**
         * 检查现在已经下载了多少
         */
        item.readedBytes = 0;
        if (FileHelper.isFileExist(file))
        {
            item.readedBytes = file.length();
        }
        /**
         * 下载任务
         */
        DownloadRequest downloadRequest = new DownloadRequest(context, item);
        downloadRequest.sendHttpRequest();
    }
    
    /**
     * 删除列表指定索引位置的任务
     * @param context 上下文
     * @param downLoadItem 下载对象
     */
    public void removeTask(final Context context, DownloadItem downLoadItem)
    {
        if (downLoadItem != null)
        {
            // 停止任务，如果在运行的话
            downLoadItem.cancelTask(context);
        }
    }
    
    /**
     * 从缓存中删除指定的下载项
     * @param context 上下文
     * @param position 索引
     */
    public void removeTaskFromPosition(final Context context, int position)
    {
        if (downloadDBHelper == null)
        {
            downloadDBHelper = new DownloadDBHelper();
        }
        
        if (lstDownloadTask == null)
        {
            lstDownloadTask = downloadDBHelper.getAllTask(context);
        }
        if (null == lstDownloadTask)
        {
            return;
        }
        synchronized (lstDownloadTask)
        {
            lstDownloadTask.remove(position);
        }
    }
    
    /**
     * 从缓存中删除下载项
     * @param context 上下文
     * @param downLoadItem 要删除的下载对象
     */
    public void removeTaskFromList(final Context context,
            DownloadItem downLoadItem)
    {
        if (downloadDBHelper == null)
        {
            downloadDBHelper = new DownloadDBHelper();
        }
        
        if (lstDownloadTask == null)
        {
            lstDownloadTask = downloadDBHelper.getAllTask(context);
        }
        if (null == lstDownloadTask)
        {
            return;
        }
        synchronized (lstDownloadTask)
        {
            Iterator<DownloadItem> items = lstDownloadTask.iterator();
            DownloadItem item = null;
            while (items.hasNext())
            {
                item = items.next();
                if (null != item.mp3Id
                        && item.mp3Id.compareTo(downLoadItem.mp3Id) == 0)
                {
                    lstDownloadTask.remove(item);
                    return;
                }
            }
        }
    }
    
    /**
     * 从缓存中删除下载项
     * @param context 上下文
     * @param downLoadItem 要删除的下载对象
     */
    public void downloadSuccessRemoveTaskFromList(final Context context,
            DownloadItem downLoadItem)
    {
        if (downloadDBHelper == null)
        {
            downloadDBHelper = new DownloadDBHelper();
        }
        
        if (lstDownloadTask == null)
        {
            lstDownloadTask = downloadDBHelper.getAllTask(context);
        }
        if (null == lstDownloadTask)
        {
            return;
        }
        synchronized (lstDownloadTask)
        {
            lstDownloadTask.remove(downLoadItem);
        }
    }
    
    /**
     * 清空缓存下载链表
     * @param context 上下文
     */
    public void removeAllLstTask(final Context context)
    {
        
        if (lstDownloadTask == null)
        {
            lstDownloadTask = downloadDBHelper.getAllTask(context);
        }
        if (null == lstDownloadTask)
        {
            return;
        }
        synchronized (lstDownloadTask)
        {
            lstDownloadTask.removeAll(lstDownloadTask);
        }
    }
    
    /**
     * 暂停任务
     * 
     * @param item 下载列表
     */
    public void onPauseTask(DownloadItem item)
    {
        if (item != null && (item.isDownloading() || item.isWaiting()))
        {
            // 暂停下载
            item.pausedTask();
        }
    }
    
    /**
     * 暂停等待任务
     * 
     * @param item 下载列表
     */
    public void onPauseWaitingTask(DownloadItem item)
    {
        if (item != null && item.isWaiting())
        {
            // 暂停下载
            item.pausedWaitingTask();
        }
    }
    
    /**
     * 等待任务
     * 
     * @param item
     *            下载列表
     */
    public void onWaitingTask(DownloadItem item)
    {
        if (item != null && item.isDownloading())
        {
            // 暂停下载
            item.waitingTask();
        }
    }
    
    /**
     * 等待任务
     * 
     * @param item
     *            下载列表
     */
    public void onWaitingRunTask(DownloadItem item)
    {
        if (item != null && item.isDownloading())
        {
            // 暂停下载
            item.waitingRunTask();
        }
    }
    
    /**
     * 暂停所有任务任务
     * @param context 上下文
     */
    public void onPauseAllTask(final Context context)
    {
        // 在客户端退出的情况下，接受到电话的广播，则返回
        if (lstDownloadTask == null)
        {
            lstDownloadTask = downloadDBHelper.getAllTask(context);
        }
        
        if (lstDownloadTask == null)
        {
            return;
        }
        
        synchronized (lstDownloadTask)
        {
            if (!lstDownloadTask.isEmpty())
            {
                // 先清空所有的任务对象
                Iterator<DownloadItem> items = lstDownloadTask.iterator();
                while (items.hasNext())
                {
                    DownloadItem item = items.next();
                    if (item != null && item.isDownloading())
                    {
                        // 暂停下载
                        item.pausedTask();
                    }
                    else if (item != null && item.isWaiting())
                    {
                        //等待任务的暂停
                        item.pausedWaitingTask();
                    }
                }
            }
        }
    }
    
    /**
     * 继续任务
     * @param context 上下文
     * @param item 下载列表
     */
    public void onContinueTask(final Context context, DownloadItem item)
    {
        if (item != null)// && item.isPaused()
        {
            // 断点续传功能
            downloadTask(context, item);
        }
    }
    
    /**
     * 根据业务id获得下载列表中的DownloadItem对象
     * @param context 上下文
     * @param appId 业务id
     * @return 下载任务对象
     */
    public DownloadItem getDownLoadItem(final Context context, String mp3Id)
    {
        // 判空操作
        if (mp3Id == null)
        {
            return null;
        }
        
        ArrayList<DownloadItem> lstDownloadTask1 = downloadDBHelper.getAllTask(context,
                true);
        if (null == lstDownloadTask1)
        {
            return null;
        }
        synchronized (lstDownloadTask1)
        {
            Iterator<DownloadItem> items = lstDownloadTask1.iterator();
            while (items.hasNext())
            {
                DownloadItem downLoadItem = items.next();
                if (downLoadItem.mp3Id.compareTo(mp3Id) == 0)
                {
                    return downLoadItem;
                }
            }
        }
        return null;
    }
    
    /**
     * 更新数据为暂停状态
     * @param context 上下文
     * @param item DownLoadItem对象
     */
    public void updateTaskToPaused(final Context context, DownloadItem item)
    {
        if (null != downloadDBHelper)
        {
            downloadDBHelper.updateTaskToPaused(context, item);
        }
    }
    
    /**
     * 根据业务contentCode获得下载列表中的DownloadItem对象
     * @param context 上下文
     * @param appId 业务id
     * @return 下载任务对象
     */
    public DownloadItem getDownLoadDownloadItem(final Context context,
            String mp3Id)
    {
        // 判空操作
        if (mp3Id == null)// || null == MobilyApplication.getInstance().getUserInfo()
        {
            return null;
        }
        if (downloadDBHelper == null)
        {
            downloadDBHelper = new DownloadDBHelper();
        }
        if (lstDownloadTask == null)
        {
            lstDownloadTask = downloadDBHelper.getAllTask(context);
        }
        if (null == lstDownloadTask)
        {
            return null;
        }
        Iterator<DownloadItem> items = lstDownloadTask.iterator();
        while (items.hasNext())
        {
            DownloadItem downLoadItem = items.next();
            if (null != downLoadItem.mp3Id
                    && downLoadItem.mp3Id.compareTo(mp3Id) == 0)
            {
                return downLoadItem;
            }
        }
        return null;
    }
    
    /**
     * 根据业务contentCode获得下载列表中的DownloadItem对象
     * @param context 上下文
     * @param appId 业务id
     * @return 下载任务对象
     */
    public DownloadItem getDownloadItem(final Context context, String mp3Id)
    {
        // 判空操作
        if (mp3Id == null)// || null == MobilyApplication.getInstance().getUserInfo())
        {
            return null;
        }
        if (downloadDBHelper == null)
        {
            downloadDBHelper = new DownloadDBHelper();
        }
        ArrayList<DownloadItem> downloadTask = downloadDBHelper.getAllTask(context,
                true);
        if (null == downloadTask)
        {
            return null;
        }
        Iterator<DownloadItem> items = downloadTask.iterator();
        while (items.hasNext())
        {
            DownloadItem downLoadItem = items.next();
            /**
             * 有一样的appId，并且下载的用户和当前的用户是同一个人，就把这个item返回去
             * 
             */
            if (null != downLoadItem.mp3Id
                    && downLoadItem.mp3Id.compareTo(mp3Id) == 0)
            {
                return downLoadItem;
            }
        }
        return null;
    }
    
    /**
     * 获取下载管理器中的任务数
     * @param context 上下文
     * @return 任务数
     */
    public int getTasksCount(final Context context)
    {
        int size = 0;
        
        if (downloadDBHelper == null)
        {
            downloadDBHelper = new DownloadDBHelper();
        }
        
        // 判空操作
        if (lstDownloadTask == null)
        {
            lstDownloadTask = downloadDBHelper.getAllTask(context);
        }
        if (null == lstDownloadTask)
        {
            return size;
        }
        // 返回listApp长度
        synchronized (lstDownloadTask)
        {
            size = lstDownloadTask.size();
        }
        
        return size;
    }
    
    /**
     * 根据索引位置获取下载任务
     * @param context 上下文
     * @param position 位置索引
     * @return 下载任务
     */
    public DownloadItem getItem(final Context context, int position)
    {
        if (lstDownloadTask == null)
        {
            lstDownloadTask = downloadDBHelper.getAllTask(context);
        }
        if (null == lstDownloadTask)
        {
            return null;
        }
        synchronized (lstDownloadTask)
        {
            return (!lstDownloadTask.isEmpty() && position < lstDownloadTask.size()) ? lstDownloadTask.get(position)
                    : null;
        }
    }
    
    /**
     * 根据mp3id获取下载对象
     * @param context 上下文
     * @param provisionCode 呈现id
     * @return 下载任务
     */
    public DownloadItem getDownloadingItem(final Context context, String mp3Id)
    {
        if (lstDownloadTask == null)
        {
            lstDownloadTask = downloadDBHelper.getAllTask(context);
        }
        if (null == lstDownloadTask)
        {
            return null;
        }
        synchronized (lstDownloadTask)
        {
            Iterator<DownloadItem> items = lstDownloadTask.iterator();
            DownloadItem downLoadItem = null;
            while (items.hasNext())
            {
                downLoadItem = items.next();
                if (downLoadItem.mp3Id.equals(mp3Id)
                        && downLoadItem.status != DownloadItem.STATUS_DOWNLOAD_FINISH)
                {
                    return downLoadItem;
                }
            }
        }
        return null;
    }
    
    /**
     * 根据业务contentCode获得已下载或者已安装的DownloadItem对象
     * @param context 上下文
     * @param appId 业务id
     * @return 下载任务对象
     */
    public DownloadItem getDownloadedAppItem(final Context context, String mp3Id)
    {
        // 判空操作
        if (mp3Id == null)// || null == MobilyApplication.getInstance().getUserInfo())
        {
            return null;
        }
        if (downloadDBHelper == null)
        {
            downloadDBHelper = new DownloadDBHelper();
        }
        ArrayList<DownloadItem> downloadTask = downloadDBHelper.getDownloadedItems(context);
        if (null == downloadTask)
        {
            return null;
        }
        Iterator<DownloadItem> items = downloadTask.iterator();
        while (items.hasNext())
        {
            DownloadItem downLoadItem = items.next();
            if (null != downLoadItem.mp3Id
                    && downLoadItem.mp3Id.compareTo(mp3Id) == 0)
            {
                return downLoadItem;
            }
        }
        return null;
    }
    
    /**
     * 获取当前下载队列中正在下载的任务数
     * @param context 上下文
     * @return 任务数
     */
    public int getDownLoadingTaskCount(final Context context)
    {
        int count = 0;
        if (lstDownloadTask == null)
        {
            lstDownloadTask = downloadDBHelper.getAllTask(context);
        }
        if (null == lstDownloadTask)
        {
            return count;
        }
        synchronized (lstDownloadTask)
        {
            Iterator<DownloadItem> items = lstDownloadTask.iterator();
            while (items.hasNext())
            {
                DownloadItem downLoadItem = items.next();
                if (downLoadItem.isDownloading())
                {
                    count++;
                }
            }
        }
        return count;
    }
    
    /**
     * 如果正在下载的记录数大于并行下载的最大值，则将超过数据的记录设置为等待状态
     * @param context
     * @param count
     */
    public void setOtherRunTask2Waiting(final Context context, int count)
    {
        int cnt = 0;
        if (lstDownloadTask == null)
        {
            lstDownloadTask = downloadDBHelper.getAllTask(context);
        }
        if (null == lstDownloadTask)
        {
            return;
        }
        synchronized (lstDownloadTask)
        {
            Iterator<DownloadItem> items = lstDownloadTask.iterator();
            DownloadItem downLoadItem = null;
            while (items.hasNext())
            {
                downLoadItem = items.next();
                if (downLoadItem.isDownloading())
                {
                    if (cnt < count)
                    {
                        onWaitingTask(downLoadItem);
                        downLoadItem.refreshUI();
                        cnt++;
                        onContinueTask(context, downLoadItem);
                    }
                    else
                    {
                        break;
                    }
                }
            }
        }
    }
    
    /**
     * 如果正在下载的记录数大于并行下载的最大值，则将超过数据的记录设置为等待状态
     * <功能详细描述>
     * @param context
     * @param currentCount
     * @param newCount [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void setOtherRunTask2Waiting(final Context context,
            int currentCount, int newCount)
    {
        int cnt = 0;
        int count = currentCount - newCount;
        if (lstDownloadTask == null)
        {
            lstDownloadTask = downloadDBHelper.getAllTask(context);
        }
        if (null == lstDownloadTask)
        {
            return;
        }
        synchronized (lstDownloadTask)
        {
            Iterator<DownloadItem> items = lstDownloadTask.iterator();
            DownloadItem downLoadItem = null;
            while (items.hasNext())
            {
                downLoadItem = items.next();
                if (downLoadItem.isDownloading())
                {
                    if (cnt < count)
                    {
                        onWaitingRunTask(downLoadItem);
                        downLoadItem.refreshUI();
                        cnt++;
                        onContinueTask(context, downLoadItem);
                    }
                    else
                    {
                        break;
                    }
                }
            }
        }
    }
    
    /**
     * 获取DownLoadItem在lstDownloadTask中的位置
     * @param context 上下文
     * @param item 要查询的DownLoadItem
     * @return item 在lstDownloadTask中的位置
     */
    public int getItemPosition(final Context context, DownloadItem item)
    {
        // 位置变量
        int position = -1;
        
        if (lstDownloadTask == null)
        {
            lstDownloadTask = downloadDBHelper.getAllTask(context);
        }
        if (null == lstDownloadTask)
        {
            return position;
        }
        // 判空操作
        if (lstDownloadTask.size() <= 0 || item == null)
        {
            return position;
        }
        
        // 获取位置
        return lstDownloadTask.indexOf(item);
    }
    
    /**
     * 根据appId和appName获取对象位置
     * @param context 上下文
     * @param item 要查询的DownLoadItem
     * @return 索引
     */
    public int getItemIndex(final Context context, DownloadItem item)
    {
        // 位置变量
        int position = -1;
        if (lstDownloadTask == null)
        {
            lstDownloadTask = downloadDBHelper.getAllTask(context);
        }
        if (null == lstDownloadTask)
        {
            return position;
        }
        int size = lstDownloadTask.size();
        
        // 判空操作
        if (size <= 0 || item == null)
        {
            return position;
        }
        
        for (int i = 0; i < size; i++)
        {
            if (lstDownloadTask.get(i).mp3Id.equals(item.mp3Id)
                    && lstDownloadTask.get(i).mp3Name.equals(item.mp3Name))
            {
                position = i;
            }
        }
        
        return position;
    }
    
    /**
     * 更新数据
     * @param context 上下文
     * @param item DownLoadItem对象
     */
    public void updateTaskToDB(final Context context, DownloadItem item)
    {
        if (downloadDBHelper != null)
        {
            downloadDBHelper.updateTaskToDB(context, item);
        }
    }
    
    /**
     * 更新数据
     * @param context 上下文
     * @param item DownLoadItem对象
     */
    public void updateTaskToDBApp(final Context context, DownloadItem item)
    {
        if (downloadDBHelper != null)
        {
            downloadDBHelper.updateTaskToDBApp(context, item);
        }
    }
    
    /**
     * 从数据库中删除数据
     * @param context 上下文
     * @param item DownLoadItem对象
     */
    public void removeTaskFromDB(final Context context, DownloadItem item)
    {
        if (downloadDBHelper != null)
        {
            downloadDBHelper.removeTaskFromDB(context, item);
        }
    }
    
    /**
     * 关闭存储下载管理列表的数据库
     */
    public void closeDB()
    {
        if (downloadDBHelper != null)
        {
            downloadDBHelper.closeDB();
        }
    }
    
    /**
     * 更新lstDownloadTask中的downloaditem,并且更新数据库中对应的下载纪录，将下载路径更新至数据库
     * @param context 上下文
     * @param downloadItem 下载对象
     */
    public void updateDownloadItemFromList(final Context context,
            DownloadItem downloadItem)
    {
        if (downloadDBHelper == null)
        {
            downloadDBHelper = new DownloadDBHelper();
        }
        
        if (lstDownloadTask == null)
        {
            lstDownloadTask = downloadDBHelper.getAllTask(context);
        }
        
        // 加入数据库
        downloadDBHelper.updateTaskToDB(context, downloadItem);
    }
    
    /**
     * 更新lstDownloadTask中的数据
     * 
     * @param context 上下文
     * @param downloadItem 下载对象
     */
    public void updateTaskFromList(final Context context,
            DownloadItem downloadItem)
    {
        if (downloadDBHelper == null)
        {
            downloadDBHelper = new DownloadDBHelper();
        }
        
        if (lstDownloadTask == null)
        {
            lstDownloadTask = downloadDBHelper.getAllTask(context);
        }
        int index = getItemPosition(context, downloadItem);
        if (-1 != index)
        {
            synchronized (lstDownloadTask)
            {
                lstDownloadTask.set(index, downloadItem);
            }
        }
    }
    
    /**
     * 获取全部的下载管理已下載完成，未安裝的应用
     * @param context 上下文
     * @return 已下載完成，未安裝的应用
     */
    public List<DownloadItem> getAllTask(Context context)
    {
        if (lstDownloadTask == null
                || (null != lstDownloadTask && lstDownloadTask.isEmpty()))
        {
            lstDownloadTask = downloadDBHelper.getAllTask(context);
        }
        ArrayList<DownloadItem> downloadFinishedList = new ArrayList<DownloadItem>();
        if (null == lstDownloadTask)
        {
            return downloadFinishedList;
        }
        synchronized (lstDownloadTask)
        {
            int downloadTaskSize = lstDownloadTask.size();
            for (int i = 0; i < downloadTaskSize; i++)
            {
                DownloadItem downloadFinshed = lstDownloadTask.get(i);
                if (downloadFinshed.status == DownloadItem.STATUS_DOWNLOAD_FINISH)
                {
                    downloadFinishedList.add(downloadFinshed);
                }
            }
        }
        return downloadFinishedList;
    }
    
    /**
     * 获取全部的appItem,无论是否下载完成
     * @param context 上下文
     * @return 所有
     */
    public ArrayList<DownloadItem> getAllItem(Context context)
    {
        if (downloadDBHelper == null)
        {
            downloadDBHelper = new DownloadDBHelper();
        }
        
        return downloadDBHelper.getAllItems(context);
    }
    
    /**
     * 根据MP3ID来判断该MP3是否正在下载
     * @param context 上下文
     * @return 所有
     */
    public boolean isDownloadingMp3(Context context, String mp3Id)
    {
        if (downloadDBHelper == null)
        {
            downloadDBHelper = new DownloadDBHelper();
        }
        
        return downloadDBHelper.isDownloadingMp3(context, mp3Id);
    }
    
    /**
     * 根据MP3ID来判断该MP3是否已经下载完
     * @param context 上下文
     * @return 所有
     */
    public boolean isDownloadedMp3(Context context, String mp3Id)
    {
        if (downloadDBHelper == null)
        {
            downloadDBHelper = new DownloadDBHelper();
        }
        
        return downloadDBHelper.isDownloadedMp3(context, mp3Id);
    }
    
    /**
     * 获取全部的已下載完成，未安裝的应用
     * @param context 上下文
     * @return 已下載完成，未安裝的应用
     */
    public ArrayList<DownloadItem> getDownloadedItem(Context context)
    {
        if (downloadDBHelper == null)
        {
            downloadDBHelper = new DownloadDBHelper();
        }
        
        return downloadDBHelper.getDownloadedItems(context);
    }
    
    /**
     * 是否已经下载完成, 供明细页面评论使用.
     * @param context 上下文
     * @param appId provisionCode
     * @return true 已经下载, false 没有下载完成
     */
    public boolean getDownloadSuccess(Context context, String mp3Id)
    {
        ArrayList<DownloadItem> downloadTask = getDownloadedItem(context);
        if (null == downloadTask)
        {
            return false;
        }
        Iterator<DownloadItem> items = downloadTask.iterator();
        DownloadItem downLoadItem = null;
        while (items.hasNext())
        {
            downLoadItem = items.next();
            if (null != downLoadItem && null != downLoadItem.mp3Id
                    && downLoadItem.mp3Id.compareTo(mp3Id) == 0)
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 下载，在状态栏添加图标
     * 
     * @param item  ：下载实体
     * @param success :是否下载成功
     */
    
    @SuppressWarnings("deprecation")
    public void showDownNotifycation(DownloadItem item, boolean success)
    {
        if (null == item)
        {
            return;
        }
        Intent intent = new Intent(WXApplication.getInstance(),
                WXMainFragmentActivity.class);
        intent.putExtra("isComeFromNotifycation", true);
        if (!success)
        {
            Notification notification = new Notification(
                    android.R.drawable.stat_sys_download,
                    WXApplication.getInstance()
                            .getResources()
                            .getString(R.string.m00_new_task_added),
                    System.currentTimeMillis());
            notification.setLatestEventInfo(WXApplication.getInstance(),
                    WXApplication.getInstance()
                            .getResources()
                            .getString(R.string.app_name),
                    item.mp3Name
                            + " "
                            + WXApplication.getInstance()
                                    .getResources()
                                    .getString(R.string.m00_down_notifycation_down),
                    PendingIntent.getActivity(WXApplication.getInstance(),
                            item.id.hashCode(),
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT));
            
            notificationManager.notify(item.id.hashCode(), notification);
        }
        else
        {
            Notification notification = new Notification(0, "",
                    System.currentTimeMillis());
            intent.putExtra("index", item.id);
            notification.setLatestEventInfo(WXApplication.getInstance(),
                    WXApplication.getInstance()
                            .getResources()
                            .getString(R.string.app_name),
                    item.mp3Name
                            + " "
                            + WXApplication.getInstance()
                                    .getResources()
                                    .getString(R.string.m00_down_notifycation_down_done),
                    PendingIntent.getActivity(WXApplication.getInstance(),
                            item.id.hashCode(),
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT));
            notificationManager.notify(item.id.hashCode(), notification);
        }
    }
    
    /**
     * 退出程序，取消状态栏下载通知
     */
    public void cancelNotification()
    {
        if (null != notificationManager)
        {
            notificationManager.cancelAll();
        }
    }
    
    /**
     * 点击下载失败的记录时取消状态栏通知。
     * @param id notificatin ID
     */
    public void cancelNotificationById(int id)
    {
        if (null != notificationManager)
        {
            notificationManager.cancel(id);
        }
    }
    
    /**
     * 下载失败，将任务状态设置为暂停，在状态栏给出提示
     * @param item 下载对象
     */
    @SuppressWarnings("deprecation")
    public void downloadFailNotification(DownloadItem item)
    {
        LogX.getInstance().e(TAG, "-----download fail--------");
        Intent intent = new Intent(WXApplication.getInstance(),
                WXMainFragmentActivity.class);
        intent.putExtra("isComeFromNotifycation", true);
        Notification notification = new Notification(
                android.R.drawable.stat_sys_download,
                WXApplication.getInstance()
                        .getResources()
                        .getString(R.string.m00_status_download_failed),
                System.currentTimeMillis());
        intent.putExtra("index", item.id);
        notification.setLatestEventInfo(WXApplication.getInstance(),
                WXApplication.getInstance()
                        .getResources()
                        .getString(R.string.app_name),
                item.mp3Name
                        + " "
                        + WXApplication.getInstance()
                                .getResources()
                                .getString(R.string.m00_status_download_failed),
                PendingIntent.getActivity(WXApplication.getInstance(),
                        item.id.hashCode(),
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT));
        notificationManager.notify(item.id.hashCode(), notification);
    }
    
    /**
     * 添加下载
     * @param context 当前activity
     * @param item 下载对象
     */
    public void addDownloadItem2Task(Context context, DownloadItem item)
    {
        createTask(context, item);
    }
    
    /**
     * 判断当前任务是否已经下载过。
     * @param context 上下文
     * @param contentCode 内容id或者呈现id
     * @return DownloadItem 下载对象
     */
    public DownloadItem getDownloaded(Context context, String contentCode)
    {
        /**
         * 从我们的数据库中去判断，是否已经下载了这个对象
         *      下载过：
         *            返回这个对象之前的下载属性
         *       未下载：
         *           返回null
         */
        DownloadItem downloadTask = getDownloadItem(context, contentCode);
        
        return downloadTask;
    }
    
    /**
     * 删除数据库中download表中的已下载完成和已安装的一条记录<BR>
     * [功能详细描述]
     * @param context 上下文
     * @param packageName 包名
     */
    public void deleteItem(Context context, String packageName)
    {
        if (downloadDBHelper == null)
        {
            downloadDBHelper = new DownloadDBHelper();
        }
        // downloadDBHelper.deleteItems(context, packageName);
    }
    
    /**
     * 下载管理页面中删除下载任务的方法
     * <功能详细描述>
     * @param mContext
     * @param lists [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void deletePuaseDownloadTask(Context mContext,
            List<DownloadItem> lists)
    {
        if (null == lists)
        {
            return;
        }
        int size = lists.size();
        DownloadItem item = null;
        for (int i = 0; i < size; i++)
        {
            item = lists.get(i);
            if (item.status == DownloadItem.STATUS_DOWNLOAD_WAITING
                    || item.status == DownloadItem.STATUS_DOWNLOADING)
            {
                item = getDownloadingItem(mContext, item.mp3Id);
                if (null == item)
                {
                    continue;
                }
            }
            if (item.status == DownloadItem.STATUS_DOWNLOAD_WAITING)
            {
                item.pausedWaitingTask();
            }
            else if (item.status == DownloadItem.STATUS_DOWNLOADING)
            {
                item.pausedTask();
            }
        }
        if (null != lstDownloadTask && !lstDownloadTask.isEmpty())
        {
            lstDownloadTask.removeAll(lists);
        }
    }
}
