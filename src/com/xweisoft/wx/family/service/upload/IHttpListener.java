package com.xweisoft.wx.family.service.upload;

/**
 * <一句话功能简述>
 * 联网回调接口
 * 
 * @author  administrator
 * @version  [版本号, 2013-5-16]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface IHttpListener
{
    
    public static final int HTTP_TIMEOUT = 501;
    
    public static final int HTTP_SER_ERROR = 500;
    
    public static final int HTTP_FAILED = 502;
    
    public static final int HTTP_IMAGE_NOTFOUND = 404;
    
    public static final int HTTP_XML_SUCCESS = 209;
    
    public static final int HTTP_IMAGE_SUCESS = 210;
    
    /**
     * 任务失败回调函数
     * 
     * @param code
     *            状态码
     * @param message
     *            错误消息
     */
    public void onError(int code, String message);
    
    /**
     * 设置进度变化回调函数
     * 
     * @param getLength
     *            下载大小
     * @param totalLength
     *            文件大小
     */
    public void onProgressChanged(long getLength, long totalLength);
    
    /**
     * 用户取消回调 函数
     */
    public void canceledCallback();
    
    /**
     * 用户取消回调 函数
     */
    public void pausedCallback();
    
    /**
     * 开始任务通知回调
     */
    public void taskStartCallback();
    
    /**
     * 任务完成回调通知接口
     */
    public void taskSuccessCallback(Object obj);
    
}