package com.xweisoft.wx.family.service.threadpool;

import com.xweisoft.wx.family.util.LogX;



/**
 * <一句话功能简述>
 * 任务控制接口实现类，类似于任务的句柄
 * 
 * @author  administrator
 * @version  [版本号, 2013-5-6]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class TaskHandleImpl implements TaskHandle
{
	/**
	 * 文件日志标志
	 */
	private static final String TAG = "TaskHandleImpl";

	/**
	 * 任务执行线程组管理对象
	 */
	private TaskQueue taskQueue = null;

	/**
	 * 任务控制对象所对应的任务对象
	 */
	private TaskObject taskObject = null;

	/**
	 * 执行当前任务的线程对象
	 */
	private TaskThread taskThread = null;

	/**
	 * 存储任务的当前状态变量
	 */
	private int state = TASK_STATE_INITIALIZE;

	/**
	 * 构造函数
	 * @param taskQueue     线程池对象
	 * @param taskObject    任务对象    
	 */
	protected TaskHandleImpl(TaskQueue taskQueue, TaskObject taskObject)
	{
		super();

		this.taskQueue = taskQueue;
		this.taskObject = taskObject;
	}

	/**
	 * 取消任务
	 * @return    是否取消成功标志
	 */
	public boolean cancel()
	{

		boolean ret = false;
		if (taskQueue == null)
		{
			LogX.getInstance().i(TAG, "::init: In TaskHandleImpl the taskQueue is  null. ");
			return ret;
		}
		// 停止任务超时定时器
		if (taskObject != null)
		{
			taskObject.stopTimeoutTimer();
		}

		switch (state)
		{
		case TASK_STATE_WAITING: // 任务还在排队等待执行
		{
			ret = taskQueue.removeTask(this);
			taskObject.onCancelTask();
			break;
		}
		case TASK_STATE_RUNNING: // 任务已经获取线程资源正在执行
		{
			ret = taskQueue.terminateTaskRunning(taskThread, this);
			taskObject.onCancelTask();
			ret = true;
			break;
		}
		case TASK_STATE_CANCEL: // 任务在任务队列中时已经被取消
		{
			ret = true;
			break;
		}
		case TASK_STATE_FINISHED: // 任务已经执行完毕
		{
			break;
		}
		default:
		{
			break;
		}
		}
		// 释放任务对象，不然StartWidget会有内存泄漏
		taskObject = null;

		return ret;
	}

	/**
	 * 根据任务句柄获取任务对象
	 * @return    返回任务对象
	 */
	public TaskObject getTaskObject()
	{
		return taskObject;
	}

	/**
	 * 获取执行任务的线程
	 * @return    执行该任务的线程对象
	 * 备注: 只有在任务执行期间调用该方法有效
	 */
	protected TaskThread getTaskThread()
	{
		return taskThread;
	}

	/**
	 * 设置用来执行该任务的线程
	 * @param taskThread    执行该任务的线程对象
	 */
	protected void setTaskThread(TaskThread taskThread)
	{
		this.taskThread = taskThread;
	}

	/**
	 * 设置任务的状态
	 * @param state    要设置的状态码 
	 */
	protected void setState(int state)
	{
		this.state = state;
	}

	/**
	 * 获取任务的状态
	 * @return    任务的状态
	 */
	public int getState()
	{
		return state;
	}

}
