/*
 * 文 件 名:  StringUtil.java
 * 描    述:  字符串工具类
 * 创 建 人:  李晨光
 * 创建时间:  2013-5-2
 */
package com.xweisoft.wx.family.util;

/**
 * 字符串工具类  此类封装了字符串相关操作方法
 * 
 * @author  李晨光
 * @version  [版本号, 2013-5-2]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class StringUtil
{
    /**
     * 判断字符串是否为空
     * @param str
     * @return true：空 ，false：非空
     * @see [类、类#方法、类#成员]
     */
    public static boolean isEmpty(String str)
    {
        if ((null != str) && (!"".equals(str)) && (!"null".equals(str)))
        {
            return false;
        }
        return true;
    }
}
