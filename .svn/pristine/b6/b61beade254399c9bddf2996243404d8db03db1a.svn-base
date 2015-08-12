package com.xweisoft.wx.family.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

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
public class VerticalScrollView extends ScrollView
{
    private static String TAG = Util.makeLogTag(VerticalScrollView.class);
    
    private float xDistance, yDistance, lastX, lastY;

    public VerticalScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
//        Log.i(TAG, "onInterceptTouchEvent...");
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                lastX = ev.getX();
                lastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();
                xDistance += Math.abs(curX - lastX);
                yDistance += Math.abs(curY - lastY);
                lastX = curX;
                lastY = curY;
                if (xDistance > yDistance)
                {
//                    UninterceptableViewPager.isMove = true;
                    return false;
                }
        }
        UninterceptableViewPager.isMove = true;
        return super.onInterceptTouchEvent(ev);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
//        Log.i(TAG, "onTouchEvent...");
//        Log.e(TAG, "UninterceptableViewPager.isMove = " + UninterceptableViewPager.isMove);
        return super.onTouchEvent(ev);
    }
}
