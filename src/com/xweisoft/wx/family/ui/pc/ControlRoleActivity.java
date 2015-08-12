package com.xweisoft.wx.family.ui.pc;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.WXApplication;
import com.xweisoft.wx.family.logic.global.HttpAddressProperties;
import com.xweisoft.wx.family.logic.model.response.CommonResp;
import com.xweisoft.wx.family.ui.BaseActivity;
import com.xweisoft.wx.family.util.CommonTitleUtil;
import com.xweisoft.wx.family.util.HttpRequestUtil;
import com.xweisoft.wx.family.util.ProgressUtil;
import com.xweisoft.wx.family.widget.NetHandler;

/**
 * 监护角色
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-5-15]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ControlRoleActivity extends BaseActivity
{
    /**
     * 角色 输入框
     */
    private EditText mRoleNameEdit;
    
    /**
     * 完成 按钮
     */
    private Button mSubmitButton;
    
    private String name;
    
    /**
     * 请求响应
     */
    private Handler handler = new NetHandler(false)
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
            //修改完成孩子 监护人的称号后更新缓存
            if (null != WXApplication.getInstance().selectedItem)
            {
                WXApplication.getInstance().selectedItem.appellation = name;
            }
            Intent intent = new Intent();
            intent.putExtra("call", name);
            setResult(RESULT_OK, intent);
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
                getResources().getString(R.string.ysh_control_rote),
                null,
                false,
                true);
        mRoleNameEdit = (EditText) findViewById(R.id.role_name_input);
        mSubmitButton = (Button) findViewById(R.id.role_submit_button);
    }
    
    @Override
    public void bindListener()
    {
        mSubmitButton.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                name = mRoleNameEdit.getText().toString().trim();
                if (TextUtils.isEmpty(name))
                {
                    showToast("请输入监护角色");
                    return;
                }
                mSubmitButton.setEnabled(false);
                ProgressUtil.showProgressDialog(mContext, "请求中...");
                Map<String, String> param = HttpRequestUtil.getCommonParams(HttpAddressProperties.ROLE);
                param.put("appellation", mRoleNameEdit.getText()
                        .toString()
                        .trim());
                HttpRequestUtil.sendHttpPostCommonRequest(mContext,
                        HttpAddressProperties.SERVICE_URL,
                        param,
                        CommonResp.class,
                        handler);
            }
        });
        
    }
    
    @Override
    public int getActivityLayout()
    {
        return R.layout.wx_control_role_activity;
    }
    
}
