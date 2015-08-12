package com.xweisoft.wx.family.logic.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.xweisoft.wx.family.logic.model.MessageItem;

public class MessageListResp extends CommonResp
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -3031947761409413359L;
    
    @SerializedName("noticeList")
    public ArrayList<MessageItem> sysMsgList;
    
    @SerializedName("pushNotices")
    public ArrayList<MessageItem> schoolMsgList;
    
    @SerializedName("classMsgList")
    public ArrayList<MessageItem> classMsgList;
    
    public String total;
    
}
