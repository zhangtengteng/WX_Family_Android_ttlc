/*
 * 文 件 名:  ScrollviewEdit.java
 * 描    述:  <描述>
 * 创 建 人:  李晨光
 * 创建时间:  2014年7月28日
 */
package com.xweisoft.wx.family.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  李晨光
 * @version  [版本号, 2014年7月28日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ScrollViewEdit extends ScrollView
{
    private static final String TAG = "ScrollviewEdit";
    
    private ScrollView parent_scrollview;
    
    public ScrollView getParent_scrollview()
    {
        return parent_scrollview;
    }
    
    public void setParent_scrollview(ScrollView parent_scrollview)
    {
        this.parent_scrollview = parent_scrollview;
    }
    
    public ScrollViewEdit(Context context)
    {
        super(context);
    }
    
    public ScrollViewEdit(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    
    int currentY;
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        if (parent_scrollview == null)
        {
            return super.onInterceptTouchEvent(ev);
        }
        else
        {
            if (ev.getAction() == MotionEvent.ACTION_DOWN)
            {
                // 将父scrollview的滚动事件拦截
                currentY = (int)ev.getY();
                setParentScrollAble(false);
                return super.onInterceptTouchEvent(ev);
            }
            else if (ev.getAction() == MotionEvent.ACTION_UP)
            {
                // 把滚动事件恢复给父Scrollview
                setParentScrollAble(true);
            }
            else if (ev.getAction() == MotionEvent.ACTION_MOVE)
            {
            }
        }
        return super.onInterceptTouchEvent(ev);
    }
    
    /**
     * 是否把滚动事件交给父scrollview
     * @param flag
     */
    private void setParentScrollAble(boolean flag)
    {
        parent_scrollview.requestDisallowInterceptTouchEvent(!flag);
    }
}
