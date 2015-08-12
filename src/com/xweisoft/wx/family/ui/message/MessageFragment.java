package com.xweisoft.wx.family.ui.message;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.WXApplication;
import com.xweisoft.wx.family.logic.global.GlobalConstant;
import com.xweisoft.wx.family.logic.model.MsgContentItem;
import com.xweisoft.wx.family.service.database.ChatMessageDBHelper;
import com.xweisoft.wx.family.service.database.NotificationMsgDBHelper;
import com.xweisoft.wx.family.service.database.SessionMessageDBHelper;
import com.xweisoft.wx.family.ui.BaseFragment;
import com.xweisoft.wx.family.ui.message.adpter.MessageFragmentListAdapter;
import com.xweisoft.wx.family.ui.sliding.WXMainFragmentActivity;
import com.xweisoft.wx.family.util.CommonTitleUtil;
import com.xweisoft.wx.family.util.LoginUtil;
import com.xweisoft.wx.family.util.Util;
import com.xweisoft.wx.family.widget.swipe.OnMenuItemClickListener;
import com.xweisoft.wx.family.widget.swipe.SwipeMenu;
import com.xweisoft.wx.family.widget.swipe.SwipeMenuCreator;
import com.xweisoft.wx.family.widget.swipe.SwipeMenuItem;
import com.xweisoft.wx.family.widget.swipe.SwipeRefreshListView;
import com.xweisoft.wx.family.widget.swipe.SwipeRefreshListView.IXListViewListener;

