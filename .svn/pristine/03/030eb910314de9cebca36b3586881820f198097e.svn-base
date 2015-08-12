package com.xweisoft.wx.family.ui.contact;

import java.util.Map;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.WXApplication;
import com.xweisoft.wx.family.logic.global.HttpAddressProperties;
import com.xweisoft.wx.family.logic.model.ChildrenItem;
import com.xweisoft.wx.family.logic.model.ContactItem;
import com.xweisoft.wx.family.logic.model.response.ContactAddResp;
import com.xweisoft.wx.family.service.database.ContactsDBHelper;
import com.xweisoft.wx.family.ui.BaseActivity;
import com.xweisoft.wx.family.ui.message.ChatMessageActivity;
import com.xweisoft.wx.family.util.CommonTitleUtil;
import com.xweisoft.wx.family.util.HttpRequestUtil;
import com.xweisoft.wx.family.util.LoginUtil;
import com.xweisoft.wx.family.util.ProgressUtil;
import com.xweisoft.wx.family.util.Util;
import com.xweisoft.wx.family.widget.CommonDialog;
import com.xweisoft.wx.family.widget.CommonDialog.OnButtonClickListener;
import com.xweisoft.wx.family.widget.NetHandler;

/**
 * 通讯录个人详细信息
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-7-23]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ContactInfoActivity extends BaseActivity implements
        OnClickListener
{
    /**
     * 个人对象
     */
    private ContactItem mItem;
    
    /**
     * 头像
     */
    private ImageView mHeaderImage;
    
    /**
     * 名称
     */
    private TextView mNameText;
    
    /**
     * 电话
     */
    private TextView mPhoneText;
    
    /**
     * 班级
     */
    private TextView mClassText;
    
    /**
     * 性别
     */
    private TextView mSexText;
    
    /**
     * 发短信
     */
    private View mSmsView;
    
    /**
     * 打电话
     */
    private View mCallView;
    
    /**
     * 聊天
     */
    private View mChatView;
    
    /**
     * 添加
     */
    private View mAddView;
    
    /**
     * 加为(取消)好友
     */
    private TextView mAddText;
    
    /**
     * 通讯录数据库帮助类
     */
    private ContactsDBHelper mDbHelper;
    
    private Handler addHandler = new NetHandler(true)
    {
        
        @Override
        public void onResponse()
        {
            super.onResponse();
            mAddView.setEnabled(true);
            ProgressUtil.dismissProgressDialog();
        }
        
        @Override
        public void onSuccess(String errMsg, Message msg)
        {
            ContactAddResp resp = null;
            if (null != msg.obj && msg.obj instanceof ContactAddResp)
            {
                resp = (ContactAddResp) msg.obj;
                mItem.contactId = resp.contactId;
                mItem.isFriend = "1";
                mAddText.setText("取消好友");
                
                mDbHelper.update(mItem);
            }
        }
        
        @Override
        public void onFailed(String errCode, String errMsg, Message msg)
        {
            showToast(errMsg);
        }
    };
    
    private Handler delHandler = new NetHandler(true)
    {
        
        @Override
        public void onResponse()
        {
            super.onResponse();
            mAddView.setEnabled(true);
            ProgressUtil.dismissProgressDialog();
        }
        
        @Override
        public void onSuccess(String errMsg, Message msg)
        {
            mItem.isFriend = "0";
            mItem.contactId = "";
            mAddText.setText("加为好友");
            mDbHelper.update(mItem);
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
        getBundle();
        initViews();
        bindListener();
    }
    
    @Override
    public void initViews()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(mItem.name);
        if (!TextUtils.isEmpty(mItem.appellation))
        {
            buffer.append("（" + mItem.appellation + "）");
        }
        CommonTitleUtil.initCommonTitle(this,
                buffer.toString(),
                "",
                false,
                true);
        mHeaderImage = (ImageView) findViewById(R.id.contact_info_header);
        mNameText = (TextView) findViewById(R.id.contact_info_name);
        mPhoneText = (TextView) findViewById(R.id.contact_info_phone);
        mClassText = (TextView) findViewById(R.id.contact_info_class);
        mSexText = (TextView) findViewById(R.id.contact_info_sex);
        mSmsView = findViewById(R.id.contact_info_sms_view);
        mCallView = findViewById(R.id.contact_info_call_view);
        mChatView = findViewById(R.id.contact_info_chat_view);
        mAddView = findViewById(R.id.contact_info_add_view);
        mAddText = (TextView) findViewById(R.id.contact_info_add_text);
        
        mNameText.setText(buffer.toString());
        mPhoneText.setText(Util.checkNull(mItem.phone));
        
        ChildrenItem item = WXApplication.getInstance().selectedItem;
        if (null != item)
        {
            mClassText.setText(Util.checkNull(item.classinfoName));
        }
        if ("1".equals(mItem.sex))
        {
            mSexText.setText("男");
        }
        else if ("2".equals(mItem.sex))
        {
            mSexText.setText("女");
        }
        if ("0".equals(mItem.isFriend))
        {
            mAddText.setText("加为好友");
        }
        else if ("1".equals(mItem.isFriend))
        {
            mAddText.setText("取消好友");
        }
    }
    
    @Override
    public void bindListener()
    {
        mSmsView.setOnClickListener(this);
        mCallView.setOnClickListener(this);
        mChatView.setOnClickListener(this);
        mAddView.setOnClickListener(this);
    }
    
    @Override
    public int getActivityLayout()
    {
        return R.layout.wx_contact_info_activity;
    }
    
    /**
     * 获取传递数据
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private void getBundle()
    {
        mDbHelper = new ContactsDBHelper(mContext,
                LoginUtil.getUserId(mContext));
        mItem = mDbHelper.queryContactById(getIntent().getStringExtra("userId"),
                getIntent().getStringExtra("studentId"));
        if (null == mItem)
        {
            mItem = new ContactItem();
            finish();
        }
    }
    
    @Override
    public void onClick(View v)
    {
        Intent intent = null;
        switch (v.getId())
        {
            case R.id.contact_info_sms_view:
                if (TextUtils.isEmpty(mItem.phone))
                {
                    showToast("对方暂无联系方式");
                    return;
                }
                Uri smsToUri = Uri.parse("smsto:" + mItem.phone);
                intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
                intent.putExtra("phone", mItem.phone);
                startActivity(intent);
                break;
            case R.id.contact_info_call_view:
                if (TextUtils.isEmpty(mItem.phone))
                {
                    showToast("对方暂无联系方式");
                    return;
                }
                CommonDialog dialog = new CommonDialog(mContext, "提示", "是否拨打："
                        + mItem.phone, "取消", "确定", new OnButtonClickListener()
                {
                    
                    @Override
                    public void onConfirmClick()
                    {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + mItem.phone));
                        startActivity(intent);
                    }
                    
                    @Override
                    public void onCancelClick()
                    {
                        
                    }
                });
                dialog.setTextGravity(Gravity.CENTER, -1);
                dialog.showDialog();
                break;
            case R.id.contact_info_chat_view:
                if (null != mItem)
                {
                    intent = new Intent(mContext, ChatMessageActivity.class);
                    intent.putExtra("userId", mItem.userId);
                    intent.putExtra("studentId", mItem.studentId);
                    startActivity(intent);
                }
                break;
            case R.id.contact_info_add_view:
                if ("0".equals(mItem.isFriend))
                {
                    ProgressUtil.showProgressDialog(mContext, "请求中...");
                    mAddView.setEnabled(false);
                    Map<String, String> params = HttpRequestUtil.getCommonParams(HttpAddressProperties.PERSON_CONTACTS_ADD);
                    params.put("contactUserId", Util.checkNull(mItem.userId));
                    params.put("contactStudentId",
                            Util.checkNull(mItem.studentId));
                    params.put("contactType", Util.checkNull(mItem.role));
                    params.put("contactAppellation",
                            Util.checkNull(mItem.appellation));
                    HttpRequestUtil.sendHttpPostCommonRequest(mContext,
                            HttpAddressProperties.SERVICE_URL,
                            params,
                            ContactAddResp.class,
                            addHandler);
                }
                else if ("1".equals(mItem.isFriend))
                {
                    ProgressUtil.showProgressDialog(mContext, "请求中...");
                    mAddView.setEnabled(false);
                    Map<String, String> params = HttpRequestUtil.getCommonParams(HttpAddressProperties.PERSON_CONTACTS_DELETE);
                    params.put("contactId", Util.checkNull(mItem.contactId));
                    HttpRequestUtil.sendHttpPostCommonRequest(mContext,
                            HttpAddressProperties.SERVICE_URL,
                            params,
                            ContactAddResp.class,
                            delHandler);
                }
                break;
            default:
                break;
        }
    }
}
