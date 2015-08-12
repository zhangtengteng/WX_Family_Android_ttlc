package com.xweisoft.wx.family.service.download;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.json.JSONException;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.logic.global.GlobalConstant;
import com.xweisoft.wx.family.logic.global.GlobalVariable;
import com.xweisoft.wx.family.service.exception.SDNotEnouchSpaceException;
import com.xweisoft.wx.family.service.exception.SDUnavailableException;
import com.xweisoft.wx.family.service.http.FakeTrustManager;
import com.xweisoft.wx.family.service.http.IHttpCallback;
import com.xweisoft.wx.family.service.threadpool.TaskObject;
import com.xweisoft.wx.family.service.upload.IHttpListener;
import com.xweisoft.wx.family.util.FileHelper;
import com.xweisoft.wx.family.util.LogX;
import com.xweisoft.wx.family.util.Util;

/**
 * 联网请求处理任务
 * 
 * @author newengine
 * 
 */
public class DownloadFileTask implements TaskObject
{
    private static final String TAG = Util.makeLogTag(DownloadFileTask.class);
    
    /**
     * 超时时间
     */
    private static final int TIMEOUT = 30000;
    
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
     * 数据缓冲区的大小
     */
    protected static final int DATA_BUFFER_LEN = 512;
    
    /**
     * 是否已经超时
     */
    protected boolean isTimeOut = false;
    
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
     * http连接对象
     */
    protected HttpURLConnection conn;
    
    /**
     * 输入流
     */
    protected InputStream is = null;
    
    /**
     * 存放请求头信息的Hashtable
     */
    protected Hashtable<String, String> sendHead = null;
    
    /**
     * 文件对象
     */
    protected RandomAccessFile file;
    
    /**
     * SD卡回调同步锁
     */
    protected Object sdSyn = new Object();
    
    /**
     * 网络响应监听接口
     */
    protected IHttpCallback httpCallback;
    
    /**
     * 上下文
     */
    protected Context context;
    
    /**
     * 是否已经被取消的标志
     */
    private boolean canceled = false;
    
    /**
     * 事件处理句柄
     */
    public Handler handler;
    
    /**
     * url地址
     */
    private String httpUrl;
    
    /**
     * 返回码
     */
    private int responseCode;
    
    /**
     * 联网回调接口
     */
    private IHttpListener httpListener;
    
    /**
     * 断点
     */
    private long breakpoint = 0L;
    
    /**
     * 下载任务是否完成
     */
    private boolean isDownloadSuccess = false;
    
    /**
     * 文件存储路径
     */
    private String filePath;
    
    private int reqType;
    
    private TimerTask timerTask;
    
    private Timer timer;
    
    private DownloadItem downloadItem;
    
    /**
     * 下载文件时，需要针对消息的文件下载进行特殊处理
     * 
     * 1. 下载消息文件， 要取数据流的前四个字节。作为错误码来进行判断
     * 2. 其他的直接下载。
     */
    public int fileModeType = 1;
    
    /**
     * 默认构造方法
     */
    public DownloadFileTask()
    {
    }
    
    /**
     * 下载构造方法
     * 
     * @param httpListener
     *            联网回调接口
     * @param httpUrl
     *            连接地址
     */
    public DownloadFileTask(IHttpListener httpListener, String httpUrl,
            String filePath)
    {
        this.httpListener = httpListener;
        this.httpUrl = httpUrl;
        this.filePath = filePath;
        
        this.downloadItem = (DownloadItem) httpListener;
    }
    
    public Context getContext()
    {
        return context;
    }
    
    public void setContext(Context context)
    {
        this.context = context;
    }
    
    /**
     * 运行联网任务
     */
    public void runTask()
    {
        try
        {
            // 通知任务开始下载
            isDownloadSuccess = false;
            startUploadCallback();
            
            // 执行网络连接操作（发送HTTP请求，并处理网络返回数据）
            connetionProcess();
            
            // 如果下载成功，回调到上层处理
            if (isDownloadSuccess)
            {
                successUploadCallback();
            }
        }
        catch (SecurityException se)
        {
            handlerSocketException(se);
        }
        catch (InterruptedIOException e)
        {
            if (canceled)
            {
                canceledCallback();
            }
        }
        catch (InterruptedException e)
        {
            // 暂停取消时的打断异常
            if (canceled)
            {
                canceledCallback();
            }
            else if (paused)
            {
                pausedCallback();
            }
            else
            {
                handlerException(e);
            }
        }
        catch (SocketException e)
        {
            // 无网络时会抛出该异常
            handlerSocketException(e);
        }
        catch (FileNotFoundException e)
        {
            // 服务器响应异常会抛出该异常
            handlerException(e);
        }
        catch (IOException e)
        {
            // 服务器响应异常会抛出该异常
            handlerException(e);
        }
        catch (Exception e)
        {
            // 其他异常
            handlerException(e);
        }
        catch (Error e)
        {
            // 错误处理
            setError(responseCode, e.toString());
        }
        finally
        {
        }
    }
    
