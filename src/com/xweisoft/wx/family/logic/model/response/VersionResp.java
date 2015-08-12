/*
 * 文 件 名:  VersionResp.java
 * 描    述:   版本升级
 * 创 建 人:  李晨光
 * 创建时间:  2014年7月19日
 */
package com.xweisoft.wx.family.logic.model.response;

import com.google.gson.annotations.SerializedName;
import com.xweisoft.wx.family.logic.model.VersionItem;

/**
 * 版本升级
 * 
 * @author  李晨光
 * @version  [版本号, 2014年7月19日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class VersionResp extends CommonResp
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 版本
     */
    @SerializedName("clientUpdate")
    private VersionItem versionItem = null;

    /**
     * @return 返回 versionItem
     */
    public VersionItem getVersionItem()
    {
        return versionItem;
    }

    /**
     * @param 对versionItem进行赋值
     */
    public void setVersionItem(VersionItem versionItem)
    {
        this.versionItem = versionItem;
    }
}
