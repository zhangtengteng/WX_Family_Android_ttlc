package com.xweisoft.wx.family.service.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.xweisoft.wx.family.logic.model.DownloadItem;
import com.xweisoft.wx.family.util.FileHelper;
import com.xweisoft.wx.family.util.ListUtil;
import com.xweisoft.wx.family.util.LogX;

/**
 * DownloadManager 数据库数据封装类 
 * <功能详细描述>
 * 
 * @author  administrator
 * @version  [版本号, 2013-11-13]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class DownloadDBHelper
{
    /**
     * 日志标志
     */
    private static final String TAG = "===DownloadDBHelper===";
    
    /**
     * id
     */
    public static final String ID = "id";
    
    /**
     * mp3的编号
     */
    public static final String MP3_ID = "mp3Id";
    
    /**
     * MP3文件所在栏目id
     */
    static final String CATEGORY_ID = "categoryId";
    
    /**
     * MP3文件所在栏目标题
     */
    static final String CATEGORY_TITLE = "categoryTitle";
    
    /**
     * mp3的名称
     */
    public static final String MP3_NAME = "mp3Name";
    
    /**
     * 歌手名
     */
    static final String SINGER_NAME = "singerName";
    
    /**
     * mp3的下载路径
     */
    public static final String DOWNLOAD_PATH = "downloadPath";
    
    /**
     * mp3的大小
     */
    public static final String MP3_SIZE = "mp3Size";
    
    /**
     * mp3的保存路径
     */
    public static final String MP3_SAVE_PATH = "mp3SavePath";
    
    /**
     * mp3的专辑图片url
     */
    public static final String MP3_ALBUM_ICON_URL = "mp3AlbumUrl";
    
    /**
     * 保存从URL传来的SIZE
     */
    public static final String URL_SIZE = "urlSize";
    
    /**
     * mp3的下载状态
     */
    public static final String STATUS = "status";
    
    /**
     * 下载完成时间
     */
    public static final String DOWNLOADED_TIME = "downloadedTime";
    
    /**
     * 百分比除数
     */
    private static final int PERCENT_100 = 100;
    
    /**
     * 同步锁
     */
    private static Object synObj = new Object();
    
    /**
     * 数据库操作对象
     */
    private DBHelper db;
    
    /**
     * 数据库初始化
     * @param context 上下文
     */
    public void initDownloadDBHelper(final Context context)
    {
        if (db == null)
        {
            if (context == null)
            {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
            db = new DBHelper(context);
        }
    }
    
    /**
     * 关闭数据库
     */
    public void closeDB()
    {
        if (db != null)
        {
            db.closeDatabase();
        }
    }
    
    /**
     * 在初始化下载任务列表时，判断该任务的状态。适用于异常退出客户端情况 正在下载，等待下载，变暂停
     * 
     * @param state
     *            状态码
     * @return 状态码
     */
    private int changeExceptionState(int state)
    {
        switch (state)
        {
        // 更新状态
            case DownloadItem.STATUS_DOWNLOADING:
            case DownloadItem.STATUS_DOWNLOAD_WAITING:
                return DownloadItem.STATUS_PAUSE;
            default:
                return state;
        }
    }
    
    /**
     * 程序启动时，获取数据库中保存的任务信息(查询全部)
     * @param context 上下文
     * @param isAll 是否全部
     * @return 数据库中保存的任务信息
     */
    public ArrayList<DownloadItem> getAllTask(final Context context,
            boolean isAll)
    {
        // 判空操作
        initDownloadDBHelper(context);
        
        // 数据读取的同步操作
        synchronized (synObj)
        {
            ArrayList<DownloadItem> downloadItemList = new ArrayList<DownloadItem>();
            Cursor cursor = db.query(DBHelper.TABLE_MYDOWNLOAD,
                    null,
                    null,
                    null,
                    null);
            
            // 判空操作
            if (cursor == null)
            {
                return null;
            }
            if (cursor.getCount() > 0)
            {
                cursor.moveToFirst();
                
                DownloadItem item = null;
                do
                {
                    // 查询信息表构造DownLoadItem对象，实现数据封装
                    item = new DownloadItem();
                    item.id = cursor.getLong(cursor.getColumnIndex(ID));
                    item.mp3Id = cursor.getString(cursor.getColumnIndex(MP3_ID));
                    item.categoryId=cursor.getString(cursor.getColumnIndex(CATEGORY_ID));
                    item.categoryTitle=cursor.getString(cursor.getColumnIndex(CATEGORY_TITLE));
                    item.mp3Singer=cursor.getString(cursor.getColumnIndex(SINGER_NAME));
                    item.mp3Name = cursor.getString(cursor.getColumnIndex(MP3_NAME));
                    item.downloadUrl = cursor.getString(cursor.getColumnIndex(DOWNLOAD_PATH));
                    item.mp3Size = cursor.getLong(cursor.getColumnIndex(MP3_SIZE));
                    item.mp3SavePath = cursor.getString(cursor.getColumnIndex(MP3_SAVE_PATH));
                    item.status = changeExceptionState(cursor.getInt(cursor.getColumnIndex(STATUS)));
                    item.mp3AlbumUrl = cursor.getString(cursor.getColumnIndex(MP3_ALBUM_ICON_URL));
                    item.downloadedTime = cursor.getLong(cursor.getColumnIndex(DOWNLOADED_TIME));
                    // 初始化进度条,特殊状态直接置为百分百
                    long fileLength = FileHelper.getLocalFileSize(item.mp3SavePath);
                    
                    // 当本地文件大小等于总大小时，直接置为100%
                    if (fileLength != 0
                            && fileLength == item.mp3Size
                            && item.status != DownloadItem.STATUS_DOWNLOAD_FAILED)
                    {
                        item.downloadPercent = DownloadItem.FULL_PERCENT_VALUE;
                    }
                    else
                    {
                        item.downloadPercent = (int) (fileLength * PERCENT_100 / (item.mp3Size + 1));
                    }
                    // 加入下载列表队列
                    downloadItemList.add(item);
                } while (cursor.moveToNext());
            }
            // 关闭游标
            if (null != cursor)
            {
                cursor.close();
            }
            return downloadItemList;
        }
    }
    
    /**
     * 查询下载完成的应用记录
     * @param context 上下文
     * @param packageName 包名
     * @return 数据库中保存的任务信息
     */
    public ArrayList<DownloadItem> getDownloadedTask(final Context context,
            String packageName)
    {
        // 判空操作
        initDownloadDBHelper(context);
        
        // 数据读取的同步操作
        synchronized (synObj)
        {
            ArrayList<DownloadItem> downloadItemList = new ArrayList<DownloadItem>();
            StringBuffer sb = new StringBuffer();
            sb.append(STATUS)
                    .append("=")
                    .append(DownloadItem.STATUS_DOWNLOAD_FINISH);
            Cursor cursor = db.query(DBHelper.TABLE_MYDOWNLOAD, sb.toString());
            // 判空操作
            if (cursor == null)
            {
                return null;
            }
            if (cursor.getCount() > 0)
            {
                cursor.moveToFirst();
                
                DownloadItem item = null;
                do
                {
                    // 查询信息表构造DownLoadItem对象，实现数据封装
                    item = new DownloadItem();
                    item.id = cursor.getLong(cursor.getColumnIndex(ID));
                    item.mp3Id = cursor.getString(cursor.getColumnIndex(MP3_ID));
                    item.categoryId=cursor.getString(cursor.getColumnIndex(CATEGORY_ID));
                    item.categoryTitle=cursor.getString(cursor.getColumnIndex(CATEGORY_TITLE));
                    item.mp3Singer=cursor.getString(cursor.getColumnIndex(SINGER_NAME));
                    item.mp3Name = cursor.getString(cursor.getColumnIndex(MP3_NAME));
                    item.downloadUrl = cursor.getString(cursor.getColumnIndex(DOWNLOAD_PATH));
                    item.mp3Size = cursor.getLong(cursor.getColumnIndex(MP3_SIZE));
                    item.mp3SavePath = cursor.getString(cursor.getColumnIndex(MP3_SAVE_PATH));
                    item.status = changeExceptionState(cursor.getInt(cursor.getColumnIndex(STATUS)));
                    item.mp3AlbumUrl = cursor.getString(cursor.getColumnIndex(MP3_ALBUM_ICON_URL));
                    item.downloadedTime = cursor.getLong(cursor.getColumnIndex(DOWNLOADED_TIME));
                    // 加入下载列表队列
                    downloadItemList.add(item);
                } while (cursor.moveToNext());
            }
            // 关闭游标
            if (null != cursor)
            {
                cursor.close();
            }
            return downloadItemList;
        }
    }
    
    /**
     * 查询所有的应用记录
     * @param context 上下文
     * @return 数据库中保存的任务信息
     */
    public ArrayList<DownloadItem> getAllItems(final Context context)
    {
        // 判空操作
        initDownloadDBHelper(context);
        
        // 数据读取的同步操作
        synchronized (synObj)
        {
            ArrayList<DownloadItem> downloadItemList = new ArrayList<DownloadItem>();
            Cursor cursor = db.query(DBHelper.TABLE_MYDOWNLOAD, null);
            // 判空操作
            if (cursor == null)
            {
                return null;
            }
            if (cursor.getCount() > 0)
            {
                cursor.moveToFirst();
                
                DownloadItem item = null;
                do
                {
                    // 查询信息表构造DownLoadItem对象，实现数据封装
                    item = new DownloadItem();
                    item.id = cursor.getLong(cursor.getColumnIndex(ID));
                    item.mp3Id = cursor.getString(cursor.getColumnIndex(MP3_ID));
                    item.categoryId=cursor.getString(cursor.getColumnIndex(CATEGORY_ID));
                    item.categoryTitle=cursor.getString(cursor.getColumnIndex(CATEGORY_TITLE));
                    item.mp3Singer=cursor.getString(cursor.getColumnIndex(SINGER_NAME));
                    item.mp3Name = cursor.getString(cursor.getColumnIndex(MP3_NAME));
                    item.downloadUrl = cursor.getString(cursor.getColumnIndex(DOWNLOAD_PATH));
                    item.mp3Size = cursor.getLong(cursor.getColumnIndex(MP3_SIZE));
                    item.mp3SavePath = cursor.getString(cursor.getColumnIndex(MP3_SAVE_PATH));
                    item.status = changeExceptionState(cursor.getInt(cursor.getColumnIndex(STATUS)));
                    item.mp3AlbumUrl = cursor.getString(cursor.getColumnIndex(MP3_ALBUM_ICON_URL));
                    item.downloadedTime = cursor.getLong(cursor.getColumnIndex(DOWNLOADED_TIME));
                    // 初始化进度条,特殊状态直接置为百分百
                    long fileLength = FileHelper.getLocalFileSize(item.mp3SavePath);
                    
                    // 当本地文件大小等于总大小时，直接置为100%
                    if (fileLength != 0
                            && fileLength == item.mp3Size
                            && item.status != DownloadItem.STATUS_DOWNLOAD_FAILED)
                    {
                        //如果已下载长度和文件长度相同时，则设置状态为下载完成
                        item.status = DownloadItem.STATUS_DOWNLOAD_FINISH;
                        item.downloadPercent = DownloadItem.FULL_PERCENT_VALUE;
                    }
                    else
                    {
                        item.downloadPercent = (int) (fileLength * PERCENT_100 / (item.mp3Size + 1));
                    }
                    // 加入下载列表队列
                    downloadItemList.add(item);
                } while (cursor.moveToNext());
            }
            // 关闭游标
            if (null != cursor)
            {
                cursor.close();
            }
            return downloadItemList;
        }
    }
    
    /**
     * 根据MP3ID来判断该MP3是否正在下载
     * @param context 上下文
     * @return 数据库中保存的任务信息
     */
    public boolean isDownloadingMp3(final Context context, String mp3Id)
    {
        // 判空操作
        initDownloadDBHelper(context);
        
        // 数据读取的同步操作
        synchronized (synObj)
        {
            StringBuffer sb = new StringBuffer();
            sb.append(MP3_ID).append("=").append(mp3Id);
            sb.append(" and ");
            sb.append(STATUS)
                    .append("!=")
                    .append(DownloadItem.STATUS_DOWNLOAD_FINISH);
            Cursor cursor = db.query(DBHelper.TABLE_MYDOWNLOAD, sb.toString());
            // 判空操作
            if (cursor == null)
            {
                return false;
            }
            if (cursor.getCount() > 0)
            {
                return true;
            }
            // 关闭游标
            if (null != cursor)
            {
                cursor.close();
            }
        }
        return false;
    }
    
    /**
     * 根据MP3ID来判断该MP3是否已下载完毕
     * @param context 上下文
     * @return 数据库中保存的任务信息
     */
    public boolean isDownloadedMp3(final Context context, String mp3Id)
    {
        // 判空操作
        initDownloadDBHelper(context);
        
        // 数据读取的同步操作
        synchronized (synObj)
        {
            StringBuffer sb = new StringBuffer();
            sb.append(MP3_ID).append("=").append(mp3Id);
            sb.append(" and ");
            sb.append(STATUS)
                    .append("=")
                    .append(DownloadItem.STATUS_DOWNLOAD_FINISH);
            Cursor cursor = db.query(DBHelper.TABLE_MYDOWNLOAD, sb.toString());
            // 判空操作
            if (cursor == null)
            {
                return false;
            }
            if (cursor.getCount() > 0)
            {
                cursor.moveToFirst();
                String mp3SavePath = cursor.getString(cursor.getColumnIndex(MP3_SAVE_PATH));
                //先判断是否文件存在
                if (TextUtils.isEmpty(mp3SavePath))
                {
                    return false;
                }
                if (FileHelper.isFileExist(mp3SavePath))
                {
                    return true;
                }
            }
            // 关闭游标
            if (null != cursor)
            {
                cursor.close();
            }
        }
        return false;
    }
    
    /**
     * 查询已经下载完成的应用记录
     * @param context 上下文
     * @return 数据库中保存的任务信息
     */
    public ArrayList<DownloadItem> getDownloadedItems(final Context context)
    {
        // 判空操作
        initDownloadDBHelper(context);
        
        // 数据读取的同步操作
        synchronized (synObj)
        {
            ArrayList<DownloadItem> downloadItemList = new ArrayList<DownloadItem>();
            StringBuffer sb = new StringBuffer();
            sb.append(STATUS)
                    .append("=")
                    .append(DownloadItem.STATUS_DOWNLOAD_FINISH);
            Cursor cursor = db.query(DBHelper.TABLE_MYDOWNLOAD, sb.toString());
            // 判空操作
            if (cursor == null)
            {
                return null;
            }
            if (cursor.getCount() > 0)
            {
                cursor.moveToFirst();
                
                DownloadItem item = null;
                do
                {
                    // 查询信息表构造DownLoadItem对象，实现数据封装
                    item = new DownloadItem();
                    item.id = cursor.getLong(cursor.getColumnIndex(ID));
                    item.mp3Id = cursor.getString(cursor.getColumnIndex(MP3_ID));
                    item.categoryId=cursor.getString(cursor.getColumnIndex(CATEGORY_ID));
                    item.categoryTitle=cursor.getString(cursor.getColumnIndex(CATEGORY_TITLE));
                    item.mp3Singer=cursor.getString(cursor.getColumnIndex(SINGER_NAME));
                    item.mp3Name = cursor.getString(cursor.getColumnIndex(MP3_NAME));
                    item.downloadUrl = cursor.getString(cursor.getColumnIndex(DOWNLOAD_PATH));
                    item.mp3Size = cursor.getLong(cursor.getColumnIndex(MP3_SIZE));
                    item.mp3SavePath = cursor.getString(cursor.getColumnIndex(MP3_SAVE_PATH));
                    item.status = changeExceptionState(cursor.getInt(cursor.getColumnIndex(STATUS)));
                    item.mp3AlbumUrl = cursor.getString(cursor.getColumnIndex(MP3_ALBUM_ICON_URL));
                    item.downloadedTime = cursor.getLong(cursor.getColumnIndex(DOWNLOADED_TIME));
                    // 加入下载列表队列
                    downloadItemList.add(item);
                } while (cursor.moveToNext());
            }
            // 关闭游标
            if (null != cursor)
            {
                cursor.close();
            }
            return downloadItemList;
        }
    }
    
    /**
     * 程序启动时，获取数据库中保存的任务信息(查询不包括已经安装的任务信息)
     * @param context 上下文
     * @return 数据库中保存的任务信息
     */
    public ArrayList<DownloadItem> getAllTask(final Context context)
    {
        // 判空操作
        initDownloadDBHelper(context);
        
        // 数据读取的同步操作
        synchronized (synObj)
        {
            ArrayList<DownloadItem> downloadItemList = new ArrayList<DownloadItem>();
            StringBuffer sb = new StringBuffer();
            sb.append(STATUS)
                    .append("!=")
                    .append(DownloadItem.STATUS_DOWNLOAD_FINISH);
            
            Cursor cursor = db.query(DBHelper.TABLE_MYDOWNLOAD, sb.toString());
            
            // 判空操作
            if (cursor == null)
            {
                return null;
            }
            if (cursor.getCount() > 0)
            {
                cursor.moveToFirst();
                
                DownloadItem item = null;
                do
                {
                    // 查询信息表构造DownLoadItem对象，实现数据封装
                    item = new DownloadItem();
                    item.id = cursor.getLong(cursor.getColumnIndex(ID));
                    item.mp3Id = cursor.getString(cursor.getColumnIndex(MP3_ID));
                    item.categoryId=cursor.getString(cursor.getColumnIndex(CATEGORY_ID));
                    item.categoryTitle=cursor.getString(cursor.getColumnIndex(CATEGORY_TITLE));
                    item.mp3Singer=cursor.getString(cursor.getColumnIndex(SINGER_NAME));
                    item.mp3Name = cursor.getString(cursor.getColumnIndex(MP3_NAME));
                    item.downloadUrl = cursor.getString(cursor.getColumnIndex(DOWNLOAD_PATH));
                    item.mp3Size = cursor.getLong(cursor.getColumnIndex(MP3_SIZE));
                    item.mp3SavePath = cursor.getString(cursor.getColumnIndex(MP3_SAVE_PATH));
                    item.status = changeExceptionState(cursor.getInt(cursor.getColumnIndex(STATUS)));
                    item.mp3AlbumUrl = cursor.getString(cursor.getColumnIndex(MP3_ALBUM_ICON_URL));
                    item.downloadedTime = cursor.getLong(cursor.getColumnIndex(DOWNLOADED_TIME));
                    // 初始化进度条,特殊状态直接置为百分百
                    long fileLength = FileHelper.getLocalFileSize(item.mp3SavePath);
                    
                    // 当本地文件大小等于总大小时，直接置为100%
                    if (fileLength != 0
                            && fileLength == item.mp3Size
                            && item.status != DownloadItem.STATUS_DOWNLOAD_FAILED)
                    {
                        item.status = DownloadItem.STATUS_DOWNLOAD_FINISH;
                        item.downloadPercent = DownloadItem.FULL_PERCENT_VALUE;
                    }
                    else
                    {
                        if (fileLength == 0)
                        {
                            //没有开始下载，将状态设置为暂停，点击重新下载
                            item.status = DownloadItem.STATUS_PAUSE;
                        }
                        item.downloadPercent = (int) (fileLength * PERCENT_100 / (item.mp3Size + 1));
                    }
                    // 加入下载列表队列
                    downloadItemList.add(item);
                } while (cursor.moveToNext());
            }
            // 关闭游标
            if (null != cursor)
            {
                cursor.close();
            }
            return downloadItemList;
        }
    }
    
    /**
     * 从数据库中删除数据
     * @param context 上下文
     * @param item DownLoadItem对象
     */
    public void removeTaskFromDB(final Context context, DownloadItem item)
    {
        if (item == null)
        {
            return;
        }
        
        initDownloadDBHelper(context);
        
        synchronized (synObj)
        {
            // 删除操作
            db.delete(DBHelper.TABLE_MYDOWNLOAD,
                    new String[] { ID },
                    new String[] { item.id + "" });
        }
    }
    
    /**
     * 向数据库中插入一条下载任务记录
     * @param context 上下文
     * @param item 下载对象
     */
    public void addToDB(final Context context, DownloadItem item)
    {
        if (item == null)
        {
            return;
        }
        
        initDownloadDBHelper(context);
        
        synchronized (synObj)
        {
            StringBuffer sb = new StringBuffer();
            sb.append(MP3_ID).append("='").append(item.mp3Id).append("'");
            Cursor cursor = null;
            try
            {
                cursor = db.query(DBHelper.TABLE_MYDOWNLOAD, sb.toString());
                // 判空操作
                if (cursor != null)
                {
                    // 加入信息表
                    ContentValues values = new ContentValues();
                    values.put(MP3_ID, item.mp3Id);
                    values.put(CATEGORY_ID, item.categoryId);
                    values.put(CATEGORY_TITLE, item.categoryTitle);
                    values.put(SINGER_NAME, item.mp3Singer);
                    values.put(MP3_NAME, item.mp3Name);
                    values.put(DOWNLOAD_PATH, item.downloadUrl);
                    values.put(MP3_SIZE, item.mp3Size);
                    values.put(MP3_SAVE_PATH, item.mp3SavePath);
                    values.put(STATUS, item.status);
                    values.put(MP3_ALBUM_ICON_URL, item.mp3AlbumUrl);
                    values.put(DOWNLOADED_TIME, item.downloadedTime);
                    
                    if (cursor.getCount() > 0)
                    {
                        cursor.moveToFirst();
                        int id = cursor.getInt(cursor.getColumnIndex(ID));
                        db.update(DBHelper.TABLE_MYDOWNLOAD,
                                values,
                                new String[] { ID },
                                new String[] { id + "" });
                        item.id = Long.valueOf(id + "");
                    }
                    else
                    {
                        item.id = db.insert(DBHelper.TABLE_MYDOWNLOAD, values);
                    }
                }
            }
            catch (Exception e)
            {
                item.id = 0l;
                LogX.getInstance().e(TAG, e.toString());
            }
            finally
            {
                if (null != cursor)
                {
                    cursor.close();
                }
            }
        }
    }
    
    /**
     * 更新数据
     * @param context 上下文
     * @param item DownLoadItem对象
     */
    public void updateTaskToDB(final Context context, DownloadItem item)
    {
        if (item == null)
        {
            return;
        }
        
        initDownloadDBHelper(context);
        
        synchronized (synObj)
        {
            ContentValues values = new ContentValues();
            values.put(MP3_ID, item.mp3Id);
            values.put(CATEGORY_ID, item.categoryId);
            values.put(CATEGORY_TITLE, item.categoryTitle);
            values.put(SINGER_NAME, item.mp3Singer);
            values.put(MP3_NAME, item.mp3Name);
            values.put(DOWNLOAD_PATH, item.downloadUrl);
            values.put(MP3_SIZE, item.mp3Size);
            values.put(MP3_SAVE_PATH, item.mp3SavePath);
            values.put(STATUS, item.status);
            values.put(MP3_ALBUM_ICON_URL, item.mp3AlbumUrl);
            values.put(DOWNLOADED_TIME, item.downloadedTime);
            if (item.id == 0)
            {
                db.update(DBHelper.TABLE_MYDOWNLOAD,
                        values,
                        new String[] { ID },
                        new String[] { item.id + "" });
            }
            else
            {
                db.update(DBHelper.TABLE_MYDOWNLOAD,
                        values,
                        new String[] { MP3_ID },
                        new String[] { item.mp3Id });
            }
        }
    }
    
    /**
     * 更新数据
     * @param context 上下文
     * @param item DownLoadItem对象
     */
    public void updateTaskToDBApp(final Context context, DownloadItem item)
    {
        if (item == null)
        {
            return;
        }
        
        initDownloadDBHelper(context);
        
        synchronized (synObj)
        {
            ContentValues values = new ContentValues();
            values.put(STATUS, item.status);
            //            db.update(DBHelper.TABLE_MYDOWNLOAD, values, new String[] { STATUS,
            //                    PACKAGENAME }, new String[] { item.installParam,
            //                    item.packageName });
        }
    }
    
    /**
     * 更新数据为暂停状态
     * @param context 上下文
     * @param item DownLoadItem对象
     */
    public void updateTaskToPaused(final Context context, DownloadItem item)
    {
        if (item == null)
        {
            return;
        }
        
        initDownloadDBHelper(context);
        
        synchronized (synObj)
        {
            ContentValues values = new ContentValues();
            values.put(STATUS, item.status);
            db.update(DBHelper.TABLE_MYDOWNLOAD,
                    values,
                    new String[] { MP3_ID },
                    new String[] { item.mp3Id });
        }
    }
    
    /**
     * 根据id更新记录<BR>
     * @param context 上下文
     * @param values 更新数据值
     * @param id 更新的id
     */
    public void updateDownLoadTable(final Context context,
            ContentValues values, String id)
    {
        initDownloadDBHelper(context);
        synchronized (synObj)
        {
            db.update(DBHelper.TABLE_MYDOWNLOAD,
                    values,
                    new String[] { ID },
                    new String[] { id + "" });
        }
        
    }
    
    /**
    * 根据MP3ID删除数据库中download表中的一条或者多条记录
    * [功能详细描述]
    * @param context 上下文
    * @param mp3IdList 删除的id列表
    */
    public void deleteItems(Context context, ArrayList<String> mp3IdList)
    {
        if (ListUtil.isEmpty(mp3IdList))
        {
            return;
        }
        StringBuffer mp3IdBuffer = new StringBuffer();
        int size = mp3IdList.size();
        for (int i = 0; i < size; i++)
        {
            String id = mp3IdList.get(i);
            mp3IdBuffer.append(id);
            if (i < size - 1)
            {
                mp3IdBuffer.append(",");
            }
        }
        initDownloadDBHelper(context);
        synchronized (synObj)
        {
            StringBuffer sb = new StringBuffer();
            sb.append(MP3_ID)
                    .append(" in(")
                    .append(mp3IdBuffer.toString())
                    .append(")");
            db.delete(DBHelper.TABLE_MYDOWNLOAD, sb.toString());
        }
    }
    
}