    /**
     * 设置断点值
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
     * 取消连接任务
     */
    public void cancelTask()
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
     * 异常处理
     * 
     * @param exception
     *            异常对象
     */
    private void handlerException(Exception exception)
    {
        if (handler != null)
        {
            handler.sendMessage(handler.obtainMessage(IHttpListener.HTTP_SER_ERROR,
                    reqType,
                    0,
                    exception.toString()));
        }
    }
    
    /**
     * 异常处理
     * 
     * @param exception
     *            异常对象
     */
    private void handlerSocketException(Exception exception)
    {
        if (handler != null)
        {
            handler.sendMessage(handler.obtainMessage(IHttpListener.HTTP_FAILED,
                    reqType,
                    0,
                    exception.toString()));
        }
    }
    
    /**
     * 判断当前链接是否是用代理
     * @param context 上下文
     * @return true 使用 false不使用
     */
    public boolean isNeedProxy(final Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null)
        {
            String type = activeNetInfo.getTypeName();
            
            // WIFI 返回不使用
            if (type.equalsIgnoreCase("WIFI"))
            {
                return false;
            }
            // 移动网络 判断当前链接类型
            else if (type.equalsIgnoreCase("MOBILE"))
            {
                String mobileProxyIp = "";
                String mobileProxyPort = "";
                Cursor c = context.getContentResolver()
                        .query(Uri.parse("content://telephony/carriers/preferapn"),
                                null,
                                null,
                                null,
                                null);
                if (c != null && c.moveToFirst())
                {
                    
                    mobileProxyIp = c.getString(9);
                    mobileProxyPort = c.getString(10);
                    c.close();
                    // IP跟端口不为空
                    if (!TextUtils.isEmpty(mobileProxyIp)
                            && !TextUtils.isEmpty(mobileProxyPort))
                    {
                        hostUrl = mobileProxyIp;
                        try
                        {
                            hostPort = Integer.parseInt(mobileProxyPort);
                            
                        }
                        catch (Exception e)
                        {
                            LogX.getInstance().i("ConnectionTask isNeedProxy",
                                    e.getMessage().toString());
                            return false;
                        }
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
            }
        }
        return false;
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
    protected void connetionProcess() throws Exception, Error,
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
            
            if (isNeedProxy(context))
            {
                // if wap
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
                        hostUrl, hostPort));
                conn = (HttpURLConnection) url.openConnection(proxy);
                LogX.getInstance().i(TAG,
                        "connetionProcess.use proxy: " + hostUrl + ":"
                                + hostPort);
            }
            else if ((url.getProtocol().toLowerCase()).equals("https"))//Https链接   
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
            
