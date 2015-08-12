package com.xweisoft.wx.family.service.http;

import com.xweisoft.wx.family.logic.model.DownloadItem;


/**
 * 网络连接请求逻辑处理类
 * <功能详细描述>
 * 
 * @author  administrator
 * @version  [版本号, 2013-11-14]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public final class ConnectionLogic
{

    /**
     * 单实例连接处理对象
     */
    private static ConnectionLogic instance;

    /**
     * 私有构造函数
     */
    private ConnectionLogic()
    {
    }

    /**
     * 单实例对象
     * 
     * @return 单实例连接处理对象
     */
    public static synchronized ConnectionLogic getInstance()
    {
        if (instance == null)
        {
            instance = new ConnectionLogic();
        }
        return instance;
    }

    /**
     * 发送请求
     * 
     * @param req 要发送的网络链接请求
     */
    public void sendRequest(ConnectionTask req)
    {
        req.sendTaskReq();
    }

    /**
     * 更新最大同时下载任务数
     * 
     * @param newMaxCount 新的最大同时下载任务数 备注: 该方法在设置界面中更改最大同时下载任务数时会调用
     */
    public void updateDownloadMaxCount(int newMaxCount)
    {
        DownloadTask.updateMaxCount(newMaxCount);
    }

    /**
     * 删除线程中的等待队列<BR>
     * 
     * @param item 下载项
     */
    public void removeTaskThreadpool(DownloadItem item)
    {
        if (item != null)
        {
            //根据删除的item状态，进行对应的队列删除
            if (item.status == DownloadItem.STATUS_DOWNLOAD_WAITING)
            {
                DownloadTask.deleteWaitingItem(item);
            }
            else
            {
                DownloadTask.deleteRunItem(item);
            }
        }
    }
    
    /**
     * 删除线程中的运行队列<BR>
     * 
     * @param item 下载项
     */
    public void removeRunTaskThreadpool(DownloadItem item)
    {
        if (item != null)
        {
            DownloadTask.deleteRunItem(item);
        }
    }
}
