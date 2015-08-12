package com.xweisoft.wx.family.logic.download;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.xweisoft.wx.family.logic.global.GlobalConstant;
import com.xweisoft.wx.family.logic.global.GlobalVariable;
import com.xweisoft.wx.family.logic.model.DownloadItem;
import com.xweisoft.wx.family.util.LogX;

/**
 * <一句话功能简述>
 * 应用安装的Handler类
 * 
 * @author  administrator
 * @version  [版本号, 2013-11-13]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class DownloadInstallHandler extends Handler
{
	private static final String TAG = "===InstallHandler===";

	private static DownloadInstallHandler instance;

	/**
	 * 获取实例
	 * @return 实例
	 */
	public static DownloadInstallHandler getInstance()
	{
		if (instance == null)
		{
			instance = new DownloadInstallHandler();
		}
		return instance;
	}

	@Override
	public void handleMessage(Message msg)
	{
		Bundle bundle = msg.getData();

		switch (msg.what)
		{

		// 安装应用
		case GlobalConstant.INSTALL_SUCESS:
			String packageName = bundle.getString("packageName");
			if (null == packageName)
			{
				return;
			}
			LogX.getInstance().e(TAG, "install success, update data start PackageName" + packageName);
			// 安装成功
            DownloadTaskManager manager = DownloadTaskManager.getInstance();
            
            DownloadItem downloadItem = new DownloadItem();
		    downloadItem.status = DownloadItem.STATUS_INSTALL_SUCCESS;
//		    downloadItem.packageName = packageName;
//		    //安装时，该参数是下载完成状态
//		    downloadItem.installParam = DownloadItem.STATUS_DOWNLOAD_FINISH + "";
//			//将数据库中的包名相同的记录状态更新
//			manager.updateTaskToDBApp(GlobalVariable.currentActivity, downloadItem); 
			
			//刷新downloaded列表页面中的数据记录状态
//			if (null != GlobalVariable.downloadedAppLock)
//			{
//			    //刷新缓存数据
//			    GlobalVariable.downloadedAppLock.changeGlobalList(GlobalVariable.currentActivity, 
//			                                                        0, downloadItem);
//			   
//			    //刷新当前页面的记录
//			    if (null != GlobalVariable.downloadedListAdapter)
//                {
//                    GlobalVariable.downloadedListAdapter.notifyDataSetChanged();
//                }
//			}
			
            LogX.getInstance().e(TAG, "=============install success, update data end PackageName======" + packageName);
			break;
		case GlobalConstant.DOWNLOADSUCESSPUSHLIST:
    		//刷新当前页面的记录
//            if (null != GlobalVariable.downloadedListAdapter)
//            {
//                GlobalVariable.downloadedListAdapter.notifyDataSetChanged();
//            }
    		break;
		default:
			break;
		}
		super.handleMessage(msg);
	}
}
