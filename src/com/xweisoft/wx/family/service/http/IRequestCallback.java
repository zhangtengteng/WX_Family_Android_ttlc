package com.xweisoft.wx.family.service.http;

/**
 * <一句话功能简述>
 * 状态回调接口用户超时
 * 
 * @author  administrator
 * @version  [版本号, 2013-10-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface IRequestCallback
{
	
	/**
	 * 接收网络层返回的数据的回调接口函数
	 * @param data 需要解析处理的数据
	 */
	public void onReceiveData(byte[] data);
}
