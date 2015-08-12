package com.xweisoft.wx.family.ui.message;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.WXApplication;
import com.xweisoft.wx.family.logic.global.GlobalVariable;
import com.xweisoft.wx.family.ui.BaseActivity;
import com.xweisoft.wx.family.util.CommonTitleUtil;

public class ApplySuccessActivity extends BaseActivity implements
        OnClickListener
{
    private ImageView mImageView;
    
    private TextView mApplyText;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayout());
        initViews();
        bindListener();
        WXApplication.getInstance().addFinishActivity(this);
    }
    
    @Override
    public void initViews()
    {
        CommonTitleUtil.initCommonTitle(this,
                getString(R.string.add_prediction_info),
                null,
                false,
                true);
        mImageView = (ImageView) findViewById(R.id.apply_success_image);
        mApplyText = (TextView) findViewById(R.id.apply_success_apply);
        /*
         * 图片因效果不可做成点9图片，根据图片长宽动态设置bitmap以及view的长宽
         */
        LayoutParams params = (LayoutParams) mImageView.getLayoutParams();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.img_prediction_sucess);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        params.width = GlobalVariable.screenWidth;
        params.height = GlobalVariable.screenWidth * h / w;
        mImageView.setImageBitmap(bitmap);
        mImageView.setLayoutParams(params);
    }
    
    @Override
    public void bindListener()
    {
        mApplyText.setOnClickListener(this);
    }
    
    @Override
    public int getActivityLayout()
    {
        return R.layout.apply_success_activity;
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.apply_success_apply:
                break;
            default:
                break;
        }
    }
    
}
