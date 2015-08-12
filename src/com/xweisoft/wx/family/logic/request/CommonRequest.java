package com.xweisoft.wx.family.logic.request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.zip.Inflater;

import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.xweisoft.wx.family.logic.global.GlobalVariable;
import com.xweisoft.wx.family.logic.global.NetWorkCodes.CommontErrorCodes;
import com.xweisoft.wx.family.logic.global.NetWorkCodes.CommontSuccessCodes;
import com.xweisoft.wx.family.logic.model.response.CommonResp;
import com.xweisoft.wx.family.service.http.ConnectionTask;
import com.xweisoft.wx.family.service.http.IRequestCallback;
import com.xweisoft.wx.family.service.http.RequestTask;
import com.xweisoft.wx.family.util.HttpRequestUtil;
import com.xweisoft.wx.family.util.LogX;

/**
 * 请求地址不变的请求类
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  guoac
 * @version  [版本号, 2015-1-17]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CommonRequest extends Request implements IRequestCallback
{
    /**
     * 日志类名
     */
    private static final String TAG = "===CommonRequest===";
    
    protected String timeStamp;
    
    /**
     * 请求类型，默认为POST
     */
    protected int requestType = 0;
    
    /**
     * 客户端请求数据集合
     */
    private Map<String, String> reqDataMap = null;
    
    /**
     * 服务器响应数据实体类
     */
    private Class<?> rspBeanClass = null;
    
    /**
     * 0 返回json字符串 / 1 请求为json字符串/ 其他正常 发送 解析
     */
    private int flag = -1;
    
    private boolean isConnectionWifi;
    
    /**
     * GET请求，四个参数
     * @param context 上下文
     * @param httpUrl HTTP接口路径
     * @param rspBeanClass 响应类对象
     * @param handler Handler处理器
     * @param wholeUrl 传递的地址是否完整(默认false不完整，需拼接)
     */
    public CommonRequest(Context context, String httpUrl,
            Class<?> rspBeanClass, boolean isConnectionWifi, Handler handler,
            boolean wholeUrl)
    {
        super(context, handler, httpUrl, wholeUrl);
        this.isConnectionWifi = isConnectionWifi;
        this.requestType = ConnectionTask.GET;
        this.rspBeanClass = rspBeanClass;
    }
    
    public CommonRequest(Context context, Handler handler, String httpUrl)
    {
        super(context, handler, httpUrl);
    }
    
    /**
     * GET请求，四个参数
     * @param context 上下文
     * @param httpUrl HTTP接口路径
     * @param rspBeanClass 响应类对象
     * @param handler Handler处理器
     */
    public CommonRequest(Context context, String httpUrl,
            Class<?> rspBeanClass, Handler handler)
    {
        super(context, handler, httpUrl, false);
        this.requestType = ConnectionTask.GET;
        this.rspBeanClass = rspBeanClass;
    }
    
    /**
     * GET请求，四个参数
     * @param context 上下文
     * @param httpUrl HTTP接口路径
     * @param rspBeanClass 响应类对象
     * @param handler Handler处理器
     * @param wholeUrl 传递的地址是否完整(默认false不完整，需拼接)
     */
    public CommonRequest(Context context, String httpUrl,
            Class<?> rspBeanClass, Handler handler, boolean wholeUrl)
    {
        super(context, handler, httpUrl, wholeUrl);
        this.requestType = ConnectionTask.GET;
        this.rspBeanClass = rspBeanClass;
    }
    
    /**
     * GET请求，四个参数
     * @param context 上下文
     * @param httpUrl HTTP接口路径
     * @param rspBeanClass 响应类对象
     * @param handler Handler处理器
     * @param flag 请求返回参数续作处理的标记
     */
    public CommonRequest(Context context, String httpUrl,
            Class<?> rspBeanClass, Handler handler, int flag)
    {
        super(context, handler, httpUrl, false);
        this.requestType = ConnectionTask.GET;
        this.rspBeanClass = rspBeanClass;
        this.flag = flag;
    }
    
    /**
     * GET请求，四个参数
     * @param context 上下文
     * @param httpUrl HTTP接口路径
     * @param rspBeanClass 响应类对象
     * @param handler Handler处理器
     * @param flag 请求返回参数续作处理的标记
     * @param wholeUrl 传递的地址是否完整(默认false不完整，需拼接)
     */
    public CommonRequest(Context context, String httpUrl,
            Class<?> rspBeanClass, Handler handler, int flag, boolean wholeUrl)
    {
        super(context, handler, httpUrl, wholeUrl);
        this.requestType = ConnectionTask.GET;
        this.rspBeanClass = rspBeanClass;
        this.flag = flag;
    }
    
    /**
     * POST请求，五个参数
     * @param context 上下文
     * @param httpUrl HTTP接口路径
     * @param reqDataMap 请求消息体Map对象
     * @param rspBeanClass 响应类对象
     * @param handler Handler处理器
     */
    public CommonRequest(Context context, String httpUrl,
            Map<String, String> reqDataMap, Class<?> rspBeanClass,
            Handler handler)
    {
        super(context, handler, httpUrl, false);
        this.requestType = ConnectionTask.POST;
        this.rspBeanClass = rspBeanClass;
        this.reqDataMap = reqDataMap;
    }
    
    /**
     * POST请求，五个参数
     * @param context 上下文
     * @param httpUrl HTTP接口路径
     * @param reqDataMap 请求消息体Map对象
     * @param rspBeanClass 响应类对象
     * @param handler Handler处理器
     * @param wholeUrl 传递的地址是否完整(默认false不完整，需拼接)
     */
    public CommonRequest(Context context, String httpUrl,
            Map<String, String> reqDataMap, Class<?> rspBeanClass,
            Handler handler, boolean wholeUrl)
    {
        super(context, handler, httpUrl, wholeUrl);
        this.requestType = ConnectionTask.POST;
        this.rspBeanClass = rspBeanClass;
        this.reqDataMap = reqDataMap;
    }
    
    /**
     * POST请求，五个参数
     * @param context 上下文
     * @param httpUrl HTTP接口路径
     * @param reqDataMap 请求消息体Map对象
     * @param rspBeanClass 响应类对象
     * @param handler Handler处理器
     * @param flag 请求返回参数续作处理的标记
     */
    public CommonRequest(Context context, String httpUrl,
            Map<String, String> reqDataMap, Class<?> rspBeanClass,
            Handler handler, int flag)
    {
        super(context, handler, httpUrl, false);
        this.requestType = ConnectionTask.POST;
        this.rspBeanClass = rspBeanClass;
        this.reqDataMap = reqDataMap;
        this.flag = flag;
    }
    
    /**
     * POST请求，五个参数
     * @param context 上下文
     * @param httpUrl HTTP接口路径
     * @param reqDataMap 请求消息体Map对象
     * @param rspBeanClass 响应类对象
     * @param handler Handler处理器
     * @param flag 请求返回参数续作处理的标记
     * @param wholeUrl 传递的地址是否完整(默认false不完整，需拼接)
     */
    public CommonRequest(Context context, String httpUrl,
            Map<String, String> reqDataMap, Class<?> rspBeanClass,
            Handler handler, int flag, boolean wholeUrl)
    {
        super(context, handler, httpUrl, wholeUrl);
        this.requestType = ConnectionTask.POST;
        this.rspBeanClass = rspBeanClass;
        this.reqDataMap = reqDataMap;
        this.flag = flag;
    }
    
    private String createRequestPara()
    {
        String params = null;
        StringBuffer buf = new StringBuffer();
        if (flag == HttpRequestUtil.REQUEST_JSON)
        {
            if (null != reqDataMap && reqDataMap.size() > 0)
            {
                Gson gson = new Gson();
                params = gson.toJson(reqDataMap);
                if (null != params)
                {
                    params = params.replace("\\\\", "\\")
                            .replace("\\\"", "\"")
                            .replace("\"[", "[")
                            .replace("]\"", "]");
                }
                LogX.getInstance().i(TAG, "post Data: " + params);
                //                params = SecurityUtil.encrypt(params);
            }
        }
        else
        {
            if (null != reqDataMap && reqDataMap.size() > 0)
            {
                for (Map.Entry<String, String> entry : reqDataMap.entrySet())
                {
                    if (null != entry)
                    {
                        buf.append(entry.getKey())
                                .append("=")
                                .append(entry.getValue());
                    }
                    buf.append("&");
                }
                buf.deleteCharAt(buf.length() - 1);
                LogX.getInstance().i(TAG, "post Data: " + buf.toString());
                //对请求的参数采用AES加密
                //                params = SecurityUtil.encrypt(buf.toString());
                params = buf.toString();
            }
        }
        return params;
    }
    
    private void parseJsonResponse(String response)
    {
        // 将服务器返回消息中的部分不可强制转换的字符串替换掉
        String json = stringReplace(response);
        Gson gson = new Gson();
        
        try
        {
            Object object = gson.fromJson(json, rspBeanClass);
            if (flag == HttpRequestUtil.RESPONSE_STRING)
            {
                if (((CommonResp) object).getCode().equals("200"))
                {
                    if (null != handler)
                    {
                        handler.sendMessage(handler.obtainMessage(CommontSuccessCodes.SUCCESS,
                                json));
                    }
                }
            }
            else
            {
                if (null != handler)
                {
                    handler.sendMessage(handler.obtainMessage(CommontSuccessCodes.SUCCESS,
                            object));
                }
            }
            
        }
        catch (Exception e) // 异常处理
        {
            if (null != handler)
            {
                handler.sendMessage(handler.obtainMessage(CommontErrorCodes.OTHER_ERROR));
            }
        }
    }
    
    @Override
    protected ConnectionTask createConnectionTask()
    {
        ConnectionTask ct = null;
        try
        {
            String req = createRequestPara();
            if (this.requestType == ConnectionTask.GET && null != req
                    && !"".equals(req))
            {
                httpUrl = httpUrl + "?" + req;//等服务器搭建好后要进行编码URLEncoder.encode(req);
            }
            ct = new RequestTask(context, this, httpUrl, MAX_CONNECTION_TIMEOUT);
            ct.setTimer(GlobalVariable.timer);
            ct.setRequestType(this.requestType);
            
            //如果请求类型是post时，添加体信息
            if (this.requestType == ConnectionTask.POST && null != req)
            {
                byte[] data = null;
                try
                {
                    data = req.getBytes("utf-8");
                }
                catch (UnsupportedEncodingException e)
                {
                    LogX.getInstance().e(TAG, e.toString());
                }
                ct.setDataBuf(data);
                ct.setDataStr(req);
            }
            
            LogX.getInstance().i(TAG,
                    "request type is: "
                            + (this.requestType == 1 ? "GET" : "POST"));
            LogX.getInstance().i(TAG, "httpUrl: " + httpUrl);
        }
        catch (Exception e)
        {
            LogX.getInstance().e(TAG, e.toString());
        }
        
        return ct;
    }
    
    /**
     * 字符串转换
     * @param response
     * @return
     * @see [类、类#方法、类#成员]
     */
    private String stringReplace(String response)
    {
        String json = response;
        
        // 这段代码针对服务器返回data为空字符串的时候导致GOSN无法类型转换问题进行的处理方案
        //当服务器返回"data":""时替换成"data":null，这样GOSN就不会出现类型转换异常了
        String emptyData = "\"data\":\"\"";
        String replaceData = "\"data\":null";
        
        // 这段代码针对服务器返回goods为空字符串的时候导致GOSN无法类型转换问题进行的处理方案
        //当服务器返回"goods":""时替换成"goods":null，这样GOSN就不会出现类型转换异常了
        String emptyGoods = "\"goods\":\"\"";
        String replaceGoods = "\"goods\":null";
        
        // 这段代码针对服务器返回child为空字符串的时候导致GOSN无法类型转换问题进行的处理方案
        //当服务器返回"child":""时替换成"child":null，这样GOSN就不会出现类型转换异常了
        String emptyChild = "\"child\":\"\"";
        String replaceChild = "\"child\":null";
        
        if (response.contains(emptyData))
        {
            json = response.replaceAll(emptyData, replaceData);
        }
        if (response.contains(emptyChild))
        {
            json = response.replaceAll(emptyChild, replaceChild);
        }
        if (response.contains(emptyGoods))
        {
            json = response.replaceAll(emptyGoods, replaceGoods);
        }
        
        return json;
    }
    
    @Override
    public void onReceiveData(byte[] data)
    {
        //对服务器返回的数据解压缩
        //        String str = new String(decompress(data));
        String str = new String(data);
        LogX.getInstance().i(TAG, "respone:\r\n" + str);
        if (null != str && str.startsWith("\ufeff"))
        {
            str = str.substring(1);
        }
        parseJsonResponse(str);
    }
    
    /**
     * zip解压缩
     * <一句话功能简述>
     * <功能详细描述>
     * @param data
     * @return [参数说明]
     * 
     * @return byte[] [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private byte[] decompress(byte[] data)
    {
        byte[] output = new byte[0];
        Inflater decompresser = new Inflater();
        decompresser.reset();
        decompresser.setInput(data);
        ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
        try
        {
            byte[] buf = new byte[1024];
            while (!decompresser.finished())
            {
                int i = decompresser.inflate(buf);
                o.write(buf, 0, i);
            }
            output = o.toByteArray();
        }
        catch (Exception e)
        {
            output = data;
            LogX.getInstance().e(TAG, e.toString());
        }
        finally
        {
            try
            {
                o.close();
            }
            catch (IOException e)
            {
                LogX.getInstance().e(TAG, e.toString());
            }
        }
        decompresser.end();
        return output;
    }
}
