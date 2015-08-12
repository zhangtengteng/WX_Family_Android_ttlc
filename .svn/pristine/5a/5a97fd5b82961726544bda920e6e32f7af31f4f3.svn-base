package com.xweisoft.wx.family.service.threadpool;

import java.util.TimerTask;


/**
 * <一句话功能简述>
 * 请求任务接口
 * 
 * @author  administrator
 * @version  [版本号, 2013-5-6]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface TaskObject
{
	/**
	 * 任务请求响应通知码    任务执行成功
	 */
	public static final int RESPONSE_SUCCESS = 0;

	/**
	 * 任务请求响应通知码    任务超时（未执行）
	 */
	public static final int RESPONSE_TIMEOUT_ONRUN = 1;

	/**
	 * 任务请求响应通知码    任务超时（正在执行）
	 */
	public static final int RESPONSE_TIMEOUT_RUNNING = 2;

	/**
	 * 任务执行体接口方法
	 * @throws InterruptedException    抛出中断异常供调用者捕捉 
	 */
	public void runTask() throws InterruptedException;

	/**
	 * 任务请求响应回调接口
	 * @param code    任务请求响应通知码 
	 */
	public void onTaskResponse(int code);
	
	/**
	 * 任务取消的回调接口方法，供线程管理模块调用
	 */
	public void onCancelTask();
	
	/**
	 * 设置任务的超时定时器任务对象
	 * @param timeoutTask    定时器任务对象  
	 */
	public void setTimeoutTask(TimerTask timeoutTask);

	/**
	 * 启动超时定时器
	 */
	public void startTimeoutTimer();

	/**
	 * 停止超时定时器
	 */
	public void stopTimeoutTimer();
	
}
