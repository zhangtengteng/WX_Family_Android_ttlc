package com.xweisoft.wx.family.widget;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 跑马灯 textView
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  gac
 * @version  [版本号, 2013-10-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MyMarqueeTextView extends TextView
{
    
    public MyMarqueeTextView(Context context)
    {
        super(context);
        createView();
    }
    
    public MyMarqueeTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        createView();
    }
    
    public MyMarqueeTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        createView();
    }
    
    private void createView()
    {
        setEllipsize(TruncateAt.MARQUEE);
        setMarqueeRepeatLimit(-1);
        setFocusableInTouchMode(true);
        setSingleLine(true);
    }
    
    @Override
    protected void onFocusChanged(boolean focused, int direction,
            Rect previouslyFocusedRect)
    {
        if (focused)
        {
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
        }
    }
    
    @Override
    public void onWindowFocusChanged(boolean focused)
    {
        if (focused)
        {
            super.onWindowFocusChanged(focused);
        }
    }
    
    @Override
    public boolean isFocused()
    {
        return true;
    }
    
}
