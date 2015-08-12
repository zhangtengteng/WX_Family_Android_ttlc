package com.xweisoft.wx.family.logic.request;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.xweisoft.wx.family.logic.global.GlobalConstant;
import com.xweisoft.wx.family.logic.global.NetWorkCodes.CommontErrorCodes;
import com.xweisoft.wx.family.logic.global.NetWorkCodes.CommontSuccessCodes;
import com.xweisoft.wx.family.service.http.IHttpCallback;
import com.xweisoft.wx.family.service.http.SDKUtil;
import com.xweisoft.wx.family.service.httpClient.NetUtil;
import com.xweisoft.wx.family.util.LogX;

/**
 * 下载升级包
 * <功能详细描述>
 * @author  administrator
 * @version  [版本号, 2013-7-15]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class DownClientApkRequest implements IHttpCallback
{
    /**
     * 日志类名
     */
    private static final String TAG = "===DownClientApkRequest===";
    
    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
    
    /**
     * 网络超时时间
     */
    protected static final int MAX_CONNECTION_TIMEOUT = 30000;
    
    private Context mContext;
    
    private Handler handler;
    
    private String httpUrl;
    
    /**
     * 当前升级版本号。
     */
    private String versionCode = "";
    
    /**
     * 代理地址
     */
    private String hostUrl = "10.0.0.172";
    
    /**
     * 代理端口
     */
    private int hostPort = 80;
    
    /**
     * 构造器
     * @param context 上下文
     * @param handler 处理类
     * @param httpUrl 请求地址
     * 
     * modify by gac 2013-7-16
     * add
     * @param versionCode 升级获取的版本号 
     */
    public DownClientApkRequest(final Context context, Handler handler,
            String httpUrl, String versionCode)
    {
        mContext = context;
        this.handler = handler;
        this.httpUrl = httpUrl;
        this.versionCode = versionCode;
    }
    
    /**
     * 实现了联网写读功能
     */
    public void connetionProcess()
    {
        if (SDKUtil.isSDKAbove9())
        {
            httpURLConnectionProcess();
        }
        else
        {
            connetionClientProcess();
        }
    }
    
    /**
     * 实现了联网写读功能
     */
    public void connetionClientProcess()
    {
        LogX.getInstance().i(TAG, "start download apk: " + httpUrl);
        int responseCode = -1;
        try
        {
            
            HttpEntity entity = NetUtil.downloadUpdateUrl(httpUrl);
            
            // 读取数据
            if (null != entity)
            {
                InputStream is = entity.getContent();
                readData(is, entity.getContentLength());
                is.close();
            }
        }
        
        catch (SocketTimeoutException e)
        {
            onTimeOut(responseCode, e.getMessage());
        }
        
        catch (IOException e)
        {
            onConnError(responseCode, e.getMessage());
        }
    }
    
    /**
     * 实现了联网写读功能
     */
    public void httpURLConnectionProcess()
    {
        LogX.getInstance().i(TAG, "start download apk: " + httpUrl);
        int responseCode = -1;
        try
        {
            HttpURLConnection conn;
            // 构建连接
            URL url = new URL(httpUrl);
            
            //            if (isNeedProxy(mContext))
            //            {
            //                // if wap
            //                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(hostUrl, hostPort));
            //                conn = (HttpURLConnection) url.openConnection(proxy);
            //                LogX.getInstance().i(TAG, "connetionProcess.use proxy: " + hostUrl + ":" + hostPort);
            //            }
            //            else
            //            {
            conn = (HttpURLConnection) url.openConnection();
            //            }
            
            conn.setConnectTimeout(MAX_CONNECTION_TIMEOUT);
            conn.setReadTimeout(MAX_CONNECTION_TIMEOUT);
            
            // 不使用Cache
            conn.setUseCaches(false);
            
            conn.setRequestMethod("GET");
            
            HttpURLConnection.setFollowRedirects(false);
            
            // 发送POST请求必须设置允许输出
            //必须设置为false ，否则请求会返回405.
            conn.setDoOutput(false);
            
            conn.setRequestProperty("Content-Type", "text/xml");
            conn.setRequestProperty("Accept", "*/*");
            // end add
            // 维持长连接
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Accept-Encoding", "identity");
            
            responseCode = conn.getResponseCode();
            LogX.getInstance().i(TAG, "responseCode:" + responseCode);
            
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
                // 支持302下载，获取Location重新去下载
                case HttpStatus.SC_MOVED_TEMPORARILY:
                    String location = conn.getHeaderField("Location");
                    if (location != null && location.startsWith("http://"))
                    {
                        LogX.getInstance().d(TAG,
                                "before location url = " + httpUrl);
                        httpUrl = location.replaceAll("&amp;", "&");
                        LogX.getInstance().d(TAG,
                                "after location url = " + httpUrl);
                        connetionProcess();
                        // 302下载解析到location后，中断第一次解析，开始去下载location的地址
                        return;
                    }
                    break;
            }
            
            // 读取数据
            if (null != conn)
            {
                InputStream is = conn.getInputStream();
                readData(is, conn.getContentLength());
                is.close();
                conn.disconnect();
            }
        }
        
        catch (SocketTimeoutException e)
        {
            onTimeOut(responseCode, e.getMessage());
        }
        
        catch (IOException e)
        {
            onConnError(responseCode, e.getMessage());
        }
    }
    
    /**
     * 读网络数据
     * 
     * @param is
     *            输入流
     * @param contentLength
     *            输入流字节长度
     */
    protected void readData(InputStream is, long contentLength)
    {
        try
        {
            File installDir = null;
            File apkFile = null;
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    && hasExternalStoragePermission(mContext))
            {
                installDir = new File(GlobalConstant.DOWNLOAD_PATH_SD);
            }
            if (installDir == null)
            {
                String dir = null;
                if (null != Environment.getExternalStorageDirectory()
                        .getParentFile()
                        && null != Environment.getExternalStorageDirectory()
                                .getParentFile()
                                .listFiles())
                {
                    dir = Environment.getExternalStorageDirectory()
                            .getParentFile()
                            .listFiles()[0]
                            + "/" + mContext.getPackageName() + "/cache/";
                }
                if (dir == null)
                {
                    Toast.makeText(mContext,
                            "抱歉！没有sd卡无法更新升级",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                installDir = new File(dir);
            }
            apkFile = new File(installDir, GlobalConstant.INSTALL_FILE_NAME
                    + versionCode + ".apk");
            if (!installDir.exists())
            {
                installDir.mkdirs();
            }
            if (apkFile.exists())
            {
                apkFile.delete();
            }
            OutputStream out = new FileOutputStream(apkFile);
            byte[] data = new byte[1024];
            int length = -1;
            long totalLength = 0;
            LogX.getInstance().i(TAG, "start read data");
            int currentProgress = 0;
            int preProgress = 0;
            while ((length = is.read(data)) > 0)
            {
                out.write(data, 0, length);
                out.flush();
                totalLength += length;
                currentProgress = (int) (totalLength * 100 / contentLength);
                if (currentProgress != preProgress)
                {
                    preProgress = currentProgress;
                    sendMessage(GlobalConstant.UPDATE_PROGRESSBAR, preProgress);
                    LogX.getInstance().i(TAG,
                            "read data percent: " + preProgress);
                }
            }
            
            LogX.getInstance().i(TAG, "read data finished");
            out.close();
            
            if (apkFile.exists())
            {
                LogX.getInstance().i(TAG, "download upgrade apk success");
                Intent it = new Intent(Intent.ACTION_VIEW);
                it.setDataAndType(Uri.parse("file://" + apkFile.getPath()),
                        "application/vnd.android.package-archive");
                it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                sendMessage(CommontSuccessCodes.SUCCESS, it);
            }
            else
            {
                LogX.getInstance().i(TAG, "download upgrade apk fail1");
                sendMessage(GlobalConstant.IOEXCEPTION);
            }
        }
        catch (FileNotFoundException e)
        {
            LogX.getInstance().i(TAG, "download upgrade apk fail2");
            LogX.getInstance().e(TAG, e.toString());
            sendMessage(GlobalConstant.IOEXCEPTION);
        }
        catch (IOException e)
        {
            LogX.getInstance().i(TAG, "download upgrade apk fail3");
            LogX.getInstance().e(TAG, e.toString());
            sendMessage(GlobalConstant.IOEXCEPTION);
        }
    }
    
    /**
     * [handler发送消息]<BR>
     * @param what
     * @param arg1
     * @param arg2
     * @param obj
     */
    private void sendMessage(int what, int arg1, int arg2, Object obj)
    {
        if (this.handler != null)
        {
            Message msg = new Message();
            msg.what = what;
            msg.arg1 = arg1;
            msg.arg2 = arg2;
            msg.obj = obj;
            handler.sendMessage(msg);
        }
    }
    
    /**
     * [handler发送消息]<BR>
     * @param what
     */
    private void sendMessage(int what)
    {
        sendMessage(what, 0, 0, null);
    }
    
    /**
     * [handler发送消息]<BR>
     * @param what
     * @param obj
     */
    private void sendMessage(int what, Object obj)
    {
        sendMessage(what, 0, 0, obj);
    }
    
    /**
     * [handler发送消息]<BR>
     * @param what
     * @param arg1
     */
    private void sendMessage(int what, int arg1)
    {
        sendMessage(what, arg1, 0, null);
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
     * 超时处理
     * 
     * @param code
     *            状态码
     * @param message
     *            异常信息
     */
    public void onTimeOut(int code, String message)
    {
        sendMessage(CommontErrorCodes.NETWORK_TIMEOUT_ERROR, message);
    }
    
    /**
     * 网络不可用处理
     * 
     * @param code
     *            状态码
     * @param message
     *            异常信息
     */
    public void onConnError(int code, String message)
    {
        sendMessage(CommontErrorCodes.NETWORK_ERROR, message);
    }
    
    /**
     * 其他错误回调函数
     * 
     * @param code
     *            状态码
     * @param message
     *            错误消息
     */
    public void onError(int code, String message)
    {
        
    }
    
    private static boolean hasExternalStoragePermission(Context context)
    {
        int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
        return perm == PackageManager.PERMISSION_GRANTED;
    }
    
}
