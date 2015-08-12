package com.xweisoft.wx.family.ui.pc;

import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.WXApplication;
import com.xweisoft.wx.family.logic.global.GlobalConstant;
import com.xweisoft.wx.family.logic.global.GlobalVariable;
import com.xweisoft.wx.family.logic.global.HttpAddressProperties;
import com.xweisoft.wx.family.logic.initialize.InitManager;
import com.xweisoft.wx.family.logic.model.ChildrenItem;
import com.xweisoft.wx.family.logic.model.UserItem;
import com.xweisoft.wx.family.logic.model.response.ChooseChildrenResp;
import com.xweisoft.wx.family.logic.model.response.ContactResp;
import com.xweisoft.wx.family.logic.model.response.LoginResp;
import com.xweisoft.wx.family.service.database.ContactsDBHelper;
import com.xweisoft.wx.family.ui.BaseActivity;
import com.xweisoft.wx.family.ui.sliding.WXMainFragmentActivity;
import com.xweisoft.wx.family.util.HttpRequestUtil;
import com.xweisoft.wx.family.util.ListUtil;
import com.xweisoft.wx.family.util.LoginUtil;
import com.xweisoft.wx.family.util.ProgressUtil;
import com.xweisoft.wx.family.util.SecurityUtil;
import com.xweisoft.wx.family.util.SharedPreferencesUtil;
import com.xweisoft.wx.family.util.StringUtil;
import com.xweisoft.wx.family.util.Util;
import com.xweisoft.wx.family.widget.NetHandler;

public class LoginActivity extends BaseActivity implements OnClickListener
{
    
    /**
     * 忘记密码
     */
    //private TextView forgetPwdText;
    
    /**
     * 手机号输入框
     */
    private EditText telphoneEditText = null;
    
    /**
     * 密码输入框
     */
    private EditText passwordEditText = null;
    
    /**
     * 记住密码checkBox
     */
    private LinearLayout checkBoxLayout = null;
    
    /**
     * 登录按钮
     */
    private Button loginButton = null;
    
    /***
     * 注册按钮
     */
   // private LinearLayout registerSubmit = null;
    
    /**
     * 记住密码选择框
     */
    private CheckBox rememberCheckBox = null;
    
    /**
     * 手机号
     */
    private String telphone = "";
    
    /**
     * 密码
     */
    private String password = "";
    
