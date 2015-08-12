package com.xweisoft.wx.family.service.exception;

/**
 * <一句话功能简述>
 * 手机空间内存和SD卡剩余空间均不足的异常
 * 
 * @author  administrator
 * @version  [版本号, 2013-10-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@SuppressWarnings("serial")
public class NotEnouchSpaceException extends Exception
{
	private String msg = null;
	
	/**
	 * 构造方法
	 * @param msg  异常信息   
	 */
	public NotEnouchSpaceException(String msg)
	{
		this.msg = msg;
	}

	/**
	 * 重写的方法，获取异常信息
	 * @return    异常信息
	 */
	@Override
	public String getMessage()
	{
		return msg;
	}
	
	
}
