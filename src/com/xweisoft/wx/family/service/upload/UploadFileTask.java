package com.xweisoft.wx.family.service.upload;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.reflect.Method;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Handler;

import com.xweisoft.wx.family.WXApplication;
import com.xweisoft.wx.family.service.threadpool.TaskObject;
import com.xweisoft.wx.family.service.upload.ProgressMultipartEntity.ProgressListener;
import com.xweisoft.wx.family.util.ListUtil;

/**
 * 联网请求处理任务
 * <功能详细描述>
 * 
 * @author  administrator
 * @version  [版本号, 2014年5月13日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class UploadFileTask implements TaskObject
{
    private final static String CHARSET = "utf-8";
    
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
     * 上传任务是否完成
     */
    private boolean isUploadSuccess = false;
    
    /**
     * 文件存储路径
     */
    private String[] filePath;
    
    private int reqType;
    
    private TimerTask timerTask;
    
    private Timer timer;
    
    private int timeout = 30000;
    
    /**
     * 请求参数集合
     */
    private ArrayList<NameValuePair> postParams = null;
    
    /**
     * 默认构造方法
     */
    public UploadFileTask()
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
    public UploadFileTask(IHttpListener httpListener, String httpUrl,
            String filePath[])
    {
        this.httpListener = httpListener;
        this.httpUrl = httpUrl;
        this.filePath = filePath;
    }
    
    public ArrayList<NameValuePair> getPostParams()
    {
        return postParams;
    }
    
    /**
     * 增加上传参数
     * <功能详细描述>
     * @param key
     * @param value [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void addPostParams(String key, String value)
    {
        if (null == postParams)
        {
            postParams = new ArrayList<NameValuePair>();
        }
        this.postParams.add(new BasicNameValuePair(key, value + ""));
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
     * 运行联网任务
     */
    public void runTask()
    {
        try
        {
            // 通知任务开始下载
            isUploadSuccess = false;
            startUploadCallback();
            
            // 执行网络连接操作（发送HTTP请求，并处理网络返回数据）
            String result = connetionProcess();
            
            // sendUploadDoneRequest();
            
            // 如果上传成功，回调到上层处理
            if (isUploadSuccess)
            {
                successUploadCallback(result);
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
     * 断点续传时获取继续上传时的断点信息
     * 
     * @return 已经上传的数据量
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
     * 判断用户是否已删除上传任务
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
                    null));
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
        // LogTrace.trace(LogTrace.ERROR,
        // "handlerSocketException:" + exception.getMessage());
        if (handler != null)
        {
            handler.sendMessage(handler.obtainMessage(IHttpListener.HTTP_FAILED,
                    reqType,
                    0,
                    null));
        }
    }
    
    long fileLen;
    
    /**
     * 实现了联网写读功能
     * 
     * 异常类
     * 
     * @throws Error
     *             错误类
     */
    private String connetionProcess() throws Exception, Error
    {
        ArrayList<File> files = new ArrayList<File>();
        if (null != filePath)
        {
            for (int i = 0; i < filePath.length; i++)
            {
                File file = new File(filePath[i]);
                files.add(file);
            }
        }
        String result = "";
        //修改上传代码。
        PostMethod filePost = new PostMethod(httpUrl);// + "?" + URLEncoder.encode(sb.toString())
        int paramSize = ListUtil.isEmpty(postParams) ? 0 : postParams.size();
        int fileSize = ListUtil.isEmpty(files) ? 0 : files.size();
        Part[] parts = new Part[paramSize + fileSize];
        if (null != postParams && !postParams.isEmpty())
        {
            for (int i = 0; i < postParams.size(); i++)
            {
                NameValuePair pair = postParams.get(i);
                if (null != pair)
                {
                    StringPart spart = new StringPart(pair.getName(),
                            pair.getValue(), CHARSET);
                    parts[i] = spart;
                }
            }
        }
        
        if (!ListUtil.isEmpty(files))
        {
            for (int i = 0; i < files.size(); i++)
            {
                Part part = new FilePart("Filedata_" + i, files.get(i));
                parts[i + paramSize] = part;
                if (null != files.get(i))
                {
                    fileLen += files.get(i).length();
                }
            }
        }
        
        ProgressMultipartEntity mulentity = new ProgressMultipartEntity(parts,
                filePost.getParams());
        mulentity.setProgressListener(new ProgressListener()
        {
            @Override
            public void transferred(long num)
            {
                if (canceled)
                {
                    canceledCallback();
                    throw new RuntimeException();
                }
                setDataLength(num, fileLen);
            }
        });
        
        filePost.setRequestEntity(mulentity);
        
        //获取client
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager()
                .getParams()
                .setConnectionTimeout(30000);
        filePost.setRequestHeader("Cookie", addCookies());
        int status = client.executeMethod(filePost);
        byte[] bytes = filePost.getResponseBody();
        
        if (status == HttpStatus.SC_OK)
        {
            result = new String(bytes);
        }
        isUploadSuccess = true;
        return result;
    }
    
    public void test()
    {
        
    }
    
    /**
     * 设置错误回调
     */
    private void setError(int responseCode, String exception)
    {
        // LogTrace.trace(LogTrace.DEBUG, "the responseCode is " + responseCode
        // + " and the exception is " + exception);
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
    private void successUploadCallback(String result)
    {
        if (httpListener != null)
        {
            httpListener.taskSuccessCallback(result);
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
            timer.schedule(timerTask, timeout);
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
        
    }
}