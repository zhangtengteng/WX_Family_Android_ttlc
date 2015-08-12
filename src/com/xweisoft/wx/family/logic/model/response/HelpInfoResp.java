package com.xweisoft.wx.family.logic.model.response;

import com.google.gson.annotations.SerializedName;
import com.xweisoft.wx.family.logic.model.HelpItem;

public class HelpInfoResp extends CommonResp
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -6285814857728200855L;
    
    @SerializedName("data")
    private HelpItem item;
    
    public HelpItem getItem()
    {
        return item;
    }
    
    public void setItem(HelpItem item)
    {
        this.item = item;
    }
    
}
