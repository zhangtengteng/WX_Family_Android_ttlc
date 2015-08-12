package com.xweisoft.wx.family.mina;

import java.util.ArrayList;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.text.TextUtils;

/**
 * mina工具类
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-6-1]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ClientUtil
{
    /**
     * 主机地址
     * 开发环境 172.20.146.225
     * 测试环境 
     * 开发环境 端口号 20177
     * 测试环境 端口号 20009
     */
    public static final String HOSTNAME = "172.20.146.225";
    
    /**
     * 端口号
     */
    //    public static final int PORT = 20009;
    public static final int PORT = 20177;
    
    /**
     * action 标识
     */
    public static final String ACTION_SHOW_NOTIFICATION = "wx_family_action_show_notification";
    
    /**
     * 系统消息
     */
    public static final int NOTIFICATION_SYSTEM = -1;
    
    /**
     * 学校消息
     */
    public static final int NOTIFICATION_SCHOOL = -2;
    
    /**
     * 班级消息
     */
    public static final int NOTIFICATION_CLASS = -3;
    
    /**
     * 班级作业
     */
    public static final int NOTIFICATION_HOMEWORK = -4;
    
    /**
     * 群组消息
     */
    public static final int NOTIFICATION_GROUP_MESSAGE = -5;
    
    /**
     * 通知内容
     */
    public static final String NOTIFICATION_CONTENT = "notification_content";
    
    /**
     * 判断service是否运行
     * <功能详细描述>
     * @return [参数说明]
     * 
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     * 修改人 gac
     * 修改方法传入参数 添加String serviceName
     */
    public static boolean isServiceRunning(Context activity, String serviceName)
    {
        if (null != activity && !TextUtils.isEmpty(serviceName))
        {
            ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
            ArrayList<RunningServiceInfo> services = (ArrayList<RunningServiceInfo>) manager.getRunningServices(100);
            RunningServiceInfo info = null;
            int size = services.size();
            for (int i = 0; i < size; i++)
            {
                info = services.get(i);
                if (null == info || null == info.service)
                {
                    continue;
                }
                if (serviceName.equals(info.service.getClassName()))
                {
                    return true;
                }
            }
        }
        return false;
    }
}
