/**
 * @file XListViewHeader.java
 * @create Apr 18, 2012 5:22:27 PM
 * @author Maxwin
 * @description XListView's header
 */
package com.xweisoft.wx.family.ui.message.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xweisoft.wx.family.R;

public class ChatListViewHeader extends LinearLayout
{
    private LinearLayout mContainer;
    
    private ImageView mArrowImageView;
    
    private ProgressBar mProgressBar;
    
    private TextView mHintTextView;
    
    private int mState = STATE_NORMAL;
    
    public final static int STATE_NORMAL = 0;
    
    public final static int STATE_READY = 1;
    
    public final static int STATE_REFRESHING = 2;
    
    public ChatListViewHeader(Context context)
    {
        super(context);
        initView(context);
    }
    
    /**
     * @param context
     * @param attrs
     */
    public ChatListViewHeader(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView(context);
    }
    
    private void initView(Context context)
    {
        // 初始情况，设置下拉刷新view高度为0
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, 0);
        mContainer = (LinearLayout) LayoutInflater.from(context)
                .inflate(R.layout.xlistview_header, null);
        addView(mContainer, lp);
        setGravity(Gravity.BOTTOM);
        
        mArrowImageView = (ImageView) findViewById(R.id.xlistview_header_arrow);
        mHintTextView = (TextView) findViewById(R.id.xlistview_header_hint_textview);
        mProgressBar = (ProgressBar) findViewById(R.id.xlistview_header_progressbar);
        
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mProgressBar.getLayoutParams();
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        params.leftMargin = 0;
        mProgressBar.setLayoutParams(params);
        
        findViewById(R.id.xlistview_header_text).setVisibility(View.GONE);
        mArrowImageView.setVisibility(View.GONE);
        mHintTextView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        
    }
    
    public void setState(int state)
    {
        if (state == mState)
            return;
        
        mState = state;
    }
    
    public void setVisiableHeight(int height)
    {
        if (height < 0)
            height = 0;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }
    
    public int getVisiableHeight()
    {
        return mContainer.getHeight();
    }
    
}
