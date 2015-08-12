package com.xweisoft.wx.family.ui.contact;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.ui.BaseFragment;
import com.xweisoft.wx.family.ui.adapter.TabPagerAdapter;
import com.xweisoft.wx.family.ui.contact.view.ContactPagerView;
import com.xweisoft.wx.family.util.CommonTitleUtil;
import com.xweisoft.wx.family.widget.TabPageIndicator;

/**
 * 通讯录
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-5-11]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ContactsFragmet extends BaseFragment
{
    
    private String[] titleArray = { "地图", "列表" };
    
   private int[] resArray = { R.drawable.wx_contacts_class_selector,
            R.drawable.wx_contacts_friend_selector };
    
    /**
     * viewPager
     */
    private ViewPager mViewPager;
    
    /**
     * 页卡集合
     */
    private List<View> mViews;
    
    /**
     * 页卡标题
     */
    private TabPageIndicator mTabPageIndicator;
    
    /**
     * 标题搜索
     */
    private View mTitleRight;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        mLayouView = inflater.inflate(R.layout.wx_contacts_activity,
                container,
                false);
        initViews();
        bindListener();
        initForumCategory();
        return mLayouView;
    }
    
    @Override
    public void initViews()
    {
        CommonTitleUtil.initCommonTitle(mLayouView,
                getString(R.string.wx_tab_contacts),
                R.drawable.wx_contact_title_search,
                true,
                false);
        mTitleRight = mLayouView.findViewById(R.id.common_title_right);
        mViewPager = (ViewPager) mLayouView.findViewById(R.id.contact_viewpager);
        mTabPageIndicator = (TabPageIndicator) mLayouView.findViewById(R.id.contact_title_indicator);
    }
    
    @Override
    public void bindListener()
    {
        mTitleRight.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mContext,
                        ContactSearchActivity.class);
                startActivity(intent);
            }
        });
    }
    
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        if (null != mViews)
        {
            mViews.clear();
            mViews = null;
        }
    }
    
    @Override
    public void onResume()
    {
        super.onResume();
        if (null != mViews && null != mViews.get(mViewPager.getCurrentItem()))
        {
            ((ContactPagerView) mViews.get(mViewPager.getCurrentItem())).updateList();
        }
    }
    
    /**
     * 初始化栏目
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private void initForumCategory()
    {
        for (int i = 0; i < titleArray.length; i++)
        {
            if (null == mViews)
            {
                mViews = new ArrayList<View>();
            }
            if (i == 0)
            {
                ContactPagerView contactClassPagerView = new ContactPagerView(
                        mContext, false);
                contactClassPagerView.requestData();
                mViews.add(contactClassPagerView);
            }
            else if (i == 1)
            {
                ContactPagerView contactClassPagerView = new ContactPagerView(
                        mContext, true);
                contactClassPagerView.requestData();
                mViews.add(contactClassPagerView);
            }
        }
        //        mViewPager.setAdapter(new TabPagerAdapter(mViews, titleArray));
        mViewPager.setAdapter(new TabPagerAdapter(mViews, titleArray, resArray));
        mTabPageIndicator.setViewPager(mViewPager);
        mViewPager.setCurrentItem(0);
        mViewPager.setOnPageChangeListener(new OnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position)
            {
                if (titleArray.length > position)
                {
                    View view = mViews.get(position);
                    if (position == 0)
                    {
                        ContactPagerView attentionPagerView = (ContactPagerView) view;
                        attentionPagerView.updateList();
                    }
                    else if (position == 1)
                    {
                        ContactPagerView attentionPagerView = (ContactPagerView) view;
                        attentionPagerView.updateList();
                    }
                }
                mTabPageIndicator.setCurrentItem(position);
            }
            
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {
                
            }
            
            @Override
            public void onPageScrollStateChanged(int arg0)
            {
            }
        });
    }
    
}
