package com.xweisoft.wx.family.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.xweisoft.wx.family.R;

/**
 * 图片选择框
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  guoac
 * @version  [版本号, 2015-1-20]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class PhotoChooseDialog extends BaseBottomDialog
{
    /**
     * 从相册选取
     */
    private TextView mLocaLTextView;
    
    /**
     * 拍照
     */
    private TextView mCameraTextView;
    
    /**
     * 取消
     */
    private TextView mCancelTextView;
    
    /**
     * 监听事件
     */
    private View.OnClickListener onClickListener;
    
    /**
     * <默认构造函数>
     */
    public PhotoChooseDialog(Context context, View.OnClickListener clickListener)
    {
        super(context, R.layout.photo_choose_dialog);
        this.onClickListener = clickListener;
    }
    
    @Override
    protected void initView()
    {
        mCameraTextView = (TextView) findViewById(R.id.photo_choose_camera);
        mLocaLTextView = (TextView) findViewById(R.id.photo_choose_local);
        mCancelTextView = (TextView) findViewById(R.id.photo_choose_cancel);
    }
    
    @Override
    protected void bindListener()
    {
        if (null != onClickListener)
        {
            mCameraTextView.setOnClickListener(onClickListener);
            mLocaLTextView.setOnClickListener(onClickListener);
            mCancelTextView.setOnClickListener(onClickListener);
        }
    }
    
}
