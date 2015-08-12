package com.xweisoft.wx.family.widget;

import com.xweisoft.wx.family.util.Util;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * 自定义字体滚动view
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  gac
 * @version  [版本号, 2014-1-23]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MarqueeView extends TextView implements Runnable
{
    /**
     * 滚动速度步伐
     */
    private int speed = 3;
    
    private int currentScrollX;// 当前滚动的位置
    
    private boolean isStop = false;
    
    private int textWidth;
    
    public MarqueeView(Context context)
    {
        super(context);
        init();
    }
    
    public MarqueeView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }
    
    public MarqueeView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }
    
    public void init()
    {
        setClickable(true);
        setSingleLine(true);
        setEllipsize(TruncateAt.MARQUEE);
        setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
    }
    
    public void setContent(String text)
    {
        setText(text);
        setTag(text);
        startScroll();
    }
    
    @Override
    public void setText(CharSequence text, BufferType type)
    {
        super.setText(text, type);
        MeasureTextWidth();
    }
    
    /** 
     * 获取文字宽度 
     */
    private void MeasureTextWidth()
    {
        Paint paint = this.getPaint();
        String str = this.getText().toString();
        textWidth = (int) paint.measureText(str);
    }
    
    @Override
    public void run()
    {
        if (textWidth < 1)
        {
            //title null api error.
            return;
        }
        currentScrollX += Util.dpToPixel(getContext(), speed);// 滚动速度 
        scrollTo(currentScrollX, 0);
        if (isStop)
        {
            return;
        }
        if (getScrollX() >= textWidth)
        {
            currentScrollX = -getWidth();
            scrollTo(currentScrollX, 0);
        }
        
        postDelayed(this, 50);
    }
    
    // 开始滚动 
    public void startScroll()
    {
        isStop = false;
        this.removeCallbacks(this);
        post(this);
    }
    
    // 停止滚动 
    public void stopScroll()
    {
        isStop = true;
    }
}
