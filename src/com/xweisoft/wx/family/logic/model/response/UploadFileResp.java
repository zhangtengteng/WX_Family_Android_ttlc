package com.xweisoft.wx.family.logic.model.response;

import java.util.ArrayList;

import com.xweisoft.wx.family.logic.model.UploadFileItem;

/**
 * 头像创穿返回类
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-6-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class UploadFileResp extends CommonResp
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -2398382653996814529L;
    
    /**
     * 消息
     */
    public String msgId;
    
    /**
     * 资源集合
     */
    public ArrayList<UploadFileItem> resourceList;
}
