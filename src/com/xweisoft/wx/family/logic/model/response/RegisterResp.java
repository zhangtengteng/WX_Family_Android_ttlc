/*
 * 文 件 名:  RegisterResp.java
 * 描    述:  注册响应
 * 创 建 人:  李晨光
 * 创建时间:  2014年6月30日
 */
package com.xweisoft.wx.family.logic.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.xweisoft.wx.family.logic.model.UserItem;

/**
 * 注册响应
 * 
 * @author  李晨光
 * @version  [版本号, 2014年6月30日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class RegisterResp extends CommonResp
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    @SerializedName("data")
    private UserItem userItem = null;

    /**
     * @return 返回 userItem
     */
    public UserItem getUserItem()
    {
        return userItem;
    }

    /**
     * @param 对userItem进行赋值
     */
    public void setUserItem(UserItem userItem)
    {
        this.userItem = userItem;
    }
}
