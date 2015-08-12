package com.xweisoft.wx.family.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.logic.global.GlobalVariable;
import com.xweisoft.wx.family.service.http.SDKUtil;
import com.xweisoft.wx.family.widget.MarqueeView;

/**
 * <一句话功能简述>
 * activity抽象基类
 * 
 * @author  administrator
 * @version  [版本号, 2013-10-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public abstract class BaseActivity extends FragmentActivity
{
    /**
     * 图片加载类
     */
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    
    protected Context mContext;
    
    /**
     * 预定服务广告滚动view
     */
    public MarqueeView mMarqueeView;
    
    /**
     * 提示
     */
    private Toast mToast;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        GlobalVariable.currentActivity = this;
        mContext = this;
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        if (hasFocus && !SDKUtil.isSDKAbove10())
        {
        }
        super.onWindowFocusChanged(hasFocus);
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        GlobalVariable.currentActivity = this;
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putBoolean("destroied", true);
        super.onSaveInstanceState(outState);
    }
    
    /**
     * 横竖屏切换参数设置<BR>
     * @param newConfig 配置参数
     * @see android.app.Activity#onConfigurationChanged(android.content.res.Configuration)
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }
    
    @Override
    protected void onPause()
    {
        super.onPause();
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
    
    @Override
    protected void onStart()
    {
        super.onStart();
    }
    
    @Override
    protected void onStop()
    {
        super.onStop();
    }
    
    /**
     * 跳转到其他界面
     * 
     * @param cls 跳转页面
     * @param bundle Bundle参数
     * @param isReturn 是否返回
     * @param requestCode 请求Code
     * @param isFinish 是否销毁当前页面    
     */
    protected void jumpToPage(Class<?> cls, Bundle bundle, boolean isReturn,
            int requestCode, boolean isFinish)
    {
        if (cls == null)
        {
            return;
        }
        
        Intent intent = new Intent();
        intent.setClass(this, cls);
        
        if (bundle != null)
        {
            intent.putExtras(bundle);
        }
        
        if (isReturn)
        {
            startActivityForResult(intent, requestCode);
        }
        else
        {
            startActivity(intent);
        }
        
        if (isFinish)
        {
            finish();
        }
    }
    
    /** 
     * 跳转到其他界面
     * 
     * @param cls 跳转页面
     * @param bundle Bundle参数
     * @param isFinish 是否销毁当前页面       
     */
    protected void jumpToPage(Class<?> cls, Bundle bundle, boolean isFinish)
    {
        jumpToPage(cls, bundle, false, 0, isFinish);
    }
    
    /** 
     * 跳转到其他界面，不销毁当前页面，也不传参数
     * 
     * @param cls 跳转页面
     */
    protected void jumpToPage(Class<?> cls)
    {
        jumpToPage(cls, null, false, 0, false);
    }
    
    /**
     * 判断当前activity是否存在
     * 
     * @param cls 类
     * @return  是否和当前页面相同
     */
    protected boolean isCurrentActivity(Class<?> cls)
    {
        if (GlobalVariable.currentActivity != null
                && GlobalVariable.currentActivity.getClass().equals(cls))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /** <一句话功能简述>
     * 显示软件盘
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    protected void showSoftInputFromWindow()
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
    }
    
    /**
     * 关闭软键盘
     * 
     * @param activity 当前页面
     */
    protected void hideSoftInputFromWindow(EditText editText)
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
    
    /**
     * 初始化控件    
     */
    public abstract void initViews();
    
    /**
     * 绑定控件事件    
     */
    public abstract void bindListener();
    
    /**
     * 返回activity的layout id
     * @return 布局文件id 
     */
    public abstract int getActivityLayout();
    
    /**
     * 显示Toast提示信息
     * @param message
     * @see [类、类#方法、类#成员]
     */
    public void showToast(String message)
    {
        if (mToast == null)
        {
            mToast = Toast.makeText(getApplicationContext(),
                    message,
                    Toast.LENGTH_SHORT);
        }
        else
        {
            mToast.setText(message);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
    
    /**
     * DIP转PX
     * @param context
     * @param dipValue
     * @return
     * @see [类、类#方法、类#成员]
     */
    public int dip2px(float dipValue)
    {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    
    /**
     * PX转DIP
     * @param context
     * @param pxValue
     * @return
     * @see [类、类#方法、类#成员]
     */
    public int px2dip(float pxValue)
    {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    
    /**
     * 此方法用于解决ListView显示不完全
     * @param listView
     * @see [类、类#方法、类#成员]
     */
    public void setListViewHeightBasedOnChildren(ListView listView)
    {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
        {
            return;
        }
        
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++)
        {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        params.height += 5;//if without this statement,the listview will be a little short
        listView.setLayoutParams(params);
    }
    
    /**
     * 网络请求超时
     * @see [类、类#方法、类#成员]
     */
    public void networkTimeOut()
    {
        showToast(getResources().getString(R.string.network_timeout));
    }
    
    /** <一句话功能简述>
     * 清空webview缓存
     * <功能详细描述>
     * @param context [参数说明]
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void clearCookies(Context context)
    {
        // Edge case: an illegal state exception is thrown if an instance of
        // CookieSyncManager has not be created.  CookieSyncManager is normally
        // created by a WebKit view, but this might happen if you start the
        // app, restore saved state, and click logout before running a UI
        // dialog in a WebView -- in which case the app crashes
        @SuppressWarnings("unused")
        CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }
}
