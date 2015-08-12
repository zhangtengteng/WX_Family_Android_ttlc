package com.xweisoft.wx.family.ui.message;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.WXApplication;
import com.xweisoft.wx.family.logic.global.HttpAddressProperties;
import com.xweisoft.wx.family.logic.model.ApplyInfoItem;
import com.xweisoft.wx.family.logic.model.ApplyParentItem;
import com.xweisoft.wx.family.logic.model.response.CommonResp;
import com.xweisoft.wx.family.ui.BaseActivity;
import com.xweisoft.wx.family.ui.message.view.CancelDialog;
import com.xweisoft.wx.family.ui.message.view.CancelDialog.OnConfirmClickListener;
import com.xweisoft.wx.family.util.CommonTitleUtil;
import com.xweisoft.wx.family.util.HttpRequestUtil;
import com.xweisoft.wx.family.util.ListUtil;
import com.xweisoft.wx.family.widget.NetHandler;

public class ApplyParentInfoActivity extends BaseActivity implements
        OnClickListener
{
    /**
     * 上一步
     */
    private TextView mPreviousText;
    
    /**
     * 提交
     */
    private TextView mNextText;
    
    /**
     * 添加
     */
    private View mAddView;
    
    /**
     * 删除
     */
    private View mDeleteView;
    
    /**
     * 第二个信息填写view
     */
    private View mTwoView;
    
    /**
     * （一）姓名
     */
    private EditText mOneNameEdit;
    
    /**
     * （一）称谓
     */
    private EditText mOneCallEdit;
    
    /**
     * （一）单位
     */
    private EditText mOneCompanyEdit;
    
    /**
     * （一）职位
     */
    private EditText mOnePositionEdit;
    
    /**
     * （一）联系方式
     */
    private EditText mOnePhoneEdit;
    
    /**
     * （二）姓名
     */
    private EditText mTwoNameEdit;
    
    /**
     * （二）称谓
     */
    private EditText mTwoCallEdit;
    
    /**
     * （二）单位
     */
    private EditText mTwoCompanyEdit;
    
    /**
     * （二）职位
     */
    private EditText mTwoPositionEdit;
    
    /**
     * （二）联系方式
     */
    private EditText mTwoPhoneEdit;
    
    /**
     * 是否是两个家长，默认为一个家长
     */
    private boolean isDouble = false;
    
    /**
     * 返回
     */
    private View mBackView;
    
    /**
     * 申请对象
     */
    private ApplyInfoItem item;
    
    /**
     * 更多信息取消提示框
     */
    private CancelDialog mCancelDialog;
    
    private Handler handler = new NetHandler()
    {
        
        @Override
        public void onResponse()
        {
            super.onResponse();
            mNextText.setEnabled(true);
        }
        
        @Override
        public void onSuccess(String errMsg, Message msg)
        {
            WXApplication.getInstance().closedFinishActivity();
            Intent intent = new Intent(mContext, ApplyActivity.class);
            startActivity(intent);
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
        WXApplication.getInstance().addFinishActivity(this);
    }
    
    @Override
    public void initViews()
    {
        CommonTitleUtil.initCommonTitle(this,
                getString(R.string.add_prediction_info),
                null,
                false,
                true);
        mPreviousText = (TextView) findViewById(R.id.apply_parent_info_previous);
        mNextText = (TextView) findViewById(R.id.apply_parent_info_next);
        mBackView = findViewById(R.id.common_left_back);
        
        mAddView = findViewById(R.id.apply_parent_info_add_view);
        mDeleteView = findViewById(R.id.apply_parent_info_two_delete);
        mTwoView = findViewById(R.id.apply_parent_info_two_view);
        
        mOneNameEdit = (EditText) findViewById(R.id.apply_parent_info_one_name);
        mOneCallEdit = (EditText) findViewById(R.id.apply_parent_info_one_call);
        mOneCompanyEdit = (EditText) findViewById(R.id.apply_parent_info_one_company);
        mOnePositionEdit = (EditText) findViewById(R.id.apply_parent_info_one_position);
        mOnePhoneEdit = (EditText) findViewById(R.id.apply_parent_info_one_phone);
        
        mTwoNameEdit = (EditText) findViewById(R.id.apply_parent_info_two_name);
        mTwoCallEdit = (EditText) findViewById(R.id.apply_parent_info_two_call);
        mTwoCompanyEdit = (EditText) findViewById(R.id.apply_parent_info_two_company);
        mTwoPositionEdit = (EditText) findViewById(R.id.apply_parent_info_two_position);
        mTwoPhoneEdit = (EditText) findViewById(R.id.apply_parent_info_two_phone);
        
        mCancelDialog = new CancelDialog(mContext, "填写更多信息", "取消填写更多信息",
                new OnConfirmClickListener()
                {
                    
                    @Override
                    public void onConfirmClick()
                    {
                        isDouble = false;
                        mAddView.setVisibility(View.VISIBLE);
                        mTwoView.setVisibility(View.GONE);
                        if (null != item)
                        {
                            item.parents.remove(1);
                            mTwoNameEdit.setText("");
                            mTwoCallEdit.setText("");
                            mTwoCompanyEdit.setText("");
                            mTwoPositionEdit.setText("");
                            mTwoPhoneEdit.setText("");
                        }
                    }
                });
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        item = WXApplication.getInstance().getApplyInfoItem();
        if (null != item)
        {
            if (ListUtil.isEmpty(item.parents))
            {
                ApplyParentItem applyParentItem = new ApplyParentItem();
                item.parents.add(applyParentItem);
            }
            else
            {
                ApplyParentItem oneItem = item.parents.get(0);
                if (null != oneItem)
                {
                    if (!TextUtils.isEmpty(oneItem.name))
                    {
                        mOneNameEdit.setText(oneItem.name);
                    }
                    if (!TextUtils.isEmpty(oneItem.appellation))
                    {
                        mOneCallEdit.setText(oneItem.appellation);
                    }
                    if (!TextUtils.isEmpty(oneItem.workUnit))
                    {
                        mOneCompanyEdit.setText(oneItem.workUnit);
                    }
                    if (!TextUtils.isEmpty(oneItem.postion))
                    {
                        mOnePositionEdit.setText(oneItem.postion);
                    }
                    if (!TextUtils.isEmpty(oneItem.phone))
                    {
                        mOnePhoneEdit.setText(oneItem.phone);
                    }
                }
                if (item.parents.size() == 2)
                {
                    isDouble = true;
                    mAddView.setVisibility(View.GONE);
                    mTwoView.setVisibility(View.VISIBLE);
                    ApplyParentItem twoItem = item.parents.get(1);
                    if (null != twoItem)
                    {
                        if (!TextUtils.isEmpty(twoItem.name))
                        {
                            mTwoNameEdit.setText(twoItem.name);
                        }
                        if (!TextUtils.isEmpty(twoItem.appellation))
                        {
                            mTwoCallEdit.setText(twoItem.appellation);
                        }
                        if (!TextUtils.isEmpty(twoItem.workUnit))
                        {
                            mTwoCompanyEdit.setText(twoItem.workUnit);
                        }
                        if (!TextUtils.isEmpty(twoItem.postion))
                        {
                            mTwoPositionEdit.setText(twoItem.postion);
                        }
                        if (!TextUtils.isEmpty(twoItem.phone))
                        {
                            mTwoPhoneEdit.setText(twoItem.phone);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void bindListener()
    {
        mPreviousText.setOnClickListener(this);
        mNextText.setOnClickListener(this);
        mBackView.setOnClickListener(this);
        mAddView.setOnClickListener(this);
        mDeleteView.setOnClickListener(this);
    }
    
    @Override
    public int getActivityLayout()
    {
        return R.layout.apply_parent_info_activity;
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.common_left_back:
                getText(false);
                finish();
                break;
            case R.id.apply_parent_info_previous:
                getText(false);
                finish();
                break;
            case R.id.apply_parent_info_add_view:
                isDouble = true;
                mAddView.setVisibility(View.GONE);
                mTwoView.setVisibility(View.VISIBLE);
                if (null != item)
                {
                    ApplyParentItem parentItem = new ApplyParentItem();
                    item.parents.add(1, parentItem);
                }
                break;
            case R.id.apply_parent_info_two_delete:
                mCancelDialog.show();
                break;
            case R.id.apply_parent_info_next:
                getText(true);
                break;
            default:
                break;
        }
    }
    
    private void getText(boolean submit)
    {
        String oneName = mOneNameEdit.getText().toString().trim();
        String oneCall = mOneCallEdit.getText().toString().trim();
        String oneCompany = mOneCompanyEdit.getText().toString().trim();
        String onePositon = mOnePositionEdit.getText().toString().trim();
        String onePhone = mOnePhoneEdit.getText().toString().trim();
        if (null != item)
        {
            item.parents.get(0).name = oneName;
            item.parents.get(0).appellation = oneCall;
            item.parents.get(0).workUnit = oneCompany;
            item.parents.get(0).postion = onePositon;
            item.parents.get(0).phone = onePhone;
        }
        if (isDouble)
        {
            String twoName = mTwoNameEdit.getText().toString().trim();
            String twoCall = mTwoCallEdit.getText().toString().trim();
            String twoCompany = mTwoCompanyEdit.getText().toString().trim();
            String twoPositon = mTwoPositionEdit.getText().toString().trim();
            String twoPhone = mTwoPhoneEdit.getText().toString().trim();
            if (null != item)
            {
                item.parents.get(1).name = twoName;
                item.parents.get(1).appellation = twoCall;
                item.parents.get(1).workUnit = twoCompany;
                item.parents.get(1).postion = twoPositon;
                item.parents.get(1).phone = twoPhone;
            }
        }
        if (submit)
        {
            mNextText.setEnabled(false);
            Map<String, String> map = HttpRequestUtil.getCommonParams(HttpAddressProperties.APPLY_SUBMIT);
            map.put("predictionInfo", new Gson().toJson(item));
            HttpRequestUtil.sendHttpPostCommonRequest(mContext,
                    HttpAddressProperties.SERVICE_URL,
                    map,
                    CommonResp.class,
                    handler);
        }
    }
}
