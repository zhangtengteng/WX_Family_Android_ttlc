package com.xweisoft.wx.family.ui;

import android.os.Bundle;
import android.widget.AbsListView;

import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

/**
 * 异步加载图片的列表基类
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  gac
 * @version  [版本号, 2014-5-15]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public abstract class AbsListViewBaseActivity extends BaseActivity
{
    protected static final String STATE_PAUSE_ON_SCROLL = "STATE_PAUSE_ON_SCROLL";
    
    protected static final String STATE_PAUSE_ON_FLING = "STATE_PAUSE_ON_FLING";
    
    protected AbsListView listView;
    
    protected boolean pauseOnScroll = false;
    
    protected boolean pauseOnFling = true;
    
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        pauseOnScroll = savedInstanceState.getBoolean(STATE_PAUSE_ON_SCROLL,
                false);
        pauseOnFling = savedInstanceState.getBoolean(STATE_PAUSE_ON_FLING, true);
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        applyScrollListener();
    }
    
    private void applyScrollListener()
    {
        if (null != listView)
        {
            listView.setOnScrollListener(new PauseOnScrollListener(imageLoader,
                    pauseOnScroll, pauseOnFling));
        }
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putBoolean(STATE_PAUSE_ON_SCROLL, pauseOnScroll);
        outState.putBoolean(STATE_PAUSE_ON_FLING, pauseOnFling);
    }
    
}
