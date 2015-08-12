package com.xweisoft.wx.family.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 不能滑动的GridView，嵌套在可滑动的布局里使用
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  gac
 * @version  [版本号, 2014-6-18]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CanNotScrollGridView extends GridView
{
    
    public CanNotScrollGridView(Context context)
    {
        super(context);
    }
    
    public CanNotScrollGridView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    
    public CanNotScrollGridView(Context context, AttributeSet attrs,
            int defStyle)
    {
        super(context, attrs, defStyle);
    }
    
    /**
     * 设置不可滑动
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
