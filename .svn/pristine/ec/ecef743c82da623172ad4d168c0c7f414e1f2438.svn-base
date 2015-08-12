/*
 * 文 件 名:  ModifyUserNameActivity.java
 * 描    述:  修改昵称
 * 创 建 人:  李晨光
 * 创建时间:  2014年7月15日
 */
package com.xweisoft.wx.family.ui.pc;

import java.util.HashMap;
import java.util.Map;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.WXApplication;
import com.xweisoft.wx.family.logic.global.GlobalConstant;
import com.xweisoft.wx.family.logic.global.HttpAddressProperties;
import com.xweisoft.wx.family.logic.model.UserItem;
import com.xweisoft.wx.family.logic.model.response.CommonResp;
import com.xweisoft.wx.family.ui.BaseActivity;
import com.xweisoft.wx.family.util.CommonTitleUtil;
import com.xweisoft.wx.family.util.HttpRequestUtil;
import com.xweisoft.wx.family.util.ProgressUtil;
import com.xweisoft.wx.family.util.StringUtil;
import com.xweisoft.wx.family.widget.NetHandler;

/**
 * 修改昵称
 * 
 * @author  李晨光
 * @version  [版本号, 2014年7月15日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ModifyUserNameActivity extends BaseActivity implements
        OnClickListener
{
    
    /**
     * 用户名输入框
     */
    private EditText usernameEditText = null;
    
    /**
     * 提交按钮
     */
    private Button submitButton = null;
    
    /**
     * 用户名
     */
    private String username = "";
    
    /**
     * 修改用户名Handler
     */
    private NetHandler usernameHandler = new NetHandler(false)
    {
        
        @Override
        public void onSuccess(String errMsg, Message msg)
        {
            // 更新用户缓存中的性别
            SharedPreferences preferences = getSharedPreferences(GlobalConstant.UserInfoPreference.PREFERENCE_NAME,
                    MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            //            editor.putString(GlobalConstant.UserInfoPreference.USERNAME,
            //                    username);
            editor.commit();
            
            //            UserItem userItem = WXApplication.getInstance().loginUserItem;
            //            userItem.setUsername(username);
            
            //            WXApplication.getInstance().loginUserItem = userItem;
            
            finish();
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
    }
    
    /** {@inheritDoc} */
    
    @Override
    public void initViews()
    {
        CommonTitleUtil.initCommonTitle(this, "个人信息", null, false, true);
        usernameEditText = (EditText) findViewById(R.id.username_edittext);
        submitButton = (Button) findViewById(R.id.submit_button);
        String username = getIntent().getStringExtra("username");
        
        // 显示老用户名
        if (!StringUtil.isEmpty(username))
        {
            usernameEditText.setText(username);
        }
    }
    
    /** {@inheritDoc} */
    
    @Override
    public void bindListener()
    {
        submitButton.setOnClickListener(this);
    }
    
    /** {@inheritDoc} */
    
    @Override
    public int getActivityLayout()
    {
        return R.layout.ysh_modify_username_activity;
    }
    
    @Override
    public void onClick(View v)
    {
        if (v == submitButton)
        {
            username = usernameEditText.getText().toString().trim();
            if (StringUtil.isEmpty(username))
            {
                showToast(getString(R.string.ysh_nikename_empty));
            }
            else if (username.length() > 20)
            {
                showToast(getString(R.string.ysh_nikename_to_long));
            }
            else
            {
                //                ProgressUtil.showProgressDialog(this,
                //                        getString(R.string.ysh_update_nikename));
                //                String uid = "";
                //                String token = "";
                //                String gender = "";
                //                UserItem userItem = WXApplication.getInstance().loginUserItem;
                //                if (null != userItem)
                //                {
                //                    uid = userItem.getUid();
                //                    gender = userItem.getGender();
                //                    token = userItem.getToken();
                //                }
                //                
                //                Map<String, Object> reqMap = new HashMap<String, Object>();
                //                reqMap.put("uid", uid);
                //                reqMap.put("token", token);
                //                reqMap.put("username", username);
                //                reqMap.put("sex", gender);
                
                //                HttpRequestUtil.sendHttpPostRequest(this,
                //                    HttpAddressProperties.PersonalCenter.MODIFY_USERINFO,
                //                    reqMap,
                //                    CommonResp.class,
                //                    usernameHandler);
            }
        }
    }
    
}
