package com.xweisoft.wx.family.service.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.xweisoft.wx.family.logic.global.GlobalConstant;
import com.xweisoft.wx.family.logic.model.DownloadItem;
import com.xweisoft.wx.family.logic.model.MsgContentItem;
import com.xweisoft.wx.family.logic.model.MsgNoticeItem;
import com.xweisoft.wx.family.util.ListUtil;
import com.xweisoft.wx.family.util.LogX;
import com.xweisoft.wx.family.util.Util;

public class NotificationMsgDBHelper extends BaseDBHelper
{
    
    /**
     * 打印日志时日志信息中的标识符
     */
    private static final String TAG = Util.makeLogTag(NotificationMsgDBHelper.class);
    
    private static final int PAGE_SIZE = 15;
    
    /**
     * 同步锁
     */
    private static Object synObj = new Object();
    
    public NotificationMsgDBHelper(Context context, String dbName)
    {
        super(context, dbName);
        initDBHelper();
    }
    
    /**
     * 关闭当前正在使用的数据库
     * @param db 正处于打开状态的数据库   
     */
    public void close(SQLiteDatabase db)
    {
        try
        {
            if (db != null)
            {
                if (db.isOpen())
                {
                    db.close();
                    db = null;
                }
            }
        }
        catch (Exception e)
        {
            LogX.getInstance().e(TAG, e.toString());
        }
        finally
        {
            if (db != null)
            {
                db.close();
            }
        }
    }
    
