package com.xweisoft.wx.family.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.widget.Toast;

import com.xweisoft.wx.family.R;

/**
 * <一句话功能简述>
 * 判断是否连接网络
 * <功能详细描述>
 * 
 * @author  houfangfang
 * @version  [版本号, 2013-10-31]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class NetworkState
{
    /**
     * Description: 是否连接网络
     * 
     * @param context
     * @return
     * @see
     */
    public static boolean isConnect(Context context)
    {
        
        boolean ret = false;
        // 获取手机所有连接管理对象（包括对wi-fi,3G等连接的管理）
        
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            State mobile = null;
            // mobile 3G Data Network
            if (connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null)
            {
                mobile = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                        .getState();
            }
            // wifi
            State wifi = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                    .getState();
            // 有网络状态
            if (mobile == State.CONNECTED || wifi == State.CONNECTED)
            {
                ret = true;
            }
            // 本地网络没有开启
            else
            {
                ret = false;
                Toast.makeText(context,
                        R.string.network_error,
                        Toast.LENGTH_SHORT).show();
            }
        }
        return ret;
    }
    
}
