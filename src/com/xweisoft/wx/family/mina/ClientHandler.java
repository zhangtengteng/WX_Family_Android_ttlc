package com.xweisoft.wx.family.mina;

import java.util.ArrayList;
import java.util.UUID;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.xweisoft.wx.family.WXApplication;
import com.xweisoft.wx.family.logic.global.GlobalConstant;
import com.xweisoft.wx.family.logic.global.GlobalVariable;
import com.xweisoft.wx.family.logic.model.ContactItem;
import com.xweisoft.wx.family.logic.model.MessageItem;
import com.xweisoft.wx.family.logic.model.MsgContentItem;
import com.xweisoft.wx.family.logic.model.MsgNoticeItem;
import com.xweisoft.wx.family.logic.model.SocketMsgItem;
import com.xweisoft.wx.family.service.database.ChatMessageDBHelper;
import com.xweisoft.wx.family.service.database.ContactsDBHelper;
import com.xweisoft.wx.family.service.database.NotificationMsgDBHelper;
import com.xweisoft.wx.family.service.database.SessionMessageDBHelper;
import com.xweisoft.wx.family.ui.message.ChatMessageActivity;
import com.xweisoft.wx.family.ui.message.NotificationListActivity;
import com.xweisoft.wx.family.ui.message.WorkActivity;
import com.xweisoft.wx.family.util.LogX;
import com.xweisoft.wx.family.util.LoginUtil;
import com.xweisoft.wx.family.util.SecurityUtil;

