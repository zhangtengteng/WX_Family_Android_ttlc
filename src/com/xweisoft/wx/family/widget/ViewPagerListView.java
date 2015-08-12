package com.xweisoft.wx.family.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class ViewPagerListView extends ListView
{
    private float mX, mY;
    public ViewPagerListView(Context context)
    {
        super(context);
    }

    public ViewPagerListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ViewPagerListView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

     //覆盖拦截器。如果是横向滑动，则不进行拦截，将事件传递给内部的ViewPager。
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event)
    {
        float offX, offY;
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                mX = event.getX();
                mY = event.getY();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                offX = event.getX() - mX;
                offY = event.getY() - mY;
                if (Math.abs(offX) > Math.abs(offY)) {// 横向滑动，不对事件进行拦截。
                    if (Math.abs(offY) > 50) { // 过滤掉小的纵向滑动，消除listview的抖动现象。
                        super.onInterceptTouchEvent(event);
                    }
                    return false;
                }
                break;
        }
        return super.onInterceptTouchEvent(event);
    }
}
