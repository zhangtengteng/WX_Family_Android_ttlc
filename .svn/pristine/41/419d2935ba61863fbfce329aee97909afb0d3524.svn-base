package com.xweisoft.wx.family.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.xweisoft.wx.family.logic.request.CommonRequest;

/**
 * HTTP请求工具类
 * 
 * @author  李晨光
 * @version  [版本号, 2014年6月17日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class HttpRequestUtil
{
    
    private static final String TAG = "===HttpRequestUtil===";
    
    /**
     * 响应方式为 json字符串
     */
    public static final int RESPONSE_STRING = 0;
    
    /**
     * 请求方式为 json字符串
     */
    public static final int REQUEST_JSON = 1;
    
    /**
     * GET请求
     * @param context 上下文
     * @param httpUrl URL接口路径
     * @param rspClass 响应消息对象
     * @param handler Handler控制器
     * @see [类、类#方法、类#成员]
     */
    public static void sendHttpGetCommonRequest(Context context,
            String httpUrl, Class<?> rspClass, Handler handler)
    {
        new CommonRequest(context, httpUrl, rspClass, handler).sendHttpRequest();
    }
    
    /**
     * GET请求
     * @param context 上下文
     * @param httpUrl URL接口路径
     * @param rspClass 响应消息对象
     * @param handler Handler控制器
     * @see [类、类#方法、类#成员]
     */
    public static void sendHttpGetCommonRequest(Context context,
            String httpUrl, Class<?> rspClass, Handler handler, boolean wholeUrl)
    {
        new CommonRequest(context, httpUrl, rspClass, handler, wholeUrl).sendHttpRequest();
    }
    
    /**
     * GET请求
     * @param context 上下文
     * @param httpUrl URL接口路径
     * @param rspClass 响应消息对象
     * @param handler Handler控制器
     * @see [类、类#方法、类#成员]
     */
    public static void sendHttpGetCommonRequest(Context context,
            String httpUrl, Class<?> rspClass, Handler handler, int isJsonString)
    {
        new CommonRequest(context, httpUrl, rspClass, handler, isJsonString).sendHttpRequest();
    }
    
    /**
     * POST请求
     * @param context 上下文
     * @param httpUrl URL接口路径
     * @param reqMap 请求消息
     * @param rspClass 响应消息对象
     * @param handler Handler控制器
     * @see [类、类#方法、类#成员]
     */
    public static void sendHttpPostCommonRequest(Context context,
            String httpUrl, Map<String, String> reqMap, Class<?> rspClass,
            Handler handler)
    {
        new CommonRequest(context, httpUrl, reqMap, rspClass, handler).sendHttpRequest();
    }
    
    /**
     * POST请求
     * @param context 上下文
     * @param httpUrl URL接口路径
     * @param reqMap 请求消息
     * @param rspClass 响应消息对象
     * @param handler Handler控制器
     * @see [类、类#方法、类#成员]
     */
    public static void sendHttpPostCommonRequest(Context context,
            String httpUrl, Map<String, String> reqMap, Class<?> rspClass,
            Handler handler, boolean wholeUrl)
    {
        new CommonRequest(context, httpUrl, reqMap, rspClass, handler, wholeUrl).sendHttpRequest();
    }
    
    /**
     * POST请求
     * @param context 上下文
     * @param httpUrl URL接口路径
     * @param reqMap 请求消息
     * @param rspClass 响应消息对象
     * @param handler Handler控制器
     * @see [类、类#方法、类#成员]
     */
    public static void sendHttpPostCommonRequest(Context context,
            String httpUrl, Map<String, String> reqMap, Class<?> rspClass,
            Handler handler, int isJsonString)
    {
        new CommonRequest(context, httpUrl, reqMap, rspClass, handler,
                isJsonString).sendHttpRequest();
    }
    
    /**
     * 拼装URL路径
     * @param baseUrl 基本URL路径
     * @param paramsMap 路径后面的参数
     * @return 完整的URL路径
     * @see [类、类#方法、类#成员]
     */
    public static String getHttpUrl(String baseUrl,
            Map<String, String> paramsMap)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(baseUrl);
        int mapSize = paramsMap.size();
        if (null != paramsMap && mapSize > 0)
        {
            buffer.append("?");
            Iterator<String> iterator = paramsMap.keySet().iterator();
            int i = 0;
            while (iterator.hasNext())
            {
                String key = iterator.next();
                String value = paramsMap.get(key);
                buffer.append(key);
                buffer.append("=");
                buffer.append(value);
                if (i != mapSize - 1)
                {
                    buffer.append("&");
                }
                i++;
            }
        }
        return buffer.toString();
    }
    
    /**
     * 公共参数
     * <一句话功能简述>
     * <功能详细描述>
     * @return [参数说明]
     * 
     * @return Map<String,String> [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static Map<String, String> getCommonParams(String cmd)
    {
        Map<String, String> params = new HashMap<String, String>();
        if (!TextUtils.isEmpty(cmd))
        {
            params.put("CMD", cmd);
        }
        return params;
    }
    
    /**
     * 公共参数
     * <一句话功能简述>
     * <功能详细描述>
     * @return [参数说明]
     * 
     * @return Map<String,String> [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static Map<String, String> getUserCommonParams(Context context,
            String cmd)
    {
        Map<String, String> params = new HashMap<String, String>();
        if (!TextUtils.isEmpty(cmd))
        {
            params.put("CMD", cmd);
        }
        params.put("userId", Util.checkNull(LoginUtil.getUserId(context)));
        return params;
    }
}
