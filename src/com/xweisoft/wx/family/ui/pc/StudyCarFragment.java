package com.xweisoft.wx.family.ui.pc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.ui.BaseFragment;

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

	private Button btnSubmit;


	@Override
	public void onClick(View view) {
		
		switch (view.getId()) {
		case R.id.btn_submit:
			Toast.makeText(getActivity(), "提交成功！", 2000).show();
			break;

		default:
			break;
		}
		
	}

	@Override
	public void initViews() {
		btnSubmit = (Button) mLayouView.findViewById(R.id.btn_submit);
		
	}

	@Override
	public void bindListener() {
		btnSubmit.setOnClickListener(this);
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
