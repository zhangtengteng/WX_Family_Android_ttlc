package com.xweisoft.wx.family.logic.request;

import android.content.Context;
import android.os.Handler;

import com.xweisoft.wx.family.WXApplication;
import com.xweisoft.wx.family.logic.global.HttpAddressProperties;
import com.xweisoft.wx.family.logic.global.NetWorkCodes.CommontErrorCodes;
import com.xweisoft.wx.family.service.http.ConnectionLogic;
import com.xweisoft.wx.family.service.http.ConnectionTask;
import com.xweisoft.wx.family.service.http.IHttpCallback;

/**
 * <一句话功能简述>
 * JSON格式数据封装类
 * 
 * @author  administrator
 * @version  [版本号, 2013-10-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public abstract class Request implements IHttpCallback
{
    /**
     * 请求方法
     */
    public static final int METHOD_HTTP = 1;
    
    /**
     *  请求方法
     */
    public static final int METHOD_HTTPS = 2;
    
    /**
     * 网络超时时间
     */
    protected static final int MAX_CONNECTION_TIMEOUT = 30000;
    
    /**
     * 引擎ID
     */
    protected String engineId = null;
    
    /**
     * 版本号
     */
    protected String engineVersion = "1.01.00";
    
    /**
     * 请求地址
     */
    protected String httpUrl = null;
    
    /**
     * 消息类型
     */
    protected String reqType = null;
    
    /**
     * 消息序列号
     */
    protected String sequenceId = null;
    
    /**
     * 用户ID
     */
    protected String userId = null;
    
    /**
     * 回调句柄 
     */
    protected Handler handler;
    
    protected Context context;
    
    /**
     * 默认的构造方法
     * @param context 上下文
     * @param handler 处理类
     * @param httpUrl 请求地址
     */
    public Request(final Context context, Handler handler, String httpUrl)
    {
        this(context, handler, httpUrl, METHOD_HTTP);
    }
    
    /**
     * 默认的构造方法
     * @param context 上下文
     * @param handler 处理类
     * @param httpUrl 请求地址
     */
    public Request(final Context context, Handler handler, String httpUrl,
            boolean flag)
    {
        this.context = context;
        this.handler = handler;
        if (flag)
        {
            this.httpUrl = httpUrl;
        }else {
            this.httpUrl = "http://"
                    + WXApplication.getInstance().getRequestUrl()
                    + HttpAddressProperties.PATH_PRE + httpUrl;
        }
    }
    
    /**
     * 构造器
     * @param context 上下文
     * @param handler 处理类
     * @param httpUrl 请求地址
     * @param methodType 请求方法
     */
    public Request(final Context context, Handler handler, String httpUrl,
            int methodType)
    {
        this.context = context;
        this.handler = handler;
        if (METHOD_HTTP == methodType)
        {
            this.httpUrl = "http://"
                    + WXApplication.getInstance().getRequestUrl()
                    + HttpAddressProperties.PATH_PRE + httpUrl;
        }
        else
        {
            this.httpUrl = "https://"
                    + WXApplication.getInstance().getRequestHttpsUrl()
                    + HttpAddressProperties.PATH_PRE + httpUrl;
        }
    }
    
    /**
     * 检查消息头
     * @throws Exception    抛出异常供调用者捕捉 
     */
    //	protected void checkHeader() throws Exception
    //	{
    //		if (httpUrl == null || engineId == null || reqType == null)
    //		{
    //			throw new Exception("Request: header parameter is error");
    //		}
    //	}
    
    /**
     * 发送请求
     */
    public final void sendHttpRequest()
    {
        ConnectionTask connectionTask = createConnectionTask();
        ConnectionLogic.getInstance().sendRequest(connectionTask);
    }
    
    /**
     * 构建链接任务
     * @return 对应请求的链接任务
     */
    protected abstract ConnectionTask createConnectionTask();
    
    /**
     * 超时处理
     * @param code     状态码
     * @param message  异常信息
     */
    public void onTimeOut(int code, String message)
    {
        // 显示错误
        if (this.handler != null)
        {
            handler.sendMessage(handler.obtainMessage(CommontErrorCodes.NETWORK_TIMEOUT_ERROR,
                    message));
        }
    }
    
    /**
     * 网络不可用处理
     * @param code    状态码
     * @param message 异常信息  
     */
    public void onConnError(int code, String message)
    {
        // 显示错误
        
        if (this.handler != null)
        {
            handler.sendMessage(handler.obtainMessage(CommontErrorCodes.NETWORK_ERROR,
                    message));
        }
    }
    
    /**
     * 其他错误回调函数
     * @param code     状态码
     * @param message  错误消息
     */
    public void onError(int code, String message)
    {
        
    }
}