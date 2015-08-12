package com.xweisoft.wx.family.mina.service;

import com.xweisoft.wx.family.mina.MinaManager;
import com.xweisoft.wx.family.util.LogX;

/**
 * mina 断线重连线程
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-6-3]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MinaReconnectionThread extends Thread
{
    
    private static final String LOGTAG = "===ReconnectionThread===";
    
    private final MinaManager minaManager;
    
    private int waiting;
    
    public MinaReconnectionThread(MinaManager minaManager)
    {
        this.minaManager = minaManager;
        this.waiting = 0;
    }
    
    public void run()
    {
        try
        {
            while (!minaManager.isConnected() && minaManager.isNeedConnect())
            {
                LogX.getInstance().d(LOGTAG,
                        "Trying to reconnect in " + waiting() + " seconds-----"
                                + getId());
                Thread.sleep((long) waiting() * 1000L);
                minaManager.connect();
                waiting++;
            }
        }
        catch (final InterruptedException e)
        {
            minaManager.getHandler().post(new Runnable()
            {
                public void run()
                {
                    //                    minaManager.getConnectionListener().reconnectionFailed(e);
                }
            });
        }
    }
    
    private int waiting()
    {
        if (waiting > 20)
        {
            return 600;
        }
        if (waiting > 13)
        {
            return 300;
        }
        return waiting <= 7 ? 10 : 60;
    }
    
    /**
     * 重置等待时间。
     * <功能详细描述>
     * @param value [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void resetWaiting()
    {
        if (waiting > 100)
        {
            this.waiting = 1;
        }
    }
    
    /**
     * 重置等待时间。
     * <功能详细描述>
     * @param value [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void setWaiting0()
    {
        this.waiting = 1;
    }
}
