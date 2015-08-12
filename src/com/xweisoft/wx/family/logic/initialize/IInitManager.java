package com.xweisoft.wx.family.logic.initialize;

import android.content.Context;

/**
 * <一句话功能简述>
 * 初始化工作接口
 * 
 * @author  administrator
 * @version  [版本号, 2013-10-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface IInitManager 
{
    /**
     * 初始化客户端信息，如版本号，版本名字，屏幕分辨率等    <BR>
     * [功能详细描述]
     * @param context 上下文
     */
    public void initClient(Context context);

}
