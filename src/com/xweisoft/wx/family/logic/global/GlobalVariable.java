package com.xweisoft.wx.family.logic.global;

import java.util.Timer;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;

import com.xweisoft.wx.family.logic.download.DownloadInstallHandler;

/**
 * 
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  administrator
 * @version  [版本号, 2013-10-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class GlobalVariable
{
    /**
     * 下载管理添加新下载任务的插入方式 0: 插入末端 1: 插入首端
     */
    public static int downloadTaskInsertType = 1;
    
    /**
     * 
     */
    public static int CATEGORY_ID = 0;
    
    /**
     * 是否更新足迹
     */
    public static boolean isUpdateHistory = true;
    
    /**
     * 网路是否连接
     */
    public static boolean isNetConnected = true;
    
    /**
     * 系统版本号
     */
    public static int versionCode = 1;
    
    /**
     * 系统版本号
     */
    public static String versionName = "1.0.0";
    
    /**
     * sd卡是否存在的标志
     */
    public static boolean sdCardIsExist = true;
    
    /**
     * 定义handelr对象，用于安装应用完成时，改变列表UI
     */
    public static DownloadInstallHandler installReceiverHandler;
    
    /**
     * 下载线程池中添加新下载任务的插入方式 0: 插入首端 1: 插入末端
     */
    public static int downloadTaskStartPriority = 1;
    
    /**
     * 保存定位到的地址(详细地址)
     */
    public static String GPS_ADDRESS_DETAILE = "";
    
    /**
     * 经度
     */
    public static double GPS_LONGITUDE = 119.197128;
    
    /**
     * 维度
     */
    public static double GPS_LATITUDE = 34.606120;
    
    /**
     * 屏幕大小
     */
    public static String screenSize = GlobalConstant.DEFAULT_SCREEN_WIDTH + "*"
            + GlobalConstant.DEFAULT_SCREEN_HEIGHT;
    
    /**
     * 下载管理器中最大可同时下载的任务数
     */
    public static int maxDownloadCount = 3;
    
    /**
     * 全局唯一定时器
     */
    public static Timer timer = new Timer();
    
    /**
     * 屏幕的宽
     */
    public static int screenWidth = 0;
    
    /**
     * 屏幕的高
     */
    public static int screenHeight = 0;
    
    /**
     * 状态栏高度
     */
    public static int statusBarHeight;
    
    /**
     * 九宫格中每一个item的宽度
     */
    public static int GRID_VIEW_ITEM_WIDTH = (480 - 50) / 2;
    
    /**
     * 九宫格中每一个item的高度
     */
    public static int GRID_VIEW_ITEM_HEIGHT = (int) (GRID_VIEW_ITEM_WIDTH * 0.8);
    
    /**
     * 当前分辨率的缩放比
     */
    public static float density;
    
    /**
     * 当前设备密度
     */
    public static float densityDPI;
    
    /**
     * 保存当前的Activity
     */
    public static Activity currentActivity;
    
    /**
     * 系统User Agent
     */
    public static String uA = "";
    
    /**
     * 横屏grid宽
     */
    public static int mtnLandGridWidth = 0;
    
    /**
     * 横屏scroll宽
     */
    public static int mtnLandScrollWidth = 0;
    
    /**
     * 竖屏grid宽
     */
    public static int mtnPortGridWidth = 0;
    
    /**
     * 竖屏scroll宽
     */
    public static int mtnPortScrollWidth = 0;
    
    /**
     * 是否允许将日志写入文件
     */
    public static boolean isAllowWriteLogFile = true;
    
    /**
     * 第一次登陆下载用户信息标记
     */
    public static boolean queryUserInfo;
    
    /**
     * 设置界面消息设置是否开启
     */
    public static boolean SET_MSG_MANAGE_IS_OPEN = true;
    
    /**
     * 正在播放的歌曲
     */
    public static int playSongId = 0;
    
    /**
     * 默认分页大小
     */
    public static int songPageSize = 5;
    
    /**
     * 默认第几页
     */
    public static int songPageNum = 1;
    
    /**
     * 记录当前播放界面页面标志
     */
    public static int currentFromPage = 0;
    
    /**
     * 是否发送下一首分页请求
     */
    public static boolean isSendPageRequest = false;
    
    /**
     * viewPager 是否在自动循环
     */
    public static boolean isViewPagerRun = false;
    
    /**
     * viewPager 是否有触摸事件
     */
    public static boolean isViewPagerDown = false;
    
    /**
     *  国际移动设备身份码
     */
    public static String IMEI = "";
    
    /**
     * 国际移动用户识别码
     */
    public static String IMSI = "";
    
    /**
     * 设置对话框的按键事件
     */
    public static OnKeyListener onKeyListener = new OnKeyListener()
    {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
        {
            if (keyCode == KeyEvent.KEYCODE_BACK
                    || keyCode == KeyEvent.KEYCODE_SEARCH)
            {
                
                return true;
            }
            return false;
        }
    };
    
}
