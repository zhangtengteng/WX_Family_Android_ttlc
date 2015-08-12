package com.xweisoft.wx.family.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;
/**
 * 能够兼容ViewPager的ScrollView
 * @Description: 解决了ViewPager在ScrollView中的滑动反弹问题
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  gac
 * @version  [版本号, 2014-8-6]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ViewPagerScrollView extends ScrollView
{
    // 滑动距离及坐标
    private float xDistance, yDistance, xLast, yLast;
    
    public ViewPagerScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();
                
                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;
                
                if (xDistance > yDistance)
                {
                    return false;
                }
        }
        
        return super.onInterceptTouchEvent(ev);
    }
}
