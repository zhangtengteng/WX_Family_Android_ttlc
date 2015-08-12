/*
 * 文 件 名:  MessageResp.java
 * 描    述:  新消息
 * 创 建 人:  李晨光
 * 创建时间:  2014年7月11日
 */
package com.xweisoft.wx.family.logic.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.xweisoft.wx.family.logic.model.MessageItem;

/**
 * 新消息
 * 
 * @author  李晨光
 * @version  [版本号, 2014年7月11日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MessageResp extends CommonResp
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    @SerializedName("data")
    private ArrayList<MessageItem> messageItems;

    /**
     * @return 返回 messageItems
     */
    public ArrayList<MessageItem> getMessageItems()
    {
        return messageItems;
    }

    /**
     * @param 对messageItems进行赋值
     */
    public void setMessageItems(ArrayList<MessageItem> messageItems)
    {
        this.messageItems = messageItems;
    }
}
