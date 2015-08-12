package com.xweisoft.wx.family.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  administrator
 * @version  [版本号, 2013-10-28]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MyGallery extends Gallery
{
    
    public MyGallery(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    
    private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2)
    {
        return e2.getX() > e1.getX();
    }
    
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
            float velocityY)
    {
        int keyCode;
        if (isScrollingLeft(e1, e2))
        {
            keyCode = KeyEvent.KEYCODE_DPAD_LEFT; // 向左移
        }
        else
        {
            keyCode = KeyEvent.KEYCODE_DPAD_RIGHT; // 右移
        }
        onKeyDown(keyCode, null);
        return false;
    }
    
}
