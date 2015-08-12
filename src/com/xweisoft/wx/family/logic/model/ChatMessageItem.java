package com.xweisoft.wx.family.logic.model;

import java.io.Serializable;

/**
 * 消息
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-5-12]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ChatMessageItem implements Serializable
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 编号id
     */
    public String id;
    
    /**
     * 文本
     */
    public String text;
    
    /**
     * 头像
     */
    public String header;
    
    /**
     * 0 自己 1 对方 （消息归属）
     */
    public String from;
}
