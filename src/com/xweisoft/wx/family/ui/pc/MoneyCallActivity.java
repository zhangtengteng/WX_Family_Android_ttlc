package com.xweisoft.wx.family.ui.pc;

import java.util.Map;

import android.app.Activity;
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
public class MoneyCallActivity extends Activity
{
	
	private Button moneynextly;
	  @Override
	    protected void onCreate(Bundle savedInstanceState)
	    {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.wallet);
	        
	        moneynextly= (Button) findViewById(R.id.moneynext);
	        moneynextly.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
				}
			});
	        
	    }


	  
	
    
}
