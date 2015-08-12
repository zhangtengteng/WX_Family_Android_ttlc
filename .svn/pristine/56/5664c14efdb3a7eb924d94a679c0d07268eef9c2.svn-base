package com.xweisoft.wx.family.service.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xweisoft.wx.family.util.LogX;

/**
 * <一句话功能简述>
 * 本类主要是对数据库进行的一系列操作
 * 
 * 公共数据库操作父类，对应的各个子类操作需要继承它
 * 
 * @author  administrator
 * @version  [版本号, 2013-5-6]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class DBHelper extends SQLiteOpenHelper
{
    
    /**
     * 存储我的下载的详细信息的索引数据表
     */
    public static final String TABLE_MYDOWNLOAD = "table_mydownLoad";
    
    /**
     * 存储推送消息数据表
     */
    public static final String TABLE_NOTIFICATION_MESSAGE = "table_notification_message";
    
    /**
     * 存储通讯录数据表
     */
    public static final String TABLE_CONTACTS = "table_contacts";
    
    /**
     * 存储聊天消息数据表
     */
    public static final String TABLE_CHAT_MESSAGE = "table_chat_message";
    
    /**
     * 存储聊天消息数据表
     */
    public static final String TABLE_SESSION_MESSAGE = "table_session_message";
    
    /**
     * 群组数据表
     */
    public static final String TABLE_GROUP_MESSAGE = "table_group_message";
    
    /**
     * 群组成员数据表
     */
    public static final String TABLE_GROUP_MEMBER_MESSAGE = "table_group_member_message";
    
    /**
     * 打印日志时日志信息中的标识符
     */
    private static final String TAG = "DBHelper";
    
    /**
     * 数据库的版本号，默认情况下是1
     */
    private static final int DATABASE_VERSION = 1;
    
    /**
     * 默认根据id降序排列
     */
    private static final String ORDERBY_DESC = " id desc";
    
    /**
     * 用来操作的数据库的实例
     */
    private SQLiteDatabase db = null;
    
    /**
     * 构造函数
     * @param context    UI环境上下文
     */
    public DBHelper(Context context, String dbName)
    {
        super(context, dbName + ".db", null, DATABASE_VERSION);
    }
    
    /**
     * <默认构造函数>
     * 创建公用数据库 wx_common_database
     */
    public DBHelper(Context context)
    {
        super(context, "wx_common_database.db", null, DATABASE_VERSION);
    }
    
    /**
     * 数据库在建立的时候执行的操作，主要是新建系统所需的数据表
     * @param db 数据库对象
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.beginTransaction();
        try
        {
            db.execSQL("create table if not exists " + TABLE_MYDOWNLOAD + "("
                    + DownloadDBHelper.ID + " Integer primary key," //id
                    + DownloadDBHelper.MP3_ID + " text," //mp3 id
                    + DownloadDBHelper.CATEGORY_ID + " text," //mp3 栏目id
                    + DownloadDBHelper.CATEGORY_TITLE + " text," //mp3 栏目标题
                    + DownloadDBHelper.SINGER_NAME + " text," //mp3 歌唱者
                    + DownloadDBHelper.MP3_NAME + " text," //mp3名字
                    + DownloadDBHelper.DOWNLOAD_PATH + " text not null," //下载地址
                    + DownloadDBHelper.MP3_SIZE + " long," //mp3实际大小
                    + DownloadDBHelper.MP3_SAVE_PATH + " text," //mp3保存路径
                    + DownloadDBHelper.MP3_ALBUM_ICON_URL + " text," //专辑图标url
                    + DownloadDBHelper.STATUS + " Integer," //下载状态
                    + DownloadDBHelper.DOWNLOADED_TIME + " long"//下载完成时间
                    + ")"
            
            );
            //创建通知消息表
            db.execSQL("create table if not exists "
                    + TABLE_NOTIFICATION_MESSAGE + "(id Integer primary key," //id" 
                    + "msgId text default ''," //其他用户id
                    + "msgType text default '',"//通知类型 
                    + "msgTime text default '',"//通知时间
                    + "msgTitle text default '',"//通知标题 
                    + "msgText text default '',"//通知文本信息
                    + "resStr text default '',"//通知资源集合 已json格式存储
                    + "audioPath text default '',"//音频本地地址
                    + "videoPath text default '',"//视频本地地址
                    + "isRead text default '',"//是否已读 0未读 1 已读
                    + "msgFrom text default '')"//消息类型 0接受 1发送 标记是自己发送的消息还是接收到的消息
            );
            //创建通讯录表
            db.execSQL("create table if not exists " + TABLE_CONTACTS
                    + "(id Integer primary key," //id" 
                    + "userId text default ''," //用户id
                    + "classinfoId text default '',"//
                    + "sex text default ''," + "studentId text default '',"//孩子id 
                    + "phone text default '',"//电话
                    + "name text default '',"//名称
                    + "isFriend text default '',"//是否好友 0 不是 1是
                    + "role text default '',"//角色 1 教师 2 家长
                    + "appellation text default '',"//称谓
                    + "contactId text default '',"//好友id（删除时使用）
                    + "flag text default '1',"//类型标识 1 仅通讯录存在 2 仅好友存在  3 通讯录和好友都存在
                    + "portraitPath text default '')"//头像
            );
            //创建聊天消息表
            db.execSQL("create table if not exists " + TABLE_CHAT_MESSAGE
                    + "(id Integer primary key," //id
                    + "userId text default '',"//对话的用户userId
                    + "isFrom text default '',"//0 接受的消息 1 发送的消息
                    + "isRead text default '',"//0 未读 1 已读
                    + "msgId text default ''," //消息id
                    + "msgType text default '',"//消息类型 
                    + "msgTime text default '',"//消息时间
                    + "uStudentId text default '',"//对话用户学生id
                    + "studentId text default '',"//用户学生id
                    + "msgText text default '',"//消息文本（图片、视频、音频）地址、GPS
                    + "msgPath text default '',"//消息图片、视频、音频本地地址
                    + "msgThumbText text default '',"//消息图片、视频、音频服务器地址
                    + "msgThumbPath text default '',"//消息资源缩略图
                    + "groupId text default '',"//消息资源缩略图
                    + "msgDuration text default '')"//消息资源 时长
            );
            //创建回话窗口聊天消息表
            db.execSQL("create table if not exists " + TABLE_SESSION_MESSAGE
                    + "(id Integer primary key," //id
                    + "userId text default '',"//用户id
                    + "uStudentId text default '',"//对话用户学生id
                    + "studentId text default '',"//用户学生id
                    + "groupId text default '',"//用户学生id
                    + "msgId text default '')" //消息id
            );
            db.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            LogX.getInstance().e(TAG, e.getMessage());
        }
        finally
        {
            db.endTransaction();
        }
    }
    
    /**
     * 数据库在变动的时候执行的操作，主要是重新建立数据库的时候
     * @param db 数据库对象
     * @param oldVersion 旧版本号
     * @param newVersion 新版本号
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }
    
    /**
     *  数据库表机构发生变化时调用
     * @param db 数据库操作对象
     * @param tableName 表名
     * @param colmnDef 新表列限制条件 如:timestamp text,temp text
     * @param fromColmns 需要复制表的字段 
     * @param toColmns     插入新表中对应的字段
     */
    protected void onUpgradeColmn(SQLiteDatabase db, String tableName,
            String colmnDef, String fromColmns, String toColmns)
    {
        db.execSQL("create table if not exists table_test_temp as select * from "
                + tableName);
        db.execSQL("drop table if exists " + tableName);
        
        db.execSQL("create table if not exists " + tableName + "(" + colmnDef
                + ")");
        db.execSQL("insert into " + tableName + " (" + toColmns + ") select "
                + fromColmns + " from table_test_temp");
        db.execSQL("drop table if exists " + "table_test_temp");
    }
    
    /**
     * 插入数据
     * 
     * @param tableName 要插入的表格的名称
     * @param values 要插入的数据由数据名称和数据值组成的键值对
     * @return 插入记录id
     */
    public long insert(String tableName, ContentValues values)
    {
        db = this.getWritableDatabase();
        
        db.beginTransaction();
        
        long id = -1;
        try
        {
            id = db.insert(tableName, null, values);
            db.setTransactionSuccessful();
            LogX.getInstance()
                    .d(TAG, tableName + " ::Insert: Insert Success!!");
        }
        catch (Exception e)
        {
            LogX.getInstance().d(TAG,
                    tableName + " ::Insert: Insert Error!! " + e.toString());
            return -1;
        }
        finally
        {
            db.endTransaction();
            close(db);
        }
        return id;
    }
    
    /**
     * 对数据库中数据的删除操作
     * 
     * @param tableName 删除操作中要操作的数据表的名称
     * @param whereArgs 要删除的数据中提供的条件参数的名称
     * @param whereArgsValues 要删除的数据中提供的条件参数的值
     */
    public void delete(String tableName, String[] whereArgs,
            String[] whereArgsValues)
    {
        db = this.getWritableDatabase();
        
        db.beginTransaction();
        try
        {
            //当传入的参数为空时，表示操作所有的记录
            if (whereArgs == null)
            {
                db.delete(tableName, null, null);
            }
            else
            {
                //当传入的参数为一个时
                if (whereArgs.length == 1)
                {
                    //当传入的参数和参数值分别为一个时
                    if (whereArgsValues.length == 1)
                    {
                        db.delete(tableName,
                                whereArgs[0] + " = ?",
                                whereArgsValues);
                    }
                    //当传入的参数为一个，参数值为多个时
                    else
                    {
                        db.execSQL(delSql(tableName,
                                createSQL(whereArgs,
                                        whereArgsValues,
                                        whereArgsValues.length)));
                    }
                }
                //当传入的参数和参数值分别为多个时，并且参数和参数值是一一对应的
                else
                {
                    db.execSQL(delSql(tableName,
                            createSQL(whereArgs,
                                    whereArgsValues,
                                    whereArgs.length)));
                }
            }
            db.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            LogX.getInstance().e(TAG, e.getMessage());
        }
        finally
        {
            db.endTransaction();
            close(db);
        }
    }
    
    /**
     * 对数据库中数据的删除操作
     * 
     * @param tableName 删除操作中要操作的数据表的名称
     * @param selection 删除的条件
     */
    public void delete(String tableName, String selection)
    {
        db = this.getWritableDatabase();
        
        db.beginTransaction();
        try
        {
            
            db.execSQL(delSql(tableName, selection));
            db.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            LogX.getInstance().e(TAG, e.getMessage());
        }
        finally
        {
            db.endTransaction();
            close(db);
        }
    }
    
    /**
     * 将用户提供的参数拼接成一条完整的删除数据库的SQL语句
     * 
     * @param tableName 要操作的表的名称
     * @param whereArgs 删除数据的条件参数
     * @param whereArgsValues 删除数据的条件参数的值
     * @param num 提供的条件中参数的个数
     * @return 拼接完成后的删除SQL语句
     */
    private String createSQL(String[] whereArgs, String[] whereArgsValues,
            int length)
    {
        StringBuffer sql = new StringBuffer(" ");
        //当传入的参数为一个时
        if (whereArgs.length == 1)
        {
            for (int i = 0; i < length; i++)
            {
                sql.append(whereArgs[0] + " = '" + whereArgsValues[i] + "'");
                if (i < length - 1)
                {
                    sql.append(" or ");
                }
            }
        }
        //当传入的参数和参数值分别为多个时，并且参数和参数值是一一对应的
        else
        {
            for (int i = 0; i < length; i++)
            {
                sql.append(whereArgs[i] + " = '" + whereArgsValues[i] + "'");
                if (i < length - 1)
                {
                    sql.append(" and ");
                }
            }
        }
        return sql.toString();
    }
    
    /**
     * 生成删除数据的sql语句
     * 
     * @param tableName 要操作的数据表的名称
     * @param str_sql where语句部分
     * @return    
     */
    private String delSql(String tableName, String strSql)
    {
        return new StringBuffer("delete from " + tableName + " where " + strSql).toString();
    }
    
    /**
     * 对数据进行的查询操作
     * 
     * @param tableName 需要操作的表名
     * @param selection 查询数据的条件语句
     * @param orderBy 排序规则
     * @return 查找的数据集的指针
     * 修改人 郭安成
     * 参数orderBy没有起作用
     */
    public Cursor query(String tableName, String selection, String orderBy)
    {
        Cursor cursor = null;
        //        db = this.getReadableDatabase();
        db = this.getWritableDatabase();
        db.beginTransaction();
        try
        {
            //当传入的参数为空时，表示操作所有的记录
            //            cursor = db.query(tableName,
            //                    null,
            //                    selection,
            //                    null,
            //                    null,
            //                    null,
            //                    ORDERBY_DESC);
            cursor = db.query(tableName,
                    null,
                    selection,
                    null,
                    null,
                    null,
                    orderBy);
            db.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            LogX.getInstance().e(TAG, e.getMessage());
        }
        finally
        {
            db.endTransaction();
            
        }
        return cursor;
    }
    
    /**
     * 对数据进行的查询操作
     * 
     * @param tableName 需要操作的表名
     * @return 查找的数据集的指针
     * @param selection 查询数据的条件语句
     */
    public Cursor query(String tableName, String selection)
    {
        Cursor cursor = null;
        //        db = this.getReadableDatabase();
        db = this.getWritableDatabase();
        db.beginTransaction();
        try
        {
            //当传入的参数为空时，表示操作所有的记录
            cursor = db.query(tableName,
                    null,
                    selection,
                    null,
                    null,
                    null,
                    ORDERBY_DESC);
            db.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            LogX.getInstance().e(TAG, e.getMessage());
        }
        finally
        {
            db.endTransaction();
            
        }
        return cursor;
    }
    
    /** <一句话功能简述>
     * 对数据进行的查询操作，需要完全的sql语句
     * <功能详细描述>
     * @param tableName  需要操作的表名
     * @param sql     sql语句
     * @return [参数说明]   查找的数据集的指针
     * 
     * @return Cursor [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Cursor queryBySql(String tableName, String sql)
    {
        Cursor cursor = null;
        //        db = this.getReadableDatabase();
        db = this.getWritableDatabase();
        db.beginTransaction();
        try
        {
            
            cursor = db.rawQuery(sql, null);
            db.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            LogX.getInstance().e(TAG, e.getMessage());
        }
        finally
        {
            db.endTransaction();
            
        }
        return cursor;
    }
    
    /** <一句话功能简述>
     * 对数据进行操作，需要完全的sql语句
     * <功能详细描述>
     * @param sql     sql语句
     * @return [参数说明]   查找的数据集的指针
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void execBySql(String sql)
    {
        db = this.getWritableDatabase();
        db.beginTransaction();
        try
        {
            db.execSQL(sql);
            db.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            LogX.getInstance().e(TAG, e.getMessage());
        }
        finally
        {
            db.endTransaction();
            
        }
    }
    
    /**
     * 对数据进行的查询操作
     * 
     * @param tableName 需要操作的表名
     * @param whereArgs 要查询的数据中提供的条件参数的名称
     * @param whereArgsValues 要查询的数据中提供的条件参数的值
     * @param column 控制哪些字段返回结果（传null是返回所有字段的结果集）
     * @param orderBy 是否对某一字段进行排序（传null不进行排序）
     * @return 查找的数据集的指针
     */
    public Cursor query(String tableName, String[] whereArgs,
            String[] whereArgsValues, String[] column, String orderBy)
    {
        Cursor cursor = null;
        db = this.getReadableDatabase();
        
        db.beginTransaction();
        try
        {
            //当传入的参数为空时，表示操作所有的记录
            if (whereArgs == null)
            {
                cursor = db.query(tableName,
                        column,
                        null,
                        null,
                        null,
                        null,
                        ORDERBY_DESC);
            }
            else
            {
                //当传入的参数为一个时
                if (whereArgs.length == 1)
                {
                    //当传入的参数和参数值分别为一个时
                    if (whereArgsValues.length == 1)
                    {
                        cursor = db.query(tableName,
                                column,
                                whereArgs[0] + "= ?",
                                whereArgsValues,
                                null,
                                null,
                                ORDERBY_DESC);
                    }
                    //当传入的参数为一个，参数值为多个时
                    else
                    {
                        cursor = db.query(tableName,
                                column,
                                createSQL(whereArgs,
                                        whereArgsValues,
                                        whereArgsValues.length),
                                null,
                                null,
                                null,
                                ORDERBY_DESC);
                    }
                }
                //当传入的参数和参数值分别为多个时，并且参数和参数值是一一对应的
                else
                {
                    cursor = db.query(tableName,
                            column,
                            createSQL(whereArgs,
                                    whereArgsValues,
                                    whereArgs.length),
                            null,
                            null,
                            null,
                            ORDERBY_DESC);
                }
            }
            db.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            LogX.getInstance().e(TAG, e.getMessage());
        }
        finally
        {
            db.endTransaction();
            
        }
        return cursor;
    }
    
    /**
     * 对数据的更新操作
     * 
     * @param tableName 是所对应的操作表
     * @param values 需要更新的数据名称和值组成的键值对
     * @param whereArgs 要更新的数据集的条件参数
     * @param whereArgsValues 要更新的数据集的条件参数的值
     */
    public void update(String tableName, ContentValues values,
            String[] whereArgs, String[] whereArgsValues)
    {
        db = this.getWritableDatabase();
        
        db.beginTransaction();
        try
        {
            if (whereArgs == null)
            {
                db.update(tableName, values, null, null);
            }
            else
            {
                //当传入的参数为一个时
                if (whereArgs.length == 1)
                {
                    //当传入的参数和参数值分别为一个时
                    if (whereArgsValues.length == 1)
                    {
                        db.update(tableName, values, whereArgs[0] + "='"
                                + whereArgsValues[0] + "'", null);
                    }
                    //当传入的参数为一个，参数值为多个时
                    else
                    {
                        db.update(tableName,
                                values,
                                createSQL(whereArgs,
                                        whereArgsValues,
                                        whereArgsValues.length),
                                null);
                    }
                }
                //当传入的参数和参数值分别为多个时，并且参数和参数值是一一对应的
                else
                {
                    db.update(tableName,
                            values,
                            createSQL(whereArgs,
                                    whereArgsValues,
                                    whereArgs.length),
                            null);
                }
            }
            db.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            LogX.getInstance().e(TAG, e.toString());
        }
        finally
        {
            db.endTransaction();
            close(db);
        }
    }
    
    /**
     * 关闭所有链接中的数据库
     */
    public void closeDatabase()
    {
        try
        {
            close();
        }
        catch (Exception e)
        {
            LogX.getInstance().e(TAG, e.getMessage());
        }
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
}
