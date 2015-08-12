package com.xweisoft.wx.family.ui.message.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.widget.BaseDialog;

/**
 * 预报名申请 放弃编辑弹出框
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-6-5]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CancelDialog extends BaseDialog implements
        android.view.View.OnClickListener
{
    /**
     * 取消
     */
    private TextView mCancelTextView;
    
    /**
     * 确定
     */
    private TextView mConfirmTextView;
    
    private TextView mTitleView;
    
    private TextView mContentView;
    
    private String title;
    
    private String content;
    
    /**
     * 点击接口实现对象
     */
    private OnConfirmClickListener onConfirmClickListener;
    
    public CancelDialog(Context context,
            OnConfirmClickListener onConfirmClickListener)
    {
        super(context, R.layout.apply_cancel_dialog);
        this.onConfirmClickListener = onConfirmClickListener;
    }
    
    public CancelDialog(Context context, String title, String content,
            OnConfirmClickListener onConfirmClickListener)
    {
        super(context, R.layout.apply_cancel_dialog);
        this.title = title;
        this.content = content;
        this.onConfirmClickListener = onConfirmClickListener;
    }
    
    @Override
    protected void initViews()
    {
        mCancelTextView = (TextView) findViewById(R.id.apply_cancel_dialog_cancel);
        mConfirmTextView = (TextView) findViewById(R.id.apply_cancel_dialog_confirm);
        mTitleView = (TextView) findViewById(R.id.apply_cancel_dialog_title);
        mContentView = (TextView) findViewById(R.id.apply_cancel_dialog_content);
        if (!TextUtils.isEmpty(title))
        {
            mTitleView.setText(title);
        }
        if (!TextUtils.isEmpty(content))
        {
            mContentView.setText(content);
        }
    }
    
    @Override
    protected void bindLinsenter()
    {
        mCancelTextView.setOnClickListener(this);
        mConfirmTextView.setOnClickListener(this);
    }
    
    /**
     * 点击接口
     * <一句话功能简述>
     * <功能详细描述>
     * 
     * @author  poorgod
     * @version  [版本号, 2015-6-5]
     * @see  [相关类/方法]
     * @since  [产品/模块版本]
     */
    public interface OnConfirmClickListener
    {
        public void onConfirmClick();
    }
    
    @Override
    public void onClick(View v)
    {
        dismiss();
        switch (v.getId())
        {
            case R.id.apply_cancel_dialog_cancel:
                break;
            case R.id.apply_cancel_dialog_confirm:
                if (null != onConfirmClickListener)
                {
                    onConfirmClickListener.onConfirmClick();
                }
                break;
            default:
                break;
        }
    }
}
