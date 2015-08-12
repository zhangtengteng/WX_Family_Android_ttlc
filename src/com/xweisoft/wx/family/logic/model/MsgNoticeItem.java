package com.xweisoft.wx.family.logic.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 消息文本对象
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-5-30]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MsgNoticeItem implements Serializable
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -1286272074936471305L;
    
    /**
     * 内容
     */
    public String content;
    
    /**
     * 标题
     */
    public String title;
    
    /**
     * 发布时间
     */
    public String publishTime;
    
    /**
     * 作业发布者
     */
    public String sendUser;
    
    /**
     * 班级id 多个已“，”隔开
     */
    public String classIds;
    
    /**
     * 通知消息id
     */
    public String noticeId;
    
    /**
     * 消息资源（音视频）集合
     */
    public ArrayList<MsgResourceItem> resourceList;
    
}
