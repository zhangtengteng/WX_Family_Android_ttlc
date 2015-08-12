package com.xweisoft.wx.family.logic.request;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;

import com.xweisoft.wx.family.logic.global.GlobalVariable;
import com.xweisoft.wx.family.service.http.ConnectionTask;
import com.xweisoft.wx.family.service.http.IRequestCallback;
import com.xweisoft.wx.family.service.http.RequestTask;
import com.xweisoft.wx.family.util.LogX;

/**
 * <一句话功能简述>
 * Json请求
 * 
 * @author  administrator
 * @version  [版本号, 2013-10-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@SuppressLint("SimpleDateFormat")
public abstract class JsonRequest extends Request implements IRequestCallback
{
    /**
     * 日志类名
     */
    private static final String TAG = "===JsonRequest===";
    
    protected String timeStamp;
    
    /**
     * 请求类型，默认为POST
     */
    protected int requestType = 0;
    
    /**
     * 一级分类数据的类型（游戏/应用）
     */
    protected String dataType;
    
    /**
     * json对象，方便添加生成json字符串
     */
    private JSONObject jsonObj = null;
    
    /**
     * 构造器
     * @param context 上下文
     * @param handler 处理类
     * @param httpUrl 请求地址
     */
    public JsonRequest(final Context context, Handler handler, String httpUrl)
    {
        super(context, handler, httpUrl);
    }
    
    /**
     * 构造器
     * @param context 上下文
     * @param handler 处理类
     * @param httpUrl 请求地址
     * @param requestType 请求类型
     */
    public JsonRequest(final Context context, Handler handler, String httpUrl,
            int requestType)
    {
        super(context, handler, httpUrl);
        this.requestType = requestType;
    }
    
    /**
     * 构造器
     * @param context 上下文
     * @param handler 处理类
     * @param httpUrl 请求地址
     * @param requestType 请求类型
     * @param isFullAdd 请求地址是否完整 true完整 false不完整
     */
    public JsonRequest(final Context context, Handler handler, String httpUrl,
            int requestType, boolean flag)
    {
        super(context, handler, httpUrl, flag);
        this.requestType = requestType;
    }
    
    /**
     * 构造器
     * @param context 上下文
     * @param handler 处理类
     * @param httpUrl 请求地址
     * @param requestType 请求类型
     * @param methodType 请求方法
     */
    public JsonRequest(final Context context, Handler handler, String httpUrl,
            int requestType, int methodType)
    {
        super(context, handler, httpUrl, methodType);
        this.requestType = requestType;
    }
    
    /**
     * 拼装请求参数<BR>
     * @return 请求参数
     */
    protected abstract String createRequestPara();
    
    @Override
    /**
     * 接受网络层返回的数据的回调处理接口
     * @param data 需要解析处理的数据
     */
    public void onReceiveData(byte[] data)
    {
        String str = new String(data);
        LogX.getInstance().i(TAG, "respone:\r\n" + str);
        if (null != str && str.startsWith("\ufeff"))
        {
            str = str.substring(1);
        }
        parseJsonResponse(str);
    }
    
    /**
     * 处理响应的消息体
     * 
     * @param json 响应json
     */
    protected abstract void parseJsonResponse(String json);
    
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
                    LogX.getInstance().i(TAG, "post Data: " + req);
                    data = req.getBytes("utf-8");
                }
                catch (UnsupportedEncodingException e)
                {
                    LogX.getInstance().e(TAG, e.toString());
                }
                ct.setDataBuf(data);
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
     * 处理请求时间<BR>
     */
    protected void dealWithTimeStamp()
    {
        DateFormat gmtFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        TimeZone gmtTime = TimeZone.getTimeZone("UTC");
        gmtFormat.setTimeZone(gmtTime);
        Calendar calendar = Calendar.getInstance();
        timeStamp = gmtFormat.format(calendar.getTime());
        calendar = null;
    }
    
    /**
     * 获取设置完成的json参数字符串
     * <功能详细描述>
     * @return [参数说明]
     * 
     * @return String [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    protected String getJsonReqParams()
    {
        if (null == jsonObj)
        {
            jsonObj = new JSONObject();
        }
        
        return jsonObj.toString();
    }
    
    /**
     * 添加请求参数
     * <功能详细描述>
     * @param key
     * @param value [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    protected void addReqParams(String key, String value)
    {
        if (null == jsonObj)
        {
            jsonObj = new JSONObject();
        }
        
        try
        {
            jsonObj.put(key, value);
        }
        catch (JSONException e)
        {
            LogX.getInstance().e(TAG,
                    "add params exception ==> key=" + key + "; value=" + value
                            + e.toString());
        }
    }
}
