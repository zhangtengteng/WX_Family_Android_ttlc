/*
 * 文 件 名:  AboutUsActivity.java
 * 描    述:  关于我们页面
 * 创 建 人:  李晨光
 * 创建时间:  2014年6月18日
 */
package com.xweisoft.wx.family.ui.setting;

import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.logic.global.HttpAddressProperties;
import com.xweisoft.wx.family.logic.model.AboutItem;
import com.xweisoft.wx.family.logic.model.response.AboutResp;
import com.xweisoft.wx.family.ui.BaseActivity;
import com.xweisoft.wx.family.util.CommonTitleUtil;
import com.xweisoft.wx.family.util.HttpRequestUtil;
import com.xweisoft.wx.family.util.StringUtil;
import com.xweisoft.wx.family.util.Util;
import com.xweisoft.wx.family.widget.NetHandler;

/**
 * 关于我们页面
 * 
 * @author  李晨光
 * @version  [版本号, 2014年6月18日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class AboutUsActivity extends BaseActivity
{
    
    private TextView aboutTextview = null;
    
    private TextView mVersionView;
    
    private NetHandler handler = new NetHandler(false)
    {
        
        @Override
        public void onSuccess(String errMsg, Message msg)
        {
            AboutResp resp = (AboutResp) msg.obj;
            AboutItem item = resp.getAboutItem();
            if (null != item)
            {
                String content = item.getContent();
                if (!StringUtil.isEmpty(content))
                {
                    aboutTextview.setText(Html.fromHtml(content));
                }
            }
        }
        
        @Override
        public void onFailed(String errCode, String errMsg, Message msg)
        {
            Toast.makeText(mContext, errMsg, Toast.LENGTH_SHORT).show();
        }
        
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayout());
        initViews();
        bindListener();
        sendRequest();
    }
    
    @Override
    public void initViews()
    {
        CommonTitleUtil.initCommonTitle(this,
                getString(R.string.ysh_about_us),
                null,
                false,
                true);
        aboutTextview = (TextView) findViewById(R.id.about_textview);
        mVersionView = (TextView) findViewById(R.id.about_version_text);
        
        if (null != Util.getApkVersionName(mContext))
        {
            mVersionView.setText("V" + Util.getApkVersionName(mContext));
        }
    }
    
    @Override
    public void bindListener()
    {
        
    }
    
    /**
     * 发送请求
     * @see [类、类#方法、类#成员]
     */
    private void sendRequest()
    {
        //        HttpRequestUtil.sendHttpPostRequest(this,
        //                HttpAddressProperties.Setting.ABOUT_US,
        //                null,
        //                AboutResp.class,
        //                handler);
    }
    
    @Override
    public int getActivityLayout()
    {
        return R.layout.ysh_aboutus_activity;
    }
    
}
