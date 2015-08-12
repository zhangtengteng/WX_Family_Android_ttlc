package com.xweisoft.wx.family.ui.message;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.WXApplication;
import com.xweisoft.wx.family.logic.model.ApplyInfoItem;
import com.xweisoft.wx.family.ui.BaseActivity;
import com.xweisoft.wx.family.ui.message.view.CancelDialog;
import com.xweisoft.wx.family.ui.message.view.CancelDialog.OnConfirmClickListener;
import com.xweisoft.wx.family.util.CommonTitleUtil;
import com.xweisoft.wx.family.widget.GenderChooseDialog;

public class ApplyStudentInfoActivity extends BaseActivity implements
        OnClickListener
{
    /**
     * 上一步
     */
    private TextView mPreviousText;
    
    /**
     * 下一步
     */
    private TextView mNextText;
    
    /**
     * 姓名
     */
    private EditText mNameEditText;
    
    /**
     * 性别选择
     */
    private View mSexView;
    
    /**
     * 性别
     */
    private TextView mSexTextView;
    
    /**
     * 名族
     */
    private EditText mNationEditText;
    
    /**
     * 身份证
     */
    private EditText mIDCardEditText;
    
    /**
     * 毕业学校
     */
    private EditText mSchoolEditText;
    
    /**
     * 户口所在地
     */
    private EditText mHuKouEditText;
    
    /**
     * 家庭住址
     */
    private EditText mHouseAddressEditText;
    
    /**
     * 性别选择框
     */
    private GenderChooseDialog mGenderChooseDialog;
    
    /**
     * 放弃编辑提示框
     */
    private CancelDialog mCancelDialog;
    
    /**
     * 返回布局
     */
    private View mBackView;
    
    /**
     * 性别 1 男 2 女
     */
    private int sex;
    
    /**
     * 申请信息对象
     */
    private ApplyInfoItem item;
    
    /**
     * 选择框监听事件
     */
    private OnClickListener dialogClickListener = new OnClickListener()
    {
        
        @Override
        public void onClick(View v)
        {
            mGenderChooseDialog.dismiss();
            switch (v.getId())
            {
                case R.id.gender_man:
                    sex = 1;
                    mSexTextView.setText("男");
                    break;
                case R.id.gender_woman:
                    sex = 2;
                    mSexTextView.setText("女");
                    break;
                case R.id.gender_cancel:
                    break;
                default:
                    break;
            }
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
        mPreviousText = (TextView) findViewById(R.id.apply_student_info_previous);
        mNextText = (TextView) findViewById(R.id.apply_student_info_next);
        
        mNameEditText = (EditText) findViewById(R.id.apply_student_info_name);
        mSexView = findViewById(R.id.apply_student_info_sex_view);
        mNationEditText = (EditText) findViewById(R.id.apply_student_info_nation);
        mIDCardEditText = (EditText) findViewById(R.id.apply_student_info_id_card);
        mSchoolEditText = (EditText) findViewById(R.id.apply_student_info_school);
        mHuKouEditText = (EditText) findViewById(R.id.apply_student_info_hukou);
        mHouseAddressEditText = (EditText) findViewById(R.id.apply_student_info_house_address);
        mSexTextView = (TextView) findViewById(R.id.apply_student_info_sex_text);
        mGenderChooseDialog = new GenderChooseDialog(mContext,
                dialogClickListener);
        mBackView = findViewById(R.id.common_left_back);
        mCancelDialog = new CancelDialog(mContext, new OnConfirmClickListener()
        {
            
            @Override
            public void onConfirmClick()
            {
                finish();
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
            if (!TextUtils.isEmpty(item.studentName))
            {
                mNameEditText.setText(item.studentName);
            }
            if (!TextUtils.isEmpty(item.familyName))
            {
                mNationEditText.setText(item.familyName);
            }
            if (!TextUtils.isEmpty(item.idNumber))
            {
                mIDCardEditText.setText(item.idNumber);
            }
            if (!TextUtils.isEmpty(item.graduateSchool))
            {
                mSchoolEditText.setText(item.graduateSchool);
            }
            if (!TextUtils.isEmpty(item.homeTown))
            {
                mHuKouEditText.setText(item.homeTown);
            }
            if (!TextUtils.isEmpty(item.homeAddress))
            {
                mHouseAddressEditText.setText(item.homeAddress);
            }
            if (0 != item.sex)
            {
                if (1 == item.sex)
                {
                    mSexTextView.setText("男");
                }
                else if (2 == item.sex)
                {
                    mSexTextView.setText("女");
                }
            }
        }
    }
    
    @Override
    public void bindListener()
    {
        mPreviousText.setOnClickListener(this);
        mNextText.setOnClickListener(this);
        mSexView.setOnClickListener(this);
        mBackView.setOnClickListener(this);
    }
    
    @Override
    public int getActivityLayout()
    {
        return R.layout.apply_student_info_activity;
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.common_left_back:
                mCancelDialog.show();
                break;
            case R.id.apply_student_info_previous:
                mCancelDialog.show();
                break;
            case R.id.apply_student_info_sex_view:
                mGenderChooseDialog.showDialog();
                break;
            case R.id.apply_student_info_next:
                String name = mNameEditText.getText().toString().trim();
                String nation = mNationEditText.getText().toString().trim();
                String idCard = mIDCardEditText.getText().toString().trim();
                String school = mSchoolEditText.getText().toString().trim();
                String hukou = mHuKouEditText.getText().toString().trim();
                String address = mHouseAddressEditText.getText()
                        .toString()
                        .trim();
                if (TextUtils.isEmpty(name))
                {
                    showToast("请输入姓名");
                    return;
                }
                if ("1".equals(sex) && "2".equals(sex))
                {
                    showToast("请选择性别");
                    return;
                }
                if (TextUtils.isEmpty(nation))
                {
                    showToast("请输入民族");
                    return;
                }
                if (TextUtils.isEmpty(idCard))
                {
                    showToast("请输入身份证号");
                    return;
                }
                if (TextUtils.isEmpty(school))
                {
                    showToast("请输入毕业学校");
                    return;
                }
                if (TextUtils.isEmpty(hukou))
                {
                    showToast("请输入户口所在地");
                    return;
                }
                if (TextUtils.isEmpty(address))
                {
                    showToast("请输入家庭住址");
                    return;
                }
                if (null != item)
                {
                    item.studentName = name;
                    item.sex = sex;
                    item.familyName = nation;
                    item.idNumber = idCard;
                    item.graduateSchool = school;
                    item.homeTown = hukou;
                    item.homeAddress = address;
                }
                Intent intent = new Intent(mContext,
                        ApplyParentInfoActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            mCancelDialog.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
