package com.xweisoft.wx.family.logic.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * 是否已经绑定手机
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  gac
 * @version  [版本号, 2014-10-14]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class IsBindPhoneItem implements Serializable
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -6139596512095683029L;
    
    /**
     * 是否已经绑定
    1已绑定，0未绑定
     */
    @SerializedName("binded")
    private String binded;
    
    public String getBinded()
    {
        return binded;
    }
    
    public void setBinded(String binded)
    {
        this.binded = binded;
    }
    
}
