package com.xweisoft.wx.family.util;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.xweisoft.wx.family.WXApplication;
import com.xweisoft.wx.family.logic.global.GlobalConstant;
import com.xweisoft.wx.family.logic.model.ChildrenItem;
import com.xweisoft.wx.family.logic.model.UserItem;
import com.xweisoft.wx.family.ui.pc.LoginActivity;

/**
 * 登录工具类
 * 
 * @author  poorgod
 * @version  [版本号, 2014年7月18日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class LoginUtil
{
    
    /**
     * 获取登录用户id
     * <一句话功能简述>
     * <功能详细描述>
     * @param context
     * @return [参数说明]
     * 
     * @return String [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static String getUserId(Context context)
    {
        if (null == context)
        {
            return null;
        }
        String userId = null;
        if (null != WXApplication.getInstance()
                && null != WXApplication.getInstance().loginUserItem)
        {
            userId = WXApplication.getInstance().loginUserItem.userId;
        }
        else
        {
            userId = SharedPreferencesUtil.getSharedPreferences(context,
                    GlobalConstant.UserInfoPreference.UID,
                    null);
        }
        return Util.checkNull(userId);
    }
    
    /**
     * 获取选择的孩子班级id
     * <一句话功能简述>
     * <功能详细描述>
     * @param context
     * @return [参数说明]
     * 
     * @return String [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static String getStudentId(Context context)
    {
        if (null == context)
        {
            return null;
        }
        String studentId = null;
        if (null != WXApplication.getInstance())
        {
            studentId = WXApplication.getInstance().selectStudentId;
        }
        else
        {
            studentId = SharedPreferencesUtil.getSharedPreferences(context,
                    GlobalConstant.UserInfoPreference.CHILDREN,
                    null);
        }
        return Util.checkNull(studentId);
    }
    
    /**
     * 获取选择的孩子班级id
     * <一句话功能简述>
     * <功能详细描述>
     * @param context
     * @return [参数说明]
     * 
     * @return String [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static String getClassinfoId(Context context)
    {
        if (null == context)
        {
            return null;
        }
        String classinfoId = null;
        if (null != WXApplication.getInstance()
                && null != WXApplication.getInstance().selectedItem)
        {
            classinfoId = WXApplication.getInstance().selectedItem.classinfoId;
        }
        else
        {
            classinfoId = SharedPreferencesUtil.getSharedPreferences(context,
                    GlobalConstant.UserInfoPreference.CLASSINFOID,
                    null);
        }
        return Util.checkNull(classinfoId);
    }
    
    /**
     * 判断是否登录
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static boolean isLogin(Context context)
    {
        UserItem user = WXApplication.getInstance().loginUserItem;
        if (null != user)
        {
            if (!TextUtils.isEmpty(user.userId))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        return false;
    }
    
    /**
     * 重新登录
     * @see [类、类#方法、类#成员]
     */
    public static void login(Context context)
    {
        
    }
    
    /**
     * 退出登录
     * @param context
     * @see [类、类#方法、类#成员]
     */
    public static void logout(Activity context)
    {
        boolean isRemember = SharedPreferencesUtil.getSharedPreferences(context,
                GlobalConstant.REMEMBER_PASSWORD,
                true);
        SharedPreferencesUtil.saveSharedPreferences(context,
                GlobalConstant.UserInfoPreference.UID,
                "");
        SharedPreferencesUtil.saveSharedPreferences(context,
                GlobalConstant.UserInfoPreference.CHILDREN,
                "");
        SharedPreferencesUtil.saveSharedPreferences(context,
                GlobalConstant.UserInfoPreference.CLASSINFOID,
                "");
        //        if (!isRemember)
        //        {
        SharedPreferencesUtil.saveSharedPreferences(context,
                GlobalConstant.UserInfoPreference.PASSWORD,
                "");
        //        }
        // 退出登录，清空TOKEN
        WXApplication.getInstance().loginUserItem = null;
        WXApplication.getInstance().cookieList.clear();
        WXApplication.getInstance().cookieContiner.clear();
        
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        
        NotificationManager manager = (NotificationManager) (context.getSystemService(Context.NOTIFICATION_SERVICE));
        manager.cancelAll();
        context.startActivity(intent);
        ((Activity) context).finish();
    }
    
    /**
     * 保存登录信息
     * @param context 上下文
     * @param userItem 用户对象
     * @param rememberPassword 是否记住密码
     * @see [类、类#方法、类#成员]
     */
    public static void saveLoginInfo(Context context, UserItem userItem)
    {
        if (null != userItem)
        {
            String uid = userItem.userId;
            String phone = userItem.phone;
            String password = userItem.password;
            String gender = userItem.parentSex;
            //            String children = userItem.children;
            
            SharedPreferencesUtil.saveSharedPreferences(context,
                    GlobalConstant.UserInfoPreference.TELPHONE,
                    phone);
            SharedPreferencesUtil.saveSharedPreferences(context,
                    GlobalConstant.UserInfoPreference.GENDER,
                    gender);
            if (SharedPreferencesUtil.getSharedPreferences(context,
                    GlobalConstant.REMEMBER_PASSWORD,
                    false))
            {
                SharedPreferencesUtil.saveSharedPreferences(context,
                        GlobalConstant.UserInfoPreference.UID,
                        uid);
                SharedPreferencesUtil.saveSharedPreferences(context,
                        GlobalConstant.UserInfoPreference.PASSWORD,
                        password);
                SharedPreferencesUtil.saveSharedPreferences(context,
                        GlobalConstant.UserInfoPreference.UID,
                        uid);
            }
            StringBuffer buffer = new StringBuffer();
            if (!ListUtil.isEmpty(userItem.children))
            {
                for (ChildrenItem item : userItem.children)
                {
                    if (null != item)
                    {
                        buffer.append(item.classinfoId + ",");
                    }
                }
            }
            WXApplication.getInstance().childrenClassIds = buffer.toString();
            // 登录成功后，同时更新全局变量中的用户信息
            WXApplication.getInstance().loginUserItem = userItem;
        }
    }
    
    /**
     * 获取用户信息
     * @param context 上下文
     * @see [类、类#方法、类#成员]
     */
    public static UserItem getLoginInfo(Context context)
    {
        
        UserItem saveUserItem = new UserItem();
        
        // 从首选项中读取用户信息
        String uid = SharedPreferencesUtil.getSharedPreferences(context,
                GlobalConstant.UserInfoPreference.UID,
                "");
        String telphone = SharedPreferencesUtil.getSharedPreferences(context,
                GlobalConstant.UserInfoPreference.TELPHONE,
                "");
        String password = SharedPreferencesUtil.getSharedPreferences(context,
                GlobalConstant.UserInfoPreference.PASSWORD,
                "");
        String gender = SharedPreferencesUtil.getSharedPreferences(context,
                GlobalConstant.UserInfoPreference.GENDER,
                "1");
        
        saveUserItem.userId = uid;
        saveUserItem.phone = telphone;
        saveUserItem.password = password;
        saveUserItem.parentSex = gender;
        //        saveUserItem.children = children;
        return saveUserItem;
    }
    
    /**
     * 获取验证登录参数的Map
     * <一句话功能简述>
     * <功能详细描述>
     * @return [参数说明]
     * 
     * @return Map<String,Object> [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static Map<String, Object> getCheckLoginParams()
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        String uid = "";
        UserItem userItem = WXApplication.getInstance().loginUserItem;
        if (null != userItem)
        {
            uid = userItem.userId;
        }
        map.put("uid", uid);
        return map;
    }
}
