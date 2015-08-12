package com.xweisoft.wx.family.ui.contact.view;

import java.util.ArrayList;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.WXApplication;
import com.xweisoft.wx.family.logic.global.HttpAddressProperties;
import com.xweisoft.wx.family.logic.model.ChildrenItem;
import com.xweisoft.wx.family.logic.model.ContactItem;
import com.xweisoft.wx.family.logic.model.response.ContactResp;
import com.xweisoft.wx.family.service.database.ContactsDBHelper;
import com.xweisoft.wx.family.ui.contact.ContactInfoActivity;
import com.xweisoft.wx.family.ui.contact.adapter.ContactsListAdapter;
import com.xweisoft.wx.family.ui.message.ChatMessageActivity;
import com.xweisoft.wx.family.util.HttpRequestUtil;
import com.xweisoft.wx.family.util.ListUtil;
import com.xweisoft.wx.family.util.LoginUtil;
import com.xweisoft.wx.family.util.Util;
import com.xweisoft.wx.family.widget.NetHandler;
import com.xweisoft.wx.family.widget.swipe.SwipeRefreshListView;
import com.xweisoft.wx.family.widget.swipe.SwipeRefreshListView.IXListViewListener;

/**
 * 
 * 获取我的通熏录
 * viewPager自定义热点布局
 * <一句话功能简述>
 * <功能详细描述>
 * @author  gac
 * @version  [版本号, 2013-12-3]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@SuppressLint("HandlerLeak")
public class ContactPagerView extends LinearLayout implements OnClickListener,
        OnItemClickListener
{
    /**
     * 列表
     */
    private SwipeRefreshListView mListView;
    
    /**
     * 群聊 按钮
     */
    private ImageView mGroupChatImage;
    
    /**
     * Context上下文
     */
    private Context mContext;
    
    /**
     * 班级名称
     */
    private TextView mClassNameText;
    
    /**
     * 列表适配器
     */
    private ContactsListAdapter mAdapter;
    
    /**
     * 集合
     */
    private ArrayList<ContactItem> mList = new ArrayList<ContactItem>();
    
    /**
     * 时候初始化
     */
    private boolean isInit = false;
    
    /**
     * 是否是个人（好友）
     */
    private boolean isPerson;
    
    /**
     * 通讯录数据库帮助类
     */
    private ContactsDBHelper mDbHelper;
    
    public ContactPagerView(Context context)
    {
        super(context);
        this.mContext = context;
        init(context);
        bindListener();
        initAdapter();
    }
    
    public ContactPagerView(Context context, boolean isPerson)
    {
        super(context);
        this.mContext = context;
        this.isPerson = isPerson;
        init(context);
        bindListener();
        initAdapter();
    }
    
    public ContactPagerView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.mContext = context;
        init(context);
        bindListener();
        initAdapter();
    }
    
    /**
     * 初始化布局
     * <一句话功能简述>
     * <功能详细描述>
     * @param context [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private void init(Context context)
    {
        inflate(context, R.layout.contacts_pager_view, this);
        
        View headerView = LayoutInflater.from(context)
                .inflate(R.layout.wx_contact_pager_view_header, null);
        
        mClassNameText = (TextView) headerView.findViewById(R.id.contact_pager_view_class_name);
        mGroupChatImage = (ImageView) headerView.findViewById(R.id.contact_pager_view_group_chat_image);
        
        mListView = (SwipeRefreshListView) findViewById(R.id.contact_pager_view_listview);
        mDbHelper = new ContactsDBHelper(context, LoginUtil.getUserId(context));
        if (!isPerson)
        {
            mListView.addHeaderView(headerView);
            ChildrenItem item = WXApplication.getInstance().selectedItem;
            if (null != item)
            {
                mClassNameText.setText(Util.checkNull(item.classinfoName));
            }
        }
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(true);
    }
    
    /**
     * 绑定item click事件
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private void bindListener()
    {
        mGroupChatImage.setOnClickListener(this);
        
        mListView.setXListViewListener(new IXListViewListener()
        {
            
            @Override
            public void onRefresh()
            {
                if (isPerson)
                {
                    sendPersonRequest();
                }
                else
                {
                    sendContactRequest();
                }
            }
            
            @Override
            public void onLoadMore()
            {
                
            }
        });
        
        mListView.setOnItemClickListener(new OnItemClickListener()
        {
            
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id)
            {
                position -= mListView.getHeaderViewsCount();
                if (position < 0 || position > mList.size() - 1)
                {
                    return;
                }
                if (null != mList.get(position))
                {
                    Intent intent = new Intent(mContext,
                            ContactInfoActivity.class);
                    intent.putExtra("userId", mList.get(position).userId);
                    intent.putExtra("studentId", mList.get(position).studentId);
                    mContext.startActivity(intent);
                }
            }
        });
    }
    
    /**
     * 个人的通讯录列表
     */
    private Handler handlerPerson = new NetHandler(false)
    {
        @Override
        public void onResponse()
        {
            super.onResponse();
            onLoadFinish();
        }
        
        @Override
        public void onSuccess(String errMsg, Message msg)
        {
            ContactResp resp = null;
            if (null != msg.obj && msg.obj instanceof ContactResp)
            {
                resp = (ContactResp) msg.obj;
                mList.clear();
                if (!ListUtil.isEmpty(resp.teacherContacts))
                {
                    for (ContactItem item : resp.teacherContacts)
                    {
                        item.role = "1";
                        mDbHelper.insertItem(item, "1");
                    }
                }
                if (!ListUtil.isEmpty(resp.parentContacts))
                {
                    for (ContactItem item : resp.parentContacts)
                    {
                        item.role = "2";
                        mDbHelper.insertItem(item, "2");
                    }
                }
                mList = mDbHelper.queryAllFriend();
                mAdapter.setList(mList);
            }
        }
        
        @Override
        public void onFailed(String errCode, String errMsg, Message msg)
        {
            showToast(errMsg);
        }
    };
    
    /**
     * 通讯录处理
     */
    private Handler contactHandler = new NetHandler(false)
    {
        
        @Override
        public void onResponse()
        {
            super.onResponse();
            onLoadFinish();
        }
        
        @Override
        public void onSuccess(String errMsg, Message msg)
        {
            ContactResp resp = null;
            if (null != msg.obj && msg.obj instanceof ContactResp)
            {
                resp = (ContactResp) msg.obj;
                if (null != mDbHelper)
                {
                    String classinfoId = LoginUtil.getClassinfoId(mContext);
                    if (!ListUtil.isEmpty(resp.teacherContacts)
                            || !ListUtil.isEmpty(resp.parentContacts))
                    {
                        mDbHelper.deleteContacts(classinfoId);
                    }
                    if (!ListUtil.isEmpty(resp.teacherContacts))
                    {
                        mDbHelper.insertList(resp.teacherContacts,
                                "1",
                                classinfoId);
                    }
                    if (!ListUtil.isEmpty(resp.parentContacts))
                    {
                        mDbHelper.insertList(resp.parentContacts,
                                "2",
                                classinfoId);
                    }
                }
                mList = mDbHelper.queryAllContact();
                mAdapter.setList(mList);
            }
        }
        
        @Override
        public void onFailed(String errCode, String errMsg, Message msg)
        {
            showToast("服务器休息了，请稍后重试");
        }
    };
    
    private void onLoadFinish()
    {
        mListView.stopLoadMore();
        mListView.stopRefresh();
    }
    
    /** 
     * <一句话功能简述>
     * 发送请求个人好友的列表请求
     * <功能详细描述> [参数说明]
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void sendPersonRequest()
    {
        Map<String, String> param = HttpRequestUtil.getCommonParams(HttpAddressProperties.PERSON_CONTACTS);
        HttpRequestUtil.sendHttpPostCommonRequest(mContext,
                HttpAddressProperties.SERVICE_URL,
                param,
                ContactResp.class,
                handlerPerson);
    }
    
    /** 
     * <一句话功能简述>
     * 发送请求个人好友的列表请求
     * <功能详细描述> [参数说明]
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void sendContactRequest()
    {
        Map<String, String> param = HttpRequestUtil.getCommonParams(HttpAddressProperties.CLASS_CONTACTS);
        HttpRequestUtil.sendHttpPostCommonRequest(mContext,
                HttpAddressProperties.SERVICE_URL,
                param,
                ContactResp.class,
                contactHandler);
    }
    
    /**
     * 更新数据
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void updateList()
    {
        if (null != mDbHelper)
        {
            if (isPerson)
            {
                mList = mDbHelper.queryAllFriend();
            }
            else
            {
                mList = mDbHelper.queryAllContact();
            }
            mAdapter.setList(mList);
        }
    }
    
    protected void showToast(String errMsg)
    {
        Toast.makeText(mContext, errMsg, Toast.LENGTH_SHORT).show();
    }
    
    /**
     * 请求首页数据
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void requestData()
    {
        if (!isInit)
        {
            isInit = true;
            if (!isPerson)
            {
                if (null != mDbHelper)
                {
                    mList = mDbHelper.queryAllContact();
                }
                mAdapter.setList(mList);
            }
            else
            {
                if (null != mDbHelper)
                {
                    mList = mDbHelper.queryAllFriend();
                }
                mAdapter.setList(mList);
                sendPersonRequest();
            }
        }
    }
    
    private void initAdapter()
    {
        mAdapter = new ContactsListAdapter(mContext);
        mAdapter.setList(mList);
        mListView.setAdapter(mAdapter);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.contact_pager_view_group_chat_image:
                Intent intent = new Intent(mContext, ChatMessageActivity.class);
                intent.putExtra("group", "group");
                mContext.startActivity(intent);
                break;
            default:
                break;
        }
    }
    
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
            long arg3)
    {
        
    }
    
}
