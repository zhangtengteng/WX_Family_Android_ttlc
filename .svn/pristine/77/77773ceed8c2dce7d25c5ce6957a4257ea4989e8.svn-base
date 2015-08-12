package com.xweisoft.wx.family.ui.help;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.ui.BaseActivity;
import com.xweisoft.wx.family.util.CommonTitleUtil;

public class HelpActivity extends BaseActivity implements OnClickListener
{
    
    private View mRightView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayout());
        initViews();
        bindListener();
    }
    
    @Override
    public void initViews()
    {
        CommonTitleUtil.initCommonTitle(this, "帮助中心", "辅导统计", true, false);
        mRightView = findViewById(R.id.common_title_right);
    }
    
    @Override
    public void bindListener()
    {
        mRightView.setOnClickListener(this);
    }
    
    @Override
    public int getActivityLayout()
    {
        return R.layout.wx_contacts_activity;
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.common_title_right:
                
                break;
            
            default:
                break;
        }
        
    }
    
}
