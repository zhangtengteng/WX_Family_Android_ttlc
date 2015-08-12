package com.xweisoft.wx.family.service.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.xweisoft.wx.family.WXApplication;
import com.xweisoft.wx.family.logic.model.MessageItem;
import com.xweisoft.wx.family.logic.model.MsgContentItem;
import com.xweisoft.wx.family.util.LogX;
import com.xweisoft.wx.family.util.LoginUtil;
import com.xweisoft.wx.family.util.Util;

/**
 * 首页消息回话
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-7-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class SessionMessageDBHelper extends BaseDBHelper
{
    
    /**
     * 打印日志时日志信息中的标识符
     */
    private static final String TAG = Util.makeLogTag(SessionMessageDBHelper.class);
    
    /**
     * 同步锁
     */
    private static Object synObj = new Object();
    
    public SessionMessageDBHelper(Context context, String dbName)
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
     * 插入数据 已存在则更新
     * <一句话功能简述>
     * <功能详细描述>
     * @param item [参数说明]
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
            String uStudentId = null;
            String studentId = null;
            ContentValues values = new ContentValues();
            values.put("userId", Util.checkNull(item.userId));
            if ("0".equals(item.isFrom))
            {
                studentId = Util.checkNull(item.to);
                uStudentId = Util.checkNull(item.from);
            }
            else
            {
                studentId = Util.checkNull(item.from);
                uStudentId = Util.checkNull(item.to);
            }
            values.put("uStudentId", Util.checkNull(uStudentId));
            values.put("studentId", Util.checkNull(studentId));
            values.put("msgId", Util.checkNull(item.messageId));
            String sql = "select * from " + DBHelper.TABLE_SESSION_MESSAGE
                    + " where userId = '" + item.userId
                    + "' and uStudentId = '" + uStudentId
                    + "' and studentId = '" + studentId + "'";
            Cursor cursor = db.queryBySql(DBHelper.TABLE_SESSION_MESSAGE, sql);
            if (null != cursor)
            {
                if (cursor.getCount() == 0)
                {
                    db.insert(DBHelper.TABLE_SESSION_MESSAGE, values);
                }
                else
                {
                    db.update(DBHelper.TABLE_SESSION_MESSAGE,
                            values,
                            new String[] { "userId", "uStudentId", "studentId" },
                            new String[] { item.userId, uStudentId, studentId });
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
     * 插入数据 已存在则更新
     * <一句话功能简述>
     * <功能详细描述>
     * @param item [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void insertGroupSession(MessageItem item)
    {
        // 数据读取的同步操作
        synchronized (synObj)
        {
            if (null == item)
            {
                return;
            }
            String uStudentId = null;
            String studentId = null;
            ContentValues values = new ContentValues();
            values.put("userId", Util.checkNull(item.userId));
            if ("0".equals(item.isFrom))
            {
                studentId = Util.checkNull(item.to);
                uStudentId = Util.checkNull(item.from);
            }
            else
            {
                studentId = Util.checkNull(item.from);
                uStudentId = Util.checkNull(item.to);
            }
            values.put("uStudentId", Util.checkNull(uStudentId));
            values.put("studentId", Util.checkNull(studentId));
            values.put("msgId", Util.checkNull(item.messageId));
            values.put("groupId", Util.checkNull(item.groupId));
            String sql = "select * from " + DBHelper.TABLE_SESSION_MESSAGE
                    + " where groupId = '" + item.groupId + "'";
            Cursor cursor = db.queryBySql(DBHelper.TABLE_SESSION_MESSAGE, sql);
            if (null != cursor)
            {
                if (cursor.getCount() == 0)
                {
                    db.insert(DBHelper.TABLE_SESSION_MESSAGE, values);
                }
                else
                {
                    db.update(DBHelper.TABLE_SESSION_MESSAGE,
                            values,
                            new String[] { "groupId" },
                            new String[] { item.groupId });
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
     * 通过数据库id删除记录
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void deleteSessionById(String id)
    {
        // 数据读取的同步操作
        synchronized (synObj)
        {
            db.delete(DBHelper.TABLE_SESSION_MESSAGE,
                    new String[] { "id" },
                    new String[] { id });
            closeDB();
        }
    }
    
    /**
     * 查询单个用户信息
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * 示例Sql
     * select a.id, a.userId, a.msgId, a.studentId, b.name, b.portraitPath,b.appellation, c.uStudentId, c.msgText,c.msgType,c.msgTime, d.count from table_session_message as a left join table_contacts as b on (a.userId = b.userId and a.studentId = b.studentId) left join table_chat_message as c on a.msgId = c.msgId left join (select c.userId, c.uStudentId, count(case when c.isRead = 0 then c.isRead end)count from table_chat_message as c group by c.uStudentId) as d on (a.userId = d.userId and a.studentId = d.uStudentId ) order by c.msgTime desc
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public ArrayList<MsgContentItem> queryAllMsg()
    {
        // 数据读取的同步操作
        synchronized (synObj)
        {
            ArrayList<MsgContentItem> list = new ArrayList<MsgContentItem>();
            String s = "select * from " + DBHelper.TABLE_SESSION_MESSAGE;
            Cursor cur = db.queryBySql(DBHelper.TABLE_SESSION_MESSAGE, s);
            if (null != cur && cur.getCount() != 0)
            {
                String sql = "select a.id, a.userId, a.msgId,a.groupId, a.uStudentId, b.name, b.portraitPath,b.appellation, c.studentId, c.isFrom, c.msgText,c.msgType,c.msgTime, d.count from "
                        + DBHelper.TABLE_SESSION_MESSAGE
                        + " as a left join "
                        + DBHelper.TABLE_CONTACTS
                        + " as b on (a.userId = b.userId and a.uStudentId = b.studentId) left join "
                        + DBHelper.TABLE_CHAT_MESSAGE
                        + " as c on a.msgId = c.msgId left join "
                        + "(select c.userId, c.uStudentId, count(case when c.isRead = 0 then c.isRead end)count from "
                        + DBHelper.TABLE_CHAT_MESSAGE
                        + " as c group by c.uStudentId) as d on (a.userId = d.userId and a.uStudentId = d.uStudentId ) where a.msgId != '' order by c.msgTime desc";
                Cursor cursor = db.queryBySql(DBHelper.TABLE_SESSION_MESSAGE,
                        sql);
                MsgContentItem item = null;
                if (cursor != null)
                {
                    if (cursor.moveToFirst())
                    {
                        do
                        {
                            try
                            {
                                item = new MsgContentItem();
                                item.id = (cursor.getString(cursor.getColumnIndex("id")));
                                item.userId = (cursor.getString(cursor.getColumnIndex("userId")));
                                item.type = (cursor.getString(cursor.getColumnIndex("msgType")));
                                item.time = (cursor.getString(cursor.getColumnIndex("msgTime")));
                                item.text = (cursor.getString(cursor.getColumnIndex("msgText")));
                                item.header = (cursor.getString(cursor.getColumnIndex("portraitPath")));
                                item.userName = (cursor.getString(cursor.getColumnIndex("name")));
                                item.userAppellation = (cursor.getString(cursor.getColumnIndex("appellation")));
                                item.unReadCount = (cursor.getInt(cursor.getColumnIndex("count")));
                                item.studentId = (cursor.getString(cursor.getColumnIndex("uStudentId")));
                                boolean add = true;
                                String to = (cursor.getString(cursor.getColumnIndex("studentId")));
                                item.groupId = (cursor.getString(cursor.getColumnIndex("groupId")));
                                if (!TextUtils.isEmpty(item.groupId))
                                {
                                    if (!item.groupId.equals(WXApplication.getInstance().groupId))
                                    {
                                        add = false;
                                    }
                                    else
                                    {
                                        String sqlStr = "select count(case when isRead = 0 then isRead end)count from "
                                                + DBHelper.TABLE_CHAT_MESSAGE
                                                + " where groupId = '"
                                                + item.groupId + "'";
                                        Cursor c = db.queryBySql(DBHelper.TABLE_CHAT_MESSAGE,
                                                sqlStr);
                                        if (null != c)
                                        {
                                            c.moveToFirst();
                                            item.unReadCount = c.getInt(0);
                                            c.close();
                                        }
                                    }
                                }
                                else
                                {
                                    if (TextUtils.isEmpty(item.userId))
                                    {
                                        add = false;
                                    }
                                    else
                                    {
                                        if (!LoginUtil.getStudentId(mContext)
                                                .equals(to))
                                        {
                                            add = false;
                                        }
                                    }
                                }
                                if (add)
                                {
                                    list.add(item);
                                }
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
            if (cur != null)
            {
                cur.close();
            }
            closeDB();
            return list;
        }
    }
}
