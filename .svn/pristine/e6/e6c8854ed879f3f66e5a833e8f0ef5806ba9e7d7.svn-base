package com.xweisoft.wx.family.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * ViewPager适配器
 * 
 * @author  gac
 * @version  [版本号, 2014年6月19日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class TabPagerAdapter extends PagerAdapter
{
    
    /**
     * 页卡集合
     */
    private List<View> mViews;
    
    /**
     * 标题集合
     */
    private String[] mItems;
    
    /**
     * 标题集合
     */
    private int[] mRes;
    
    public TabPagerAdapter(List<View> mViews, String[] mItems)
    {
        super();
        this.mViews = mViews;
        this.mItems = mItems;
        if (null == mViews)
        {
            this.mViews = new ArrayList<View>();
        }
        if (null == mItems)
        {
            this.mItems = new String[mViews.size()];
        }
        if (null == mRes)
        {
            this.mRes = new int[mViews.size()];
        }
    }
    
    public TabPagerAdapter(List<View> mViews, String[] mItems, int[] mRes)
    {
        super();
        this.mViews = mViews;
        this.mItems = mItems;
        this.mRes = mRes;
        if (null == mViews)
        {
            this.mViews = new ArrayList<View>();
        }
        if (null == mItems)
        {
            this.mItems = new String[mViews.size()];
        }
        if (null == mRes)
        {
            this.mRes = new int[mViews.size()];
        }
    }
    
    @Override
    public int getCount()
    {
        return mViews.size();
    }
    
    @Override
    public boolean isViewFromObject(View arg0, Object arg1)
    {
        return arg0 == arg1;
    }
    
    @Override
    public void destroyItem(View container, int position, Object object)
    {
        if (mViews != null && mViews.size() > position)
        {
            ((ViewPager) container).removeView(mViews.get(position));
        }
    }
    
    @Override
    public Object instantiateItem(View container, int position)
    {
        ((ViewPager) container).addView(mViews.get(position));
        return mViews.get(position);
    }
    
    @Override
    public void finishUpdate(View arg0)
    {
        
    }
    
    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1)
    {
        
    }
    
    @Override
    public Parcelable saveState()
    {
        return null;
    }
    
    @Override
    public void startUpdate(View arg0)
    {
        
    }
    
    @Override
    public CharSequence getPageTitle(int position)
    {
        return null != mItems[position] ? mItems[position] : "";
    }
    
    public int getTitleRes(int position)
    {
        return 0 != mRes[position] ? mRes[position] : 0;
    }
}
