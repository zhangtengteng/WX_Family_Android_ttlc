package com.xweisoft.wx.family.service.upload;

import java.util.Map;

import android.content.Context;

import com.xweisoft.wx.family.service.threadpool.TaskQueue;

/**
 * 网络连接请求逻辑处理类
 * <功能详细描述>
 * 
 * @author  administrator
 * @version  [版本号, 2014年5月13日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public final class FileUploadManager
{
    
    private final static int MAXCOUNT = 3;
    
    private static FileUploadManager instance;
    
    private TaskQueue requestQueue;
    
    private UploadItem item;
    
    private FileUploadManager(Context context)
    {
        requestQueue = new TaskQueue(MAXCOUNT);
    }
    
    public synchronized static FileUploadManager getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new FileUploadManager(context);
        }
        return instance;
    }
    
    /**
     * 添加上传任务到队列
     * <功能详细描述>
     * @param item [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void addUploadTask(UploadItem item)
    {
        if (null == item)
        {
            return;
        }
        UploadFileTask uploadTask = new UploadFileTask(item, item.uploadServer,
                item.filePath);
        
        //增加post请求的参数
        item.uploadTask = uploadTask;
        uploadTask.handler = item.handler;
        this.item = item;
        if (null != item.params)
        {
            for (Map.Entry<String, String> entry : item.params.entrySet())
            {
                if (null != entry)
                {
                    uploadTask.addPostParams(entry.getKey(), entry.getValue());
                }
            }
        }
        addRequest(uploadTask);
    }
    
    public UploadItem getItem()
    {
        return item;
    }
    
    public void clearItem()
    {
        this.item = null;
    }
    
    /**
     * 增加请求任务
     */
    private void addRequest(UploadFileTask req)
    {
        if (requestQueue != null)
        {
            requestQueue.addTask(req);
        }
    }
}