            conn.setRequestProperty("Content-Type", "text/xml");
            conn.setRequestProperty("Accept", "*/*");
            // end add
            // 维持长连接
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            
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
                default:
                    throw new IOException("Connection response status not OK:"
                            + responseCode);
            }
            
            if (canceled || paused || isTimeOut || bWaiting)
            {
                throw new InterruptedException();
            }
            isDownloadSuccess = true;
            // 读取数据
            readData();
        }
        finally
        {
            // 关闭连接
            clearNet();
        }
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
        
        sendHead.put("RANGE", breakPointStr.toString());
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
        // 获取网络数据输入流
        is = conn.getInputStream();
        long fileLenght = (long) conn.getContentLength();
        
        this.downloadItem.fileSize = fileLenght;
        
        //下载文件时，需要针对消息的文件下载进行特殊处理
        // 1. 下载消息文件， 要取数据流的前四个字节。作为错误码来进行判断
        // 2. 其他的直接下载。
        //        if (fileModeType == EnumCanstant.DownloadModeType.msgModeType.getValue())
        //        {
        //            int headLength = 4;
        //            byte[] headBuffer = new byte[headLength];
        //            headLength = is.read(headBuffer);
        //            
        //            //如果下载失败，则返回。不写文件
        //            if (isDownloadFileError(headBuffer))
        //            {
        //                return;
        //            }  
        //            
        //            readDownloadData(fileLenght - headLength);
        //        }
        //        else
        //        {
        readDownloadData(fileLenght);
        //        }
    }
    
    /**
     * 读取下载业务的业务包数据
     * 
     * @param context 上下文
     * @param dataLen 数据长度
     * @throws IOException 抛出IO异常供调用者捕捉
     * @throws InterruptedException 抛出中断异常供调用者捕捉
     */
    protected void readDownloadData(long dataLen) throws IOException,
            InterruptedException
    {
        
        // 超时时间未到，连接成功停止超时任务
        stopTimeoutTimer();
        
        long contentLen = 0L;
        // 从返回头中获取返回长度
        String contentLength = conn.getHeaderField("Content-Length");
        if (contentLength != null && contentLength.length() > 0)
        {
            try
            {
                contentLen = Long.parseLong(contentLength.trim());
            }
            catch (Exception e)
            {
                LogX.getInstance().i(TAG, e.toString());
            }
        }
        else
        {
            String contentRange = conn.getHeaderField("content-range");
            if (contentRange != null)
            {
                contentLen = Long.parseLong(Util.split2(contentRange, "/")[1])
                        - breakpoint;
            }
        }
        
        try
        {
            // 从流中获取返回长度
            long isLen = (long) (is.available());
            
            // 取3种返回长度的最大值作为真正的返回长度
            dataLen = dataLen > contentLen ? dataLen : contentLen;
            dataLen = dataLen > isLen ? dataLen : isLen;
            
            setDataLength(breakpoint, dataLen + breakpoint);
            
            // 存储已经下载的长度
            long getLen = 0;
            // 存储每次从网络层读取到的数据
            // 临时数据缓冲区
            int buffLen = 0;
            byte[] buff = new byte[DATA_BUFFER_LEN];
            byte[] tempBuff = null;
            
            // 创建文件
            if (!createFile(context, dataLen))
            {
                return;
            }
            long fileLength = 0;
            while (buffLen != -1)
            {
                fileLength = FileHelper.getLocalFileSize(filePath);
                if (fileLength == 0 && getLen != 0)
                {
                    throw new InterruptedException();
                }
                buffLen = is.read(buff);
                
                // 网络连接被暂停或取消，需要抛出中断异常，并关闭写文件线程
                if (canceled || paused || isTimeOut || bWaiting)
                {
                    throw new InterruptedException();
                }
                
                // 如果没有读到数据就不需要写文件了
                if (buffLen <= 0)
                {
                    continue;
                }
                
                // sd卡存在时将下载的数据写入文件
                if (GlobalVariable.sdCardIsExist)
                {
                    tempBuff = new byte[buffLen];
                    
                    System.arraycopy(buff, 0, tempBuff, 0, buffLen);
                    int len = FileHelper.writeFile(context, file, tempBuff);
                    if (len == FileHelper.ERROR)
                    {
                        throw new InterruptedException();
                    }
                    else
                    {
                        getLen += len;
                        // 设置下载量（百分比）
                        setDataLength(breakpoint + getLen, breakpoint + dataLen);
                    }
                }
                // sd卡不存在直接抛出异常
                else
                {
                    // SDCard不存在直接弹出提示
                    Toast.makeText(GlobalVariable.currentActivity,
                            "抱歉，无法下载",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                
                // 网络连接被暂停或取消，需要抛出中断异常，并关闭写文件线程
                if (canceled || paused || isTimeOut || bWaiting)
                {
                    throw new InterruptedException();
                    
                }
            }
            
            // 保证下载完了文本进度显示100%
            if (dataLen <= getLen)
            {
                dataLen = getLen;
                setDataLength(breakpoint + getLen, breakpoint + dataLen);
            }
        }
        catch (IOException e)
        {
            throw new IOException();
        }
        catch (SDUnavailableException e)
        {
            throw new InterruptedException("SDUnavailableException");
        }
        catch (SDNotEnouchSpaceException e)
        {
            throw new InterruptedException("SDNotEnouchSpaceException");
        }
        
        // 网络连接被暂停或取消，需要抛出中断异常，并关闭写文件线程
        if (paused || canceled || isTimeOut || bWaiting)
        {
            throw new InterruptedException();
        }
    }
    
    /**
     * <一句话功能简述>
     * 如果是消息模式下下载文件，则需要取头四个字节来判断错误码。
     * @param headData 头四个字节
     * @return [参数说明]
     * 
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private boolean isDownloadFileError(byte[] headData)
    {
        try
        {
            int errorCode = Util.bytesToInt(headData);
            
            LogX.getInstance().e(TAG, "head code ============= " + errorCode);
            
            if (1000 == errorCode)
            {
                return false;
            }
        }
        catch (Exception e)
        {
            LogX.getInstance().e(TAG, e.toString());
        }
        return true;
    }
    
    /**
     * 取消连接任务
     */
    public void cancelConnect()
    {
        canceled = true;
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
     * 创建下载文件目录  
     * @param context 上下文
     * @param fileLength 文件长度
     * @throws SDUnavailableException SD卡不可用的异常
     * @throws SDNotEnouchSpaceException SD卡空间不足的异常
     * @return 创建下载文件目录 是否成功
     */
    protected boolean createFile(final Context context, long fileLength)
            throws SDUnavailableException, SDNotEnouchSpaceException
    {
        boolean success = true;
        File tempFile = null;
        // 判断路径是否存在
        FileHelper.makeDir();
        try
        {
            LogX.getInstance().i(TAG, "downloadItem.savePath = " + filePath);
            tempFile = FileHelper.createDownloadFile(filePath, "");
            file = new RandomAccessFile(tempFile, "rw");
        }
        catch (IOException e)
        {
            success = false;
            setError(1015, "IOException");
        }
        catch (SDNotEnouchSpaceException e)
        {
            success = false;
            setError(GlobalConstant.LOW_MEMORY, "NotEnoughSpaceException");
        }
        return success;
    }
    
    /**
     * 关闭连接
     */
    protected void clearNet()
    {
        synchronized (sdSyn)
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
    }
    
    /**
     * 设置错误回调
     */
    private void setError(int responseCode, String exception)
    {
        if (httpListener != null)
        {
            httpListener.onError(responseCode, exception);
        }
    }
    
    /**
     * 设置取消回调
     */
    private void canceledCallback()
    {
        if (httpListener != null)
        {
            httpListener.canceledCallback();
        }
    }
    
    /**
     * 设置暂停回调
     */
    private void pausedCallback()
    {
        if (httpListener != null)
        {
            httpListener.pausedCallback();
        }
    }
    
    /**
     * 设置下载回调
     */
    private void startUploadCallback()
    {
        if (httpListener != null)
        {
            httpListener.taskStartCallback();
        }
    }
    
    /**
     * 下载完成回调通知接口
     */
    private void successUploadCallback()
    {
        if (httpListener != null)
        {
            httpListener.taskSuccessCallback(null);
        }
    }
    
    /**
     * 回调网络接收的及时数据长度
     * 
     * @param getLength
     *            已下载文件大小
     * @param totalLength
     *            文件的总大小
     */
    public void setDataLength(long getLength, long totalLength)
    {
        if (httpListener != null)
        {
            httpListener.onProgressChanged(getLength, totalLength);
        }
    }
    
    /**
     * 设置请求的url
     * 
     * @param httpUrl
     *            连接地址
     */
    public void setHttpUrl(String httpUrl)
    {
        this.httpUrl = httpUrl;
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
    
    private void setTimeOut()
    {
        if (handler != null)
        {
            handler.sendMessage(handler.obtainMessage(IHttpListener.HTTP_TIMEOUT,
                    reqType,
                    0,
                    null));
        }
    }
    
    public void setTimer(Timer timer)
    {
        this.timer = timer;
    }
    
    @Override
    public void setTimeoutTask(TimerTask timeoutTask)
    {
        this.timerTask = timeoutTask;
    }
    
    @Override
    public void startTimeoutTimer()
    {
        if (timer != null)
        {
            timer.schedule(timerTask, TIMEOUT);
        }
    }
    
    @Override
    public void stopTimeoutTimer()
    {
        if (timerTask != null)
        {
            timerTask.cancel();
        }
    }
    
    @Override
    public void onTaskResponse(int code)
    {
        setTimeOut();
    }
}