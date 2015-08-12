package com.xweisoft.wx.family.ui.pc;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.logic.global.GlobalConstant;
import com.xweisoft.wx.family.logic.global.HttpAddressProperties;
import com.xweisoft.wx.family.logic.model.response.CommonResp;
import com.xweisoft.wx.family.ui.BaseActivity;
import com.xweisoft.wx.family.util.CommonTitleUtil;
import com.xweisoft.wx.family.util.HttpRequestUtil;
import com.xweisoft.wx.family.util.ProgressUtil;
import com.xweisoft.wx.family.util.SecurityUtil;
import com.xweisoft.wx.family.util.SharedPreferencesUtil;
import com.xweisoft.wx.family.util.StringUtil;
import com.xweisoft.wx.family.util.Util;
import com.xweisoft.wx.family.widget.NetHandler;

/**
 * 忘记密码页面
 * 
 * @author  poorgod
 * @version  [版本号, 2014年6月18日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ForgetPasswordActivity extends BaseActivity implements
        OnClickListener
{
    
    /**
     * 电话输入框
     */
    private EditText telphoneEditText = null;
    
    /**
     * 验证码输入框
     */
    private EditText codeEditText = null;
    
    /**
     * 密码输入框
     */
    private EditText passwordEditText = null;
    
    /**
     * 验证码按钮
     */
    private TextView codeButton = null;
    
    /**
     * 提交按钮
     */
    private Button submitButton = null;
    
    //  private VerifyCodeCount mVerifyCodeCount;
    
    private String telphone;
    
    /**
     * 验证码倒计时
     */
    private CountDownTimer timer = new CountDownTimer(61 * 1000, 1000)
    {
        @Override
        public void onTick(long millisUntilFinished)
        {
            codeButton.setText(getString(R.string.ysh_get_validate_number_again)
                    + "(" + (millisUntilFinished / 1000 - 1) + ")");
        }
        
        @Override
        public void onFinish()
        {
            codeButton.setEnabled(true);
            codeButton.setText(getString(R.string.ysh_get_validate_number));
        }
    };
    
    /**
     * 验证码Handler
     */
    private NetHandler codeHandler = new NetHandler(false)
    {
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
        };
        
        @Override
        public void onSuccess(String errMsg, Message msg)
        {
            showToast(getString(R.string.ysh_codetophone));
        }
        
        @Override
        public void onFailed(String errCode, String errMsg, Message msg)
        {
            Toast.makeText(mContext, errMsg, Toast.LENGTH_SHORT).show();
            cancelCodeControl();
        }
        
        /** {@inheritDoc} */
        
        @Override
        public void netTimeout()
        {
            cancelCodeControl();
        }
        
        /** {@inheritDoc} */
        
        @Override
        public void networkErr()
        {
            cancelCodeControl();
        }
        
        /** {@inheritDoc} */
        
        @Override
        public void otherErr()
        {
            cancelCodeControl();
        }
    };
    
    /**
     * 忘记密码
     */
    private NetHandler forgetHandler = new NetHandler(false)
    {
        
        @Override
        public void handleMessage(Message msg)
        {
            ProgressUtil.dismissProgressDialog();
            super.handleMessage(msg);
        }
        
        @Override
        public void onSuccess(String errMsg, Message msg)
        {
            // 修改密码成功后，删除首选项中保存的旧密码，并跳转到登录页面
            SharedPreferencesUtil.saveSharedPreferences(mContext,
                    GlobalConstant.UserInfoPreference.TELPHONE,
                    telphone);
            SharedPreferencesUtil.saveSharedPreferences(mContext,
                    GlobalConstant.UserInfoPreference.PASSWORD,
                    "");
            Intent intent = new Intent(ForgetPasswordActivity.this,
                    LoginActivity.class);
            startActivity(intent);
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
                getString(R.string.ysh_forget_password2),
                null,
                false,
                true);
        telphoneEditText = (EditText) findViewById(R.id.telphone_edittext);
        codeEditText = (EditText) findViewById(R.id.code_edittext);
        passwordEditText = (EditText) findViewById(R.id.password_edittext);
        codeButton = (TextView) findViewById(R.id.code_button);
        submitButton = (Button) findViewById(R.id.submit_button);
    }
    
    @Override
    public void bindListener()
    {
        codeButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);
    }
    
    @Override
    public int getActivityLayout()
    {
        return R.layout.ysh_forget_password_activity;
    }
    
    @Override
    public void onClick(View v)
    {
        if (v == codeButton)
        {
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
                // 单击获取验证码按钮，马上设为不可用，并开始倒计时30秒收，重新恢复可用
                codeButton.setEnabled(false);
                
                codeControl();
                
                showToast(getString(R.string.ysh_send_sms));
                
                // 调用获取验证码的接口
                Map<String, String> params = HttpRequestUtil.getCommonParams(HttpAddressProperties.GET_VERIFYCODE);
                params.put("phone", telphone);
                params.put("type", "1");
                HttpRequestUtil.sendHttpPostCommonRequest(mContext,
                        HttpAddressProperties.CODE_AUTH, 
                        params,
                        CommonResp.class,
                        codeHandler);
                
            }
        }
        else if (v == submitButton)
        {
            telphone = telphoneEditText.getText().toString().trim();
            String code = codeEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            //            String confirmPassword = confirmEditText.getText().toString().trim();
            if (StringUtil.isEmpty(telphone))
            {
                showToast(getString(R.string.ysh_telphone_empty));
            }
            else if (!Util.isPhone(telphone))
            {
                showToast(getString(R.string.publish_info_phone_check_toast));
            }
            if (StringUtil.isEmpty(code))
            {
                showToast(getString(R.string.ysh_code_empty));
            }
            else if (StringUtil.isEmpty(password))
            {
                showToast(getString(R.string.ysh_password_empty));
            }
            //            else if(StringUtil.isEmpty(confirmPassword))
            //            {
            //                showToast(getString(R.string.ysh_conform_password_empty));
            //            }
            else if (password.length() < 6 || password.length() > 16)
            {
                showToast(getString(R.string.ysh_password_error));
            }
            //            else if (confirmPassword.length() < 6 || confirmPassword.length() > 16)
            //            {
            //                showToast(getString(R.string.ysh_sure_password_error));
            //            }
            //            else if (!confirmPassword.equals(password))
            //            {
            //                showToast(getString(R.string.ysh_two_password_not_equals));
            //            }
            else
            {
                ProgressUtil.showProgressDialog(this,
                        getString(R.string.ysh_update_password));
                Map<String, String> reqMap = HttpRequestUtil.getCommonParams("forgotPwd");
                reqMap.put("phone", telphone);
                reqMap.put("authCode", code);
                reqMap.put("newPassword", SecurityUtil.MD5(password));
                
                HttpRequestUtil.sendHttpPostCommonRequest(this,
                        HttpAddressProperties.FORGET_PASSWORD,
                        reqMap,
                        CommonResp.class,
                        forgetHandler);
                
            }
        }
    }
    
    /**
     * 验证码控制
     * @see [类、类#方法、类#成员]
     */
    private void codeControl()
    {
        timer.start();
    }
    
    /**
     * 取消控制器
     * @see [类、类#方法、类#成员]
     */
    private void cancelCodeControl()
    {
        timer.cancel();
        codeButton.setEnabled(true);
        codeButton.setText(getString(R.string.ysh_get_validate_number));
    }
}
