/*
 * 文 件 名:  UploadUtil.java
 * 描    述:  上传工具类
 * 创 建 人:  李晨光
 * 创建时间:  2014年7月17日
 */
package com.xweisoft.wx.family.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.xweisoft.wx.family.logic.global.NetWorkCodes.CommontErrorCodes;
import com.xweisoft.wx.family.logic.global.NetWorkCodes.CommontSuccessCodes;

import android.os.Handler;

/**
 * 上传工具类
 * 
 * @author  李晨光
 * @version  [版本号, 2014年7月17日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class UploadUtil
{
    
    /**
     * 上传任务
     * @param url
     * @param filePath
     * @param handler
     * @see [类、类#方法、类#成员]
     */
    public static void uploadTask(final String url, final String filePath, final Handler handler)
    {
        new Thread(new Runnable()
        {
            
            @Override
            public void run()
            {
                getConnection(url, filePath, handler);
            }
        }).start();
    }
    
    /**
     * 建立连接
     * @param url
     * @param filePath
     * @param handler
     * @see [类、类#方法、类#成员]
     */
    private static void getConnection(String url, String filePath, Handler handler)
    {
        String json = "";
        
        try
        {
            HttpParams params = new BasicHttpParams();
            
            // 设置HTTP连接参数
            HttpConnectionParams.setConnectionTimeout(params, 15 * 1000);
            HttpConnectionParams.setSoTimeout(params, 15 * 1000);
            HttpConnectionParams.setSocketBufferSize(params, 8 * 1024);
            
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = null;
            
            // 创建HTTP POST请求对象，并指定报文编码为GBK
            HttpPost httpPost = new HttpPost(url.trim());
            
            byte[] fileBytes = getFileBytes(filePath);
            
            httpPost.setHeader("Content-Type", "application/octet-stream");
            
            // 将XML报文转换成字节流
            HttpEntity entity = new ByteArrayEntity(fileBytes);
            httpPost.setEntity(entity);
            response = client.execute(httpPost);
            
            json = EntityUtils.toString(response.getEntity(), "UTF-8");
            handler.sendMessage(handler.obtainMessage(CommontSuccessCodes.SUCCESS, json));
        }
        catch (ClientProtocolException e)
        {
            handler.sendMessage(handler.obtainMessage(CommontErrorCodes.NETWORK_TIMEOUT_ERROR));
        }
        catch (IOException e)
        {
            handler.sendMessage(handler.obtainMessage(CommontErrorCodes.NETWORK_ERROR));
        }
    }
    
    /**
     * 获得指定文件的byte数组
     */
    private static byte[] getFileBytes(String filePath)
    {
        byte[] buffer = null;
        try
        {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }
        catch (FileNotFoundException e)
        {
            
        }
        catch (IOException e)
        {
            
        }
        return buffer;
    }
}