/**
 * 客户端的业务逻辑处理类
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-5-22]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ClientHandler extends IoHandlerAdapter
{
    private static final String TAG = "===ClientHandler===";
    
    /**
     * mina 管理类
     */
    private MinaManager minaManager;
    
    /**
     * 通知消息数据库帮助类
     */
    private NotificationMsgDBHelper mDbHelper;
    
    /**
     * 通知消息数据库帮助类
     */
    private ChatMessageDBHelper mChatDbHelper;
    
    /**
     * 通知消息数据库帮助类
     */
    private ContactsDBHelper mContactsDbHelper;
    
    /**
     * 通知消息数据库帮助类
     */
    private SessionMessageDBHelper mSessionDbHelper;
    
    /**
     * 上下文
     */
    private Context mContex;
    
    private Vibrator mVibrator;
    
    private Ringtone mRingtone;
    
    /**
     * <默认构造函数>
     */
    public ClientHandler(MinaManager minaManager, Context context)
    {
        this.minaManager = minaManager;
        this.mContex = context;
        mDbHelper = new NotificationMsgDBHelper(context,
                LoginUtil.getUserId(context));
        mChatDbHelper = new ChatMessageDBHelper(context,
                LoginUtil.getUserId(context));
        mSessionDbHelper = new SessionMessageDBHelper(context,
                LoginUtil.getUserId(context));
        mContactsDbHelper = new ContactsDBHelper(context,
                LoginUtil.getUserId(context));
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mRingtone = RingtoneManager.getRingtone(mContex, notification);
    }
    
    /**
     * 连结到服务器
     * 建立连接后发送登录请求
     * @param session
     * @throws Exception
     */
    @Override
    public void sessionOpened(IoSession session) throws Exception
    {
        LogX.getInstance().i(TAG, "====server connected===");
        if (!TextUtils.isEmpty(LoginUtil.getUserId(mContex)))
        {
            LogX.getInstance().i(TAG,
                    "====server connected====fromm=="
                            + LoginUtil.getUserId(mContex));
            SocketMsgItem item = new SocketMsgItem();
            item.from = LoginUtil.getUserId(mContex);
            item.funcid = "login";
            item.msgid = UUID.randomUUID().toString();
            item.pwd = SecurityUtil.MD5(item.from + item.msgid);
            String login = new Gson().toJson(item);
            session.write(login);
        }
    }
    
    /**
     * 连接断开
     * @param session
     * @throws Exception
     */
    @Override
    public void sessionClosed(IoSession session) throws Exception
    {
        LogX.getInstance().i(TAG, "====server closed===");
        if (null != minaManager && minaManager.isNeedConnect())
        {
            minaManager.startReconnectionThread();
        }
    }
    
    /**
     * 消息接受
     * 消息类型为login, biz, ack
     * login为客户端验证登录消息，不做处理
     * biz为正常消息，接收侯需向服务器发送到达消息ack
     * ack为消息到达，不做处理
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception
    {
        try
        {
            LogX.getInstance().i(TAG,
                    "====message receive====" + (String) message);
            String msg = (String) message;
            SocketMsgItem item = new Gson().fromJson(msg, SocketMsgItem.class);
            MsgContentItem contentItem = null;
            if (null != item)
            {
                if ("biz".equals(item.funcid))
                {
                    if (!TextUtils.isEmpty(item.content))
                    {
                        String content = item.content;
                        contentItem = new Gson().fromJson(content,
                                MsgContentItem.class);
                        if (null != contentItem)
                        {
                            if (GlobalConstant.NOTIFICATION_MESSAGE_CLASS.equals(contentItem.cmd)
                                    || GlobalConstant.NOTIFICATION_MESSAGE_WORK.equals(contentItem.cmd))
                            {
                                MsgNoticeItem noticeItem = contentItem.notice;
                                if (null != noticeItem)
                                {
                                    String ids = noticeItem.classIds;
                                    if (null != ids)
                                    {
                                        String[] array = ids.split("[,]");
                                        ArrayList<MsgContentItem> list = new ArrayList<MsgContentItem>();
                                        for (String string : array)
                                        {
                                            MsgContentItem msgContentItem = new Gson().fromJson(content,
                                                    MsgContentItem.class);
                                            msgContentItem.cmd += string;
                                            list.add(msgContentItem);
                                        }
                                        if (null != mDbHelper)
                                        {
                                            mDbHelper.insertList(list);
                                        }
                                    }
                                }
                            }
                            else
                            {
                                if (null != mDbHelper)
                                {
                                    mDbHelper.insert(contentItem);
                                }
                            }
                            Intent i = new Intent(
                                    GlobalConstant.NOTIFICATION_MSG_RECEIVER);
                            if (null != mContex)
                            {
                                mContex.sendBroadcast(i);
                            }
                            Intent intent = new Intent(
                                    ClientUtil.ACTION_SHOW_NOTIFICATION);
                            if (null != contentItem.notice)
                            {
                                boolean send = true;
                                if (GlobalVariable.currentActivity instanceof NotificationListActivity)
                                {
                                    if (GlobalConstant.NOTIFICATION_MESSAGE_CLASS.equals(contentItem.cmd)
                                            || GlobalConstant.NOTIFICATION_MESSAGE_SCHOOL.equals(contentItem.cmd)
                                            || GlobalConstant.NOTIFICATION_MESSAGE_SYSTEM.equals(contentItem.cmd))
                                    {
                                        send = false;
                                    }
                                    
                                }
                                else if (GlobalVariable.currentActivity instanceof WorkActivity)
                                {
                                    if (GlobalConstant.NOTIFICATION_MESSAGE_WORK.equals(contentItem.cmd))
                                    {
                                        send = false;
                                    }
                                }
                                if (send)
                                {
                                    intent.putExtra(ClientUtil.NOTIFICATION_CONTENT,
                                            contentItem.notice.content);
                                    intent.putExtra("cmd", contentItem.cmd);
                                    if (null != mContex)
                                    {
                                        mContex.sendBroadcast(intent);
                                    }
                                }
                            }
                        }
                        item.funcid = "ack";
                        item.content = null;
                        String claimMsg = new Gson().toJson(item);
                        session.write(claimMsg);
                    }
                }
                else if ("kickout".equals(item.funcid))
                {
                    if (null != minaManager)
                    {
                        minaManager.setNeedConnect(false);
                        minaManager.disconnect();
                        Intent intent = new Intent();
                        intent.setAction("com.xweisoft.wx.family.kickout.broadcast");
                        if (null != mContex)
                        {
                            mContex.sendBroadcast(intent);
                        }
                    }
                }
                else if ("im".equals(item.funcid) || "img".equals(item.funcid))
                {
                    if (!TextUtils.isEmpty(item.content))
                    {
                        if (!TextUtils.isEmpty(item.from)
                                && !item.from.equals(item.to))
                        {
                            MessageItem msgItem = new Gson().fromJson(item.content,
                                    MessageItem.class);
                            if (null != msgItem)
                            {
                                ContactItem contactItem = mContactsDbHelper.queryContactById(item.from,
                                        msgItem.from);
                                msgItem.isRead = "0";
                                msgItem.isFrom = "0";
                                msgItem.userId = item.from;
                                msgItem.recTime = item.sendtime;
                                msgItem.userName = contactItem.name;
                                msgItem.userAppellation = contactItem.appellation;
                                msgItem.userHeader = contactItem.portraitPath;
                                mChatDbHelper.insert(msgItem);
                                if ("im".equals(item.funcid))
                                {
                                    mSessionDbHelper.insert(msgItem);
                                }
                                else
                                {
                                    mSessionDbHelper.insertGroupSession(msgItem);
                                }
                                Intent i = new Intent(
                                        GlobalConstant.NOTIFICATION_MSG_RECEIVER);
                                if (GlobalVariable.currentActivity instanceof ChatMessageActivity)
                                {
                                    i.putExtra("item", msgItem);
                                    if (null != mContex)
                                    {
                                        mContex.sendBroadcast(i);
                                    }
                                    if (WXApplication.getInstance().system_voice)
                                    {
                                        if (!mRingtone.isPlaying())
                                        {
                                            mRingtone.play();
                                        }
                                    }
                                    if (WXApplication.getInstance().system_vibrate)
                                    {
                                        if (null != mVibrator)
                                        {
                                            if (mVibrator.hasVibrator())
                                            {
                                                long parent[] = { 100, 400, 0,
                                                        0 };
                                                mVibrator.vibrate(parent, -1);
                                            }
                                        }
                                    }
                                }
                                else
                                {
                                    if (null != mContex)
                                    {
                                        mContex.sendBroadcast(i);
                                    }
                                    if (WXApplication.getInstance().im_message_receive)
                                    {
                                        Intent intent = new Intent();
                                        intent.setAction(ClientUtil.ACTION_SHOW_NOTIFICATION);
                                        if (WXApplication.getInstance().im_message_notifi_show_info)
                                        {
                                            if ("1".equals(msgItem.messageBodyType))
                                            {
                                                intent.putExtra(ClientUtil.NOTIFICATION_CONTENT,
                                                        msgItem.text);
                                            }
                                            else if ("4".equals(msgItem.messageBodyType))
                                            {
                                                intent.putExtra(ClientUtil.NOTIFICATION_CONTENT,
                                                        "[图片]");
                                            }
                                        }
                                        else
                                        {
                                            intent.putExtra(ClientUtil.NOTIFICATION_CONTENT,
                                                    "您收到一条新消息");
                                        }
                                        if ("im".equals(item.funcid)
                                                && LoginUtil.getStudentId(mContex)
                                                        .equals(msgItem.to))
                                        {
                                            if (null != contactItem)
                                            {
                                                intent.putExtra("userId",
                                                        contactItem.userId);
                                                intent.putExtra("studentId",
                                                        contactItem.studentId);
                                                
                                                StringBuffer buffer = new StringBuffer();
                                                buffer.append(contactItem.name);
                                                if (!TextUtils.isEmpty(contactItem.appellation))
                                                {
                                                    buffer.append("（"
                                                            + contactItem.appellation
                                                            + "）");
                                                }
                                                intent.putExtra("title",
                                                        buffer.toString());
                                                intent.putExtra("cmd",
                                                        GlobalConstant.IM_MESSAGE_PERSON);
                                            }
                                            if (null != mContex)
                                            {
                                                mContex.sendBroadcast(intent);
                                            }
                                        }
                                        else if ("img".equals(item.funcid))
                                        {
                                            if (null != msgItem.groupId
                                                    && msgItem.groupId.equals(WXApplication.getInstance().groupId))
                                            {
                                                intent.putExtra("title", "群组消息");
                                                intent.putExtra("cmd",
                                                        GlobalConstant.IM_MESSAGE_GROUP);
                                                if (null != mContex)
                                                {
                                                    mContex.sendBroadcast(intent);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        item.funcid = "ack";
                        item.content = null;
                        String claimMsg = new Gson().toJson(item);
                        session.write(claimMsg);
                    }
                }
                else if ("ack".equals(item.funcid))
                {
                    if (null != WXApplication.getInstance().getImMsgIds()
                            && WXApplication.getInstance()
                                    .getImMsgIds()
                                    .contains(item.msgid))
                    {
                        mChatDbHelper.updateTime(item.msgid, item.sendtime);
                        WXApplication.getInstance()
                                .getImMsgIds()
                                .remove(item.msgid);
                    }
                }
            }
        }
        catch (Exception e)
        {
            LogX.getInstance().e(TAG, e.toString());
        }
    }
    
    /**
     * 消息发送
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    public void messageSent(IoSession session, Object message) throws Exception
    {
        super.messageSent(session, message);
        LogX.getInstance().i(TAG, "====message send====" + (String) message);
    }
    
    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception
    {
        super.exceptionCaught(session, cause);
        LogX.getInstance().i(TAG,
                null != cause ? cause.getMessage().toString() : "");
    }
    
}
