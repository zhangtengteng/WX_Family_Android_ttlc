package com.xweisoft.wx.family.service.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;

import android.content.Context;
import android.text.TextUtils;

import com.xweisoft.wx.family.WXApplication;
import com.xweisoft.wx.family.logic.global.NetWorkCodes.CommontErrorCodes;
import com.xweisoft.wx.family.service.httpClient.HttpManager;
import com.xweisoft.wx.family.service.threadpool.TaskObject;
import com.xweisoft.wx.family.util.ListUtil;
import com.xweisoft.wx.family.util.LogX;
import com.xweisoft.wx.family.util.StringUtil;

/**
 * <一句话功能简述>
 * 联网请求处理任务 
 * 
 * @author  administrator
 * @version  [版本号, 2013-10-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public abstract class ConnectionTask implements TaskObject//, DownloadObserver
{
    /**
     * 断点下载 关键参数名
     */
    private static final String RANGE = "Range";
    
    /**
     * 代理地址
     */
    public String hostUrl = "10.0.0.172";
    
    /**
     * 代理端口
     */
    public int hostPort = 80;
    
    /**
     * post请求定义
     */
    public static final int POST = 0;
    
    /**
     * get请求定义
     */
    public static final int GET = 1;
    
    /**
     * 日志名
     */
    static final String TAG = "===ConnectionTask===";
    
    /**
     * 数据缓冲区的大小
     */
    protected static final int DATA_BUFFER_LEN = 512;
    
    /**
     * 是否已经超时
     */
    protected boolean isTimeOut = false;
    
    /**
     * 是否已经被取消的标志
     */
    protected boolean canceled = false;
    
    /**
     * 是否已被暂停的标志
     */
    protected boolean paused = false;
    
    /**
     * 是否正处于等待状态
     */
    protected boolean bWaiting = false;
    
    /**
     * 请求类型，默认为POST
     */
    protected int requestType = POST;
    
    /**
     * 超时定时器
     */
    protected Timer timer;
    
    /**
     * 超时定时器任务
     */
    protected TimerTask timerTask;
    
    /**
     * 超时时间
     */
    protected int timeout = 30 * 1000;
    
    /**
     * 区分联网请求JSON的标志码
     */
    protected int fusionCode;
    
    /**
     * post请求消息体
     */
    protected byte[] dataBuf = null;
    
    /**
     * post请求消息体
     */
    protected String dataStr = null;
    
    /**
     * url地址
     */
    protected String httpUrl;
    
    /**
     * 返回码
     */
    protected int responseCode;
    
    /**
     * http连接对象
     */
    protected HttpURLConnection conn;
    
    /**
     * 响应实体类
     */
    protected HttpEntity entity;
    
    protected HttpResponse response;
    
    /**
     * 输入流
     */
    protected InputStream is = null;
    
    /**
     * 存放请求头信息的Hashtable
     * eg.
     *  "Range", "bytes=" + from + "-" + to
     *  to is "", meaning from-fileLength
     */
    protected Hashtable<String, String> sendHead = null;
    
    /**
     * 断点
     */
    protected long breakpoint = 0L;
    
    /**
     * 文件对象
     */
    protected RandomAccessFile file;
    
    /**
     * SD卡回调同步锁
     */
    protected Object sdSyn = new Object();
    
    /**
     * 文件存储路径
     */
    protected String filePath;
    
    /**
     * 网络响应监听接口
     */
    protected IHttpCallback httpCallback;
    
    /**
     * 上下文
     */
    protected Context context;
    
    /**
     * 默认构造方法
     * @param context 上下文
     * @param httpUrl 请求地址
     * @param httpCallback 请求回调
     */
    public ConnectionTask(final Context context, String httpUrl,
            IHttpCallback httpCallback)
    {
        this.context = context;
        this.httpUrl = httpUrl;
        this.httpCallback = httpCallback;
    }
    
    /**
     * 设置断点值
     * @param breakpoint 续传断点
     */
    public void setBreakPoint(long breakpoint)
    {
        this.breakpoint = breakpoint;
    }
    
    /**
     * 断点续传时获取继续下载时的断点信息
     * 
     * @return 已经下载的数据量
     */
    public long getBreakPoint()
    {
        return breakpoint;
    }
    
    /**
     * 设置断点
     */
    public void setBreakPointHeader()
    {
        if (sendHead == null)
        {
            sendHead = new Hashtable<String, String>();
        }
        StringBuffer breakPointStr = new StringBuffer("bytes=");
        breakPointStr.append(breakpoint);
        breakPointStr.append("-");
        
        if (isSDKAbove9())
        {
            sendHead.put("RANGE", breakPointStr.toString());
        }
        else
        {
            sendHead.put(RANGE, breakPointStr.toString());
        }
    }
    
    /**
     * 取消连接任务
     */
    public void cancelConnect()
    {
        canceled = true;
    }
    
    /**
     * 判断用户是否已删除下载任务
     * 
     * @return 是否删除标志
     */
    public boolean isCanceled()
    {
        return canceled;
    }
    
    /**
     * 暂停连接任务
     */
    public void pausedConnect()
    {
        paused = true;
    }
    
    /**
     * 将任务置于等待状态
     */
    public void waitingConnect()
    {
        bWaiting = true;
    }
    
    /**
     * 判断用户是否已暂停下载
     * 
     * @return 是否暂停下载标志
     */
    public boolean isPaused()
    {
        return paused;
    }
    
    /**
     * 发送请求
     */
    public abstract void sendTaskReq();
    
    /**
     * 运行联网任务
     */
    public void runTask()
    {
        try
        {
            doTask();
        }
        catch (SecurityException se)
        {
            LogX.getInstance().e(TAG, "1runTask");
            hanlderException(se);
        }
        catch (InterruptedIOException e)
        {
            LogX.getInstance().e(TAG, "2runTask");
            handlerInterruptedIOException(e);
        }
        catch (InterruptedException e)
        {
            LogX.getInstance().e(TAG,
                    "==========SocketTimeoutException========" + e.toString());
            LogX.getInstance().e(TAG, "3runTask");
            handlerInterruptedException(e);
        }
        catch (SocketException e)
        {
            LogX.getInstance().e(TAG, "4runTask");
            // 无网络时会抛出该异常
            hanlderException(e);
        }
        catch (UnsupportedEncodingException e)
        {
            LogX.getInstance().e(TAG, "5runTask");
            setConnError(responseCode, e.toString());
        }
        catch (JSONException e)
        {
            LogX.getInstance().e(TAG, "6runTask");
            setConnError(responseCode, e.toString());
        }
        catch (IOException e)
        {
            LogX.getInstance().e(TAG + "IOException", e.toString());
            // 服务器响应异常会抛出该异常
            hanlderException(e);
        }
        catch (Exception e)
        {
            LogX.getInstance().e(TAG, "8::runTask::" + e.toString());
            // 其他异常
            hanlderException(e);
        }
        catch (Error e)
        {
            LogX.getInstance().e(TAG, "runTask===9");
            handlerError(e);
        }
        finally
        {
            LogX.getInstance().d(TAG, "10runTask");
            clearNet();
        }
        
    }
    
    /**
     * 执行请求
     * @throws Exception 异常
     * @throws Error 错误异常
     * @throws InterruptedException 终端异常
     */
    protected void doTask() throws Exception, Error, InterruptedException
    {
        // 执行网络连接操作（发送HTTP请求，并处理网络返回数据）
        connetionProcess();
    }
    
    /**
     * 执行请求
     * @throws Exception 异常
     * @throws Error 错误异常
     * @throws InterruptedException 终端异常
     */
    protected void connetionProcess() throws Exception, Error,
            InterruptedException
    {
        // 执行网络连接操作（发送HTTP请求，并处理网络返回数据）
        if (isSDKAbove9())
        {
            //使用httpURLConnection
            httpURLConnetionProcess();
        }
        else
        {
            // Prior to Gingerbread, HttpUrlConnection was unreliable.
            //使用httpClient
            httpClientConnetionProcess();
        }
    }
    
    /**
     * 判断当前手机的sdk是否大于9即是否是2.3以上包括2.3
     * API等级9：Android 2.3 - 2.3.2 Gingerbread
     * @return [参数说明]
     * 
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    protected boolean isSDKAbove9()
    {
        if (SDKUtil.isSDKAbove9())
        {
            return true;
        }
        return false;
    }
    
    /**
     * 网络异常默认处理
     * 
     * @param e
     *            异常对象
     */
    protected void hanlderException(Exception e)
    {
        //如果isTimeOut是true，则表明需要超时提醒，不需要网络异常提醒
        if (isTimeOut)
        {
            LogX.getInstance().d(TAG, "===hanlderException==do nothing..==");
        }
        else
        {
            setConnError(responseCode, e.toString());
        }
    }
    
    /**
     * 处理InterruptedIOException异常
     * 
     * @param e
     *            异常对象
     */
    protected void handlerInterruptedIOException(Exception e)
    {
        // 在connetionProcess()方法执行中或读写流中或打开连接时，打断会抛此异常
        if (isTimeOut)
        {
            LogX.getInstance().d(TAG,
                    "===handlerInterruptedIOException==do nothing..==");
        }
        else
        {
            hanlderException(e);
        }
    }
    
    /**
     * 处理中断异常
     * 
     * @param e
     *            中断异常
     */
    protected void handlerInterruptedException(InterruptedException e)
    {
        // 暂停取消时的打断异常
        if (isTimeOut)
        {
            setError(CommontErrorCodes.NETWORK_TIMEOUT_ERROR, "TIMEOUT");
        }
        else
        {
            hanlderException(e);
        }
    }
    
    /**
     * 实现了联网写读功能
     * 
     * @throws Exception
     *             异常类
     * @throws Error
     *             错误类
     * @throws InterruptedException
     *             错误类
     */
    protected void httpURLConnetionProcess() throws Exception, Error,
            InterruptedException
    {
        try
        {
            if (canceled || paused || isTimeOut || bWaiting)
            {
                throw new InterruptedException();
            }
            
            // 构建连接
            URL url = new URL(httpUrl);
            if ((url.getProtocol().toLowerCase()).equals("https"))//Https链接   
            {
                X509HostnameVerifier hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null,
                        new TrustManager[] { new FakeTrustManager() },
                        null);
                
                HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
                conn = (HttpsURLConnection) url.openConnection();
                
                conn.setDoInput(true);
                //              conn.setRequestProperty("Content-Type", "text/xml");
            }
            else
            {
                conn = (HttpURLConnection) url.openConnection();
                //              conn.setRequestProperty("Content-Type", "text/xml");
                //              conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            }
            
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
            
            // 不使用Cache
            conn.setUseCaches(false);
            
            // 设置请求类型
            if (requestType == POST)
            {
                conn.setRequestMethod("POST");
            }
            else
            {
                conn.setRequestMethod("GET");
            }
            
            if (sendHead != null && sendHead.containsKey("RANGE"))
            {
                LogX.getInstance().i(TAG,
                        "connetionProcess: " + "range:"
                                + (String) sendHead.get("RANGE"));
                conn.addRequestProperty("RANGE", (String) sendHead.get("RANGE"));
            }
            
            HttpURLConnection.setFollowRedirects(false);
            
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setRequestProperty("Accept", "*/*");
            // end add
            // 维持长连接
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            
            //缓存
            conn.setRequestProperty("Cookie", addCookies());
            
            // 以内容实体方式发送请求参数
            if (requestType == POST && dataBuf != null)
            {
                // 发送POST请求必须设置允许输出
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Length",
                        String.valueOf(dataBuf.length));
                
                // 开始写入数据
                DataOutputStream outStream = new DataOutputStream(
                        conn.getOutputStream());
                outStream.write(dataBuf);
                outStream.close();
            }
            
            if (canceled || paused || isTimeOut || bWaiting)
            {
                throw new InterruptedException();
            }
            
            responseCode = conn.getResponseCode();
            LogX.getInstance().d(TAG, "====responseCode===" + responseCode);
            
            // 请求状态
            switch (responseCode)
            {
                case HttpStatus.SC_OK:
                    //houff @20150427 begin for 获取http请求头信息
                    readCookies();
                    //houff @20150427 end
                    break;
                case HttpStatus.SC_PARTIAL_CONTENT:
                    String contentType = conn.getHeaderField("Content-type");
                    if (contentType != null
                            && (contentType.startsWith("text/vnd.wap.wml") || contentType.startsWith("application/vnd.wap.wmlc")))
                    {
                        conn.disconnect();
                        conn = null;
                    }
                    break;
                // 支持302下载，获取Location重新去下载
                case HttpStatus.SC_MOVED_TEMPORARILY:
                    String location = conn.getHeaderField("Location");
                    if (canceled || paused || isTimeOut || bWaiting)
                    {
                        throw new InterruptedException();
                    }
                    
                    if (location != null && location.startsWith("http://"))
                    {
                        LogX.getInstance().d(TAG,
                                "before location url = " + httpUrl);
                        httpUrl = location.replaceAll("&amp;", "&");
                        LogX.getInstance().d(TAG,
                                "after location url = " + httpUrl);
                        if (this.isConnectionWifi)
                        {
                            
                            break;
                        }
                        httpURLConnetionProcess();
                        // 302下载解析到location后，中断第一次解析，开始去下载location的地址
                        return;
                    }
                    break;
                default:
                    throw new IOException("Connection response status not OK:"
                            + responseCode);
            }
            
            if (canceled || paused || isTimeOut || bWaiting)
            {
                throw new InterruptedException();
            }
            
            // 读取数据
            readData();
        }
        finally
        {
            // 关闭连接
            clearNet();
        }
    }
    
    /** <一句话功能简述>
     * 获取cookie
     * <功能详细描述> [参数说明]
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    protected void readCookies()
    {
        List<String> cookieList = conn.getHeaderFields().get("Set-Cookie");
        if (!ListUtil.isEmpty(cookieList))
        {
            for (int i = 0; i < cookieList.size(); i++)
            {
                String cookie = cookieList.get(i);
                //保存cookie到本地缓存
                WXApplication.getInstance().cookieList.add(cookie);
                if (!StringUtil.isEmpty(cookie))
                {
                    int p = cookie.indexOf("=");
                    String key = cookie.substring(0, p);
                    String value = cookie.substring(p + 1, cookie.indexOf(";"));
                    WXApplication.getInstance().cookieContiner.put(key, value);
                }
            }
        }
    }
    
    /**
     * 增加Cookie
     * @param request
     */
    public String addCookies()
    {
        StringBuilder sb = new StringBuilder();
        if (WXApplication.getInstance().cookieContiner != null)
        {
            Iterator iter = WXApplication.getInstance().cookieContiner.entrySet()
                    .iterator();
            while (iter.hasNext())
            {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = entry.getKey().toString();
                String val = entry.getValue().toString();
                sb.append(key);
                sb.append("=");
                sb.append(val);
                sb.append(";");
            }
        }
        return sb.toString();
    }
    
    /**
     * 处理Error异常
     * 
     * @param e
     *            异常对象
     */
    protected void handlerError(Error e)
    {
        setConnError(responseCode, e.toString());
    }
    
    /**
    * 实现了联网写读功能
    * 
    * @throws Exception
    *             异常类
    * @throws Error
    *             错误类
    * @throws InterruptedException
    *             错误类
    */
    protected void httpClientConnetionProcess() throws Exception, Error,
            InterruptedException
    {
        try
        {
            //判断是否取消请求。
            if (canceled || paused || isTimeOut || bWaiting)
            {
                throw new InterruptedException();
            }
            
            //判断是否是post 请求
            if (requestType == POST)
            {
                if (canceled || paused || isTimeOut || bWaiting)
                {
                    throw new InterruptedException();
                }
                HttpPost httppost = new HttpPost(httpUrl);
                
                //是post请求，判断是否有请求参数
                if (!TextUtils.isEmpty(dataStr))
                {
                    StringEntity entity = new StringEntity(dataStr, HTTP.UTF_8);
                    httppost.setEntity(entity);
                }
                response = HttpManager.execute(httppost);
            }
            else
            {
                //判断是否是get 请求
                if (canceled || paused || isTimeOut || bWaiting)
                {
                    throw new InterruptedException();
                }
                
                HttpGet httpGet = new HttpGet(httpUrl);
                
                //如果是电信wifi请求地址，则设置次content-type
                if (this.isConnectionWifi)
                {
                    httpGet.addHeader("Content-Type",
                            "application/x-www-form-urlencoded");
                }
                else
                {
                    httpGet.addHeader("Content-Type", "text/xml");
                }
                
                //get请求，判断是否需要设置请求数据的范围
                if (sendHead != null && sendHead.containsKey(RANGE))
                {
                    httpGet.addHeader(RANGE, sendHead.get(RANGE));
                }
                response = HttpManager.execute(httpGet);
            }
            
            if (canceled || paused || isTimeOut || bWaiting)
            {
                throw new InterruptedException();
            }
            
            //暂时针对响应code 不做请求成功与否的判断
            StatusLine status = response.getStatusLine();
            if (null != status)
            {
                responseCode = response.getStatusLine().getStatusCode();
            }
            
            entity = response.getEntity();
            
            if (canceled || paused || isTimeOut || bWaiting)
            {
                throw new InterruptedException();
            }
            
            readData();
        }
        finally
        {
            // 关闭连接
            clearNet();
        }
    }
    
    /**
    * 读网络数据
    * 
    * @throws IOException
    *             IO异常类
    * @throws InterruptedException
    *             中断异常类
    * @throws JSONException json的异常
    */
    protected void readData() throws Exception, IOException,
            InterruptedException, UnsupportedEncodingException, JSONException
    {
        if (isSDKAbove9())
        {
            is = conn.getInputStream();
        }
        else
        {
            // 获取网络数据输入流
            is = entity.getContent();
        }
    }
    
    /**
     * 关闭连接
     */
    protected void clearNet()
    {
        synchronized (sdSyn)
        {
            if (isSDKAbove9())
            {
                try
                {
                    if (is != null)
                    {
                        is.close();
                    }
                    if (conn != null)
                    {
                        conn.disconnect();
                    }
                    if (file != null)
                    {
                        file.close();
                    }
                }
                catch (Exception e)
                {
                    LogX.getInstance().i(TAG, e.toString());
                }
                finally
                {
                    file = null;
                    is = null;
                    conn = null;
                }
            }
            else
            {
                try
                {
                    if (is != null)
                    {
                        is.close();
                    }
                    if (null != entity)
                    {
                        entity.consumeContent();
                        entity = null;
                    }
                    if (file != null)
                    {
                        file.close();
                    }
                }
                catch (Exception e)
                {
                    LogX.getInstance().i(TAG, e.toString());
                }
                finally
                {
                    file = null;
                    is = null;
                }
            }
        }
    }
    
    /**
     * 设置错误回调
     * @param responseCode 错误码
     * @param exception 异常信息 
     */
    protected void setError(int responseCode, String exception)
    {
        // 异常信息打印，正常情况下不会打出该日志
        if (httpCallback != null)
        {
            httpCallback.onError(responseCode, exception);
        }
    }
    
    /**
     * 无网络错误处理
     * 
     * @param responseCode
     *            状态码
     * @param exception
     *            异常信息
     */
    private void setConnError(int responseCode, String exception)
    {
        // 异常信息打印，正常情况下不会打出该日志
        LogX.getInstance().d(TAG,
                "setConnError: the responseCode is " + responseCode
                        + " and the exception is" + exception.toString());
        if (httpCallback != null)
        {
            
            httpCallback.onConnError(responseCode, exception);
        }
    }
    
    /**
     * 超时处理
     * 
     * @param responseCode
     *            状态码
     * @param exception
     *            异常信息
     */
    protected void setTimeOut(int responseCode, String exception)
    {
        // 异常信息打印，正常情况下不会打出该日志
        if (httpCallback != null)
        {
            httpCallback.onTimeOut(responseCode, exception);
        }
    }
    
    /**
     * 设置请求头信息
     * 
     * @param dataBuf
     *            请求头参数
     */
    public void setDataBuf(byte[] dataBuf)
    {
        this.dataBuf = dataBuf;
    }
    
    /**
     * 设置请求头信息
     * 
     * @param dataBuf
     *            请求头参数
     */
    public void setDataStr(String dataStr)
    {
        this.dataStr = dataStr;
    }
    
    /**
     * 任务取消的回调接口方法
     */
    public void onCancelTask()
    {
        canceled = true;
    }
    
    /**
     * 获取网络连接任务对象请求的url
     * 
     * @return 请求的url
     */
    public String getHttpUrl()
    {
        return httpUrl;
    }
    
    /**
     * SD 卡监听接口
     * 
     * @param arg
     *            系统通知的action此类需要监听sd的插拔
     */
    //	public void update(Object arg)
    //	{
    //		clearNet();
    //	}
    
    /**
     * 设置定时器对象
     * 
     * @param timer 定时器
     */
    public void setTimer(Timer timer)
    {
        this.timer = timer;
    }
    
    /**
     * 任务请求响应回调接口
     * 
     * @param code
     *            响应通知码
     */
    public void onTaskResponse(int code)
    {
        if (code == TaskObject.RESPONSE_TIMEOUT_RUNNING)
        {
            LogX.getInstance()
                    .d(TAG,
                            ":onTaskResponse: responseCode" + responseCode
                                    + " TIMEOUT");
            
            setError(responseCode, "TIMEOUT");
            isTimeOut = true;
            clearNet();
        }
    }
    
    /**
     * 设置任务的超时定时器任务对象
     * 
     * @param timeoutTask
     *            定时器任务对象
     */
    public void setTimeoutTask(TimerTask timeoutTask)
    {
        this.timerTask = timeoutTask;
    }
    
    /**
     * 启动超时定时器
     */
    public void startTimeoutTimer()
    {
        if (timer != null)
        {
            timer.schedule(timerTask, timeout);
        }
    }
    
    /**
     * 停止超时定时器
     */
    public void stopTimeoutTimer()
    {
        if (timerTask != null)
        {
            timerTask.cancel();
        }
    }
    
    //    /**
    //     * 判断当前链接是否是用代理
    //     * @param context 上下文
    //     * @return true 使用 false不使用
    //     */
    //    public boolean isNeedProxy(final Context context)
    //    {
    //        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    //        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
    //        if (activeNetInfo != null)
    //        {
    //            String type = activeNetInfo.getTypeName();
    //            
    //            // WIFI 返回不使用
    //            if (type.equalsIgnoreCase("WIFI"))
    //            {
    //                return false;
    //            }
    //            // 移动网络 判断当前链接类型
    //            else if (type.equalsIgnoreCase("MOBILE"))
    //            {
    //                
    //                // 获取默认代理主机ip
    //                hostUrl = android.net.Proxy.getDefaultHost();
    //                // 获取端口
    //                hostPort = android.net.Proxy.getDefaultPort();
    //                if (hostUrl != null && hostPort != -1)
    //                {
    //                    //                    // 封装代理连接主机IP与端口号。
    //                    return true;
    //                }
    //                else
    //                {
    //                    String mobileProxyIp = "";
    //                    String mobileProxyPort = "";
    //                    Cursor c = context.getContentResolver()
    //                            .query(Uri.parse("content://telephony/carriers/preferapn"),
    //                                    null,
    //                                    null,
    //                                    null,
    //                                    null);
    //                    if (c != null && c.moveToFirst())
    //                    {
    //                        
    //                        mobileProxyIp = c.getString(9);
    //                        mobileProxyPort = c.getString(10);
    //                        c.close();
    //                        // IP跟端口不为空
    //                        if (!TextUtils.isEmpty(mobileProxyIp)
    //                                && !TextUtils.isEmpty(mobileProxyPort))
    //                        {
    //                            hostUrl = mobileProxyIp;
    //                            try
    //                            {
    //                                hostPort = Integer.parseInt(mobileProxyPort);
    //                                
    //                            }
    //                            catch (Exception e)
    //                            {
    //                                LogX.getInstance()
    //                                        .i("ConnectionTask isNeedProxy",
    //                                                e.getMessage().toString());
    //                                return false;
    //                            }
    //                            return true;
    //                        }
    //                        else
    //                        {
    //                            return false;
    //                        }
    //                    }
    //                }
    //            }
    //        }
    //        return false;
    //    }
    
    /**
     * 
     * [一句话功能简述]<BR>
     * [功能详细描述]
     * 
     * @param requestType 请求类型
     */
    public void setRequestType(int requestType)
    {
        this.requestType = requestType;
    }
    
    private boolean isConnectionWifi = false;
    
    public void settIsConnectionWifi(boolean isConnectionWifi)
    {
        this.isConnectionWifi = isConnectionWifi;
    }
}
