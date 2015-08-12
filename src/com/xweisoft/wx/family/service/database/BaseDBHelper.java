package com.xweisoft.wx.family.service.database;


import android.content.Context;

/**
 * <一句话功能简述>
 * 数据库操作的基础类
 * 
 * @author  administrator
 * @version  [版本号, 2013-5-6]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public abstract class BaseDBHelper
{
    protected Context mContext;
    
    protected String dbName;
    
    /**
     * 数据库操作对象
     */
    protected DBHelper db;
    
    /**
     * 构造器
     * @param context 上下文
     */
    protected BaseDBHelper(Context context, String dbName)
    {
        mContext = context;
        this.dbName = dbName;
    }
    
    /**
     * 构造器
     * @param context 上下文
     */
    protected BaseDBHelper(Context context)
    {
        mContext = context;
    }
    
    /**
     * 数据库初始化
     */
    protected void initDBHelper()
    {
        if (db == null)
        {
            if (mContext == null)
            {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
            if (null != dbName)
            {
                db = new DBHelper(mContext, dbName);
            }else {
                db = new DBHelper(mContext);
            }
        }
    }

    /**
     * 关闭数据库
     */
    protected void closeDB()
    {
        
        if (db != null)
        {
            db.closeDatabase();
        }
    }
}
