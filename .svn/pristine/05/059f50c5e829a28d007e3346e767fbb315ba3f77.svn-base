package com.xweisoft.wx.family.widget;

import com.xweisoft.wx.family.R;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.PopupWindow;

/**
 * <一句话功能简述>
 * popupwindow的父类窗体,弹出在界面底部
 * <功能详细描述>
 * @author  houfangfang
 * @version  [版本号, 2014-3-13]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public abstract class MyPopupWindow
{
    protected Context mContext;
    
    /**
     * popwindow的内容布局
     */
    protected int layoutId;
    
    /**
     * popwindow的句柄
     */
    protected PopupWindow popupWindow;
    
    /**
     * popwindow的内容view
     */
    protected View view;
    
    /**
     * <默认构造函数>
     * @param layoutId 内容布局id
     */
    public MyPopupWindow(Context mContext, int layoutId)
    {
        this.mContext = mContext;
        this.layoutId = layoutId;
    }
    
    /**
     * 初始化popwindow
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void initPopupWindow()
    {
        if (popupWindow == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(layoutId, null);
            popupWindow = new PopupWindow(view,
                    WindowManager.LayoutParams.FILL_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
        }
        
        popupWindow.setAnimationStyle(R.style.popWindowAnimBottom);
        
        // 使其聚集  
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失  
        popupWindow.setOutsideTouchable(true);
        
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景  
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        view.setOnTouchListener(new OnTouchListener()
        {
            View popView = null;
            
            public boolean onTouch(View v, MotionEvent event)
            {
                popView = view.findViewById(R.id.pop_window);
                if (null == popView)
                {
                    return true;
                }
                int height = popView.getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    if (y < height)
                    {
                        popupWindow.dismiss();
                    }
                }
                return true;
            }
        });
    }
    
    /**
     * 初始化发帖子popwindow，宽度不全屏显示
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void initPostPopupWindow()
    {
        if (popupWindow == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(layoutId, null);
            popupWindow = new PopupWindow(view,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
        }
        
        // 使其聚集  
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失  
        popupWindow.setOutsideTouchable(true);
        
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景  
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
    }
    
    /**
     * 初始化发帖子popwindow，宽度不全屏显示
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void initCommonPopupWindow()
    {
        if (popupWindow == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(layoutId, null);
            popupWindow = new PopupWindow(view,
                    WindowManager.LayoutParams.FILL_PARENT,
                    WindowManager.LayoutParams.FILL_PARENT);
        }
        
        // 使其聚集  
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失  
        popupWindow.setOutsideTouchable(true);
        
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景  
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
    }
    
    /**
     * 显示底部popWindow
     * <功能详细描述>
     * @param parent popwindow 的父view
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void showWindow(View parent)
    {
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }
    
    /**
     * 显示中间popWindow
     * <功能详细描述>
     * @param parent popwindow 的父view
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void showWindowCenter(View parent)
    {
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }
    
    /**
     * 显示顶部左边还是右边popWindow
     * <功能详细描述>
     * @param parent popwindow 的父view
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void showWindow(View parent, int gravity, int x, int y)
    {
        popupWindow.showAtLocation(parent, gravity, x, y);
    }
    
    /**
     * 隐藏popwindow
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void dismissWindow()
    {
        if (null != popupWindow)
        {
            popupWindow.dismiss();
        }
    }
    
    /**
     * 初始化views
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public abstract void initViews();
    
    /**
     * 绑定事件
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public abstract void bindLisener();
}
