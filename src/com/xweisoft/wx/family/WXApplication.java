package com.xweisoft.wx.family;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.xweisoft.wx.family.logic.global.GlobalConstant;
import com.xweisoft.wx.family.logic.global.GlobalVariable;
import com.xweisoft.wx.family.logic.model.ApplyInfoItem;
import com.xweisoft.wx.family.logic.model.ChildrenItem;
import com.xweisoft.wx.family.logic.model.MsgContentItem;
import com.xweisoft.wx.family.logic.model.UserItem;
import com.xweisoft.wx.family.mina.MinaManager;
import com.xweisoft.wx.family.util.LogX;
import com.xweisoft.wx.family.util.SharedPreferencesUtil;
import com.xweisoft.wx.family.util.StringUtil;
import com.xweisoft.wx.family.util.UEHandler;

/**
 * 必须继承FrontiaApplication，供百度推送服务使用
 * <一句话功能简述> <功能详细描述>
 * 
 * @author administrator
 * @version [版本号, 2013-10-24]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class WXApplication extends Application
{
    
    private final String TAG = "===WXApplication===";
    
    /**
     * MobilyApplication实例
     */
    private static WXApplication instance;
    
    /**
     * 保存配置文件map
     */
    private Hashtable<String, String> systemInfo;
    
    /**
     * 应用程序请求地址
     */
    private String requestUrl;
    
    /**
     * https请求地址
     */
    private String requestHttpsUrl;
    
    /**
     * 异常处理类
     */
    private UEHandler ueHandler;
    
    /**
     * 用户对象
     */
    public UserItem loginUserItem;
    
    /**
     * 存放cookie的缓存list
     */
    public List<String> cookieList = new ArrayList<String>();
    
    /**
     * finishActivity管理,用于管理activity切换间，finish掉中间所有的activity
     */
    private List<Activity> finishActivities = new ArrayList<Activity>();
    
    /**
     * 即时消息管理,用于缓存消息编号，收到应答消息更新时间并移除
     */
    private List<String> imMsgIds = new ArrayList<String>();
    
    /**
     * 存放cookie的缓存map
     */
    public HashMap<String, String> cookieContiner = new HashMap<String, String>();
    
    /**
     * 普通图片配置类
     */
    public DisplayImageOptions options;
    
    /**
     * 圆形图片配置类
     */
    public DisplayImageOptions optionsCircle;
    
    /**
     * mina管理类
     */
    public MinaManager minaManager;
    
    /**
     * 消息中心 分类消息存储Map
     */
    public Map<String, MsgContentItem> notificationMsgMap = new HashMap<String, MsgContentItem>();
    
    public ApplyInfoItem applyInfoItem;
    
    public long lastTime;
    
    public String childrenClassIds;
    
    public String selectStudentId;
    
    /**
     * 登录选择的孩子
     */
    public ChildrenItem selectedItem;
    
    public String groupId;
    
    public boolean im_message_receive = true;;
    
    public boolean im_message_notifi_show_info = true;
    
    public boolean system_voice = true;
    
    public boolean system_vibrate = false;
    
    public static WXApplication getInstance()
    {
        return instance;
    }
    
    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
        //百度map
        SDKInitializer.initialize(getApplicationContext());
        systemInfo = new Hashtable<String, String>();
        
        // 设置异常处理实例
        ueHandler = new UEHandler();
        Thread.setDefaultUncaughtExceptionHandler(ueHandler);
        
        getUserPreference();
        
        // 获取系统初始化信息
        getSystemConfig();
        
        initPhoneParams(this);
        initImageLoader(getApplicationContext());
        getCookie();
        
        //        CCPAppManager.setContext(instance);
        
        //定义全局的图片配置类
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ysh_transparent_img)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
        optionsCircle = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ysh_transparent_img)
                .cacheInMemory(true)
                .displayer(new CircleBitmapDisplayer())
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
        
    }
    
    /**
     * 获取登录保存的cookie数据
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private void getCookie()
    {
        //获取本地cookie
        String cookieJsonStr = SharedPreferencesUtil.getSharedPreferences(getApplicationContext(),
                SharedPreferencesUtil.SP_KEY_LOGIN_COOKIE,
                "");
        try
        {
            JSONArray jsonArray = new JSONArray(cookieJsonStr);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                String cookie = jsonArray.getString(i);
                if (!StringUtil.isEmpty(cookie))
                {
                    int p = cookie.indexOf("=");
                    String key = cookie.substring(0, p);
                    String value = cookie.substring(p + 1, cookie.indexOf(";"));
                    WXApplication.getInstance().cookieContiner.put(key, value);
                }
            }
        }
        catch (JSONException e)
        {
        }
    }
    
    /**
     * 初始化图片加载工具类
     * <一句话功能简述>
     * <功能详细描述>
     * @param context [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static void initImageLoader(Context context)
    {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }
    
    /**
     * 启动时初始化系统UA
     * 
     * @param context
     */
    private void initPhoneParams(Context context)
    {
        WebView wv = new WebView(context);
        WebSettings webset = wv.getSettings();
        GlobalVariable.uA = webset.getUserAgentString();
        GlobalVariable.IMSI = ((android.telephony.TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getSubscriberId();
        GlobalVariable.IMEI = ((android.telephony.TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId();
    }
    
    /**
     * 获取系统信息，如服务器地址，桌面信息等
     */
    private void getSystemConfig()
    {
        InputStream is = getApplicationContext().getResources()
                .openRawResource(R.raw.system);
        try
        {
            String[] array = null;
            byte[] a = new byte[is.available()];
            is.read(a);
            String temp = new String(a);
            String[] temps = temp.split("\r\n");
            for (String string : temps)
            {
                array = string.split("==");
                systemInfo.put(array[0], array[1].replace("\r\n", ""));
            }
            // 设置请求地址
            requestUrl = systemInfo.get("yshpb_request_url");
            
            requestHttpsUrl = systemInfo.get("yshpb_https_request_url");
            String infoLog = systemInfo.get("yshpb_info_log_record");
            if ("1".equals(infoLog))
            {
                LogX.infoFlag = true;
            }
        }
        catch (IOException e)
        {
            LogX.getInstance().e(TAG, e.toString());
        }
    }
    
    public Hashtable<String, String> getSystemInfo()
    {
        return systemInfo;
    }
    
    public String getRequestUrl()
    {
        return requestUrl;
    }
    
    /**
     * 获取下载地址<BR>
     * 
     * @return 下载地址
     */
    public String getDownloadSavePath()
    {
        return GlobalConstant.DOWNLOAD_PATH_SD;
    }
    
    public String getRequestHttpsUrl()
    {
        return requestHttpsUrl;
    }
    
    public void setRequestHttpsUrl(String requestHttpsUrl)
    {
        this.requestHttpsUrl = requestHttpsUrl;
    }
    
    public void addFinishActivity(Activity activity)
    {
        if (!finishActivities.contains(activity))
        {
            finishActivities.add(activity);
        }
    }
    
    /**
     * 关闭所有切换之间需要finish的页面
     * <功能详细描述> [参数说明]
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void closedFinishActivity()
    {
        if (null != finishActivities && !finishActivities.isEmpty())
        {
            for (Activity a : finishActivities)
            {
                a.finish();
            }
            
            finishActivities.clear();
        }
    }
    
    public MinaManager getMinaManager()
    {
        return minaManager;
    }
    
    public void setMinaManager(MinaManager minaManager)
    {
        this.minaManager = minaManager;
    }
    
    public Map<String, MsgContentItem> getNotificationMsgMap()
    {
        return notificationMsgMap;
    }
    
    public ApplyInfoItem getApplyInfoItem()
    {
        return applyInfoItem;
    }
    
    public void setApplyInfoItem(ApplyInfoItem applyInfoItem)
    {
        this.applyInfoItem = applyInfoItem;
    }
    
    public List<String> getImMsgIds()
    {
        return imMsgIds;
    }
    
    /**
     * 获取设置信息
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private void getUserPreference()
    {
        im_message_receive = SharedPreferencesUtil.getSharedPreferences(getApplicationContext(),
                SharedPreferencesUtil.SP_KEY_MESSAGE_SET_RECEIVE,
                true);
        im_message_notifi_show_info = SharedPreferencesUtil.getSharedPreferences(getApplicationContext(),
                SharedPreferencesUtil.SP_KEY_MESSAGE_INFO_NOTIFI_SET_SHOW,
                true);
        system_voice = SharedPreferencesUtil.getSharedPreferences(getApplicationContext(),
                SharedPreferencesUtil.SP_KEY_SYSTEM_SET_VOICE,
                true);
        system_vibrate = SharedPreferencesUtil.getSharedPreferences(getApplicationContext(),
                SharedPreferencesUtil.SP_KEY_SYSTEM_SET_VIBRATE,
                false);
    }
}
