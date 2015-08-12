package com.xweisoft.wx.family.service.http;

import android.content.Context;

/**
 * <一句话功能简述>
 * 联网回调接口
 * 
 * @author  administrator
 * @version  [版本号, 2013-10-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface IDownloadCallback
{

	/**
	 * 设置进度变化回调函数
	 * @param getLength      下载大小
	 * @param totalLength    文件大小
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
	 * 进入等待状态回调
	 */
	public void waitingCallback();
	
	/**
	 * 开始下载任务通知回调
	 */
	public void startDownloadCallback();

	/**
	 * 下载完成回调通知接口
	 * @param context 上下文
	 */
	public void successDownloadCallback(final Context context);

}