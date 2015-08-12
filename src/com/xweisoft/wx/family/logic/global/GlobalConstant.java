package com.xweisoft.wx.family.logic.global;

/**
 * <一句话功能简述>
 * 常量类
 * 
 * @author  yangchao
 * @version  [版本号, 2013-10-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class GlobalConstant
{
    
    /**
     * 安装成功的action
     */
    public static final String ACTION_COM_XWEISOFT_LYGINFO_INSTALLED = "android.intent.com.xweisoft.lyginfo.installed";
    
    /**
     * 下载成功的action
     */
    public static final String ACTION_COM_XWEISOFT_LYGINFO_UNINSTALLED = "android.intent.com.xweisoft.lyginfo.uninstalled";
    
    /**
     * 通知更新下载进度条的标识
     */
    public static final int UPDATE_PROGRESSBAR = 1001;
    
    /**
     * 存储空间不足
     */
    public static final int LOW_MEMORY = 1014;
    
    /**
     * 下载url不合法，返回的url不含后缀名
     */
    public static final int URL_ILLAGELE = 1015;
    
    /**
     * mtn应用的包名
     */
    public static final String MTN_APP_PACKAGE_NAME = "com.xweisoft.wx.family";
    
    /**
     * SD卡路径
     */
    public static final String SDCARD_PATH = "/mnt/sdcard/";
    
    /**
     * sd卡目录
     */
    public static final String SDCARD_BASE_PATH = SDCARD_PATH
            + MTN_APP_PACKAGE_NAME + "/";
    
    /**
     * 文件缓存的目录
     */
    public static final String FILE_CACHE_DIR = GlobalConstant.SDCARD_BASE_PATH
            + "Cache/";
    
    /**
     * 图片缓存的目录
     */
    public static final String IMAGE_CACHE_DIR = GlobalConstant.SDCARD_BASE_PATH
            + "Image/";
    
    /**
     * 安装路径（SD卡）
     */
    public static final String INSTALL_PATH_SD = SDCARD_BASE_PATH + "Install/";
    
    /**
     * 升级包名称
     */
    public static final String INSTALL_FILE_NAME = "WXFamily_update_";
    
    /**
     * 保存路径（SD卡）
     */
    public static final String DOWNLOAD_PATH_SD = SDCARD_BASE_PATH
            + "DownLoad/";
    
    /**
     * 日志保存路径（SD卡）
     */
    public static final String LOG_PATH_SD = SDCARD_BASE_PATH + "Log/";
    
    /**
     * 应用图标的默认名称
     */
    public static final String APP_DEFAULT_ICON_NAME = "default.png";
    
    /**
     * 文件读写错误
     */
    public static final int IOEXCEPTION = 0x124342;
    
    /**
     * 默认屏幕的宽
     */
    public static final int DEFAULT_SCREEN_WIDTH = 0;
    
    /**
     * 默认屏幕的高
     */
    public static final int DEFAULT_SCREEN_HEIGHT = 0;
    
    /**
     * 刷新列表
     */
    public static final int DOWNLOADSUCESSPUSHLIST = 99999;
    
    /**
     * "3gwap"字符串常量
     */
    public static final String NETWORK_TYPE_WAP = "wap";
    
    /**
     * 安装成功发送handler消息标示
     */
    public static final int INSTALL_SUCESS = 0;
    
    /**
     * APP安装
     */
    public static final int INSTALL_APP = 1;
    
    /**
     *  下载文件除 zip,wgt,apk以为的不支持安装的类型
     */
    public static final int INSTALL_OTHER = 3;
    
    /**
     * AES加密密钥
     */
    public static final String AES_KEY = "odp5789421mobily";
    
    /**
     * 安装
     */
    public static final int BUTTON_TYPE_INSTALL = 2;
    
    /**
     * 已安装  status
     */
    public static final int BUTTON_TYPE_UNINSTALL = 1;
    
    /**
     * 图片文件
     */
    public static final int FILE_PHOTO = 0;
    
    /**
     * 视频文件
     */
    public static final int FILE_VIDEO = 1;
    
    /**
     * 设置消息管理SharePreferences
     */
    public static final String SETTING_MSG_MANAGE_PREFERENCE = "setting_msg_manage_preference";
    
    /**
     * 设置消息管理存储开关状态的key
     */
    public static final String SETTING__MSG_MANAGE_KEY = "msg_manage";
    
    /**
     * 音乐停止时间
     */
    public static final String SETTING_MUSIC_STOP_TIME_KEY = "music_stop_time";
    
    /**
     * 登录用户SharePreferences
     */
    public static final String LOGIN_USER_PREFERENCE = "login_user_preference";
    
    /**
     * 登录用户ID
     */
    public static final String LOGIN_USER_ID = "login_user_id";
    
    /**
     * 登录用户Name
     */
    public static final String LOGIN_USER_NAME = "login_user_name";
    
    /**
     * 登录用户Cookie
     */
    public static final String LOGIN_COOKIE = "login_user_cookie";
    
    /**
     * 登录用户UploadHash
     */
    public static final String LOGIN_UPLAODHASH = "login_user_uploadhash";
    
    /**
     * 用户信息
     * 
     * @author  李晨光
     * @version  [版本号, 2014年6月30日]
     * @see  [相关类/方法]
     * @since  [产品/模块版本]
     */
    public static class UserInfoPreference
    {
        /**
         * 首选项文件名
         */
        public static final String PREFERENCE_NAME = "userinfo_preference";
        
        /**
         * 用户ID
         */
        public static final String UID = "uid";
        
        /**
         * 手机号码
         */
        public static final String TELPHONE = "telphone";
        
        /**
         * 密码
         */
        public static final String PASSWORD = "password";
        
        /**
         * 性别
         */
        public static final String GENDER = "gender";
        
        /**
         * 头像
         */
        public static final String HEADER = "header";
        
        /**
         * 用户分组
         */
        public static final String CHILDREN = "children";
        
        /**
         * 班级id
         */
        public static final String CLASSINFOID = "classinfoid";
        
    }
    
    /**
     * 是否记住密码
     */
    public static final String REMEMBER_PASSWORD = "remember_pwd";
    
    /**
     * 男
     */
    public static final String MAN = "1";
    
    /**
     * 女
     */
    public static final String WOMAN = "0";
    
    /**
     * Android客户端类型是1 ios是2
     */
    public static final String CLIENT_TYPE_ANDROID = "1";
    
    /**
     * 系统消息 通知消息
     */
    public static final String NOTIFICATION_MESSAGE_SYSTEM = "sysMsg";
    
    /**
     * 学校消息 通知消息
     */
    public static final String NOTIFICATION_MESSAGE_SCHOOL = "schoolMsg";
    
    /**
     * 班级消息 通知消息
     */
    public static final String NOTIFICATION_MESSAGE_CLASS = "classMsg";
    
    /**
     * 班级作业 通知消息
     */
    public static final String NOTIFICATION_MESSAGE_WORK = "homeworkMsg";
    
    /**
     * 个人消息
     */
    public static final String IM_MESSAGE_PERSON = "imPerson";
    
    /**
     * 群组消息
     */
    public static final String IM_MESSAGE_GROUP = "imGroup";
    
    /**
     * 通知/消息广播
     */
    public static final String NOTIFICATION_MSG_RECEIVER = "wx_family_notification_msg";
    
}
