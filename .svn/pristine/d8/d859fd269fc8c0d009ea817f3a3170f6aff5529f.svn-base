package com.xweisoft.wx.family.widget;

import android.widget.ListView;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  guoac
 * @version  [版本号, 2014年1月18日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MyListView extends ListView
{
    public MyListView(android.content.Context context,
            android.util.AttributeSet attrs)
    {
        super(context, attrs);
    }
    
    /** 
     * 设置不滚动 
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
    
}
