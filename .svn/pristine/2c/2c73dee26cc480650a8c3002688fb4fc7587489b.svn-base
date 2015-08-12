package com.xweisoft.wx.family.logic.download;

import java.util.Vector;

/**
 * <一句话功能简述>
 * 被观察对象
 * 
 * @author  administrator
 * @version  [版本号, 2013-11-14]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public final class DownloadObservable
{
	/**
	 * 单例模式对象
	 */
	private static DownloadObservable instance;

	/**
	 * 观察者向量
	 */
	private Vector<DownloadObserver> obs;

	/**
	 * 用0个观察者构造一个被观察者    
	 */
	private DownloadObservable()
	{
		obs = new Vector<DownloadObserver>();
	}

	/**
	 * 获取该类实例方法
	 * @return    单例对象
	 */
	public synchronized static DownloadObservable getInstance()
	{
		if (instance == null)
		{
			instance = new DownloadObservable();
		}
		return instance;
	}

	/** 
	* 将一个观察者加到观察者列表上面。 
	*/
	public synchronized void addObserver(DownloadObserver o)
	{
		// 判空操作
		if (o == null || obs == null)
		{
			return;
		}

		if (!obs.contains(o))
		{
			obs.addElement(o);
		}
	}

	/** 
	* 将一个观察者对象从观察者列表上删除。 
	*/
	public synchronized void deleteObserver(DownloadObserver o)
	{
		// 判空操作
		if (o == null || obs == null || obs.isEmpty())
		{
			return;
		}

		if (obs.contains(o))
		{
			obs.removeElement(o);
		}
	}

	/** 
	* 如果本对象有变化（那时hasChanged 方法会返回true） 
	* 调用本方法通知所有登记在案的观察者，即调用它们的update()方法， 
	* arg作为参量。 
	*/
	public void notifyObservers(Object arg)
	{
		//临时存放当前的观察者的状态。参见备忘录模式。	
		synchronized (this)
		{
			for (int i = obs.size() - 1; i >= 0; i--)
			{
				((DownloadObserver) obs.elementAt(i)).update(arg);
			}
		}
	}
}