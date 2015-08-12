package com.xweisoft.wx.family.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import com.xweisoft.wx.family.WXApplication;
import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.logic.global.GlobalConstant;
import com.xweisoft.wx.family.logic.model.DownloadItem;
import com.xweisoft.wx.family.service.exception.NotEnouchSpaceException;
import com.xweisoft.wx.family.service.exception.SDNotEnouchSpaceException;
import com.xweisoft.wx.family.service.exception.SDUnavailableException;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  yangchao
 * @version  [版本号, 2013-10-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class FileHelper
{
    /**
     * 当出错的时候返回的字节数为-1
     */
    public static final int ERROR = -1;
    
    /**
     * 当没有写入任何数据的时候返回0
     */
    public static final int NONE = 0;
    
    /**
     * 文件的类型是资源文件
     */
    public static final int RESFILE = 0;
    
    /**
     * 文件的类型是下载的文件
     */
    public static final int DOWNLOADFILE = 1;
    
    /**
     * 文件的类型是widget的解压文件
     */
    public static final int INSTALLFILE = 2;
    
    /**
     * 文件的类型是系统的日志
     */
    public static final int SYSTEMLOG = 3;
    
    /**
     * 文件的类型是客户端升级包
     */
    public static final int UPGRADE = 4;
    
    /**
     * 日志标识
     */
    private static final String TAG = "FileHelper";
    
    /**
     * 该变量是从sd卡读取文件时默认的字符缓冲区的大小
     */
    private static final int MAX_LENTH = 1024;
    
    /**
     * 文件的存放路径
     */
    private static String filePath = null;
    
    /**
     * 在程序第一次安装的时候就在sd卡的主目录下新建一个系统所需的目录
     * 
     * @return 创建目录是否成功 changed by shaoqing.huang 2011-03-10创建一个存储WGT应用的目录
     */
    public static boolean makeDir()
    {
        boolean isComplete = false;
        
        // 创建下载目录
        File file = new File(WXApplication.getInstance()
                .getDownloadSavePath());
        if (!isFileExist(file))
        {
            isComplete = file.mkdirs();
        }
        
        // 创建安装目录
        file = new File(GlobalConstant.INSTALL_PATH_SD);
        if (!isFileExist(file))
        {
            isComplete = file.mkdirs();
        }
        
        // 创建日志目录
        file = new File(GlobalConstant.LOG_PATH_SD);
        if (!isFileExist(file))
        {
            isComplete = file.mkdirs();
        }
        
        return isComplete;
    }
    
    /**
     * 选择文件的存储路径
     * 
     * @param context 上下文
     * @param fileSize 要存的文件的大小
     * @param fileSpecies 创建的文件种类，是资源文件，是下载的文件还是解压的文件
     * @param downloadItem 下载对象
     * @throws NotEnouchSpaceException  手机和SD卡存储空间均不足的异常
     * @throws SDUnavailableException SD卡不可用的异常
     * @throws SDNotEnouchSpaceException SD卡空间不足的异常 
     * @return 拼接好的文件的存储路径
     */
    public static String selectFileSavaPath(final Context context,
            long fileSize, int fileSpecies, DownloadItem downloadItem)
            throws NotEnouchSpaceException, SDUnavailableException,
            SDNotEnouchSpaceException
    {
        switch (fileSpecies)
        {
        // 文件的类型是下载的文件
            case DOWNLOADFILE:
                if (!Environment.getExternalStorageState()
                        .equals(Environment.MEDIA_MOUNTED))
                {
                    throw new SDUnavailableException(context.getResources()
                            .getString(R.string.sdcard_not_enough_space));
                }
                else if (!(fileSize <= getFreeSD()))
                {
                    throw new SDNotEnouchSpaceException(context.getResources()
                            .getString(R.string.sdcard_not_enough_space));
                }
                else
                {
                    filePath = WXApplication.getInstance()
                            .getDownloadSavePath();
                }
                break;
            default:
                break;
        }
        return filePath;
    }
    
    /**
     * 创建File文件
     * 
     * @param path  资源文件路径
     * @param fileName 文件名
     * @throws SDNotEnouchSpaceException sd控件不足异常
     * @return 生成的文件
     */
    public static File createDownloadFile(String path, String fileName)
            throws SDNotEnouchSpaceException
    {
        return createFile(path + fileName);
    }
    
    /**
     * 通过提供的文件名在默认路径下生成文件
     * 
     * @param fileName 文件的名称
     * @throws SDNotEnouchSpaceException sd控件不足异常
     * @return 生成的文件
     */
    private static File createFile(String fileName)
            throws SDNotEnouchSpaceException
    {
        File file = new File(fileName);
        if (!isFileExist(file))
        {
            try
            {
                file.createNewFile();
            }
            catch (IOException e)
            {
                throw new SDNotEnouchSpaceException(e.getMessage());
            }
        }
        return file;
    }
    
    /**
     * 将从下载管理那里获取来的数据流写入文件中
     * 
     * @param context 上下文
     * @param io 从下载管理那里获取来的io流
     * @param fileName 需要存储的文件的路径和名称
     * @throws SDUnavailableException sd卡不可用
     * @throws SDNotEnouchSpaceException sd卡控件不足
     * @return 总共存储成功的字节数
     */
    public static int writeFile(final Context context, byte[] io,
            String fileName) throws SDUnavailableException,
            SDNotEnouchSpaceException
    {
        int length = NONE;
        if (io != null)
        {
            RandomAccessFile file = null;
            try
            {
                file = new RandomAccessFile(createFile(fileName), "rw");
                file.seek(file.length());
                file.write(io);
            }
            catch (IOException e)
            {
                LogX.getInstance().e(TAG, "::writeFile: fail");
                checkSD(context, io);
                // 如果出现异常，返回的字节数为-1，表示出现了异常，没有写入成功
                return ERROR;
            }
            finally
            {
                try
                {
                    if (null != file)
                    {
                        file.close();
                    }
                }
                catch (IOException e)
                {
                    LogX.getInstance().e(TAG, "::writeFile: " + e.getMessage());
                }
            }
            length = io.length;
        }
        return length;
    }
    
    /**
     * 将从网络获取来的数据流写入文件中
     * 
     * @param context 上下文
     * @param file 文件对象
     * @param io 字节流
     * @throws SDUnavailableException sd卡不可用
     * @throws SDNotEnouchSpaceException sd卡控件不足
     * @return 总共存储成功的字节数
     */
    public static int writeFile(final Context context, RandomAccessFile file,
            byte[] io) throws SDUnavailableException, SDNotEnouchSpaceException
    {
        int length = NONE;
        
        if (io != null)
        {
            if (file != null)
            {
                try
                {
                    file.seek(file.length());
                    file.write(io);
                }
                catch (IOException e)
                {
                    LogX.getInstance().e(TAG,
                            "::writeFile(): " + e.getMessage());
                    checkSD(context, io);
                    // 如果出现异常，返回的字节数为-1，表示出现了异常，没有写入成功
                    return ERROR;
                }
                length = io.length;
            }
            else
            {
                checkSD(context, io);
                // 如果出现异常，返回的字节数为-1，表示出现了异常，没有写入成功
                return ERROR;
            }
            
        }
        
        return length;
    }
    
    /**
     * 从本地文件中读取文件信息
     * 
     * @param fileName
     *            文件的名称
     * @return 文件中的信息
     */
    public static String readFile(String fileName)
    {
        RandomAccessFile file = null;
        byte[] buffer = new byte[MAX_LENTH];
        StringBuffer content = new StringBuffer();
        try
        {
            try
            {
                file = new RandomAccessFile(createFile(fileName), "rw");
                while (file.read(buffer) != -1)
                {
                    content.append(new String(buffer));
                }
            }
            catch (SDNotEnouchSpaceException e)
            {
                LogX.getInstance().e(TAG,
                        "::readFile: " + e.getMessage()
                                + " sd not enouch sapce");
            }
        }
        catch (IOException e)
        {
            LogX.getInstance()
                    .e(TAG, "::readFile: " + e.getMessage() + " fail");
        }
        finally
        {
            try
            {
                if (null != file)
                {
                    file.close();
                }
            }
            catch (IOException e)
            {
                LogX.getInstance().e(TAG,
                        "::readFile: " + e.getMessage() + " fail");
            }
        }
        return content.toString();
    }
    
    /**
     * 是否存在此文件
     * 
     * @param file
     *            判断是否存在的文件
     * @return 存在返回true，否则返回false
     */
    public static boolean isFileExist(final File file)
    {
        // 在无SD卡时file会为空
        if (file == null)
        {
            return false;
        }
        return file.exists();
    }
    
    /**
     * 是否存在此文件
     * 
     * @param filePath
     *            判断是否存在的文件路径
     * @return 存在返回true，否则返回false
     */
    public static boolean isFileExist(final String filePath)
    {
        try
        {
            File file = new File(filePath);
            return file.exists();
        }
        catch (Exception e)
        {
            return false;
        }
    }
    
    /**
     * 删除下载目录里的文件
     * 
     * @param fileName
     *            文件名
     * @return true删除成功，false删除失败
     */
    public static boolean deleteDownloadFile(String fileName)
    {
        return deleteFile(WXApplication.getInstance()
                .getDownloadSavePath() + fileName);
    }
    
    /**
     * 删除下载目录里的文件（）
     * 
     * @param fileName
     *            （文件的绝对路径名，如：/sdcard/AppMarket/DownLoad/test.wgt） add by
     * @return 删除文件结果
     */
    public static boolean delFileByAbsolutepath(String fileName)
    {
        if (null == fileName)
        {
            return false;
        }
        return deleteFile(fileName);
    }
    
    /**
     * 删除路径指向的文件
     * 
     * @param fileName
     *            文件的名称
     * @return true删除成功，false删除失败
     */
    private static boolean deleteFile(final String fileName)
    {
        boolean isComplete = false;
        
        File file = new File(fileName);
        
        if (file.exists())
        {
            isComplete = file.delete();
        }
        else
        {
            isComplete = true;
        }
        return isComplete;
    }
    
    /**
     * 本地文件大小
     * 
     * @param fileName
     *            文件的名称
     * @return 返回文件的大小
     */
    public static long getLocalFileSize(final String fileName)
    {
        if (TextUtils.isEmpty(fileName))
        {
            return 0;
        }
        File file = null;
        file = new File(fileName);
        long length = 0;
        if (isFileExist(file))
        {
            length = file.length();
        }
        return length;
    }
    
    /**
     * 对sdcard的检查，主要是检查sd是否可用，并且sd卡的存储空间是否充足
     * 
     * @param context 上下文
     * @param io 字节流
     * @throws SDUnavailableException sd卡不可用
     * @throws SDNotEnouchSpaceException sd卡控件不足
     */
    public static void checkSD(final Context context, byte[] io)
            throws SDUnavailableException, SDNotEnouchSpaceException
    {
        if (!Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED))
        {
            throw new SDUnavailableException(context.getResources()
                    .getString(R.string.sdcard_unload));
        }
        else
        {
            throw new SDNotEnouchSpaceException(context.getResources()
                    .getString(R.string.sdcard_not_enough_space));
        }
    }
    
    /**
     * 获取SD卡的剩余空间
     * 
     * @return SD卡的剩余的字节数
     */
    public static long getFreeSD()
    {
        long nAvailableCount = 0l;
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
                .getPath());
        long lSize = stat.getBlockSize();
        long lBlock = stat.getAvailableBlocks();
        nAvailableCount = lSize * lBlock;
        return nAvailableCount;
    }
    
    /**
     * 获取手机的剩余空间
     * 
     * @return 手机的剩余的字节数
     */
    public static long getFreePhone()
    {
        long nAvailableCount = 0l;
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        nAvailableCount = stat.getBlockSize() * stat.getAvailableBlocks();
        return nAvailableCount;
    }
    
    /**
     * 通过绝对路径来获取目录
     * @param path 目录路径
     * @return  路径
     */
    public static String transAbsPathToDir(String path)
    {
        String dir = "";
        if (path != null && (!path.equals("")))
        {
            
            if (path.endsWith("/"))
            {
                dir = path;
            }
            else
            {
                int pos = path.lastIndexOf("/");
                if (pos != -1)
                {
                    dir = path.substring(0, pos + 1);
                }
            }
            
        }
        return dir;
    }
    
    /**
     * 用户设置新的路径时创建目录，当用户更换非WGT存储路径时，需要调用此方法
     * @param path 目录路径
     * @return 创建目录是否成功 
     */
    public static boolean makeDir(String path)
    {
        boolean isComplete = false;
        File file = new File(path);
        if (!isFileExist(file))
        {
            isComplete = file.mkdirs();
        }
        return isComplete;
    }
    
    /**
     * 删除文件夹下的所有文件
     * <一句话功能简述>
     * <功能详细描述>
     * @param delFile [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static void deleteDir(File delFile)
    {
        File[] f = delFile.listFiles(); //取得文件夹里面的路径
        if (f == null)
        {
            delFile.delete();
        }
        else
        {
            if (f != null)
            {
                for (File nFile : f)
                {
                    if (nFile.isDirectory())
                    {
                        deleteDir(nFile);
                    }
                    else
                    {
                        nFile.delete();
                    }
                }
            }
            delFile.delete();
        }
        delFile.delete();
    }
}
