package com.xweisoft.wx.family.logic.download;

import com.xweisoft.wx.family.logic.model.DownloadItem;

/**
 * <一句话功能简述>
 * 下载按钮的点击事件
 * 
 * @author  administrator
 * @version  [版本号, 2014年1月13日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface IDownloadButtonListener
{
    /**
     * 点击按钮的回掉方法
     * <功能详细描述> [参数说明]
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    void onButtonClick(DownloadItem item,int position);
}