/**
 * 消息中心
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-5-11]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MessageFragment extends BaseFragment implements OnClickListener
{
    
    /**
     * 头部 通知集合
     */
    private ArrayList<MsgContentItem> mHeaderList = new ArrayList<MsgContentItem>();
    
    /**
     * 侧滑、下拉 listView
     */
    private SwipeRefreshListView mListView;
    
    /**
     * 消息列表适配器
     */
    private MessageFragmentListAdapter mAdapter;
    
    /**
     * 消息集合
     */
    private ArrayList<MsgContentItem> mList = new ArrayList<MsgContentItem>();
    
    /**
     * 数据库管理类
     */
    private NotificationMsgDBHelper mDbHelper;
    
    /**
     * 会话数据库帮助类
     */
    private SessionMessageDBHelper mSessionDbHelper;
    
    /**
     * 聊天消息数据库帮助类
     */
    private ChatMessageDBHelper mChatDBHelper;
    
    /**
     * 当前所选孩子班级Id
     */
    private String classId;
    
    /**
     * 接受广播 更新通知数据
     */
    private BroadcastReceiver mNotificationMsgReceiver = new BroadcastReceiver()
    {
        
        @Override
        public void onReceive(Context context, Intent intent)
        {
            initHeaderData();
        }
    };
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        mLayouView = inflater.inflate(R.layout.wx_message_fragment,
                container,
                false);
        initViews();
        bindListener();
        getActivity().registerReceiver(mNotificationMsgReceiver,
                new IntentFilter(GlobalConstant.NOTIFICATION_MSG_RECEIVER));
        return mLayouView;
    }
    
    @Override
    public void onResume()
    {
        super.onResume();
        initHeaderData();
    }
    
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        getActivity().unregisterReceiver(mNotificationMsgReceiver);
    }
    
    @Override
    public void initViews()
    {
        mDbHelper = new NotificationMsgDBHelper(mContext,
                LoginUtil.getUserId(mContext));
        mSessionDbHelper = new SessionMessageDBHelper(mContext,
                LoginUtil.getUserId(mContext));
        mChatDBHelper = new ChatMessageDBHelper(mContext,
                LoginUtil.getUserId(mContext));
        
        CommonTitleUtil.initCommonTitle(mLayouView,
                getString(R.string.ysh_new_message_notice),
                null,
                true,
                true);
        mListView = (SwipeRefreshListView) mLayouView.findViewById(R.id.message_fragment_listview);
        mListView.setPullLoadEnable(false);
        
        mListView.setMenuCreator(new SwipeMenuCreator()
        {
            
            @Override
            public void create(SwipeMenu menu)
            {
                SwipeMenuItem item = new SwipeMenuItem(mContext);
                item.setWidth(Util.dpToPixel(mContext, 70));
                item.setHeight(Util.dpToPixel(mContext, 70));
                item.setBackground(R.drawable.wx_message_delete_bg);
                item.setIcon(R.drawable.wx_message_delete_icon);
                menu.addMenuItem(item);
            }
        });
        initAdapter();
    }
    
    @Override
    public void bindListener()
    {
        mListView.setXListViewListener(new IXListViewListener()
        {
            
            @Override
            public void onRefresh()
            {
                new Handler().postDelayed(new Runnable()
                {
                    
                    @Override
                    public void run()
                    {
                        onLoadFinish();
                    }
                }, 2000);
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
                Intent intent = null;
                if (position == 0)
                {
                    if (hasMsgContentItem(GlobalConstant.NOTIFICATION_MESSAGE_SYSTEM))
                    {
                        intent = new Intent(mContext,
                                NotificationListActivity.class);
                        intent.putExtra("title", "系统消息");
                        startActivity(intent);
                    }
                    else
                    {
                        showToast("暂无系统消息");
                    }
                }
                else if (position == 1)
                {
                    if (hasMsgContentItem(GlobalConstant.NOTIFICATION_MESSAGE_SCHOOL))
                    {
                        intent = new Intent(mContext,
                                NotificationListActivity.class);
                        intent.putExtra("title", "学校通知");
                        startActivity(intent);
                    }
                    else
                    {
                        showToast("暂无学校通知");
                    }
                }
                else if (position == 2)
                {
                    if (hasMsgContentItem(GlobalConstant.NOTIFICATION_MESSAGE_CLASS
                            + Util.checkNull(classId)))
                    {
                        intent = new Intent(mContext,
                                NotificationListActivity.class);
                        intent.putExtra("title", "班级通知");
                        startActivity(intent);
                    }
                    else
                    {
                        showToast("暂无班级通知");
                    }
                }
                else if (position == 3)
                {
                    if (hasMsgContentItem(GlobalConstant.NOTIFICATION_MESSAGE_WORK
                            + Util.checkNull(classId)))
                    {
                        intent = new Intent(mContext, WorkActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        showToast("暂无班级作业");
                    }
                }
                else
                {
                    if (null != mList.get(position))
                    {
                        intent = new Intent(mContext, ChatMessageActivity.class);
                        if (TextUtils.isEmpty(mList.get(position).groupId))
                        {
                            intent.putExtra("userId",
                                    mList.get(position).userId);
                            intent.putExtra("studentId",
                                    mList.get(position).studentId);
                        }
                        else
                        {
                            intent.putExtra("group", "group");
                        }
                        startActivity(intent);
                    }
                }
            }
        });
        
        mListView.setOnMenuItemClickListener(new OnMenuItemClickListener()
        {
            
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index)
            {
                if (null != mList.get(position))
                {
                    mSessionDbHelper.deleteSessionById(mList.get(position).id);
                    mChatDBHelper.updateAllRead(mList.get(position).userId,
                            mList.get(position).studentId);
                    mList.remove(position);
                    mAdapter.setList(mList);
                    initHeaderData();
                }
                else
                {
                    showToast("抱歉，删除失败");
                }
            }
        });
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
        mListView.setRefreshTime("刚刚");
    }
    
    /**
     * 初始化列表顶部固定通知消息
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private void initHeaderData()
    {
        List<MsgContentItem> list = mDbHelper.querryAllLatestMsgAndUnReadCount();
        WXApplication.getInstance().getNotificationMsgMap().clear();
        mHeaderList.clear();
        classId = LoginUtil.getClassinfoId(mContext);
        int count = mDbHelper.getUnReadCount(classId)
                + mChatDBHelper.getUnReadCount();
        if (count <= 0)
        {
            ((WXMainFragmentActivity) getActivity()).setUnreadImageVisibility(View.GONE);
        }
        else
        {
            ((WXMainFragmentActivity) getActivity()).setUnreadImageVisibility(View.VISIBLE);
        }
        for (MsgContentItem msgContentItem : list)
        {
            if (null != msgContentItem)
            {
                if (GlobalConstant.NOTIFICATION_MESSAGE_SYSTEM.equals(msgContentItem.cmd))
                {
                    WXApplication.getInstance()
                            .getNotificationMsgMap()
                            .put(GlobalConstant.NOTIFICATION_MESSAGE_SYSTEM,
                                    msgContentItem);
                }
                if (GlobalConstant.NOTIFICATION_MESSAGE_SCHOOL.equals(msgContentItem.cmd))
                {
                    WXApplication.getInstance()
                            .getNotificationMsgMap()
                            .put(GlobalConstant.NOTIFICATION_MESSAGE_SCHOOL,
                                    msgContentItem);
                }
                if ((GlobalConstant.NOTIFICATION_MESSAGE_CLASS + Util.checkNull(classId)).equals(msgContentItem.cmd))
                {
                    WXApplication.getInstance()
                            .getNotificationMsgMap()
                            .put(GlobalConstant.NOTIFICATION_MESSAGE_CLASS
                                    + Util.checkNull(classId),
                                    msgContentItem);
                }
                if ((GlobalConstant.NOTIFICATION_MESSAGE_WORK + Util.checkNull(classId)).equals(msgContentItem.cmd))
                {
                    WXApplication.getInstance()
                            .getNotificationMsgMap()
                            .put(GlobalConstant.NOTIFICATION_MESSAGE_WORK
                                    + Util.checkNull(classId),
                                    msgContentItem);
                }
            }
        }
        mHeaderList.add(getMsgContentItem(GlobalConstant.NOTIFICATION_MESSAGE_SYSTEM));
        mHeaderList.add(getMsgContentItem(GlobalConstant.NOTIFICATION_MESSAGE_SCHOOL));
        mHeaderList.add(getMsgContentItem(GlobalConstant.NOTIFICATION_MESSAGE_CLASS
                + Util.checkNull(classId)));
        mHeaderList.add(getMsgContentItem(GlobalConstant.NOTIFICATION_MESSAGE_WORK
                + Util.checkNull(classId)));
        //预报名一期不展示
        mList.clear();
        mList.addAll(mHeaderList);
        mList.addAll(mSessionDbHelper.queryAllMsg());
        mAdapter.setList(mList);
    }
    
    /**
     * 获取对象分类的最新消息
     * <一句话功能简述>
     * <功能详细描述>
     * @param type
     * @return [参数说明]
     * 
     * @return MsgContentItem [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private MsgContentItem getMsgContentItem(String type)
    {
        MsgContentItem item = null;
        if (null == WXApplication.getInstance().getNotificationMsgMap()
                || null == WXApplication.getInstance()
                        .getNotificationMsgMap()
                        .get(type))
        {
            item = new MsgContentItem();
        }
        else
        {
            item = WXApplication.getInstance()
                    .getNotificationMsgMap()
                    .get(type);
        }
        return item;
    }
    
    /**
     * 是否有新消息
     * <一句话功能简述>
     * <功能详细描述>
     * @param type
     * @return [参数说明]
     * 
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private boolean hasMsgContentItem(String type)
    {
        if (null == WXApplication.getInstance()
                .getNotificationMsgMap()
                .get(type))
        {
            return false;
        }
        else
        {
            return true;
        }
        
    }
    
    /** 
     * 添加listview的头布局
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private void initAdapter()
    {
        mAdapter = new MessageFragmentListAdapter(mContext);
        mAdapter.setList(mList);
        mAdapter.setListView(mListView);
        mListView.setAdapter(mAdapter);
    }
    
    @Override
    public void onClick(View v)
    {
        
    }
    
}
