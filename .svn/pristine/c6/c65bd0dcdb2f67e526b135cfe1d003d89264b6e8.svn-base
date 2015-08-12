package com.xweisoft.wx.family.service.http;

/**
 * <一句话功能简述>
 * http请求的回调接口
 * 
 * @author  administrator
 * @version  [版本号, 2013-10-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface IHttpCallback 
{

	/**
	 * 其他错误回调函数
	 * @param code     状态码
	 * @param message  错误消息
	 */
	public void onError(int code, String message);
	
	/**
	 * 超时处理
	 * @param code    状态码
	 * @param message 异常信息
	 */
	public void onTimeOut(int code, String message);
	
	/**
	 * 网络不可用 
	 * @param code    状态码
	 * @param message 异常信息 
	 */
	public void onConnError(int code, String message);
}
