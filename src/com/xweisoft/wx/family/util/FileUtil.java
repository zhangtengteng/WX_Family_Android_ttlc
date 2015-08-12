/*
 * 文 件 名:  FileUtil.java
 * 描    述:  文件工具类
 * 创 建 人:  李晨光
 * 创建时间:  2013年7月7日
 */
package com.xweisoft.wx.family.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 文件工具类
 * 
 * @author  李晨光
 * @version  [版本号, 2013年7月7日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class FileUtil
{
    
    /**
     * 创建文件
     * @param path 文件所在目录路径
     * @param fileName 文件名
     * @see [类、类#方法、类#成员]
     */
    public static void createFile(String path, String fileName)
    {
        
        // 创建目录
        ctreateDir(path);
        File file = new File(path + File.separator + fileName);
        if(!fileIsExist(path, fileName))
        {
            try
            {
                file.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 创建目录
     * @param path 目录路径
     * @see [类、类#方法、类#成员]
     */
    public static void ctreateDir(String path)
    {
        File file = new File(path);
        if(!dirIsExist(path))
        {
            file.mkdirs();
        }
    }
    
    /**
     * 判断目录是否存在
     * @param path
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static boolean dirIsExist(String path)
    {
        File file = new File(path);
        if(file.isFile() && file.exists())
        {
            return true;
        }
        return false;
    }
    
   /**
    * 判断文件是否存在
    * @param path
    * @param fileName
    * @return
    * @see [类、类#方法、类#成员]
    */
    public static boolean fileIsExist(String path, String fileName)
    {
        File file = new File(path + File.separator + fileName);
        if(file.isFile() && file.exists())
        {
            return true;
        }
        return false;
    }
    
    /**
     * 获取文件的二进制流
     * @param filePath 文件路径
     * @return 文件的二进制流
     * @see [类、类#方法、类#成员]
     */
    public static byte[] getFileBytes(String filePath)
    {
        byte[] buffer = null;
        try
        {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }
        catch (FileNotFoundException e)
        {
            
        }
        catch (IOException e)
        {
            
        }
        return buffer;
    }
}
