package com.xweisoft.wx.family.service.upload;

import java.util.Map;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.google.gson.Gson;
import com.xweisoft.wx.family.logic.model.response.UploadFileResp;
import com.xweisoft.wx.family.util.LogX;
import com.xweisoft.wx.family.util.Util;

/**
 * 上传文件实体类
 * <功能详细描述>
 * 
 * @author  administrator
 * @version  [版本号, 2013-5-16]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class UploadItem implements IHttpListener
{
    private static final String TAG = "===UploadItem===";
    
    /**
     * 上传等待中
     */
    public static final int MSG_UPLOAD_FILE_WAITING = 100;
    
    /**
     * 开始上传
     */
    public static final int MSG_UPLOAD_FILE_START = 102;
    
    /**
     * 上传成功
     */
    public static final int MSG_UPLOAD_FILE_SUCCESS = 103;
    
    /**
     * 上传失败
     */
    public static final int MSG_UPLOAD_FILE_FAIL = 104;
    
    /**
     * 上传暂停
     */
    public static final int MSG_UPLOAD_FILE_PAUSE = 105;
    
    /**
     * 上传取消
     */
    public static final int MSG_UPLOAD_FILE_CANCEL = 106;
    
    /**
     * 上传进度通知消息
     */
    public static final int MSG_UPLOAD_PROCESS = 107;
    
    //////////////////////////////
    
    /**
     * 满百分比值
     */
    public static final int FULL_PERCENT_VALUE = 100;
    
    public static final int UPLOAD_UNKNOWN = -100;
    
    public static final int UPLOAD_START = 101;
    
    public static final int UPLOAD_ERROR = 102;
    
    public static final int UPLOAD_PROGRESS = 103;
    
    public static final int UPLOAD_CANCEL = 104;
    
    public static final int UPLOAD_COMPLETE = 105;
    
    public static final int SEND_UPLOAD_DONE_REQUEST = 103;
    
    public static final int UPLOAD_SCUUCSS = 1008;
    
    public String uploadServer = null;
    
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
     * 是否正在上传
     */
    public boolean isUploading = false;
    
    public Map<String, String> params;
    
    /**
     * 上传地址
     * 文件存储路径
     * 说明： 
     * 1. 如果上传一个文件，则filePath的长度只能为1， 且不能为空
     * 2. 如果是两个文件，即 图片和声音  或者  图片(缩略图)和视频， 则filePath的长度只能为2, 
     *    图片地址必须放在filePath[0] 声音 或者 视频放在filePath[1]
     */
    public String[] filePath;
    
    /**
     * 消息模式
     * 1:普通聊天, 2:隐私聊天，3：公共数据（需要审核）
     */
    public int msgMode = 1;
    
    /**
     * 上传文件类型枚举
     * <功能详细描述>
     * 1:上传为图片, 2:声音，3：视频  4 : 图片和声音 5:缩略图和视频
     */
    public int fileType = 1;
    
    /**
     * 是否来自社区私信
     */
    public boolean isFromMessage = false;
    
    /**
     * 私信接受者id
     */
    public String touid;
    
    /**
     * 私信内容
     */
    public String message;
    
    /**
     * 文件总长度
     */
    public long fileSize;
    
    /**
     * 下载任务的当前进度
     */
    public int uploadPercent;
    
    public Handler handler;
    
    public UploadFileTask uploadTask;
    
    /**
     * 增加一个标志来区分是否是重新上传此任务，默认为true
     */
    public boolean isRestart = true;
    
    ///////////////////////////////////////////
    
    /**
     * 上传等待
     */
    public static final int STATUS_WAITING = 10001;
    
    /**
     * 上传暂停
     */
    public static final int STATUS_PAUSE = 10002;
    
    /**
     * 上传中
     */
    public static final int STATUS_UPLOADING = 10003;
    
    /**
     * 上传失败
     */
    public static final int STATUS_FAILED = 10004;
    
    /**
     * 上传结束
     */
    public static final int STATUS_FINISHED = 10005;
    
    /**
     * view对象
     */
    public View view;
    
    /**
     * 数据库中的记录的消息id
     */
    public String msgId;
    
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
     * 上传视频的状态 见 STATUS_WAITING .....等状态
     */
    public int status;
    
    /**
     * 已经上传的的大小
     */
    public long size;
    
    /**
     * 上传视频的日期 yyyy-MM-dd
     */
    public long uploadTime;
    
    /**
     * 上传成功，是否发布成功 0 成功， 其他不成功
     */
    public int pubStatus = 0;
    
    /**
     * 上传成功后，服务器返回的文件名，需要在发布时携带
     */
    public String fileName;
    
    /**
     * 上传成功后，服务器返回的url，需要在发布时携带
     */
    public String fileUrl;
    
    /////////////////////////////////////////////
    
    /**
     * 错误信息
     */
    public String errMsg;
    
    /**
     * 上一次遍历上传任务集合的索引位置
     */
    public int prePosition;
    
    /**
     * 上传任务状态控制同步对象
     */
    public Object syncStateCtrl = new Object();
    
    public UploadItem()
    {
    }
    
    /**
     * 取消正在运行的任务
     */
    public void cancelTask()
    {
        
        // 如果正在上传，取消网络连接
        if (uploadTask != null)
        {
            uploadTask.cancelTask();
        }
        // 如果没有正在下载从数据库和本地文件中删除
        else
        {
            canceledCallback();
        }
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
        uploadPercent = 0;
        
        setStatus(STATUS_FAILED);
        
        sendMsgToUI(UPLOAD_ERROR, 0);
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
        
        if (fileSize != totalLength)
        {
            // 获取文件总长度
            fileSize = totalLength;
        }
        
        if (uploadPercent < precent)
        {
            uploadPercent = precent;
            isUploading = true;
            sendMsgToUI(UPLOAD_PROGRESS, uploadPercent);
        }
    }
    
    /**
     * 增量上传视频后的转码以及入库
     */
    private void sendUploadDoneRequest(String result)
    {
        LogX.getInstance().i("======UPLOAD_SCUUCSS=====", result);
        UploadFileResp resp = null;
        try
        {
            resp = new Gson().fromJson(result, UploadFileResp.class);
            if (null != resp)
            {
                resp.msgId = this.msgId;
            }
        }
        catch (Exception e)
        {
        }
        Message msg = handler.obtainMessage();
        msg.obj = resp;
        msg.what = UPLOAD_SCUUCSS;
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
        
        isUploading = true;
        
        setStatus(STATUS_UPLOADING);
        
        sendMsgToUI(UPLOAD_START, 0);
    }
    
    @Override
    public void taskSuccessCallback(Object obj)
    {
        isUploading = false;
        
        setStatus(STATUS_FINISHED);
        
        sendUploadDoneRequest((String) obj);
    }
    
    @Override
    public void canceledCallback()
    {
        isUploading = false;
        
        sendMsgToUI(UPLOAD_CANCEL, 0);
    }
    
    @Override
    public void pausedCallback()
    {
        isUploading = false;
        
        setStatus(STATUS_PAUSE);
        
        sendMsgToUI(UPLOAD_CANCEL, 0);
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
}