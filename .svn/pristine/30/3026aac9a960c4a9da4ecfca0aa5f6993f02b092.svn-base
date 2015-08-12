package com.xweisoft.wx.family.widget;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.xweisoft.wx.family.logic.global.GlobalVariable;
import com.xweisoft.wx.family.util.Util;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  administrator
 * @version  [版本号, 2013-10-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class UninterceptableViewPager extends ViewPager
{
    private static String TAG = Util.makeLogTag(UninterceptableViewPager.class);
    
    public static boolean isMove = true;
    
    /**
     * 用于更新图片自动滑动 的 开始与停止
     */
    private Handler handler;
    
    public UninterceptableViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    
    public void setHandler(Handler handler)
    {
        this.handler = handler;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_DOWN)
        {
            GlobalVariable.isViewPagerRun = false;
            GlobalVariable.isViewPagerDown = true;
            if (null != handler)
            {
                handler.removeCallbacksAndMessages(null);
            }
        }
        else if (action == MotionEvent.ACTION_UP)
        {
            GlobalVariable.isViewPagerRun = true;
            GlobalVariable.isViewPagerDown = false;
            if (null != handler)
            {
                handler.removeCallbacksAndMessages(null);
                handler.sendEmptyMessage(1);
            }
        }
        return super.onTouchEvent(ev);
    }
}
