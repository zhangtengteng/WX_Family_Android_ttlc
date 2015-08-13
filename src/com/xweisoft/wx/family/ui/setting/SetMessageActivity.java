package com.xweisoft.wx.family.ui.setting;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.WXApplication;
import com.xweisoft.wx.family.ui.BaseActivity;
import com.xweisoft.wx.family.util.CommonTitleUtil;
import com.xweisoft.wx.family.util.SharedPreferencesUtil;
import com.xweisoft.wx.family.widget.SettingSlipButton;
import com.xweisoft.wx.family.widget.SettingSlipButton.OnChangedListener;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 消息设置界面
 * @author  houfangfang
 * @version  [版本号, 2015-7-17]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class SetMessageActivity extends BaseActivity implements OnClickListener
{
    
    /**
     * 新消息通知
     */
    private SettingSlipButton receiveNoticeSlipBt;
    
    /**
     * 显示具体信息
     */
    private SettingSlipButton showInfoSlipBt;
    
    /**
     * 声音
     */
    private SettingSlipButton voiceSlipBt;
    
    /**
     * 震动
     */
    private SettingSlipButton vibrateSlipBt;
    
    private Button chage;
    
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
       /* CommonTitleUtil.initCommonTitle(this,
                getString(R.string.ysh_setting),
                null,
                false,
                true);*/
        
//        chage=(Button) findViewById(R.id.common_qiehuan);
//        chage.setVisibility(View.GONE);
    	
    	
    	tvTop = (TextView) findViewById(R.id.generalTitleLabel);
    	tvTop.setText(R.string.app_set);
        receiveNoticeSlipBt = (SettingSlipButton) findViewById(R.id.set_message_receive_notice_slip_button);
        showInfoSlipBt = (SettingSlipButton) findViewById(R.id.set_message_show_info_slip_button);
        voiceSlipBt = (SettingSlipButton) findViewById(R.id.set_message_voice_slip_button);
        vibrateSlipBt = (SettingSlipButton) findViewById(R.id.set_message_vibrate_slip_button);
        
        receiveNoticeSlipBt.setCheck(WXApplication.getInstance().im_message_receive);
        showInfoSlipBt.setCheck(WXApplication.getInstance().im_message_notifi_show_info);
        voiceSlipBt.setCheck(WXApplication.getInstance().system_voice);
        vibrateSlipBt.setCheck(WXApplication.getInstance().system_vibrate);
    }
    
    @Override
    public void bindListener()
    {
        receiveNoticeSlipBt.SetOnChangedListener(new OnChangedListener()
        {
            @Override
            public void OnChanged(boolean CheckState)
            {
                SharedPreferencesUtil.saveSharedPreferences(mContext,
                        SharedPreferencesUtil.SP_KEY_MESSAGE_SET_RECEIVE,
                        CheckState);
                WXApplication.getInstance().im_message_receive = CheckState;
            }
        });
        
        showInfoSlipBt.SetOnChangedListener(new OnChangedListener()
        {
            @Override
            public void OnChanged(boolean CheckState)
            {
                SharedPreferencesUtil.saveSharedPreferences(mContext,
                        SharedPreferencesUtil.SP_KEY_MESSAGE_INFO_NOTIFI_SET_SHOW,
                        CheckState);
                WXApplication.getInstance().im_message_notifi_show_info = CheckState;
            }
        });
        
        voiceSlipBt.SetOnChangedListener(new OnChangedListener()
        {
            @Override
            public void OnChanged(boolean CheckState)
            {
                SharedPreferencesUtil.saveSharedPreferences(mContext,
                        SharedPreferencesUtil.SP_KEY_SYSTEM_SET_VOICE,
                        CheckState);
                WXApplication.getInstance().system_voice = CheckState;
            }
        });
        
        vibrateSlipBt.SetOnChangedListener(new OnChangedListener()
        {
            @Override
            public void OnChanged(boolean CheckState)
            {
                SharedPreferencesUtil.saveSharedPreferences(mContext,
                        SharedPreferencesUtil.SP_KEY_SYSTEM_SET_VIBRATE,
                        CheckState);
                WXApplication.getInstance().system_vibrate = CheckState;
            }
        });
    }
    
    @Override
    public int getActivityLayout()
    {
        return R.layout.set_message_activity;
    }
    
    @Override
    public void onClick(View v)
    {
        
    }
    
}
