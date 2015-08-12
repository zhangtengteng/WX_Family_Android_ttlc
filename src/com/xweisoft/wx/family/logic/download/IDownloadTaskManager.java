package com.xweisoft.wx.family.logic.download;

import android.content.Context;

import com.xweisoft.wx.family.logic.model.DownloadItem;

/**
 * 下载任务接口
 * <功能详细描述>
 * 
 * @author  administrator
 * @version  [版本号, 2013-11-13]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface IDownloadTaskManager
{

	/**
	 * 获取正在下载的任务数
	 * @param context 当前activity
	 * @return 当前正在下载的任务数
	 */
	public int getDownLoadingTaskCount(final Context context);

	/**
	 * 设置同时下载最大数，如果当前下载任务数大于设置的数。则将正在下载的任务置为等待状态
	 * @param context 当前activity
	 * @param count 设置为等待状态的任务数
	 */
	public void setOtherRunTask2Waiting(final Context context, int count);
	
	/**
	 * 关闭存储下载管理列表的数据库
	 */
	public void closeDB();

	/**
	 * 
	 * 更新数据
	 * @param context 上下文
	 * @param item 下载对象
	 */
	public void updateTaskToDB(final Context context, DownloadItem item);

	/**
     * 
     * 更新数据
     * @param context 上下文
     * @param item 下载对象
     */
    public void updateTaskToDBApp(final Context context, DownloadItem item);
	
	/**
	 * 
	 * 在应用管理里卸载应用，对应修改下载管理里的应用
	 * 
	 * @param context 上下文
     * @param item 下载对象
	 */
	public void removeTaskFromDB(final Context context, DownloadItem item);
	
	/**
	 * 根据在下载管理器中的索引位置获取下载任务对象
	 * @param context 上下文
	 * @param position 位置索引
	 * @return 下载任务对象
	 */
	public DownloadItem getItem(final Context context, int position);

	/**
	 * 根据在下载管理器中的下载任务对象获取下载任务对象的索引位置
	 * @param context 上下文
	 * @param item 下载任务对象
	 * @return 位置索引
	 */
	public int getItemPosition(final Context context, DownloadItem item);

	/**
	 * 获取下载管理器中的下载任务总数
	 * @param context 上下文
	 * @return 下载任务总数
	 */
	public int getTasksCount(final Context context);

	/**
	 * 初始化下载管理
	 * @param context 初始化数据库的上下文
	 */
	public void initDownLoadMgr(final Context context);

	/**
	 * 继续任务
	 * @param context 上下文
	 * @param item 下载列表
	 */
	public void onContinueTask(final Context context, DownloadItem item);

	/**
	 * 暂停任务
	 * 
	 * @param item 下载列表
	 */
	public void onPauseTask(DownloadItem item);
	
	/**
     * 暂停等待任务
     * 
     * @param item 下载列表
     */
    public void onPauseWaitingTask(DownloadItem item);

	/**
	 * 删除列表指定索引位置的任务
	 * @param context 上下文
	 * @param downLoadItem 下载对象
	 */
	public void removeTask(final Context context, DownloadItem downLoadItem);

	/**
	 * 重新下载任务
	 * @param context 上下文
	 * @param item 下载项
	 */
	public void redownloadTask(final Context context, DownloadItem item);

	/**
	 * 从缓存中删除下载项
	 * @param context 上下文
	 * @param downLoadItem 要删除的下载对象
	 */
	public void removeTaskFromList(final Context context,
			DownloadItem downLoadItem);
}
