package com.xweisoft.wx.family.mina.listener;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.xweisoft.wx.family.mina.service.NotificationService;
import com.xweisoft.wx.family.util.LogX;

/**
 * 手机信号状态监听器
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-6-3]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class PhoneStateChangeListener extends PhoneStateListener
{
    
    private static final String LOGTAG = "===PhoneStateChangeListener===";
    
    /**
     * mina service
     */
    private final NotificationService notificationService;
    
    public PhoneStateChangeListener(NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }
    
    @Override
    public void onDataConnectionStateChanged(int state)
    {
        super.onDataConnectionStateChanged(state);
        LogX.getInstance().d(LOGTAG,
                "Data Connection State = " + getState(state));
        if (state == TelephonyManager.DATA_CONNECTED)
        {
            notificationService.connect();
        }
    }
    
    private String getState(int state)
    {
        switch (state)
        {
            case 0: // '\0'
                return "DATA_DISCONNECTED";
            case 1: // '\001'
                return "DATA_CONNECTING";
            case 2: // '\002'
                return "DATA_CONNECTED";
            case 3: // '\003'
                return "DATA_SUSPENDED";
        }
        return "DATA_<UNKNOWN>";
    }
    
}
