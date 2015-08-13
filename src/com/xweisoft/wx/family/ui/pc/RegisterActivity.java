package com.xweisoft.wx.family.ui.pc;

import java.util.Map;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.logic.global.HttpAddressProperties;
import com.xweisoft.wx.family.logic.model.response.CommonResp;
import com.xweisoft.wx.family.ui.BaseActivity;
import com.xweisoft.wx.family.util.HttpRequestUtil;
import com.xweisoft.wx.family.util.ProgressUtil;
import com.xweisoft.wx.family.util.SecurityUtil;
import com.xweisoft.wx.family.util.StringUtil;
import com.xweisoft.wx.family.util.Util;
import com.xweisoft.wx.family.util.VerifyCodeCount;
import com.xweisoft.wx.family.widget.NetHandler;

/**
 * 注册
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-5-12]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class RegisterActivity extends BaseActivity implements OnClickListener
{
    
    /**
     * 电话号码输入框
     */
    private EditText telphoneEditText = null;
    
    /**
     * 密码输入框
     */
    private EditText passwordEditText = null;
    
    /**
     * 验证码输入框
     */
    private EditText codeEditText = null;
    
    /**
     * 获取验证码按钮
     */
    private Button codeButton = null;
    
    /**
     * 注册按钮
     */
    private Button registerButton = null;
    
    private VerifyCodeCount mVerifyCodeCount;
    
    /**
     * 验证码请求响应
     */
    private NetHandler codeHandler = new NetHandler(false)
    {
        
        @Override
        public void onSuccess(String errMsg, Message msg)
        {
            showToast(getString(R.string.ysh_codetophone));
        }
        
        @Override
        public void onFailed(String errCode, String errMsg, Message msg)
        {
            showToast(errMsg);
        }
        
    };
    
    /**
     * 注册请求响应处理
     */
    private NetHandler registerHandler = new NetHandler(false)
    {
        
        @Override
        public void onResponse()
        {
            super.onResponse();
            registerButton.setEnabled(true);
        }
        
        @Override
        public void onSuccess(String errMsg, Message msg)
        {
            showToast("注册成功");
            finish();
        }
        
        @Override
        public void onFailed(String errCode, String errMsg, Message msg)
        {
            showToast(errMsg);
        }
        
    };

	private TextView tvTop;
    
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
		tvTop = (TextView) findViewById(R.id.generalTitleLabel);
		tvTop.setText(R.string.login_regist);
    	
        //手机验证码定时器
        telphoneEditText = (EditText) findViewById(R.id.register_telphone_edittext);
        passwordEditText = (EditText) findViewById(R.id.register_password_edittext);
        //        surePasswordEditText = (EditText) findViewById(R.id.register_sure_password_edittext);
        codeButton = (Button) findViewById(R.id.register_get_verifycode);
        codeEditText = (EditText) findViewById(R.id.register_code_edittext);
        registerButton = (Button) findViewById(R.id.register_submit_button);
        mVerifyCodeCount = new VerifyCodeCount(60000, 1000, codeButton);
    }
    
    @Override
    public void bindListener()
    {
        codeButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }
    
    @Override
    public int getActivityLayout()
    {
        return R.layout.wx_register_activity;
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.register_get_verifycode:
                String telphone = telphoneEditText.getText().toString().trim();
                if (StringUtil.isEmpty(telphone))
                {
                    showToast(getString(R.string.ysh_telphone_empty));
                }
                else if (!Util.isPhone(telphone))
                {
                    showToast(getString(R.string.publish_info_phone_check_toast));
                }
                else
                {
                    Map<String, String> params = HttpRequestUtil.getCommonParams(HttpAddressProperties.GET_VERIFYCODE);
                    params.put("phone", telphone);
                    params.put("type", "2");
                    HttpRequestUtil.sendHttpPostCommonRequest(mContext,
                            HttpAddressProperties.SERVICE_URL,
                            params,
                            CommonResp.class,
                            codeHandler);
                    mVerifyCodeCount.start();
                }
                break;
            case R.id.register_submit_button:
                String password = passwordEditText.getText().toString().trim();
                String phone = telphoneEditText.getText().toString().trim();
                String code = codeEditText.getText().toString().trim();
                // 输入框文本内容校验
                if (TextUtils.isEmpty(phone))
                {
                    showToast(getString(R.string.ysh_telphone_empty));
                    return;
                }
                if (!Util.isPhone(phone))
                {
                    showToast(getString(R.string.publish_info_phone_check_toast));
                    return;
                }
                if (StringUtil.isEmpty(password))
                {
                    showToast(getString(R.string.ysh_password_empty));
                    return;
                }
                if (StringUtil.isEmpty(code))
                {
                    showToast(getString(R.string.ysh_code_empty));
                    return;
                }
                //点击后设置按钮不可用，再请求获得响应后重置为可用状态，防止点击多次请求多次
                registerButton.setEnabled(false);
                Map<String, String> reqMap = HttpRequestUtil.getCommonParams("");
                reqMap.put("password", SecurityUtil.MD5(password));
                reqMap.put("phone", phone);
                reqMap.put("authCode", code);
                // 调用注册的接口
                ProgressUtil.showProgressDialog(this,
                        getString(R.string.ysh_submit_register));
                HttpRequestUtil.sendHttpPostCommonRequest(this,
                        HttpAddressProperties.REGISTER_URL,
                        reqMap,
                        CommonResp.class,
                        registerHandler);
                break;
            default:
                break;
        }
    }
    
}
