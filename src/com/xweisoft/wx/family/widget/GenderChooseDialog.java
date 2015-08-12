package com.xweisoft.wx.family.widget;

import com.xweisoft.wx.family.R;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 * 性别选择框
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-6-5]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class GenderChooseDialog extends BaseBottomDialog
{
    /**
     * 从相册选取
     */
    private TextView mManTextView;
    
    /**
     * 拍照
     */
    private TextView mWomanTextView;
    
    /**
     * 取消
     */
    private TextView mCancelTextView;
    
    /**
     * 监听事件
     */
    private View.OnClickListener onClickListener;
    
    public GenderChooseDialog(Context context,
            View.OnClickListener clickListener)
    {
        super(context, R.layout.wx_gender_popwindow);
        this.onClickListener = clickListener;
    }
    
    @Override
    protected void initView()
    {
        mManTextView = (TextView) findViewById(R.id.gender_man);
        mWomanTextView = (TextView) findViewById(R.id.gender_woman);
        mCancelTextView = (TextView) findViewById(R.id.gender_cancel);
    }
    
    @Override
    protected void bindListener()
    {
        if (null != onClickListener)
        {
            mManTextView.setOnClickListener(onClickListener);
            mWomanTextView.setOnClickListener(onClickListener);
            mCancelTextView.setOnClickListener(onClickListener);
        }
    }
    
}