    /**
     * 背景顶部图片
     */
    private ImageView mImageView;
    
    
//    /**
//     * 登录Handler
//     */
//    private NetHandler loginHandler = new NetHandler(true)
//    {
//        
//        @Override
//        public void onResponse()
//        {
//            super.onResponse();
//            loginButton.setEnabled(true);
//        }
//        
//        @Override
//        public void onSuccess(String errMsg, Message msg)
//        {
//            LoginResp resp = null;
//            if (null != msg.obj && msg.obj instanceof LoginResp)
//            {
//                SharedPreferencesUtil.saveSharedPreferences(mContext,
//                        GlobalConstant.REMEMBER_PASSWORD,
//                        rememberCheckBox.isChecked());
//                
//                resp = (LoginResp) msg.obj;
//                UserItem item = new UserItem();
//                item.phone = telphone;
//                item.password = password;
//                item.parentSex = resp.parentSex;
//                item.userId = resp.userId;
//                item.children = resp.children;
//                LoginUtil.saveLoginInfo(mContext, item);
//                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
//                String cookieStr = gson.toJson(WXApplication.getInstance().cookieList);
//                SharedPreferencesUtil.saveSharedPreferences(mContext,
//                        SharedPreferencesUtil.SP_KEY_LOGIN_COOKIE,
//                        cookieStr);
//                finishActivity(item);
//            }
//        }
//        
//        @Override
//        public void onFailed(String errCode, String errMsg, Message msg)
//        {
//            showToast(errMsg);
//        }
//        
//    };
    
    
    /**
     * 关闭登录页面
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private void finishActivity(UserItem item)
    {
        if (null != item)
        {
            if (ListUtil.isEmpty(item.children))
            {
                showToast("无法连接值服务器，请稍后再试");
            }
         
        }
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayout());
        initViews();
        bindListener();
       // initData();
    }
    
    @Override
    public void initViews()
    {
        mImageView = (ImageView) findViewById(R.id.login_top_image);
       // forgetPwdText = (TextView) findViewById(R.id.forget_password_textview);
        telphoneEditText = (EditText) findViewById(R.id.login_telphone_edittext);
        passwordEditText = (EditText) findViewById(R.id.login_password_edittext);
        loginButton = (Button) findViewById(R.id.login_submit);
       // registerSubmit = (LinearLayout) findViewById(R.id.register_submit);
        rememberCheckBox = (CheckBox) findViewById(R.id.login_remember_checkbox);
        if (0 == GlobalVariable.screenWidth)
        {
            InitManager.getInstance().getDisplay(this);
        }
        /*
         * 图片因效果不可做成点9图片，根据图片长宽动态设置bitmap以及view的长宽
         */
        //LayoutParams params = (LayoutParams) mImageView.getLayoutParams();
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
              //  R.drawable.wx_login_top);
//        int w = bitmap.getWidth();
//        int h = bitmap.getHeight();
//        params.width = GlobalVariable.screenWidth;
//        params.height = GlobalVariable.screenWidth * h / w;
//        mImageView.setImageBitmap(bitmap);
//        mImageView.setLayoutParams(params);
    }
    
    /**
     * 初始化数据
     * @see [类、类#方法、类#成员]
     */
    private void initData()
    {
        telphoneEditText.setText(SharedPreferencesUtil.getSharedPreferences(mContext,
                GlobalConstant.UserInfoPreference.TELPHONE,
                ""));
        passwordEditText.setText(SharedPreferencesUtil.getSharedPreferences(mContext,
                GlobalConstant.UserInfoPreference.PASSWORD,
                ""));
      //  rememberCheckBox.setChecked(SharedPreferencesUtil.getSharedPreferences(mContext,
        //        GlobalConstant.REMEMBER_PASSWORD,
         //       false));
    }
    
    @Override
    public void bindListener()
    {
        //forgetPwdText.setOnClickListener(this);
        loginButton.setOnClickListener(this);
       // registerSubmit.setOnClickListener(this);
       // checkBoxLayout.setOnClickListener(this);
    }
    
    @Override
    public int getActivityLayout()
    {
        return R.layout.activity_login;
    }
    
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.common_title_right:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.forget_password_textview:
                Intent intent2 = new Intent(this, ForgetPasswordActivity.class);
                startActivity(intent2);
                break;
          //  case R.id.register_submit:
            ////    Intent intent1 = new Intent(this, RegisterActivity.class);
             //   startActivity(intent1);
             //   break;
            case R.id.login_submit:
//                telphone = telphoneEditText.getText().toString().trim();
//                password = passwordEditText.getText().toString().trim();
//                if (StringUtil.isEmpty(telphone))
//                {
//                    showToast(getString(R.string.ysh_telphone_empty));
//                }
//                else if (!Util.isPhoneNumber(telphone))
//                {
//                    showToast(getString(R.string.publish_info_phone_check_toast));
//                }
//                else if (StringUtil.isEmpty(password))
//                {
//                    showToast(getString(R.string.ysh_password_empty));
//                }
//                else
//                {
//                	
//                	
               	Intent inten = new Intent(this,
                           WXMainFragmentActivity.class);
                   startActivity(inten);
                	
//                    loginButton.setEnabled(false);
//                    Map<String, String> reqMap = HttpRequestUtil.getCommonParams("");
//                    reqMap.put("phone", telphone);
//                    reqMap.put("password", SecurityUtil.MD5(password));
//                    reqMap.put("clientType", GlobalConstant.CLIENT_TYPE_ANDROID);
//                    reqMap.put("serviceType", "1");//拓展属性 默认为1
//                    // 调用登录接口
//                    ProgressUtil.showProgressDialog(this,
//                            getString(R.string.ysh_submit_login));
//                    HttpRequestUtil.sendHttpPostCommonRequest(this,
//                            HttpAddressProperties.LOGIN_URL,
//                            reqMap,
//                            LoginResp.class,
//                            loginHandler);
                	
                	
                	
              //  }
                break;
            case R.id.chekbox_layout:
                if (rememberCheckBox.isChecked())
                {
                    rememberCheckBox.setChecked(false);
                }
                else
                {
                    rememberCheckBox.setChecked(true);
                }
                break;
            default:
                break;
        }
    }
    
    
}
