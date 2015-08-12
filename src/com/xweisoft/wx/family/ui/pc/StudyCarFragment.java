package com.xweisoft.wx.family.ui.pc;

import java.io.File;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.WXApplication;
import com.xweisoft.wx.family.logic.global.GlobalConstant;
import com.xweisoft.wx.family.logic.global.GlobalVariable;
import com.xweisoft.wx.family.logic.global.HttpAddressProperties;
import com.xweisoft.wx.family.logic.global.NetWorkCodes.CommontErrorCodes;
import com.xweisoft.wx.family.logic.global.NetWorkCodes.CommontSuccessCodes;
import com.xweisoft.wx.family.logic.model.ChildrenItem;
import com.xweisoft.wx.family.logic.model.VersionItem;
import com.xweisoft.wx.family.logic.model.response.CommonResp;
import com.xweisoft.wx.family.logic.model.response.UploadFileResp;
import com.xweisoft.wx.family.logic.model.response.VersionResp;
import com.xweisoft.wx.family.logic.request.DownClientApkRequest;
import com.xweisoft.wx.family.service.upload.IHttpListener;
import com.xweisoft.wx.family.service.upload.UploadItem;
import com.xweisoft.wx.family.ui.BaseFragment;
import com.xweisoft.wx.family.ui.setting.AboutUsActivity;
import com.xweisoft.wx.family.ui.setting.FeedbackActivity;
import com.xweisoft.wx.family.ui.setting.ModifyPasswordActivity;
import com.xweisoft.wx.family.ui.setting.SetMessageActivity;
import com.xweisoft.wx.family.ui.setting.SettingActivity;
import com.xweisoft.wx.family.util.CommonTitleUtil;
import com.xweisoft.wx.family.util.FileHelper;
import com.xweisoft.wx.family.util.HttpRequestUtil;
import com.xweisoft.wx.family.util.ListUtil;
import com.xweisoft.wx.family.util.LoginUtil;
import com.xweisoft.wx.family.util.ProgressUtil;
import com.xweisoft.wx.family.util.StringUtil;
import com.xweisoft.wx.family.util.Util;
import com.xweisoft.wx.family.widget.CommonDialog;
import com.xweisoft.wx.family.widget.CommonDialog.OnButtonClickListener;
import com.xweisoft.wx.family.widget.NetHandler;

/**
 * 个人中心
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-5-11]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class StudyCarFragment extends BaseFragment implements
        OnClickListener
{

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bindListener() {
		// TODO Auto-generated method stub
		
	}
	
	
	  @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState)
	    {
	        mLayouView = inflater.inflate(R.layout.activity_studentcar,
	                container,
	                false);
	        initViews();
	        bindListener();
	        return mLayouView;
	    }
    
    
}
