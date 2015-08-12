package com.xweisoft.wx.family.ui.setting;

import java.util.Map;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.logic.global.HttpAddressProperties;
import com.xweisoft.wx.family.logic.model.response.CommonResp;
import com.xweisoft.wx.family.ui.BaseActivity;
import com.xweisoft.wx.family.util.CommonTitleUtil;
import com.xweisoft.wx.family.util.HttpRequestUtil;
import com.xweisoft.wx.family.util.ProgressUtil;
import com.xweisoft.wx.family.widget.NetHandler;

/**
 * 意见反馈页面
 * 
 * @author  poorgod
 * @version  [版本号, 2014年6月18日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class FeedbackActivity extends BaseActivity implements OnClickListener
{
    private EditText feedbackEditText = null;
    
    private Button mSubmitButton;
    
    private NetHandler handler = new NetHandler(false)
    {
        
        @Override
        public void onResponse()
        {
            super.onResponse();
            mSubmitButton.setEnabled(true);
        }
        
        @Override
        public void onSuccess(String errMsg, Message msg)
        {
            finish();
        }
        
        @Override
        public void onFailed(String errCode, String errMsg, Message msg)
        {
            showToast(errMsg);
        }
    };
    
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
        CommonTitleUtil.initCommonTitle(this,
                getString(R.string.ysh_feedbak),
                getString(R.string.ysh_send),
                false,
                false);
        feedbackEditText = (EditText) findViewById(R.id.feedback_edittext);
        mSubmitButton = (Button) findViewById(R.id.feedback_submit);
    }
    
    @Override
    public void bindListener()
    {
        mSubmitButton.setOnClickListener(this);
    }
    
    @Override
    public int getActivityLayout()
    {
        return R.layout.wx_feedback_activity;
    }
    
    @Override
    public void onClick(View v)
    {
        if (v == mSubmitButton)
        {
            String feedback = feedbackEditText.getText().toString().trim();
            if (TextUtils.isEmpty(feedback))
            {
                showToast("请输入反馈意见");
                return;
            }
            ProgressUtil.showProgressDialog(this, "正在提交反馈意见...");
            mSubmitButton.setEnabled(false);
            Map<String, String> param = HttpRequestUtil.getCommonParams(HttpAddressProperties.FEEDBACK);
            param.put("content", feedback);
            HttpRequestUtil.sendHttpPostCommonRequest(mContext,
                    HttpAddressProperties.SERVICE_URL,
                    param,
                    CommonResp.class,
                    handler);
        }
    }
    
}
