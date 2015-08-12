package com.xweisoft.wx.family.logic.request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.xweisoft.wx.family.logic.global.NetWorkCodes;
import com.xweisoft.wx.family.logic.model.response.CommonResp;

/**
 * 
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  administrator
 * @version  [版本号, 2013-11-7]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class JsonUtils
{
    /**
     * <一句话功能简述>
     * 返回  true为该name对应的值存在，   false则表示不存在
     * @param jsonObj json对象
     * @param name json对象的name
     * @return [参数说明]
     * 
     * @return boolean true为该name对应的值存在，   false则表示不存在
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean isJsonHaveName(JSONObject jsonObj, String name)
    {
        if (null != jsonObj)
        {
            return jsonObj.has(name);
        }
        return false;
    }
    
    /**
     * 获取JSON中的子JSON对象
     * @param jsonObject
     * @param name
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static JSONObject getJsonObjectValue(JSONObject jsonObject,
            String name)
    {
        JSONObject jsonObj = null;
        try
        {
            jsonObj = jsonObject.getJSONObject(name);
        }
        catch (Exception e)
        {
            return null;
        }
        return jsonObj;
    }
    
    /**
     * 获取json的int值
     * <功能详细描述>
     * @param strJson string对象
     * @param name json对象的name
     * @return [参数说明]
     * 
     * @return String 如果值存在则返回，不存在则返回""
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static int getJsonIntValue(String strJson, String name)
    {
        try
        {
            JSONObject jsonObj = new JSONObject(strJson);
            if (isJsonHaveName(jsonObj, name))
            {
                return jsonObj.getInt(name);
            }
        }
        catch (Exception e)
        {
            return 0;
        }
        return 0;
    }
    
    /**
     * 获取json的String值
     * <功能详细描述>
     * @param jsonObj json对象
     * @param name json对象的name
     * @return [参数说明]
     * 
     * @return String 如果值存在则返回，不存在则返回""
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static String getJsonStringValue(JSONObject jsonObj, String name)
    {
        if (isJsonHaveName(jsonObj, name))
        {
            try
            {
                return jsonObj.getString(name);
            }
            catch (JSONException e)
            {
                return "";
            }
        }
        return "";
    }
    
    /**
     * 获取json的int值
     * <功能详细描述>
     * @param jsonObj json对象
     * @param name json对象的name
     * @return [参数说明]
     * 
     * @return int 如果值存在则返回，不存在则返回0
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static int getJsonIntValue(JSONObject jsonObj, String name)
    {
        if (isJsonHaveName(jsonObj, name))
        {
            try
            {
                return jsonObj.getInt(name);
            }
            catch (JSONException e)
            {
                return 0;
            }
        }
        return 0;
    }
    
    /**
     * 获取json的long值
     * <功能详细描述>
     * @param jsonObj json对象
     * @param name json对象的name
     * @return [参数说明]
     * 
     * @return long 如果值存在则返回，不存在则返回0
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static long getJsonLongValue(JSONObject jsonObj, String name)
    {
        if (isJsonHaveName(jsonObj, name))
        {
            try
            {
                return jsonObj.getLong(name);
            }
            catch (JSONException e)
            {
                return 0;
            }
        }
        return 0;
    }
    
    /**
     * 获取json的long值
     * <功能详细描述>
     * @param jsonObj json对象
     * @param name json对象的name
     * @return [参数说明]
     * 
     * @return long 如果值存在则返回，不存在则返回0
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static JSONArray getJSONArrayValue(JSONObject jsonObj, String name)
    {
        if (isJsonHaveName(jsonObj, name))
        {
            try
            {
                return jsonObj.getJSONArray(name);
            }
            catch (JSONException e)
            {
                return null;
            }
        }
        return null;
    }
    
    /**
     * 获取JsonArray中指定索引的对象   name对应的String 值
     * <功能详细描述>
     * @param jsonObj jsonArray json对象
     * @param index 索引
     * @param name 指定index的jsonObject的name对应的value
     * @return [参数说明]
     * 
     * @return String [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static String getJsonStringValue(JSONArray jsonObj, int index,
            String name)
    {
        if (null != jsonObj && !jsonObj.isNull(index))
        {
            try
            {
                return ((JSONObject) jsonObj.get(index)).getString(name);
            }
            catch (JSONException e)
            {
                return "";
            }
        }
        return "";
    }
    
    /**
     * 判断返回的json对象是不是服务器返回的错误数据
     * <功能详细描述>
     * @param result json对象
     * @param resp 响应对象  只取 响应的code 和 message
     * 
     * @return void [返回类型说明]
     * @throws JSONException 
     * @throws NumberFormatException 
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static void checkHasVerifyError(CommonResp resp, JSONObject result)
            throws NumberFormatException, JSONException
    {
        if (result.has("resultCode"))
        {
            int resultCode = Integer.valueOf(result.getString("resultCode"));
            if (NetWorkCodes.CommontSuccessCodes.SUCCESS != resultCode
                    && result.has("resultDesc"))
            {
                resp.setMessage(result.getString("resultDesc"));
            }
            resp.setCode(result.getString("resultCode"));
        }
    }
}
