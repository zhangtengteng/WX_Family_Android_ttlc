package com.xweisoft.wx.family.mina;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.xweisoft.wx.family.WXApplication;
import com.xweisoft.wx.family.mina.service.MinaReconnectionThread;
import com.xweisoft.wx.family.mina.service.NotificationService;
import com.xweisoft.wx.family.util.LogX;
import com.xweisoft.wx.family.util.Util;

/**
 * mina管理类
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-6-3]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MinaManager
{
    
    private final String TAG = Util.makeLogTag(MinaManager.class);
    
    private NotificationService.TaskSubmitter taskSubmitter;
    
    private NotificationService.TaskTracker taskTracker;
    
    private Context context;
    
    private Handler handler;
    
    private List<Runnable> taskList;
    
    private boolean running = false;
    
    private boolean needConnect = true;
    
    private Future<?> futureTask;
    
    private MinaReconnectionThread reconnection;
    
    private ConnectFuture connectFuture;
    
    public MinaManager(NotificationService notificationService)
    {
        context = notificationService;
        taskSubmitter = notificationService.getTaskSubmitter();
        taskTracker = notificationService.getTaskTracker();
        handler = new Handler();
        taskList = new ArrayList<Runnable>();
        //        reconnection = new MinaReconnectionThread(this);
        WXApplication.getInstance().setMinaManager(this);
    }
    
    public void connect()
    {
        //重连时如果任务队列不为空，且任务大小大于3，则清空，
        //因为这个是一个单任务队列, 超过3时，就证明，前边的任务没有执行。
        synchronized (taskList)
        {
            if (!taskList.isEmpty() && taskList.size() > 3)
            {
                //                reconnection.resetWaiting();
                running = false;
                taskList.clear();
            }
        }
        addTask(new ConnectTask());
    }
    
    public void disconnect()
    {
        LogX.getInstance().d(Util.makeLogTag(getClass()),
                "terminatePersistentConnection()...");
        Runnable runnable = new Runnable()
        {
            
            final MinaManager minaManager = MinaManager.this;
            
            public void run()
            {
                if (minaManager.isConnected())
                {
                    needConnect = false;
                    minaManager.getConnectFuture().getSession().close(true);
                }
                minaManager.runTask();
            }
            
        };
        addTask(runnable);
    }
    
    public synchronized void startReconnectionThread()
    {
        if (null == reconnection
                || (null != reconnection && !reconnection.isAlive()))
        {
            LogX.getInstance().e(TAG, "thread start");
            reconnection = new MinaReconnectionThread(this);
            reconnection.resetWaiting();
            reconnection.setName("Mina Reconnection Thread");
            reconnection.start();
        }
    }
    
    public Context getContext()
    {
        return context;
    }
    
    public Handler getHandler()
    {
        return handler;
    }
    
    public void setConnectFuture(ConnectFuture connectFuture)
    {
        this.connectFuture = connectFuture;
    }
    
    public boolean isNeedConnect()
    {
        return needConnect;
    }
    
    public void setNeedConnect(boolean needConnect)
    {
        this.needConnect = needConnect;
    }
    
    public boolean isConnected()
    {
        return connectFuture != null && connectFuture.isConnected()
                && null != connectFuture.getSession()
                && connectFuture.getSession().isConnected();
    }
    
    public ConnectFuture getConnectFuture()
    {
        return connectFuture;
    }
    
    /**
     * A runnable task to connect the server.
     * <一句话功能简述>
     * <功能详细描述>
     * 
     * @author  poorgod
     * @version  [版本号, 2015-5-28]
     * @see  [相关类/方法]
     * @since  [产品/模块版本]
     */
    private class ConnectTask implements Runnable
    {
        
        final MinaManager minaManager;
        
        private ConnectTask()
        {
            this.minaManager = MinaManager.this;
        }
        
        public void run()
        {
            if (!minaManager.isConnected())
            {
                try
                {
                    // 创建一个tcp/ip 连接
                    //创建客户端连接器
                    LogX.getInstance().i(TAG,
                            "PORT:" + ClientUtil.PORT + "=====HostName:"
                                    + ClientUtil.HOSTNAME);
                    NioSocketConnector connector = new NioSocketConnector();
                    /*---------接收字符串---------*/
                    // //创建接收数据的过滤器
                    // DefaultIoFilterChainBuilder chain = connector.getFilterChain();
                    // // 设定这个过滤器将一行一行(/r/n)的读取数据
                    // chain.addLast("myChin", new ProtocolCodecFilter(
                    // new TextLineCodecFactory()));
                    /*---------接收对象---------*/
                    // 创建接收数据的过滤器
                    // 创建接收数据的过滤器
                    DefaultIoFilterChainBuilder chain = connector.getFilterChain();
                    //设置日志记录器
                    //            chain.addLast("Logger", new LoggingFilter());
                    // 设定这个过滤器将以对象为单位读取数据
                    PrefixedStringCodecFactory factory = new PrefixedStringCodecFactory(
                            Charset.forName("UTF-8"));
                    factory.setDecoderMaxDataLength(102400);
                    factory.setDecoderPrefixLength(4);
                    factory.setEncoderMaxDataLength(102400);
                    factory.setEncoderPrefixLength(4);
                    //设置编码过滤器
                    chain.addLast("codec", new ProtocolCodecFilter(factory));
                    //设置连接超时检查时间
                    connector.setConnectTimeoutCheckInterval(10000);
                    //设置事件处理器
                    connector.setHandler(new ClientHandler(MinaManager.this,
                            context));
                    //建立连接
                    ConnectFuture cf = connector.connect(new InetSocketAddress(
                            ClientUtil.HOSTNAME, ClientUtil.PORT));
                    minaManager.setConnectFuture(cf);
                    //等待连接创建完成
                    cf.awaitUninterruptibly();
                    //如果连接未成功，释放请求的资源
                    if (cf.isDone())
                    {
                        if (!cf.isConnected())
                        {
                            if (!connector.isDisposed()
                                    && !connector.isDisposing())
                            {
                                connector.dispose(true);
                            }
                            //给出客户端连接信息
                            LogX.getInstance().e(TAG,
                                    "==== Mina connect to server failed ====");
                            startReconnectionThread();
                        }
                    }
                }
                catch (Exception e)
                {
                    LogX.getInstance().e(TAG,
                            "==== Mina connect to server failed ====");
                    LogX.getInstance().e(Util.makeLogTag(getClass()),
                            e.toString());
                    //给出客户端连接信息
                    //显示连接失败的消息，并进行一定的界面处理
                    startReconnectionThread();
                }
                minaManager.runTask();
            }
            else
            {
                LogX.getInstance().i(TAG, "==== sever isConnected ====");
                minaManager.runTask();
            }
        }
    }
    
    public void addTask(Runnable runnable)
    {
        LogX.getInstance().d(Util.makeLogTag(getClass()),
                "addTask(runnable)...");
        taskTracker.increase();
        synchronized (taskList)
        {
            if (taskList.isEmpty() && !running)
            {
                running = true;
                futureTask = taskSubmitter.submit(runnable);
                if (futureTask == null)
                {
                    taskTracker.decrease();
                }
            }
            else
            {
                taskList.add(runnable);
            }
        }
        LogX.getInstance().d(Util.makeLogTag(getClass()),
                "addTask(runnable)... done");
    }
    
    public void runTask()
    {
        LogX.getInstance().d(Util.makeLogTag(getClass()), "runTask()...");
        synchronized (taskList)
        {
            running = false;
            futureTask = null;
            if (!taskList.isEmpty())
            {
                Runnable runnable = (Runnable) taskList.get(0);
                taskList.remove(0);
                running = true;
                futureTask = taskSubmitter.submit(runnable);
                if (futureTask == null)
                {
                    taskTracker.decrease();
                }
            }
        }
        taskTracker.decrease();
        LogX.getInstance().d(Util.makeLogTag(getClass()), "runTask()...done");
    }
    
    public void sendMessage(String msg)
    {
        try
        {
            if (isConnected())
            {
                if (!TextUtils.isEmpty(msg))
                {
                    connectFuture.getSession().write(msg);
                }
            }
            else
            {
                startReconnectionThread();
            }
        }
        catch (Exception e)
        {
            LogX.getInstance().e(TAG, "=== send message failed ===");
        }
    }
}
