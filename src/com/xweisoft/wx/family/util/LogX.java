package com.xweisoft.wx.family.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.xweisoft.wx.family.logic.global.GlobalConstant;
import com.xweisoft.wx.family.logic.global.GlobalVariable;

/**
 * <一句话功能简述>
 * 日志类
 * 
 * @author  administrator
 * @version  [版本号, 2013-10-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public final class LogX extends Thread
{
	/**
	 * 保存路径
	 */
	public static final String DEFAULT_LOG_PATH = GlobalConstant.LOG_PATH_SD;
	   
    /**
     * 日志名
     */
    private static final String TAG = "===LogX===";

    /**
     * 写入的文件名
     */
    private static final String FILENAME = "log.txt";

    /**
     * 日志最大条数，超过该条数时，增加日志会先删除队首日志
     */
    private static final long MAXLOGSIZE = 1024 * 1024 * 3;
	
    /**
     * 实例引用
     */
    private static LogX instance;
    
	/**
	 * 保存路径
	 */
    private String logPath = DEFAULT_LOG_PATH;
	
	/**
	 * 写文件任务的列表
	 */
	private Queue<String> lstStorageTask;

	/**
	 * 写文件线程是否正在运行
	 */
	private boolean isRunnig;

	/**
	 * 需要随机写入的文件流对象
	 */
	private RandomAccessFile randomAccessFile = null;
	
	private File file = null;
	
	/**
     * 是否记录系统正常日志  true 记录， false 不记录
     */
    public static boolean infoFlag = false;
	
    private static LogX inst = null;
    
    /**
     * 构造函数      
     */
    private LogX()
    {
        this(DEFAULT_LOG_PATH);
    }
    
    /**
     * 构造函数  
     */
    private LogX(String path)
    {
        isRunnig = true;
        logPath = path;
        lstStorageTask = new LinkedList<String>();
        openFile();
    }
    
    /**
     * 构造函数  
     * @param flag true 记录日志  false 不记录
     */
    private LogX(boolean flag)
    {
        isRunnig = flag;
    }
	
    /**
     * [一句话功能简述]<BR>
     * 根据infoFlag true 记录系统日志， false 不记录
     * @return 日志实例
     */
    public static LogX getInstance()
    {
        if (infoFlag)
        {
            inst = getNewInstance();
        }
        else
        {
            if (null == inst)
            {
                inst = new LogX(infoFlag);
            }
        }
        return inst;
    }
    
    /**
     * 获取日志实例<BR>
     * @return 日志实例
     */
	public static LogX getNewInstance()
	{
		if (instance != null)
		{
			return instance;
		}
		
		synchronized (LogX.class)
		{
			if (instance == null)
			{
				instance = new LogX();
				if (GlobalVariable.isAllowWriteLogFile)
				{
					instance.start();
				}
			}
			
			if (GlobalVariable.isAllowWriteLogFile && !instance.isAlive())
			{
				instance.interrupt();
				instance = new LogX();
				instance.start();
			}
		}

		return instance;
	}

	/**
	 * 如果sdcard拔出，则停止记录日志
	 */
	public void stopLog()
	{
	    try
	    {
	        isRunnig = false;
	        if (instance != null)
	        {
	            instance.interrupt();
	        }
	        closeFile();
	    }
	    catch (Exception e)
	    {
	        Log.i(TAG, "stop the write log thread.");
	    }
	}
	
	/**
	 * 记录info日志<BR>
	 * @param tag 日志标示
	 * @param message 信息
	 */
	public void i(String tag, String message)
	{
		message = formatMessage(message);
		Log.i(tag, message);
		trace(tag, message);
	}
	
	/**
	 * 记录error日志<BR>
     * @param tag 日志标示
     * @param message 信息
	 */
	public void e(String tag, String message)
	{
		message = formatMessage(message);
		Log.e(tag, message);
		trace(tag, message);
	}
	
	/**
	 * 记录debug日志<BR>
     * @param tag 日志标示
     * @param message 信息
	 */
	public void d(String tag, String message)
	{
		message = formatMessage(message);
		Log.d(tag, message);
		trace(tag, message);
	}
	
	/**
	 * 记录verbose日志<BR>
     * @param tag 日志标示
     * @param message 信息
	 */
	public void v(String tag, String message)
	{
		message = formatMessage(message);
		Log.v(tag, message);
		trace(tag, message);
	}
	
	/**
	 * 记录warning日志<BR>
     * @param tag 日志标示
     * @param message 信息
	 */
	public void w(String tag, String message)
	{
		message = formatMessage(message);
		Log.w(tag, message);
		trace(tag, message);
	}
	
	/**
	 * 获取日志，如果为null，返回空指针日志<BR>
	 * [功能详细描述]
	 * @param message 日志信息
	 * @return 日志信息
	 */
	private String formatMessage(String message)
	{
		if (message == null)
		{
			message = "java.lang.NullPointerException.";
		}
		return message;
	}
	
	/**
	 * 向存储线程中添加写文件任务
	 * @param log    日志信息
	 */
	private void trace(String tag, String message)
	{
		if (isRunnig)
		{
			synchronized (lstStorageTask)
			{
				if (needClearLogs())
				{
					deleteFile();
					closeFile();
					openFile();
				}
				
				lstStorageTask.add(tag + "---||  " + message + "\n");
				lstStorageTask.notify();
			}
		}
	}

	/**
	 * 线程执行体
	 */
	public void run()
	{
		while (isRunnig)
		{
			try
			{
				if (lstStorageTask == null)
				{
					Log.i(TAG, "In storage thread the lstStorageTask is null.");
					break;
				}

				String dataBlock = null;
				synchronized (lstStorageTask)
				{
					// 如果当前没有要写的数据，等待...
					if (lstStorageTask.isEmpty())
					{
						lstStorageTask.wait();
					}

					// 如果有数据，取出要写的内容
					if (!lstStorageTask.isEmpty())
					{
						dataBlock = (String) lstStorageTask.poll();
					}
				}

				// 执行写文件的操作
				if (dataBlock != null)
				{
					writeFile(dataBlock.getBytes());
				}
			}
			catch (InterruptedException e)
			{
				// 终止写文件线程
				Log.i(TAG, "The write file thread is closed.");
				isRunnig = false;
				break;
			}
		}
	}

	/**
	 * 向SD卡中存储文件时出错处理
	 */
	private void writeLogError()
	{
		// 置位写文件线程运行标志
		isRunnig = false;

		// 关闭文件流
		closeFile();

		// 清空写文件任务
		clearLogTaskList();
	}

	/**
	 * 关闭文件对象 
	 */
	private void closeFile()
	{
		try
		{
			if (randomAccessFile != null)
			{
				randomAccessFile.close();
			}
		}
		catch (IOException e)
		{
			Log.e(TAG, e.getMessage());
		}
		finally
		{
			randomAccessFile = null;
		}
	}

	/**
	 *  清空写文件任务
	 */
	private void clearLogTaskList()
	{
		synchronized (lstStorageTask)
		{
			// 先清空所有的任务对象
			Iterator<String> tasks = lstStorageTask.iterator();
			while (tasks.hasNext())
			{
				String task = tasks.next();
				if (task != null)
				{
					// 清空内存
					task = null;
				}
			}
			// 清空任务列表
			lstStorageTask.clear();
		}
	}

	/**
	 * 是否需要清除<BR>
	 * @return 是否需要清除
	 */
	private boolean needClearLogs()
	{
		if (file.length() >= MAXLOGSIZE)
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * 构造文件对象    
	 */
	private void openFile() 
	{
		File dir = new File(logPath);
		if (!dir.exists())
		{
			dir.mkdirs();
		}
		
		createFile();
	}
	
	/**
	 * 删除文件    
	 */
	private void deleteFile()
	{
		if (file.exists())
		{
			try
			{
				if (file.delete())
				{
					Log.i(TAG, "Delete log file success");
				}
				else
				{
					Log.e(TAG, "Delete log file failed");
				}
			}
			catch (Exception e)
			{
				Log.e(TAG, e.getMessage());
			}
		}
	}
	
	/**
	* 通过提供的文件名在默认路径下生成文件
	* @param fileName     文件的名称
	* @return 生成的文件
	*/
	private void createFile()
	{
		if (file == null)
		{
			file = new File(logPath + FILENAME);
		}
		
		if (!file.exists())
		{
			try
			{
				file.createNewFile();
				closeFile();
			}
			catch (IOException e)
			{
				Log.e(TAG, e.getMessage());
			}
			
			createRandomAccessFile();
		}
		else if (randomAccessFile == null)
		{
			createRandomAccessFile();
		}
	}
	
	/**
	 * 创建RandomAccessFile
	 */
	private void createRandomAccessFile()
	{
		try
		{
			randomAccessFile = new RandomAccessFile(file, "rw");
			
			if (randomAccessFile == null)
			{
				Log.e(TAG, "initial LogX file error.");
			}
		}
		catch (Exception e)
		{
			closeFile();
			Log.e(TAG, e.getMessage());
		}
	}
	
	private void writeFile(byte[] io)
	{
		if (io != null)
		{
			if (randomAccessFile != null)
			{
				try
				{
					createFile();
					randomAccessFile.seek(randomAccessFile.length());
					randomAccessFile.write(io);
				}
				catch (IOException e)
				{
					Log.e(TAG, e.getMessage());
					String state = Environment.getExternalStorageState();
			        if (!state.equals(Environment.MEDIA_MOUNTED) || io.length >= getAvailableStore()) 
			        {
			        	writeLogError();
			        }
				}
			}
			else
			{
				String state = Environment.getExternalStorageState();
				if (!state.equals(Environment.MEDIA_MOUNTED) || io.length >= getAvailableStore()) 
		        {
		        	writeLogError();
		        }
			}
		}
	}
	
    private long getAvailableStore() 
    {

        // 取得sdcard文件路径
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory()
				.getAbsolutePath());

        // 获取block的SIZE
        long blocSize = statFs.getBlockSize();

        // 获取BLOCK数量
        //long totalBlocks = statFs.getBlockCount();
        // 可使用的Block的数量
        long availaBlock = statFs.getAvailableBlocks();
        
        //long total = totalBlocks * blocSize;
        long availableSpare = availaBlock * blocSize;
        
        return availableSpare;

}
}