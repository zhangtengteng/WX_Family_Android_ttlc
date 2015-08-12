package com.xweisoft.wx.family.util;

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * <一句话功能简述>
 * SharedPreferences统一管理类
 * 
 * @author  administrator
 * @version  [版本号, 2013-10-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class SharedPreferencesUtil
{
    /**
     * 登陆cookie
     */
    public static final String SP_KEY_LOGIN_COOKIE = "sp_key_login_cookie";
    
    /**
     * 宣传界面sharepreference的name
     */
    public static final String GUIDE_VIEW_PREFERENCE_NAME = "guide_view_preference_flag";
    
    /**
     * 启动图片url
     */
    public static final String SP_KEY_LOAD_IMAGE_URL = "sp_key_load_image_url";
    
    /**
     * 启动图片url
     */
    public static final String SP_KEY_DOWNLOAD_URL = "sp_key_download_url";
    
    /**
     * 消息设置新消息通知
     */
    public static final String SP_KEY_MESSAGE_SET_RECEIVE = "sp_key_message_set_receive";
    
    /**
     * 消息设置显示详情
     */
    public static final String SP_KEY_MESSAGE_INFO_NOTIFI_SET_SHOW = "sp_key_message_info_notifi_set_show";
    
    /**
     * 消息设置声音
     */
    public static final String SP_KEY_SYSTEM_SET_VOICE = "sp_key_system_set_voice";
    
    /**
     * 消息设置震动
     */
    public static final String SP_KEY_SYSTEM_SET_VIBRATE = "sp_key_system_set_vibrate";
    
    /**
     * 搜索数据缓存
     */
    public static final String SP_KEY_SEARCH_HISTORY_DATA = "sp_key_search_history_data";
    
    /**
     * 获取默认的SharedPreferences
     * @param context
     * @return    
     */
    private static SharedPreferences getSharedPreferences(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
    
    /**
     * 
     * 保存设置<BR>
     * 保存一些常量到SharedPreferences
     * @param context
     * @param map
     */
    public static void saveSharedPreferences(Context context,
            Map<String, String> map)
    {
        if (map == null || map.isEmpty())
        {
            return;
        }
        
        SharedPreferences sp = getSharedPreferences(context);
        Editor editor = sp.edit();
        for (String key : map.keySet())
        {
            editor.putString(key, map.get(key));
        }
        editor.commit();
    }
    
    /**
     * 
     * 保存设置<BR>
     * 保存一些常量到SharedPreferences
     * @param key 键
     * @param value 要保存的值
     */
    public static void saveSharedPreferences(Context context, String key,
            String value)
    {
        SharedPreferences sp = getSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }
    
    /**
     * 
     * 保存设置<BR>
     * 保存一些常量到SharedPreferences
     * @param key 键
     * @param value 要保存的值
     */
    public static void saveSharedPreferences(Context context, String key,
            boolean value)
    {
        SharedPreferences sp = getSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    
    /**
     * 
     * 获得保存到SharedPreferences的常量<BR>
     * 
     * @param context
     * @param key 键
     * @param defaultValue 默认值
     * @return 保存在SharedPreferences中键所对应的值
     */
    public static String getSharedPreferences(Context context, String key,
            String defaultValue)
    {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getString(key, defaultValue);
    }
    
    /**
     * 
     * 获得保存到SharedPreferences的常量<BR>
     * 
     * @param context
     * @param key 键
     * @param defaultValue 默认值
     * @return 保存在SharedPreferences中键所对应的值
     */
    public static boolean getSharedPreferences(Context context, String key,
            boolean defaultValue)
    {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getBoolean(key, defaultValue);
    }
    
}