    /**
     * 插入数据
     * <一句话功能简述>
     * <功能详细描述>
     * @param msg [参数说明]
     *
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void insert(MsgContentItem msg)
    {
        // 数据读取的同步操作
        synchronized (synObj)
        {
            String sql = "select * from " + DBHelper.TABLE_NOTIFICATION_MESSAGE
                    + " where msgId = " + msg.noticeId;
            Cursor cursor = db.queryBySql(DBHelper.TABLE_NOTIFICATION_MESSAGE,
                    sql);
            if (null != cursor && cursor.getCount() == 0)
            {
                ContentValues values = new ContentValues();
                values.put("msgId", Util.checkNull(msg.noticeId));
                values.put("msgType", Util.checkNull(msg.cmd));
                if (null != msg.notice)
                {
                    values.put("msgTitle", Util.checkNull(msg.notice.title));
                    values.put("msgText", Util.checkNull(msg.notice.content));
                    values.put("msgTime",
                            Util.checkNull(msg.notice.publishTime));
                }
                values.put("msgFrom", Util.checkNull(msg.from));
                values.put("isRead", "0");
                values.put("audioPath", Util.checkNull(msg.audioPath));
                values.put("videoPath", Util.checkNull(msg.videoPath));
                values.put("resStr", new Gson().toJson(msg));
                db.insert(DBHelper.TABLE_NOTIFICATION_MESSAGE, values);
            }
            if (cursor != null)
            {
                cursor.close();
            }
            closeDB();
        }
    }
    
    /**
     * 插入数据
     * <一句话功能简述>
     * <功能详细描述>
     * @param msg [参数说明]
     *
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void insertList(ArrayList<MsgContentItem> list)
    {
        // 数据读取的同步操作
        synchronized (synObj)
        {
            if (ListUtil.isEmpty(list))
            {
                return;
            }
            StringBuffer buffer = new StringBuffer();
            buffer.append("insert into " + DBHelper.TABLE_NOTIFICATION_MESSAGE
                    + " (msgId,msgType,msgTitle,msgText,msgTime,resStr,isRead)");
            int count = list.size();
            for (int i = 0; i < count; i++)
            {
                MsgContentItem item = list.get(i);
                if (null != item)
                {
                    buffer.append(" select '"
                            + Util.checkNull(item.noticeId)
                            + "',"
                            + "'"
                            + Util.checkNull(item.cmd)
                            + "','"
                            + (null == item.notice ? ""
                                    : Util.checkNull(item.notice.title))
                            + "','"
                            + (null == item.notice ? ""
                                    : Util.checkNull(item.notice.content))
                            + "','"
                            + (null == item.notice ? ""
                                    : Util.checkNull(item.notice.publishTime))
                            + "','"
                            + (null == item ? "" : new Gson().toJson(item))
                            + "','0'");
                }
                if (i != count - 1)
                {
                    buffer.append(" union all");
                }
            }
            db.execBySql(buffer.toString());
            closeDB();
        }
    }
    
    /**
     * 更新正常消息为已读
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private void updateStatus(String type)
    {
        // 数据读取的同步操作
        synchronized (synObj)
        {
            ContentValues values = new ContentValues();
            values.put("isRead", "1");
            db.update(DBHelper.TABLE_NOTIFICATION_MESSAGE,
                    values,
                    new String[] { "msgType", "isRead" },
                    new String[] { type, "0" });
            closeDB();
        }
    }
    
    /**
     * 查询所有用户的最新消息
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<MsgContentItem> queryTypeAllMsg(String type)
    {
        // 数据读取的同步操作
        synchronized (synObj)
        {
            
            String sql = "select * from " + DBHelper.TABLE_NOTIFICATION_MESSAGE
                    + " where msgType = '" + type + "' order by msgTime desc ";
            Cursor cursor = db.queryBySql(DBHelper.TABLE_NOTIFICATION_MESSAGE,
                    sql);
            List<MsgContentItem> list = new ArrayList<MsgContentItem>();
            MsgContentItem msg = null;
            if (cursor != null)
            {
                if (cursor.moveToFirst())
                {
                    do
                    {
                        try
                        {
                            msg = new MsgContentItem();
                            msg.notice = new MsgNoticeItem();
                            msg.id = (cursor.getString(cursor.getColumnIndex("id")));
                            msg.noticeId = (cursor.getString(cursor.getColumnIndex("msgId")));
                            msg.cmd = (cursor.getString(cursor.getColumnIndex("msgType")));
                            msg.notice.title = (cursor.getString(cursor.getColumnIndex("msgTitle")));
                            msg.notice.content = (cursor.getString(cursor.getColumnIndex("msgText")));
                            msg.notice.publishTime = (cursor.getString(cursor.getColumnIndex("msgTime")));
                            msg.from = (cursor.getString(cursor.getColumnIndex("msgFrom")));
                            msg.audioPath = (cursor.getString(cursor.getColumnIndex("audioPath")));
                            msg.videoPath = (cursor.getString(cursor.getColumnIndex("videoPath")));
                            msg.notice.resourceList = ((MsgContentItem) new Gson().fromJson(cursor.getString(cursor.getColumnIndex("resStr")),
                                    MsgContentItem.class)).notice.resourceList;
                            list.add(msg);
                        }
                        catch (Exception e)
                        {
                            LogX.getInstance().e(Util.makeLogTag(getClass()),
                                    e.toString());
                        }
                    } while (cursor.moveToNext());
                }
            }
            if (cursor != null)
            {
                cursor.close();
            }
            closeDB();
            return list;
        }
    }
    
    /**
     * 根据类型分组取时间最近的一条和类别的未读消息数量
     * select *,max(msgTime),count(isRead=0)count from table_notification_message group by msgType
     * <一句话功能简述>
     * <功能详细描述>
     * @return [参数说明]
     * 
     * @return List<MsgContentItem> [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<MsgContentItem> querryAllLatestMsgAndUnReadCount()
    {
        // 数据读取的同步操作
        synchronized (synObj)
        {
            String sql = "select *,max(msgTime),count(case when isRead = 0 then isRead end)count from "
                    + DBHelper.TABLE_NOTIFICATION_MESSAGE + " group by msgType";
            Cursor cursor = db.queryBySql(DBHelper.TABLE_NOTIFICATION_MESSAGE,
                    sql);
            List<MsgContentItem> list = new ArrayList<MsgContentItem>();
            MsgContentItem msg = null;
            if (cursor != null)
            {
                if (cursor.moveToFirst())
                {
                    do
                    {
                        msg = new MsgContentItem();
                        msg.notice = new MsgNoticeItem();
                        msg.id = (cursor.getString(cursor.getColumnIndex("id")));
                        msg.noticeId = (cursor.getString(cursor.getColumnIndex("msgId")));
                        msg.cmd = (cursor.getString(cursor.getColumnIndex("msgType")));
                        msg.notice.title = (cursor.getString(cursor.getColumnIndex("msgTitle")));
                        msg.notice.content = (cursor.getString(cursor.getColumnIndex("msgText")));
                        msg.notice.publishTime = (cursor.getString(cursor.getColumnIndex("msgTime")));
                        msg.from = (cursor.getString(cursor.getColumnIndex("msgFrom")));
                        msg.audioPath = (cursor.getString(cursor.getColumnIndex("audioPath")));
                        msg.videoPath = (cursor.getString(cursor.getColumnIndex("videoPath")));
                        msg.notice.resourceList = ((MsgContentItem) new Gson().fromJson(cursor.getString(cursor.getColumnIndex("resStr")),
                                MsgContentItem.class)).notice.resourceList;
                        msg.unReadCount = (cursor.getInt(cursor.getColumnIndex("count")));
                        list.add(msg);
                    } while (cursor.moveToNext());
                }
            }
            if (cursor != null)
            {
                cursor.close();
            }
            closeDB();
            return list;
        }
    }
    
    /**
     * 获取未读通知到数量
     * <一句话功能简述>
     * <功能详细描述>
     * @param classId 所选孩子班级Id
     * @return [参数说明]
     * 
     * @return int [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public int getUnReadCount(String classId)
    {
        int count = 0;
        String sql = "select count(case when isRead=0 then isRead end) from "
                + DBHelper.TABLE_NOTIFICATION_MESSAGE + " where msgType = '"
                + GlobalConstant.NOTIFICATION_MESSAGE_SYSTEM + "' or msgType='"
                + GlobalConstant.NOTIFICATION_MESSAGE_SCHOOL
                + "' or msgType= '" + GlobalConstant.NOTIFICATION_MESSAGE_CLASS
                + classId + "' or msgType= '"
                + GlobalConstant.NOTIFICATION_MESSAGE_WORK + classId + "'";
        Cursor cursor = db.queryBySql(DBHelper.TABLE_NOTIFICATION_MESSAGE, sql);
        if (null != cursor)
        {
            cursor.moveToFirst();
            count = cursor.getInt(0);
            cursor.close();
        }
        closeDB();
        return count;
    }
    
    /**
     * 查询当前用户的所有聊天记录中的某个用户的未读的消息的条数
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public int queryNotReadMsgCount(String type)
    {
        // 数据读取的同步操作
        synchronized (synObj)
        {
            int count = 0;
            String sql = "select * from " + DBHelper.TABLE_NOTIFICATION_MESSAGE
                    + " where isRead = 0 and msgType = '" + type + "'";
            Cursor cursor = db.queryBySql(DBHelper.TABLE_NOTIFICATION_MESSAGE,
                    sql);
            if (cursor != null)
            {
                count = cursor.getCount();
            }
            if (cursor != null)
            {
                cursor.close();
            }
            closeDB();
            return count;
        }
    }
    
    /**
     * 查询当前用户的所有聊天记录中所有的未读的消息的条数
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public int queryAllNotReadMsgCount()
    {
        // 数据读取的同步操作
        synchronized (synObj)
        {
            int count = 0;
            String sql = "select * from " + DBHelper.TABLE_NOTIFICATION_MESSAGE
                    + " where isRead = 0";
            Cursor cursor = db.queryBySql(DBHelper.TABLE_NOTIFICATION_MESSAGE,
                    sql);
            if (cursor != null)
            {
                count = cursor.getCount();
            }
            if (cursor != null)
            {
                cursor.close();
            }
            closeDB();
            return count;
        }
    }
    
    /**
     * 查询分页后每页数据
     * <一句话功能简述>
     * <功能详细描述>
     * @param otherJID
     * @param page
     * @return [参数说明]
     * 
     * @return List<DialogueMsg> [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public ArrayList<MsgContentItem> selectMsgByPage(String type, int page)
    {
        // 数据读取的同步操作
        synchronized (synObj)
        {
            Cursor cur = db.query(DBHelper.TABLE_NOTIFICATION_MESSAGE,
                    "msgType = '" + type + "'");
            ArrayList<MsgContentItem> list = new ArrayList<MsgContentItem>();
            if (null != cur && cur.getCount() != 0)
            {
                int page_count = 0;
                if (cur.getCount() % PAGE_SIZE != 0)
                {
                    page_count = cur.getCount() / PAGE_SIZE + 1;
                }
                else
                {
                    page_count = cur.getCount() / PAGE_SIZE;
                }
                String limit = null;
                if (page > page_count)
                {
                    return list;
                }
                else
                {
                    limit = " limit " + PAGE_SIZE * (page - 1) + ","
                            + PAGE_SIZE;
                }
                String sql = "select * from "
                        + DBHelper.TABLE_NOTIFICATION_MESSAGE
                        + " where msgType = '" + type + "'"
                        + " order by id desc" + limit;
                Cursor cursor = db.queryBySql(null, sql);
                if (cursor != null)
                {
                    if (cursor.moveToFirst())
                    {
                        do
                        {
                            MsgContentItem msg = new MsgContentItem();
                            msg.notice = new MsgNoticeItem();
                            msg.noticeId = (cursor.getString(cursor.getColumnIndex("msgId")));
                            msg.cmd = (cursor.getString(cursor.getColumnIndex("msgType")));
                            msg.notice.title = (cursor.getString(cursor.getColumnIndex("msgTitle")));
                            msg.notice.content = (cursor.getString(cursor.getColumnIndex("msgText")));
                            msg.notice.publishTime = (cursor.getString(cursor.getColumnIndex("msgTime")));
                            msg.notice.sendUser = ((MsgContentItem) new Gson().fromJson(cursor.getString(cursor.getColumnIndex("resStr")),
                                    MsgContentItem.class)).notice.sendUser;
                            msg.from = (cursor.getString(cursor.getColumnIndex("msgFrom")));
                            msg.audioPath = (cursor.getString(cursor.getColumnIndex("audioPath")));
                            msg.videoPath = (cursor.getString(cursor.getColumnIndex("videoPath")));
                            msg.notice.resourceList = ((MsgContentItem) new Gson().fromJson(cursor.getString(cursor.getColumnIndex("resStr")),
                                    MsgContentItem.class)).notice.resourceList;
                            list.add(msg);
                        } while (cursor.moveToNext());
                    }
                }
                updateStatus(type);
                if (cursor != null)
                {
                    cursor.close();
                }
            }
            if (cur != null)
            {
                cur.close();
            }
            closeDB();
            return list;
        }
    }
    
    /**
     * 获取分页后 到第几页的全部消息数据
     * <一句话功能简述>
     * <功能详细描述>
     * @param otherJID
     * @param page 第几页
     * @return [参数说明]
     * 
     * @return List<DialogueMsg> [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<MsgContentItem> selectPartMsgByPage(String type, int page)
    {
        // 数据读取的同步操作
        synchronized (synObj)
        {
            Cursor cur = db.query(DBHelper.TABLE_NOTIFICATION_MESSAGE,
                    "msgType = '" + type + "'");
            List<MsgContentItem> list = new ArrayList<MsgContentItem>();
            if (null != cur && cur.getCount() != 0)
            {
                int page_count = 0;
                if (cur.getCount() % PAGE_SIZE != 0)
                {
                    page_count = cur.getCount() / PAGE_SIZE + 1;
                }
                else
                {
                    page_count = cur.getCount() / PAGE_SIZE;
                }
                String limit = null;
                if (page > page_count)
                {
                    if (null != cur)
                    {
                        cur.close();
                    }
                    closeDB();
                    return list;
                }
                else
                {
                    limit = " limit 0," + page * PAGE_SIZE;
                }
                Cursor cursor = db.query(DBHelper.TABLE_NOTIFICATION_MESSAGE,
                        "msgType = '" + type + "'" + " order by id desc"
                                + limit,
                        null);
                if (cursor != null)
                {
                    if (cursor.moveToFirst())
                    {
                        do
                        {
                            MsgContentItem msg = new MsgContentItem();
                            msg.notice = new MsgNoticeItem();
                            msg.noticeId = (cursor.getString(cursor.getColumnIndex("msgId")));
                            msg.cmd = (cursor.getString(cursor.getColumnIndex("msgType")));
                            msg.notice.title = (cursor.getString(cursor.getColumnIndex("msgTitle")));
                            msg.notice.content = (cursor.getString(cursor.getColumnIndex("msgText")));
                            msg.notice.publishTime = (cursor.getString(cursor.getColumnIndex("msgTime")));
                            msg.notice.sendUser = ((MsgContentItem) new Gson().fromJson(cursor.getString(cursor.getColumnIndex("resStr")),
                                    MsgContentItem.class)).notice.sendUser;
                            msg.from = (cursor.getString(cursor.getColumnIndex("msgFrom")));
                            msg.audioPath = (cursor.getString(cursor.getColumnIndex("audioPath")));
                            msg.videoPath = (cursor.getString(cursor.getColumnIndex("videoPath")));
                            msg.notice.resourceList = ((MsgContentItem) new Gson().fromJson(cursor.getString(cursor.getColumnIndex("resStr")),
                                    MsgContentItem.class)).notice.resourceList;
                            list.add(msg);
                        } while (cursor.moveToNext());
                    }
                }
                if (null != cursor)
                {
                    cursor.close();
                }
            }
            if (null != cur)
            {
                cur.close();
            }
            closeDB();
            return list;
        }
    }
    
    public int getOneMsgCount(String type)
    {
        // 数据读取的同步操作
        synchronized (synObj)
        {
            Cursor cur = db.query(DBHelper.TABLE_NOTIFICATION_MESSAGE,
                    "msgType = '" + type + "'",
                    null);
            int count = 0;
            if (null != cur)
            {
                count = cur.getCount();
            }
            if (null != cur)
            {
                cur.close();
            }
            closeDB();
            return count;
        }
    }
    
    /**
     * 
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public int getMsgID(String type, String time)
    {
        // 数据读取的同步操作
        synchronized (synObj)
        {
            int id = -1;
            Cursor cursor = db.query(DBHelper.TABLE_NOTIFICATION_MESSAGE,
                    "msgType = '" + type + "' and msgTime = " + time);
            if (null != cursor)
            {
                cursor.moveToFirst();
                id = cursor.getInt(0);
            }
            if (null != cursor)
            {
                cursor.close();
            }
            closeDB();
            return id;
        }
    }
    
    /**
     * 删除 单个用户的所有聊天数据
     * <一句话功能简述>
     * <功能详细描述>
     * @param otherJID [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void deleteAllMsg(String type)
    {
        // 数据读取的同步操作
        synchronized (synObj)
        {
            db.delete(DBHelper.TABLE_NOTIFICATION_MESSAGE, "msgType = '" + type
                    + "'");
            closeDB();
        }
    }
    
    /** <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 删除 当前用户所有聊天记录（设置界面清除记录）
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void deleteAllMsg()
    {
        // 数据读取的同步操作
        synchronized (synObj)
        {
            db.delete(DBHelper.TABLE_NOTIFICATION_MESSAGE, null, null);
            closeDB();
        }
    }
    
    /**
     * 删除用户的单条聊天信息
     * <一句话功能简述>
     * <功能详细描述>
     * @param id [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void deleteOneMsg(String id)
    {
        // 数据读取的同步操作
        synchronized (synObj)
        {
            db.delete(DBHelper.TABLE_NOTIFICATION_MESSAGE, "id = " + id);
            closeDB();
        }
    }
    
    /** <一句话功能简述>
     * 根据用户jid查询相对应的信息
     * <功能详细描述>
     * @param item  所传入的下载对象
     * @return [参数说明]
     * 
     * @return DownloadItem [返回类型说明]  下载对象
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public DownloadItem queryDownloadById(DownloadItem item)
    {
        // 数据读取的同步操作
        //        synchronized (synObj)
        //        {
        //            StringBuffer sb = new StringBuffer();
        //            ////下载中增加下载状态等5,如果没有下载完成或者下载失败，则继续下载。
        //            //status=5，下载完成
        //            sb.append("JID='")
        //                    .append(item.jid)
        //                    .append("' and videoUrl='")
        //                    .append(item.downloadServer)
        //                    .append("' and (status=5 or fileLength=downloadLength) and fileLength!=0");
        //            
        //            Cursor cursor = db.query(DBHelper.TABLE_HISTORY_MESSAGE,
        //                    sb.toString());
        //            if (cursor == null)
        //            {
        //                return null;
        //            }
        //            while (cursor.moveToNext())
        //            {
        //                item.jid = cursor.getString(cursor.getColumnIndex("JID"));
        //                item.downloadServer = cursor.getString(cursor.getColumnIndex("videoUrl"));
        //                item.filePath = cursor.getString(cursor.getColumnIndex("videoPath"));
        //                item.status = cursor.getInt(cursor.getColumnIndex("status"));
        //                item.fileSize = cursor.getInt(cursor.getColumnIndex("fileLength"));
        //                item.size = cursor.getInt(cursor.getColumnIndex("downloadLength"));
        //            }
        //            if (cursor != null)
        //            {
        //                cursor.close();
        //            }
        //            closeDB();
        //            return item;
        //        }
        return null;
    }
    
    /** <一句话功能简述>
     * 插入数据, 先查下，如果不存在插入，否则更新
     * <功能详细描述>
     * @param user [参数说明]
     * 要插入的对象
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void addDb(DownloadItem user)
    {
        // 数据读取的同步操作
        //        synchronized (synObj)
        //        {
        //            StringBuffer sb = new StringBuffer();
        //            sb.append("videoUrl")
        //                    .append("='")
        //                    .append(user.downloadServer)
        //                    .append("'");
        //            Cursor cursor = null;
        //            try
        //            {
        //                cursor = db.query(DBHelper.TABLE_HISTORY_MESSAGE, sb.toString());
        //                // 判空操作
        //                if (cursor != null)
        //                {
        //                    // 加入信息表
        //                    ContentValues values = new ContentValues();
        //                    values.put("JID", user.jid);
        //                    values.put("videoUrl", user.downloadServer);
        //                    values.put("videoPath", user.filePath);
        //                    
        //                    if (cursor.getCount() > 0)
        //                    {
        //                        //如果存在，直接更新
        //                        cursor.moveToFirst();
        //                        
        //                        user.status = cursor.getInt(cursor.getColumnIndex("status"));
        //                        user.fileSize = cursor.getInt(cursor.getColumnIndex("fileLength"));
        //                        user.size = cursor.getInt(cursor.getColumnIndex("downloadLength"));
        //                        
        //                        int tid = cursor.getInt(cursor.getColumnIndex("id"));
        //                        db.update(DBHelper.TABLE_HISTORY_MESSAGE,
        //                                values,
        //                                new String[] { "id" },
        //                                new String[] { tid + "" });
        //                        user.id = tid + "";
        //                    }
        //                    else
        //                    {
        //                        values.put("status", user.status);
        //                        values.put("fileLength", user.fileSize);
        //                        values.put("downloadLength", user.size);
        //                        
        //                        user.id = db.insert(DBHelper.TABLE_HISTORY_MESSAGE,
        //                                values) + "";
        //                    }
        //                }
        //                
        //            }
        //            catch (Exception e)
        //            {
        //                user.id = "0";
        //                LogX.getInstance().e(TAG, e.toString());
        //            }
        //            finally
        //            {
        //                if (null != cursor)
        //                {
        //                    cursor.close();
        //                }
        //                closeDB();
        //            }
        //        }
    }
    
    /**
     * 更新数据
     * <一句话功能简述>
     * <功能详细描述>
     * @param item [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void updateById(DownloadItem item)
    {
        // 数据读取的同步操作
        //        synchronized (synObj)
        //        {
        //            ContentValues values = new ContentValues();
        //            values.put("status", item.status);
        //            values.put("downloadLength", item.size);
        //            values.put("fileLength", item.fileSize);
        //            db.update(DBHelper.TABLE_HISTORY_MESSAGE,
        //                    values,
        //                    new String[] { "id" },
        //                    new String[] { item.id });
        //            closeDB();
        //        }
    }
    
}
