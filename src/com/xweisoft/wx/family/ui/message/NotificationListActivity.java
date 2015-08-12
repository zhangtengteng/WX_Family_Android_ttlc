package com.xweisoft.wx.family.ui.message;

import java.util.ArrayList;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.logic.global.GlobalConstant;
import com.xweisoft.wx.family.logic.model.MsgContentItem;
import com.xweisoft.wx.family.mina.ClientUtil;
import com.xweisoft.wx.family.service.database.NotificationMsgDBHelper;
import com.xweisoft.wx.family.ui.BaseActivity;
import com.xweisoft.wx.family.ui.message.adpter.NotificationListAdapter;
import com.xweisoft.wx.family.util.CommonTitleUtil;
import com.xweisoft.wx.family.util.ListUtil;
import com.xweisoft.wx.family.util.LoginUtil;
import com.xweisoft.wx.family.util.Util;
import com.xweisoft.wx.family.widget.swipe.SwipeRefreshListView;
import com.xweisoft.wx.family.widget.swipe.SwipeRefreshListView.IXListViewListener;

/**
 * 通知列表界面
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-7-16]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class NotificationListActivity extends BaseActivity
{
    /**
     * 下拉、侧滑 listView
     */
    private SwipeRefreshListView mListView;
    
    /**
     * 列表适配器
     */
    private NotificationListAdapter mAdapter;
    
    /**
     * 集合
     */
    private ArrayList<MsgContentItem> mList = new ArrayList<MsgContentItem>();
    
    /**
     * 页码
     */
    private int page = 1;
    
    /**
     * 通知 类型（系统消息、学校通知、班级通知）
     */
    private String type;
    
    /**
     * 标题
     */
    private String title;
    
    /**
     * 数据库帮助类
     */
    private NotificationMsgDBHelper mDbHelper;
    
    /**
     * 班级id
     */
    private String classId;
    
    private NotificationManager mNotificationManager;
    
    /**
     * 广播接收器 用于更新数据
     */
    private BroadcastReceiver mNotificationMsgReceiver = new BroadcastReceiver()
    {
        
        @Override
        public void onReceive(Context context, Intent intent)
        {
            mList = mDbHelper.selectMsgByPage(type, 1);
            mAdapter.setList(mList);
        }
    };
    
    /**
     * 更新UI
     */
    private Handler handler = new Handler()
    {
        
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            ArrayList<MsgContentItem> list = mDbHelper.selectMsgByPage(type,
                    page);
            if (page == 1)
            {
                mList.clear();
            }
            if (!ListUtil.isEmpty(list))
            {
                mList.addAll(list);
                mAdapter.setList(mList);
            }
            else
            {
                showToast("无更多数据");
            }
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
        registerReceiver(mNotificationMsgReceiver, new IntentFilter(
                GlobalConstant.NOTIFICATION_MSG_RECEIVER));
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        
    }
    
    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        title = intent.getStringExtra("title");
        initViews();
        getListByPage();
    }
    
    @Override
    public void initViews()
    {
        String str;
        if (TextUtils.isEmpty(title))
        {
            str = "通知";
        }
        else
        {
            str = title;
        }
        CommonTitleUtil.initCommonTitle(this, str, null, false, true);
        mListView = (SwipeRefreshListView) findViewById(R.id.newmessage_listview);
        initAdapter();
    }
    
    /**
     * 初始化适配器
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private void initAdapter()
    {
        mListView.setPullRefreshEnable(false);
        if (mList.size() != 15)
        {
            mListView.setPullLoadEnable(false);
        }
        else
        {
            mListView.setPullLoadEnable(true);
        }
        mAdapter = new NotificationListAdapter(this);
        mAdapter.setList(mList);
        mListView.setAdapter(mAdapter);
    }
    
    @Override
    public void bindListener()
    {
        mListView.setXListViewListener(new IXListViewListener()
        {
            
            @Override
            public void onRefresh()
            {
                
            }
            
            @Override
            public void onLoadMore()
            {
                new Handler().postDelayed(new Runnable()
                {
                    
                    @Override
                    public void run()
                    {
                        page++;
                        getListByPage();
                        onLoadFinish();
                    }
                }, 2000);
            }
        });
        mListView.setOnItemClickListener(new OnItemClickListener()
        {
            
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id)
            {
                position -= mListView.getHeaderViewsCount();
                if (position < 0 || position >= mList.size())
                {
                    return;
                }
                if (null != mList.get(position))
                {
                    Intent intent = new Intent(mContext,
                            NotificationDetailActivity.class);
                    intent.putExtra("type", type);
                    intent.putExtra("item", mList.get(position).notice);
                    startActivity(intent);
                }
            }
        });
    }
    
    @Override
    public int getActivityLayout()
    {
        return R.layout.wx_message_activity;
    }
    
    /**
     * 获取数据
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private void getBundle()
    {
        mDbHelper = new NotificationMsgDBHelper(mContext,
                LoginUtil.getUserId(mContext));
        title = getIntent().getStringExtra("title");
        getListByPage();
    }
    
    private void getListByPage()
    {
        if (title.equals("系统消息"))
        {
            type = GlobalConstant.NOTIFICATION_MESSAGE_SYSTEM;
            mList = mDbHelper.selectMsgByPage(GlobalConstant.NOTIFICATION_MESSAGE_SYSTEM,
                    page);
        }
        else if (title.equals("学校通知"))
        {
            type = GlobalConstant.NOTIFICATION_MESSAGE_SCHOOL;
            mList = mDbHelper.selectMsgByPage(GlobalConstant.NOTIFICATION_MESSAGE_SCHOOL,
                    page);
        }
        else if (title.equals("班级通知"))
        {
            classId = LoginUtil.getClassinfoId(mContext);
            type = GlobalConstant.NOTIFICATION_MESSAGE_CLASS
                    + Util.checkNull(classId);
            mList = mDbHelper.selectMsgByPage(GlobalConstant.NOTIFICATION_MESSAGE_CLASS
                    + Util.checkNull(classId),
                    page);
        }
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        if (title.equals("系统消息"))
        {
            mNotificationManager.cancel(ClientUtil.NOTIFICATION_SYSTEM);
        }
        else if (title.equals("学校通知"))
        {
            mNotificationManager.cancel(ClientUtil.NOTIFICATION_SCHOOL);
        }
        else if (title.equals("班级通知"))
        {
            mNotificationManager.cancel(ClientUtil.NOTIFICATION_CLASS);
        }
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(mNotificationMsgReceiver);
    }
    
    /**
     * 加载、刷新完成重置状态
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private void onLoadFinish()
    {
        mListView.stopRefresh();
        mListView.stopLoadMore();
    }
}
