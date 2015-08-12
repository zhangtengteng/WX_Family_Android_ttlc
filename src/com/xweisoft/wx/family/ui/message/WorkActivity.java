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
import android.view.View;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.logic.global.GlobalConstant;
import com.xweisoft.wx.family.logic.model.MsgContentItem;
import com.xweisoft.wx.family.mina.ClientUtil;
import com.xweisoft.wx.family.service.database.NotificationMsgDBHelper;
import com.xweisoft.wx.family.ui.BaseActivity;
import com.xweisoft.wx.family.ui.message.adpter.WorkListAdapter;
import com.xweisoft.wx.family.util.CommonTitleUtil;
import com.xweisoft.wx.family.util.ListUtil;
import com.xweisoft.wx.family.util.LoginUtil;
import com.xweisoft.wx.family.util.Util;
import com.xweisoft.wx.family.widget.swipe.SwipeRefreshListView;
import com.xweisoft.wx.family.widget.swipe.SwipeRefreshListView.IXListViewListener;

/**
 * 班级作业页面
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-7-15]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class WorkActivity extends BaseActivity
{
    
    /**
     * 下拉、侧滑listView
     */
    private SwipeRefreshListView mListView;
    
    /**
     * 列表适配器
     */
    private WorkListAdapter mAdapter;
    
    /**
     * 集合
     */
    private ArrayList<MsgContentItem> mList = new ArrayList<MsgContentItem>();
    
    /**
     * 页码 
     */
    private int page = 1;
    
    /**
     * 数据库查询类
     */
    private NotificationMsgDBHelper mDbHelper;
    
    /**
     * 类型 数据库用于区分通知消息类别
     */
    private String type;
    
    /**
     * 班级ID
     */
    private String classId;
    
    /**
     * 广播接收器 用于更新列表数据
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
        initData();
        initViews();
        bindListener();
        registerReceiver(mNotificationMsgReceiver, new IntentFilter(
                GlobalConstant.NOTIFICATION_MSG_RECEIVER));
    }
    
    @Override
    public void initViews()
    {
        CommonTitleUtil.initCommonTitle(this,
                getString(R.string.ysh_class_home_message_notice),
                null,
                false,
                true);
        mListView = (SwipeRefreshListView) findViewById(R.id.newmessage_listview);
        mAdapter = new WorkListAdapter(this);
        if (mList.size() != 15)
        {
            mListView.setPullLoadEnable(false);
        }
        else
        {
            mListView.setPullLoadEnable(true);
        }
        mAdapter.setList(mList);
        
        TextView view = new TextView(mContext);
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                Util.dpToPixel(mContext, 10)));
        mListView.addHeaderView(view);
        
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
                if (position < 0 || position > mList.size() - 1)
                {
                    return;
                }
                if (null != mList.get(position)
                        && null != mList.get(position).notice)
                {
                    Intent intent = new Intent(mContext,
                            WorkDetailActivity.class);
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
    
    private void initData()
    {
        mDbHelper = new NotificationMsgDBHelper(mContext,
                LoginUtil.getUserId(mContext));
        classId = LoginUtil.getClassinfoId(mContext);
        type = GlobalConstant.NOTIFICATION_MESSAGE_WORK
                + Util.checkNull(classId);
        mList = mDbHelper.selectMsgByPage(type, 1);
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(ClientUtil.NOTIFICATION_HOMEWORK);
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
