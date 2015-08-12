package com.xweisoft.wx.family.ui.load;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.ui.pc.LoginActivity;

/**
 * <一句话功能简述>
 * 宣传页：Android实现左右滑动指引效果
 * <功能详细描述>
 * 
 * @author  houfangfang
 * @version  [版本号, 2013-8-27]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class GuideViewActivity extends Activity
{
    private View beginView;
    
    private ViewPager viewPager;
    
    private ArrayList<View> pageViews;
    
    private ImageView[] imageViews;
    
    /**
     * 包裹滑动图片LinearLayout
     */
    private View main;
    
    /**
     * 包裹小圆点的LinearLayout
     */
    private LinearLayout group;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = getLayoutInflater();
        pageViews = new ArrayList<View>();
        pageViews.add(inflater.inflate(R.layout.guide_view_item_1, null));
        pageViews.add(inflater.inflate(R.layout.guide_view_item_2, null));
        pageViews.add(inflater.inflate(R.layout.guide_view_item_3, null));
        
        // 有几个布局页面就有几个圆点图片
        imageViews = new ImageView[pageViews.size()];
        main = inflater.inflate(R.layout.guide_view_main, null);
        
        group = (LinearLayout) main.findViewById(R.id.viewGroup);
        viewPager = (ViewPager) main.findViewById(R.id.guidePages);
        
        // 通过for循环设置圆点图片的布局
        for (int i = 0; i < pageViews.size(); i++)
        {
            ImageView imageView = new ImageView(GuideViewActivity.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    25, 25);
            params.leftMargin = 20;
            params.rightMargin = 20;
            imageView.setLayoutParams(params);
            imageViews[i] = imageView;
            
            if (i == 0)
            {
                // 默认选中第一张图片
                //                imageViews[i].setBackgroundResource(R.drawable.guide_view_indicator_focused);
            }
            else
            {
                //                imageViews[i].setBackgroundResource(R.drawable.guide_view_indicator_default);
            }
            group.addView(imageViews[i]);
        }
        setContentView(main);
        viewPager.setAdapter(new GuidePageAdapter());
        viewPager.setOnPageChangeListener(new GuidePageChangeListener());
        //        beginView = pageViews.get(2).findViewById(R.id.guide_view_begin_bt);
        //        beginView.setOnClickListener(new OnClickListener()
        //        {
        //            @Override
        //            public void onClick(View v)
        //            {
        //                Intent intent = new Intent(GuideViewActivity.this,
        //                        LoginActivity.class);
        //                startActivity(intent);
        //                finish();
        //            }
        //        });
    }
    
    // 指引页面数据适配器
    class GuidePageAdapter extends PagerAdapter
    {
        
        @Override
        public int getCount()
        {
            return pageViews.size();
        }
        
        @Override
        public boolean isViewFromObject(View arg0, Object arg1)
        {
            return arg0 == arg1;
        }
        
        @Override
        public int getItemPosition(Object object)
        {
            return super.getItemPosition(object);
        }
        
        @Override
        public void destroyItem(View arg0, int arg1, Object arg2)
        {
            ((ViewPager) arg0).removeView(pageViews.get(arg1));
        }
        
        @Override
        public Object instantiateItem(View arg0, int arg1)
        {
            ((ViewPager) arg0).addView(pageViews.get(arg1));
            return pageViews.get(arg1);
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
        public void finishUpdate(View arg0)
        {
        }
    }
    
    // 指引页面更改事件监听器
    class GuidePageChangeListener implements OnPageChangeListener
    {
        
        @Override
        public void onPageScrollStateChanged(int arg0)
        {
        }
        
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2)
        {
        }
        
        @Override
        public void onPageSelected(int arg0)
        {
            for (int i = 0; i < imageViews.length; i++)
            {
                //                imageViews[arg0].setBackgroundResource(R.drawable.guide_view_indicator_focused);
                if (arg0 != i)
                {
                    //                    imageViews[i].setBackgroundResource(R.drawable.guide_view_indicator_default);
                }
            }
        }
    }
    
}