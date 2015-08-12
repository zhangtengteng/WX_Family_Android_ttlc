package com.xweisoft.wx.family.mina.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.xweisoft.wx.family.mina.ClientUtil;
import com.xweisoft.wx.family.mina.MinaManager;
import com.xweisoft.wx.family.mina.listener.ConnectivityReceiver;
import com.xweisoft.wx.family.mina.listener.NotificationReceiver;
import com.xweisoft.wx.family.mina.listener.PhoneStateChangeListener;
import com.xweisoft.wx.family.util.LogX;
import com.xweisoft.wx.family.util.Util;

/**
 * mina后台service
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-6-3]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class NotificationService extends Service
{
    
    public static final String SERVICE_NAME = "com.xweisoft.wx.family.mina.notification.service";
    
    /**
     * mina管理类
     */
    private MinaManager minaManager;
    
    /**
     * 手机管理类
     */
    private TelephonyManager telephonyManager;
    
    /**
     * 通知广播
     */
    private BroadcastReceiver notificationReceiver;
    
    /**
     * 网络状态广播
     */
    private BroadcastReceiver connectivityReceiver;
    
    /**
     * 手机信号状态监听器
     */
    private PhoneStateListener phoneStateListener;
    
    private ExecutorService executorService;
    
    private TaskSubmitter taskSubmitter;
    
    private TaskTracker taskTracker;
    
    /**
     * <默认构造函数>
     */
    public NotificationService()
    {
        notificationReceiver = new NotificationReceiver();
        connectivityReceiver = new ConnectivityReceiver(this);
        phoneStateListener = new PhoneStateChangeListener(this);
        executorService = Executors.newSingleThreadExecutor();
        taskSubmitter = new TaskSubmitter(this);
        taskTracker = new TaskTracker(this);
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }
    
    @Override
    public void onCreate()
    {
        super.onCreate();
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        minaManager = new MinaManager(this);
        
        taskSubmitter.submit(new Runnable()
        {
            public void run()
            {
                NotificationService.this.start();
            }
        });
    }
    
    @Override
    public void onDestroy()
    {
        stop();
        Intent intent = new Intent();
        intent.setAction(SERVICE_NAME);
        intent.setPackage("com.xweisoft.wx.family");
        startService(intent);
    }
    
    /**
     * mina 连接服务器
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void connect()
    {
        taskSubmitter.submit(new Runnable()
        {
            public void run()
            {
                NotificationService.this.getMinaManager().connect();
            }
        });
    }
    
    /**
     * mina 关闭连接
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void disconnect()
    {
        taskSubmitter.submit(new Runnable()
        {
            public void run()
            {
                NotificationService.this.getMinaManager().disconnect();
            }
        });
    }
    
    /**
     * 注册通知广播
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private void registerNotificationReceiver()
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ClientUtil.ACTION_SHOW_NOTIFICATION);
        registerReceiver(notificationReceiver, filter);
    }
    
    /**
     * 取消通知广播
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private void unregisterNotificationReceiver()
    {
        unregisterReceiver(notificationReceiver);
    }
    
    /**
     * 注册网络状态广播和手机信号监听器
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private void registerConnectivityReceiver()
    {
        telephonyManager.listen(phoneStateListener,
                PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityReceiver, filter);
    }
    
    /**
     * 取消网络状态广播和手机信号监听器
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private void unregisterConnectivityReceiver()
    {
        LogX.getInstance().d(Util.makeLogTag(getClass()),
                "unregisterConnectivityReceiver()...");
        telephonyManager.listen(phoneStateListener,
                PhoneStateListener.LISTEN_NONE);
        unregisterReceiver(connectivityReceiver);
    }
    
    /**
     * 开启service
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private void start()
    {
        LogX.getInstance().d(Util.makeLogTag(getClass()), "start()...");
        registerNotificationReceiver();
        registerConnectivityReceiver();
        minaManager.connect();
    }
    
    /**
     * 停止service
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private void stop()
    {
        LogX.getInstance().d(Util.makeLogTag(getClass()), "stop()...");
        unregisterNotificationReceiver();
        unregisterConnectivityReceiver();
        minaManager.disconnect();
        executorService.shutdown();
    }
    
    public static Intent getIntent()
    {
        return new Intent(SERVICE_NAME);
    }
    
    public ExecutorService getExecutorService()
    {
        return executorService;
    }
    
    public TaskSubmitter getTaskSubmitter()
    {
        return taskSubmitter;
    }
    
    public TaskTracker getTaskTracker()
    {
        return taskTracker;
    }
    
    public MinaManager getMinaManager()
    {
        return minaManager;
    }
    
    /**
     * Class for summiting a new runnable task.
     */
    public class TaskSubmitter
    {
        
        final NotificationService notificationService;
        
        public TaskSubmitter(NotificationService notificationService)
        {
            this.notificationService = notificationService;
        }
        
        @SuppressWarnings("rawtypes")
        public Future submit(Runnable task)
        {
            Future result = null;
            if (!notificationService.getExecutorService().isTerminated()
                    && !notificationService.getExecutorService().isShutdown()
                    && task != null)
            {
                result = notificationService.getExecutorService().submit(task);
            }
            return result;
        }
        
    }
    
    /**
     * Class for monitoring the running task count.
     */
    public class TaskTracker
    {
        
        final NotificationService notificationService;
        
        public int count;
        
        public TaskTracker(NotificationService notificationService)
        {
            this.notificationService = notificationService;
            this.count = 0;
        }
        
        public void increase()
        {
            synchronized (notificationService.getTaskTracker())
            {
                notificationService.getTaskTracker().count++;
                LogX.getInstance().d(Util.makeLogTag(getClass()),
                        "Incremented task count to " + count);
            }
        }
        
        public void decrease()
        {
            synchronized (notificationService.getTaskTracker())
            {
                notificationService.getTaskTracker().count--;
                LogX.getInstance().d(Util.makeLogTag(getClass()),
                        "Decremented task count to " + count);
            }
        }
        
    }
    
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
    
}
