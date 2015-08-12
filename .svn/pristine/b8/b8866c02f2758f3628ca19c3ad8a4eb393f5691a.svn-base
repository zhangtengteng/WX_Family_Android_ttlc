package com.xweisoft.wx.family.logic.initialize;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.DisplayMetrics;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.logic.global.GlobalVariable;
import com.xweisoft.wx.family.util.LogX;
import com.xweisoft.wx.family.util.Util;

/**
 * <一句话功能简述>
 * 初始化工作类
 * 
 * @author  administrator
 * @version  [版本号, 2013-10-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public final class InitManager implements IInitManager
{
    private static final String TAG = "===InitManager===";
    
    /**
     * 单实例对象
     */
    private static InitManager instance;
    
    /**
     * 私有构造，单例模式
     */
    private InitManager()
    {
    }
    
    /**
     * 创建MainManager实例
     * @return    实例对象
     */
    public static InitManager getInstance()
    {
        if (instance == null)
        {
            instance = new InitManager();
        }
        return instance;
    }
    
    /**
     * 初始化客户端信息，如版本号，版本名字，屏幕分辨率等    
     * @param context 上下文
     */
    public void initClient(Context context)
    {
        try
        {
            PackageInfo info = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            GlobalVariable.versionCode = info.versionCode;
            GlobalVariable.versionName = info.versionName;
        }
        catch (NameNotFoundException e)
        {
            LogX.getInstance().e(TAG, e.getMessage());
        }
        
        getDisplay();
    }
    
    /**
     * 获取屏幕信息
     * 
     * @return String 分别率
     */
    public String getDisplay()
    {
        DisplayMetrics dm = new DisplayMetrics();
        GlobalVariable.currentActivity.getWindowManager()
                .getDefaultDisplay()
                .getMetrics(dm);
        GlobalVariable.screenWidth = dm.widthPixels;
        GlobalVariable.screenHeight = dm.heightPixels;
        GlobalVariable.densityDPI = dm.densityDpi;
        
        if (GlobalVariable.screenWidth > GlobalVariable.screenHeight)
        {
            GlobalVariable.screenWidth = dm.heightPixels;
            GlobalVariable.screenHeight = dm.widthPixels;
        }
        
        GlobalVariable.GRID_VIEW_ITEM_WIDTH = (int) ((GlobalVariable.screenWidth - GlobalVariable.currentActivity.getResources()
                .getDimension(R.dimen.category_two_textview_layout_space)) / 2);
        //设置首页和二级页面中的图片高和宽的比为1：0.836.
        GlobalVariable.GRID_VIEW_ITEM_HEIGHT = (int) (GlobalVariable.GRID_VIEW_ITEM_WIDTH * 0.836);
        
        GlobalVariable.screenSize = dm.widthPixels + "*" + dm.heightPixels;
        return GlobalVariable.screenSize;
    }
    
    /**
     * 获取屏幕信息
     * 
     * @return String 分别率
     */
    public void getDisplay(Activity activity)
    {
        if (null != activity)
        {
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            GlobalVariable.screenWidth = dm.widthPixels;
            GlobalVariable.screenHeight = dm.heightPixels;
            GlobalVariable.densityDPI = dm.densityDpi;
        }
    }
}
