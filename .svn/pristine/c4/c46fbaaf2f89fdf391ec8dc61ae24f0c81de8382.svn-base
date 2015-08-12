/*
 * 文 件 名:  ModifyPasswordActivity.java
 * 描    述:   修改密码页面
 * 创 建 人:  李晨光
 * 创建时间:  2014年6月18日
 */
package com.xweisoft.wx.family.ui.setting;

import java.util.Map;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.logic.global.GlobalConstant;
import com.xweisoft.wx.family.logic.global.HttpAddressProperties;
import com.xweisoft.wx.family.logic.model.response.CommonResp;
import com.xweisoft.wx.family.ui.BaseActivity;
import com.xweisoft.wx.family.ui.pc.LoginActivity;
import com.xweisoft.wx.family.util.CommonTitleUtil;
import com.xweisoft.wx.family.util.HttpRequestUtil;
import com.xweisoft.wx.family.util.ProgressUtil;
import com.xweisoft.wx.family.util.SecurityUtil;
import com.xweisoft.wx.family.util.SharedPreferencesUtil;
import com.xweisoft.wx.family.widget.NetHandler;

/**
 * 修改密码页面
 * 
 * @author  李晨光
 * @version  [版本号, 2014年6月18日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ModifyPasswordActivity extends BaseActivity implements
        OnClickListener
{
    
    private EditText newPasswordEditText = null;
    
    private EditText  oldPasswordEdiText =null;
    
    private EditText  nextPassword=null;
    
    private Button submitButton = null;
    
    private String password;
    private String oldpassword;
    private String netpwd;
    
    private NetHandler handler = new NetHandler(false)
    {
        
        @Override
        public void onResponse()
        {
            super.onResponse();
            submitButton.setEnabled(true);
        }
        
        @Override
        public void onSuccess(String errMsg, Message msg)
        {
            // 修改密码成功后，删除首选项中保存的旧密码，并跳转到登录页面
//            SharedPreferences preferences = getSharedPreferences(GlobalConstant.UserInfoPreference.PREFERENCE_NAME,
//                    MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putString(GlobalConstant.UserInfoPreference.PASSWORD, "");
//            editor.commit();
            //取密码的shared和重置的不一致。导致登录密码获取值有问题。现将密码统一保存到shared中
            SharedPreferencesUtil.saveSharedPreferences(mContext, GlobalConstant.UserInfoPreference.PASSWORD, password);
            
            Intent intent = new Intent(ModifyPasswordActivity.this,
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
                getResources().getString(R.string.ysh_change_password),
                null,
                false,
                true);
     
        newPasswordEditText = (EditText) findViewById(R.id.new_password_edittext);
        oldPasswordEdiText=(EditText) findViewById(R.id.modify_password_oldpwd);
        nextPassword=(EditText) findViewById(R.id.new_next_password_edittext);
        
        submitButton = (Button) findViewById(R.id.modify_password_submit_button);
    }
    
    @Override
    public void bindListener()
    {
        submitButton.setOnClickListener(this);
    }
    
    @Override
    public int getActivityLayout()
    {
        return R.layout.wx_updates_password_activity;
    }
    
    @Override
    public void onClick(View v)
    {
        if (v == submitButton)
        {
            password = newPasswordEditText.getText().toString().trim();
            oldpassword= oldPasswordEdiText.getText().toString().trim();
            netpwd=nextPassword.getText().toString().trim();
            
            if (TextUtils.isEmpty(newPasswordEditText.getText()))
            {
                showToast(getString(R.string.ysh_new_password_empty));
                return;
            }
            
            
            if (TextUtils.isEmpty(oldPasswordEdiText.getText()))
            {
                showToast(getString(R.string.ysh_new_password_empty));
                return;
            }
            
            
            if (!netpwd.equals(password)) {
            	
            	 showToast(getString(R.string.ysh_pwd_not_eq));
                 return;
				
			}
            
                        if (password.length() < 6 || password.length() > 16)
                        {
                            showToast(getString(R.string.ysh_password_error));
                            return;
                        }
            ProgressUtil.showProgressDialog(this,
                    getString(R.string.ysh_update_password2));
            submitButton.setEnabled(false);
            Map<String, String> param = HttpRequestUtil.getCommonParams("updatePassword");
            param.put("newPassword", SecurityUtil.MD5(password));
            param.put("oldPassword", SecurityUtil.MD5(oldpassword));
            
            HttpRequestUtil.sendHttpPostCommonRequest(mContext,
                    HttpAddressProperties.MODIFY_PASSWORD_URL,
                    param,
                    CommonResp.class,
                    handler);
        }
    }
}
