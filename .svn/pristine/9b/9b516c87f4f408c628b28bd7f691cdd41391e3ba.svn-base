package com.xweisoft.wx.family.service.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.json.JSONException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.xweisoft.wx.family.service.threadpool.TaskQueue;
import com.xweisoft.wx.family.util.LogX;

/**
 * <一句话功能简述>
 * 获取图片任务类
 * 
 * @author  administrator
 * @version  [版本号, 2013-10-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class BitmapTask extends ConnectionTask
{
    /**
     * 日志名
     */
    static final String TAG = "BitmapTask";
    
	/**
	 * 订购列表图标请求队列线程数
	 */
	private static final int MAX_ICON_REQ_COUNT = 1;
	
	/**
	 * 订购列表图标请求队列
	 */
	private static TaskQueue bitmapQueue;
	
	/**
	 * 请求图片构造方法
	 * @param context 上下文
	 * @param callback     JSON或bitmap回调接口   
	 * @param httpUrl     连接地址
	 */
	public BitmapTask(final Context context, IHttpCallback callback, 
			String httpUrl)
	{
		super(context, httpUrl, callback);
		this.requestType = GET;
		if (bitmapQueue == null)
		{
			bitmapQueue = new TaskQueue(MAX_ICON_REQ_COUNT);
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
		
		readBitmapData();
	}
	
	/**
	 *  读取图片数据
	 */
	private void readBitmapData()
	{
		Bitmap bitmap = null;
		if (is != null)
		{
			// 取出图片
			bitmap = BitmapFactory.decodeStream(is);
			try
			{
				is.close();
			}
			catch (IOException e)
			{
				LogX.getInstance().e(TAG, "::readBitmapData: " + e.toString());
			}
			if (httpCallback != null)
			{
				((IBitmapCallback)httpCallback).onReceiveData(bitmap);
			}
		}
	}
	
	/**
	 * 发送请求
	 */
	@Override
	public void sendTaskReq() 
	{
		if (bitmapQueue != null)
		{
			bitmapQueue.addTask(this);
		}
	}
}
