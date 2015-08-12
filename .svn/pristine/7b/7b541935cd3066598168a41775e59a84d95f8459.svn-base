/*
 * 文 件 名:  UpdateUtil.java
 * 描    述:  升级工具类
 * 创 建 人:  李晨光
 * 创建时间:  2014年6月10日
 */
package com.xweisoft.wx.family.util;

import java.util.regex.Pattern;

/**
 * 升级工具类
 * 
 * @author  李晨光
 * @version  [版本号, 2014年6月10日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class UpdateUtil
{
    /**
     * 判断是否升级
     * @param locVersion 本地版本号
     * @param serVersion 服务器版本号
     * @return 是否升级
     * @see [类、类#方法、类#成员]
     */
    public static boolean isUpdate(String locVersion, String serVersion)
    {
        if(null != locVersion && null != serVersion)
        {
            
            // 去除字符串左右的空格
            locVersion = locVersion.trim();
            serVersion = serVersion.trim();
            
            // 判断服务器给出的版本号和本地的版本号是否由数字和小数点组成
            Pattern ploc = Pattern.compile("^\\d([.\\d]*\\d$)*");
            boolean bloc = ploc.matcher(locVersion).matches();
            boolean bser = ploc.matcher(serVersion).matches();
            if(bloc && bser)
            {
                // 根据小数点截取数字
                String[] strs = locVersion.split("\\.");
                String[] strs2 = serVersion.split("\\.");
                int strlen = strs.length;
                int str2len = strs2.length;
                
                // 以下代码用于版本号差位补齐
                if(str2len > strlen) // 如果服务器版本位数大于本地版本位数
                {
                    int len = str2len-strlen;
                    for(int i= 0; i < len; i++)
                    {
                        locVersion += ".0";
                    }
                }
                if(str2len < strlen) // 如果服务器版本位数小于本地版本位数
                {
                    int len = strlen-str2len;
                    for(int i= 0; i < len; i++)
                    {
                        serVersion += ".0";
                    }
                }
                
                // 版本号差位补齐之后进行按位比较升级
                String[] loc = locVersion.split("\\.");
                String[] ser = serVersion.split("\\.");
                int loclen = loc.length;
                for(int j = 0; j < loclen; j++)
                {
                    int num1 = Integer.valueOf(loc[j]);
                    int num2 = Integer.valueOf(ser[j]);
                    if(num2 == num1) // 同位数字相等，继续
                    {
                        continue;
                    }
                    if(num2 > num1) // 同位数字num2>num1，需要升级
                    {
                        return true;
                    }
                    else  // 其余的情况下不升级
                    {
                        return false;
                    }
                }
                return false;
            }
            return false;
        }
        return false;
    }
}
