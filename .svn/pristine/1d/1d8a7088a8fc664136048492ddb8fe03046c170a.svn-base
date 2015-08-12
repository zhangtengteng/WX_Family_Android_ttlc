package com.xweisoft.wx.family.service.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.json.JSONException;

import android.content.Context;

import com.xweisoft.wx.family.service.threadpool.TaskObject;
import com.xweisoft.wx.family.service.threadpool.TaskQueue;
import com.xweisoft.wx.family.util.LogX;

/**
 * <一句话功能简述>
 * 请求任务类
 * 
 * @author  administrator
 * @version  [版本号, 2013-10-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class RequestTask extends ConnectionTask
{
	/**
	 * 最大任务请求数
	 */
	private static final int MAXCOUNT = 2;
	
	/**
	 * 连接队列
	 */
	private static TaskQueue requestQueue;

	/**
	 * 连接商城服务器构造方法
	 * @param context 上下文
     * @param callback 回调接口
     * @param httpUrl 请求地址  
     * @param timeout  超时时间
	 */
	public RequestTask(final Context context, IHttpCallback callback, String httpUrl, 
			int timeout)
	{
		super(context, httpUrl, callback);
		this.timeout = timeout;
		if (requestQueue == null)
		{
			requestQueue = new TaskQueue(MAXCOUNT);
		}
	}
	
	/**
	 * 读网络数据
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
	    long fileLenght = 0;
	    if (isSDKAbove9())
	    {
	        fileLenght = (long) conn.getContentLength();
	    }
	    else
	    {
	        fileLenght = (long) entity.getContentLength();
	    }
		readUserFormatData(fileLenght);
	}
	
	/**
	 * 读取用户请求响应数据
	 * @param dataLen    数据长度
	 * @throws Exception             异常类
     * @throws InterruptedException    中断异常类
     * @throws UnsupportedEncodingException 编码的异常
     * @throws JSONException json的异常
	 */
	private void readUserFormatData(long dataLen) throws Exception,
			InterruptedException, UnsupportedEncodingException, JSONException
	{
		ByteArrayOutputStream bos = null;
		try
		{
			bos = new ByteArrayOutputStream();
			int ch = 0;
			byte[] data = null;
			byte[] d = new byte[DATA_BUFFER_LEN];
			while ((ch = is.read(d)) != -1)
			{
				if (canceled)
				{
					data = bos.toByteArray();
					bos.close();
					throw new InterruptedException();
				}
				bos.write(d, 0, ch);

				if (canceled)
				{
					data = bos.toByteArray();
					bos.close();
					throw new InterruptedException();
				}
			}
			data = bos.toByteArray();
			
			if (httpCallback != null)
			{
				((IRequestCallback)httpCallback).onReceiveData(data);
			}
		}
		finally
		{
			if (bos != null)
			{
				bos.close();
				bos = null;
			}
		}

	}
	
	/**
	 * 任务请求响应回调接口
	 * @param code    响应通知码
	 */
	public void onTaskResponse(int code)
	{
	    if (code == TaskObject.RESPONSE_TIMEOUT_RUNNING)
		{
			LogX.getInstance().i(TAG, "::onTaskResponse: responseCode "
					+ responseCode + " TIMEOUT");
			
			setTimeOut(responseCode, "TIMEOUT");
			
			isTimeOut = true;
			clearNet();
		}
	}

	/**
	 * 发送请求
	 */
	@Override
	public void sendTaskReq() 
	{
		if (requestQueue != null)
		{
			requestQueue.addTask(this);
		}
	}
}
