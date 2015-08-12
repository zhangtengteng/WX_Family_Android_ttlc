/*
 * 文 件 名:  SDKUtil.java
 * 版    权:  XiaoWei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  Chao
 * 修改时间:  2014年4月22日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.xweisoft.wx.family.service.http;

import android.os.Build;

/**
 * <一句话功能简述>
 * sdk版本工具类
 * 
 * @author  administrator
 * @version  [版本号, 2014年4月22日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class SDKUtil
{
    /**
     * 判断当前手机的sdk是否大于9即是否是2.3以上包括2.3
     * API等级9：Android 2.3 - 2.3.2 Gingerbread
     * @return [参数说明]
     * 
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean isSDKAbove9()
    {
        if (Build.VERSION.SDK_INT >= 9) 
        {
            return true;
        }
        return false;
    }
    
    
    /**
     * 判断当前手机的sdk是否大于10即是否是3.0以上不包括包括3.0
     * API等级10：Android 2.3.3
     * @return [参数说明]
     * 
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean isSDKAbove10()
    {
        if (Build.VERSION.SDK_INT > 10) 
        {
            return true;
        }
        return false;
    }
}
