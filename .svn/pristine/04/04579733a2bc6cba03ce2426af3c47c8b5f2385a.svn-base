package com.xweisoft.wx.family.logic.model;

/**
 * 即时通信msg对象
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-5-22]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class SocketMsgItem
{
    /**
     * 消息类型
     * ============
     * 1 登录请求login 服务端返回login不做消息到达反馈 
     * 2 系统推送消息biz，接受后做消息到达反馈回复给服务端一条ack的消息
     * ============
     */
    public String funcid;
    
    /**
     * 即时消息id
     */
    public String msgid;
    
    /**
     * 连接授权验证
     * MD5(pushid + msgid)
     */
    public String pwd;
    
    /**
     * 消息内容对象
     */
    public String content;
    
    /**
     * 发送方 uid
     */
    public String from;
    
    /**
     * 接收方 uid
     */
    public String to;
    
    /**
     * 消息发送时间
     */
    public String sendtime;
}
