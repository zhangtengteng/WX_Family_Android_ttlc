package com.xweisoft.wx.family.service.threadpool;

import com.xweisoft.wx.family.util.LogX;


/**
 * <一句话功能简述>
 * 线程对象实现类
 * 
 * @author  adminitrator
 * @version  [版本号, 2013-5-6]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class TaskThread extends Thread
{
	/**
	 * 日志标志
	 */
	private static final String TAG = "TaskThread";

	/**
	 * 在线程中执行的任务对象
	 */
	private TaskQueue taskQueue = null;

	/**
	 * 调用interrupt()时是否需要终止线程，如果用户取消任务或任务超时不
	 * 需要终止线程
	 */
	private boolean isTerminate = false;

	/**
	 * 构造函数
	 * @param taskQueue    线程所在的线程池对象  
	 */
	protected TaskThread(TaskQueue taskQueue)
	{
		super();

		this.taskQueue = taskQueue;
	}

	/**
	 * 线程执行体函数
	 */
	public void run()
	{
		// 增加空闲线程的计数
		taskQueue.increaseIdleCount(this);
		

		while (true)
		{
			TaskHandleImpl task = null;
			try
			{
				// 如果当前线程个数超过了最大线程数，需要关闭当前线程
				if (taskQueue.getThreadCount() > taskQueue.getMaxThreadCount())
				{
					LogX.getInstance().i(TAG,"Currently running thread already has more " 
					                    + " than the maximum number of threads");
					break;
				}
				else
				{
					task = taskQueue.obtainTask(this);
				}
			}
			catch (InterruptedException e)
			{
				LogX.getInstance().e(TAG, "::run:Exception occur while obtainTask ："
						+ e.toString());
				break;
			}

			if (task != null)
			{
				// 减小空闲线程的计数
				taskQueue.decreaseIdleCount(this);

				// 执行任务
				try
				{
					TaskObject object = task.getTaskObject();
					if (object != null)
					{
						// 调用任务回调接口启动超时定时器
						object.startTimeoutTimer();

						// 执行任务
						object.runTask();

						// 设置任务的状态为执行完毕状态
						task.setState(TaskHandleImpl.TASK_STATE_FINISHED);

						// 增加空闲线程的计数
						taskQueue.increaseIdleCount(this);

						// 通知任务请求者任务执行完毕
						object.onTaskResponse(TaskObject.RESPONSE_SUCCESS);

						task.cancel();

						task = null;
					}
				}
				catch (InterruptedException e)
				{
					LogX.getInstance().e(TAG, "::run: While task running , user terminate the task "
									+ e.toString());
					if (getIsTerminate())
					{
						break;
					}
					else
					{
						// 增加空闲线程的计数
						taskQueue.increaseIdleCount(this);
						continue;
					}
				}
			}
			else
			{
				LogX.getInstance().i(TAG, "::run: Access to the task is empty");
				break;
			}
		}

		// 减小空闲线程的计数
		taskQueue.decreaseIdleCount(this);

		// 从线程队列中删除线程
		taskQueue.deleteThread(this);
		LogX.getInstance().i(TAG, "::run: Thread is terminate!");
	}

	/**
	 * 调用interrupt()时是否需要终止线程
	 * @param isTerminate    设置是否终止线程
	 */
	public void setTerminate(boolean isTerminate)
	{
		synchronized (this)
		{
			this.isTerminate = isTerminate;
		}
	}

	/**
	 * 判断是否需要终止线程
	 * @return    线程收到中断异常时是否关闭线程的标志
	 */
	public synchronized boolean getIsTerminate()
	{
		return isTerminate;
	}
}
