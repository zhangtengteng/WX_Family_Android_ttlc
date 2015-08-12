package com.xweisoft.wx.family.mina.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.xweisoft.wx.family.mina.service.NotificationService;
import com.xweisoft.wx.family.util.LogX;

/**
 * 网络连接状态广播接收器
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-6-3]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ConnectivityReceiver extends BroadcastReceiver
{
    private static final String LOGTAG = "===ConnectivityReceiver===";
    
    /**
     * mina service
     */
    private NotificationService notificationService;
    
    private boolean isConnected = false;
    
    public ConnectivityReceiver(NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        LogX.getInstance().d(LOGTAG, "ConnectivityReceiver.onReceive()...");
        String action = intent.getAction();
        LogX.getInstance().d(LOGTAG, "action=" + action);
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        
        if (networkInfo != null)
        {
            LogX.getInstance().d(LOGTAG,
                    "Network Type  = " + networkInfo.getTypeName());
            LogX.getInstance().d(LOGTAG,
                    "Network State = " + networkInfo.getState());
            if (networkInfo.isConnected() && !isConnected)
            {
                isConnected = true;
                LogX.getInstance().i(LOGTAG, "Network connected");
                notificationService.connect();
            }
        }
        else
        {
            LogX.getInstance().e(LOGTAG, "Network unavailable");
            //暂时先不断开网络
            notificationService.disconnect();
            isConnected = false;
        }
    }
    
}
