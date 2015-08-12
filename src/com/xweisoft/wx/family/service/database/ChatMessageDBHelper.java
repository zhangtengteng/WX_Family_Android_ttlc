package com.xweisoft.wx.family.service.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.xweisoft.wx.family.logic.model.MessageItem;
import com.xweisoft.wx.family.util.DateTools;
import com.xweisoft.wx.family.util.LogX;
import com.xweisoft.wx.family.util.LoginUtil;
import com.xweisoft.wx.family.util.Util;

/**
 * 聊天数据库帮助类
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-7-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ChatMessageDBHelper extends BaseDBHelper
{
    
    /**
     * 打印日志时日志信息中的标识符
     */
    private static final String TAG = Util.makeLogTag(ChatMessageDBHelper.class);
    
    /**
     * 每页数量
     */
    public static final int PAGE_SIZE = 10;
    
    /**
     * 同步锁
     */
    private static Object synObj = new Object();
    
    /**
     * <默认构造函数>
     */
    public ChatMessageDBHelper(Context context, String dbName)
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
     * 更新数据
     * <一句话功能简述>
     * <功能详细描述>
     * @param msg [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void updateTime(String msgId, String time)
    {
        // 数据读取的同步操作
        synchronized (synObj)
        {
            ContentValues values = new ContentValues();
            values.put("msgTime", Util.checkNull(time));
            db.update(DBHelper.TABLE_CHAT_MESSAGE,
                    values,
                    new String[] { "msgId" },
                    new String[] { msgId });
            closeDB();
        }
    }
    
    /**
     * 更新数据
     * <一句话功能简述>
     * <功能详细描述>
     * @param msg [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void updateMessageResource(String msgId, MessageItem item)
    {
        // 数据读取的同步操作
        synchronized (synObj)
        {
            ContentValues values = new ContentValues();
            values.put("msgText", item.text);
            if (null != item.msgLocalPath)
            {
                values.put("msgPath", Util.checkNull(item.msgLocalPath));
            }
            if (null != item.thumbLocalPath)
            {
                values.put("msgThumbPath", Util.checkNull(item.thumbLocalPath));
            }
            if (null != item.thumbnailRemotePath)
            {
                values.put("msgThumbText",
                        Util.checkNull(item.thumbnailRemotePath));
            }
            db.update(DBHelper.TABLE_CHAT_MESSAGE,
                    values,
                    new String[] { "msgId" },
                    new String[] { msgId });
            closeDB();
        }
    }
    
    /**
     * 更新数据
     * <一句话功能简述>
     * <功能详细描述>
     * @param msg [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void updateAllRead(String userId, String studentId)
    {
        // 数据读取的同步操作
        synchronized (synObj)
        {
            ContentValues values = new ContentValues();
            values.put("isRead", "1");
            db.update(DBHelper.TABLE_CHAT_MESSAGE,
                    values,
                    new String[] { "userId", "uStudentId", "isRead", "groupId" },
                    new String[] { userId, studentId, "0", "" });
            closeDB();
        }
    }
    
    /**
     * 更新数据
     * <一句话功能简述>
     * <功能详细描述>
     * @param msg [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void updateGroupAllRead(String groupId)
    {
        // 数据读取的同步操作
        synchronized (synObj)
        {
            ContentValues values = new ContentValues();
            values.put("isRead", "1");
            db.update(DBHelper.TABLE_CHAT_MESSAGE, values, new String[] {
                    "groupId", "isRead" }, new String[] { groupId, "0" });
            closeDB();
        }
    }
    
    /**
     * 更新数据
     * <一句话功能简述>
     * <功能详细描述>
     * @param msg [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void updateReadByMsgId(String msgId)
    {
        // 数据读取的同步操作
        synchronized (synObj)
        {
            ContentValues values = new ContentValues();
            values.put("isRead", "1");
            db.update(DBHelper.TABLE_CHAT_MESSAGE,
                    values,
                    new String[] { "msgId" },
                    new String[] { msgId });
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
    public void insert(MessageItem item)
    {
        // 数据读取的同步操作
        synchronized (synObj)
        {
            if (null == item)
            {
                return;
            }
            String sql = "select * from " + DBHelper.TABLE_CHAT_MESSAGE
                    + " where msgId = '" + item.messageId + "'";
            Cursor cursor = db.queryBySql(DBHelper.TABLE_CHAT_MESSAGE, sql);
            ContentValues values = new ContentValues();
            values.put("msgId", Util.checkNull(item.messageId));
            values.put("userId", Util.checkNull(item.userId));
            values.put("isFrom", Util.checkNull(item.isFrom));
            values.put("isRead", Util.checkNull(item.isRead));
            values.put("msgType", Util.checkNull(item.messageBodyType));
            values.put("msgTime", Util.checkNull(item.recTime));
            if ("0".equals(item.isFrom))
            {
                values.put("studentId", Util.checkNull(item.to));
                values.put("uStudentId", Util.checkNull(item.from));
            }
            else
            {
                values.put("studentId", Util.checkNull(item.from));
                values.put("uStudentId", Util.checkNull(item.to));
            }
            values.put("msgText", Util.checkNull(item.text));
            values.put("msgPath", Util.checkNull(item.msgLocalPath));
            values.put("msgThumbText", Util.checkNull(item.thumbnailRemotePath));
            values.put("msgThumbPath", Util.checkNull(item.thumbLocalPath));
            values.put("msgDuration", Util.checkNull(item.duration));
            values.put("groupId", Util.checkNull(item.groupId));
            if (null != cursor)
            {
                if (cursor.getCount() == 0)
                {
                    db.insert(DBHelper.TABLE_CHAT_MESSAGE, values);
                }
            }
            if (cursor != null)
            {
                cursor.close();
            }
            closeDB();
        }
    }
    
    /**
     * 分页查询单个用户
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * 示例Sql
     * select a.id, a.userId, a.isFrom, a.msgId, a.msgType, a.msgTime, a.uStudentId, a.studentId, a.msgText, a.msgPath,a.msgThumbText, a.msgThumbPath,a.msgDuration, b.name, b.portraitPath,b.appellation from table_chat_message as a left join table_contacts as b on (a.userId = b.userId and a.uStudentId = b.studentId) where a.userId = '496' and a.uStudentId = '176'
     * 
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public ArrayList<MessageItem> queryMsgByPage(String userId,
            String studentId, int page)
    {
        // 数据读取的同步操作
        synchronized (synObj)
        {
            userId = Util.checkNull(userId);
            studentId = Util.checkNull(studentId);
            ArrayList<MessageItem> list = new ArrayList<MessageItem>();
            if (TextUtils.isEmpty(userId))
            {
                return list;
            }
            Cursor cur = db.query(DBHelper.TABLE_CHAT_MESSAGE, "userId = '"
                    + userId + "' and uStudentId = '" + studentId
                    + "' and groupId = ''", null);
            if (null != cur && cur.getCount() != 0)
            {
                int yushu = 0;
                int page_count = 0;
                if (cur.getCount() % PAGE_SIZE != 0)
                {
                    page_count = cur.getCount() / PAGE_SIZE + 1;
                    yushu = cur.getCount() % PAGE_SIZE;
                }
                else
                {
                    page_count = cur.getCount() / PAGE_SIZE;
                    yushu = PAGE_SIZE;
                }
                String limit = null;
                if (page > page_count)
                {
                    return list;
                }
                else
                {
                    if (page_count - page == 0)
                    {
                        limit = " limit " + yushu;
                    }
                    else
                    {
                        limit = " limit "
                                + (PAGE_SIZE * (page_count - page - 1) + yushu)
                                + ","
                                + (PAGE_SIZE * (page_count - page) + yushu);
                    }
                }
                String sql = "select a.id, a.userId, a.isFrom, a.msgId, a.msgType, a.msgTime, a.uStudentId, a.studentId, a.msgText, a.msgPath, a.msgThumbText, a.msgThumbPath, a.msgDuration, a.groupId, b.name, b.portraitPath,b.appellation from "
                        + DBHelper.TABLE_CHAT_MESSAGE
                        + " as a left join "
                        + DBHelper.TABLE_CONTACTS
                        + " as b on (a.userId = b.userId and a.uStudentId = b.studentId) where a.userId = '"
                        + userId
                        + "' and a.uStudentId = '"
                        + studentId
                        + "' and a.groupId = '' order by a.msgTime asc "
                        + limit;
                Cursor cursor = db.queryBySql(DBHelper.TABLE_CHAT_MESSAGE, sql);
                MessageItem item = null;
                int index = 0;
                if (cursor != null)
                {
                    if (cursor.moveToFirst())
                    {
                        do
                        {
                            try
                            {
                                item = new MessageItem();
                                item.id = (cursor.getString(cursor.getColumnIndex("id")));
                                item.userId = (cursor.getString(cursor.getColumnIndex("userId")));
                                item.messageId = (cursor.getString(cursor.getColumnIndex("msgId")));
                                item.isFrom = (cursor.getString(cursor.getColumnIndex("isFrom")));
                                item.messageBodyType = (cursor.getString(cursor.getColumnIndex("msgType")));
                                item.recTime = (cursor.getString(cursor.getColumnIndex("msgTime")));
                                if ("0".equals(item.isFrom))
                                {
                                    item.from = (cursor.getString(cursor.getColumnIndex("uStudentId")));
                                    item.to = (cursor.getString(cursor.getColumnIndex("studentId")));
                                }
                                else
                                {
                                    item.from = (cursor.getString(cursor.getColumnIndex("studentId")));
                                    item.to = (cursor.getString(cursor.getColumnIndex("uStudentId")));
                                }
                                item.text = (cursor.getString(cursor.getColumnIndex("msgText")));
                                item.thumbnailRemotePath = (cursor.getString(cursor.getColumnIndex("msgThumbText")));
                                item.duration = (cursor.getString(cursor.getColumnIndex("msgDuration")));
                                item.userHeader = (cursor.getString(cursor.getColumnIndex("portraitPath")));
                                item.userName = (cursor.getString(cursor.getColumnIndex("name")));
                                item.userAppellation = (cursor.getString(cursor.getColumnIndex("appellation")));
                                item.thumbLocalPath = (cursor.getString(cursor.getColumnIndex("msgThumbPath")));
                                item.msgLocalPath = (cursor.getString(cursor.getColumnIndex("msgPath")));
                                if (list.size() == 0)
                                {
                                    item.msgTime = DateTools.getChatTime(item.recTime);
                                }
                                else
                                {
                                    String preTime = list.get(index - 1).recTime;
                                    if (DateTools.timeDifference(preTime,
                                            item.recTime) > 5 * 60 * 1000)
                                    {
                                        item.msgTime = DateTools.getChatTime(item.recTime);
                                    }
                                }
                                boolean add = true;
                                if ("0".equals(item.isFrom)
                                        && !LoginUtil.getStudentId(mContext)
                                                .equals(item.to))
                                {
                                    add = false;
                                }
                                if (add)
                                {
                                    list.add(item);
                                }
                                else
                                {
                                    index--;
                                }
                                index++;
                            }
                            catch (Exception e)
                            {
                                LogX.getInstance()
                                        .e(Util.makeLogTag(getClass()),
                                                e.toString());
                            }
                        } while (cursor.moveToNext());
                    }
                }
                if (cursor != null)
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
    
    /**
     * 分页查询单个用户
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * 示例Sql
     * select a.id, a.userId, a.isFrom, a.msgId, a.msgType, a.msgTime, a.uStudentId, a.studentId, a.msgText, a.msgPath,a.msgThumbText, a.msgThumbPath,a.msgDuration, a.groupId,b.name, b.portraitPath,b.appellation from table_chat_message as a left join table_contacts as b on (a.userId = b.userId and a.uStudentId = b.studentId) where a.groupId = '496'
     * 
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public ArrayList<MessageItem> queryGroupMsgByPage(String groupId, int page)
    {
        // 数据读取的同步操作
        synchronized (synObj)
        {
            ArrayList<MessageItem> list = new ArrayList<MessageItem>();
            Cursor cur = db.query(DBHelper.TABLE_CHAT_MESSAGE, "groupId = '"
                    + groupId + "'", null);
            if (null != cur && cur.getCount() != 0)
            {
                int yushu = 0;
                int page_count = 0;
                if (cur.getCount() % PAGE_SIZE != 0)
                {
                    page_count = cur.getCount() / PAGE_SIZE + 1;
                    yushu = cur.getCount() % PAGE_SIZE;
                }
                else
                {
                    page_count = cur.getCount() / PAGE_SIZE;
                    yushu = PAGE_SIZE;
                }
                String limit = null;
                if (page > page_count)
                {
                    return list;
                }
                else
                {
                    if (page_count - page == 0)
                    {
                        limit = " limit " + yushu;
                    }
                    else
                    {
                        limit = " limit "
                                + (PAGE_SIZE * (page_count - page - 1) + yushu)
                                + ","
                                + (PAGE_SIZE * (page_count - page) + yushu);
                    }
                }
                String sql = "select a.id, a.userId, a.isFrom, a.msgId, a.msgType, a.msgTime, a.uStudentId, a.studentId, a.msgText, a.msgPath,a.msgThumbText, a.msgThumbPath,a.msgDuration, a.groupId, b.name, b.portraitPath,b.appellation from "
                        + DBHelper.TABLE_CHAT_MESSAGE
                        + " as a left join "
                        + DBHelper.TABLE_CONTACTS
                        + " as b on (a.userId = b.userId and a.uStudentId = b.studentId) where a.groupId = '"
                        + groupId + "' order by a.msgTime asc " + limit;
                Cursor cursor = db.queryBySql(DBHelper.TABLE_CHAT_MESSAGE, sql);
                MessageItem item = null;
                int index = 0;
                if (cursor != null)
                {
                    if (cursor.moveToFirst())
                    {
                        do
                        {
                            try
                            {
                                item = new MessageItem();
                                item.id = (cursor.getString(cursor.getColumnIndex("id")));
                                item.userId = (cursor.getString(cursor.getColumnIndex("userId")));
                                item.messageId = (cursor.getString(cursor.getColumnIndex("msgId")));
                                item.isFrom = (cursor.getString(cursor.getColumnIndex("isFrom")));
                                item.messageBodyType = (cursor.getString(cursor.getColumnIndex("msgType")));
                                item.recTime = (cursor.getString(cursor.getColumnIndex("msgTime")));
                                if ("0".equals(item.isFrom))
                                {
                                    item.from = (cursor.getString(cursor.getColumnIndex("uStudentId")));
                                    item.to = (cursor.getString(cursor.getColumnIndex("studentId")));
                                }
                                else
                                {
                                    item.from = (cursor.getString(cursor.getColumnIndex("studentId")));
                                    item.to = (cursor.getString(cursor.getColumnIndex("uStudentId")));
                                }
                                item.text = (cursor.getString(cursor.getColumnIndex("msgText")));
                                item.thumbnailRemotePath = (cursor.getString(cursor.getColumnIndex("msgThumbText")));
                                item.duration = (cursor.getString(cursor.getColumnIndex("msgDuration")));
                                item.userHeader = (cursor.getString(cursor.getColumnIndex("portraitPath")));
                                item.userName = (cursor.getString(cursor.getColumnIndex("name")));
                                item.userAppellation = (cursor.getString(cursor.getColumnIndex("appellation")));
                                item.thumbLocalPath = (cursor.getString(cursor.getColumnIndex("msgThumbPath")));
                                item.msgLocalPath = (cursor.getString(cursor.getColumnIndex("msgPath")));
                                item.groupId = (cursor.getString(cursor.getColumnIndex("groupId")));
                                if (list.size() == 0)
                                {
                                    item.msgTime = DateTools.getChatTime(item.recTime);
                                }
                                else
                                {
                                    String preTime = list.get(index - 1).recTime;
                                    if (DateTools.timeDifference(preTime,
                                            item.recTime) > 5 * 60 * 1000)
                                    {
                                        item.msgTime = DateTools.getChatTime(item.recTime);
                                    }
                                }
                                list.add(item);
                                index++;
                            }
                            catch (Exception e)
                            {
                                LogX.getInstance()
                                        .e(Util.makeLogTag(getClass()),
                                                e.toString());
                            }
                        } while (cursor.moveToNext());
                    }
                }
                if (cursor != null)
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
    
    /**
     * 分页查询单个用户
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * 示例Sql
     * select a.id, a.userId, a.isFrom, a.msgId, a.msgType, a.msgTime, a.uStudentId, a.studentId, a.msgText, a.msgPath,a.msgThumbText, a.msgThumbPath,a.msgDuration, b.name, b.portraitPath,b.appellation from table_chat_message as a left join table_contacts as b on (a.userId = b.userId and a.uStudentId = b.studentId) where a.userId = '496' and a.uStudentId = '176'
     * 
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public ArrayList<MessageItem> queryPartMsgByPage(String userId,
            String studentId, int page)
    {
        // 数据读取的同步操作
        synchronized (synObj)
        {
            userId = Util.checkNull(userId);
            studentId = Util.checkNull(studentId);
            ArrayList<MessageItem> list = new ArrayList<MessageItem>();
            if (TextUtils.isEmpty(userId))
            {
                return list;
            }
            Cursor cur = db.query(DBHelper.TABLE_CHAT_MESSAGE, "userId = '"
                    + userId + "' and uStudentId = '" + studentId + "'", null);
            if (null != cur)
            {
                int yushu = 0;
                int page_count = 0;
                if (cur.getCount() % PAGE_SIZE != 0)
                {
                    page_count = cur.getCount() / PAGE_SIZE + 1;
                    yushu = cur.getCount() % PAGE_SIZE;
                }
                else
                {
                    page_count = cur.getCount() / PAGE_SIZE;
                    yushu = PAGE_SIZE;
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
                    if (page_count - page == 0)
                    {
                        limit = "";
                    }
                    else
                    {
                        limit = " limit "
                                + (PAGE_SIZE * (page_count - page - 1) + yushu + 1)
                                + "," + (PAGE_SIZE * (page_count - 1) + yushu);
                    }
                }
                String sql = "select a.id, a.userId, a.isFrom, a.msgId, a.msgType, a.msgTime, a.uStudentId, a.studentId, a.msgText, a.msgPath,a.msgThumbText, a.msgThumbPath,a.msgDuration, b.name, b.portraitPath,b.appellation from "
                        + DBHelper.TABLE_CHAT_MESSAGE
                        + " as a left join "
                        + DBHelper.TABLE_CONTACTS
                        + " as b on (a.userId = b.userId and a.uStudentId = b.studentId) where a.userId = '"
                        + userId
                        + "' and a.uStudentId = '"
                        + studentId
                        + "' and a.group = ''" + limit;
                Cursor cursor = db.queryBySql(DBHelper.TABLE_CHAT_MESSAGE, sql);
                MessageItem item = null;
                if (cursor != null)
                {
                    if (cursor.moveToFirst())
                    {
                        do
                        {
                            try
                            {
                                item = new MessageItem();
                                item.id = (cursor.getString(cursor.getColumnIndex("id")));
                                item.userId = (cursor.getString(cursor.getColumnIndex("userId")));
                                item.messageId = (cursor.getString(cursor.getColumnIndex("msgId")));
                                item.isFrom = (cursor.getString(cursor.getColumnIndex("isFrom")));
                                item.messageBodyType = (cursor.getString(cursor.getColumnIndex("msgType")));
                                item.recTime = (cursor.getString(cursor.getColumnIndex("msgTime")));
                                if ("0".equals(item.isFrom))
                                {
                                    item.from = (cursor.getString(cursor.getColumnIndex("uStudentId")));
                                    item.to = (cursor.getString(cursor.getColumnIndex("studentId")));
                                }
                                else
                                {
                                    item.from = (cursor.getString(cursor.getColumnIndex("studentId")));
                                    item.to = (cursor.getString(cursor.getColumnIndex("uStudentId")));
                                }
                                item.text = (cursor.getString(cursor.getColumnIndex("msgText")));
                                item.thumbnailRemotePath = (cursor.getString(cursor.getColumnIndex("msgThumbText")));
                                item.duration = (cursor.getString(cursor.getColumnIndex("msgDuration")));
                                item.userHeader = (cursor.getString(cursor.getColumnIndex("portraitPath")));
                                item.userName = (cursor.getString(cursor.getColumnIndex("name")));
                                item.userAppellation = (cursor.getString(cursor.getColumnIndex("appellation")));
                                item.thumbLocalPath = (cursor.getString(cursor.getColumnIndex("msgThumbPath")));
                                item.msgLocalPath = (cursor.getString(cursor.getColumnIndex("msgPath")));
                                list.add(item);
                            }
                            catch (Exception e)
                            {
                                LogX.getInstance()
                                        .e(Util.makeLogTag(getClass()),
                                                e.toString());
                            }
                        } while (cursor.moveToNext());
                    }
                }
                if (cursor != null)
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
    
    /**
     * 获取未读消息的数量
     * <一句话功能简述>
     * <功能详细描述>
     * @return [参数说明]
     * 
     * @return int [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public int getUnReadCount()
    {
        int count = 0;
        String sql = "select count(case when isRead=0 then isRead end) from "
                + DBHelper.TABLE_CHAT_MESSAGE;
        Cursor cursor = db.queryBySql(DBHelper.TABLE_CHAT_MESSAGE, sql);
        if (null != cursor)
        {
            cursor.moveToFirst();
            count = cursor.getInt(0);
            cursor.close();
        }
        closeDB();
        return count;
    }
}
