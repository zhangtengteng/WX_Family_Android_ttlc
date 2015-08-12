package com.xweisoft.wx.family.logic.model.response;

import java.util.ArrayList;

import com.xweisoft.wx.family.logic.model.ApplyItem;

/**
 * 预报名列表请求解析类
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-5-19]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ApplyListResp extends CommonResp
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 429975420565574998L;
    
    /**
     * 预报名集合
     */
    public ArrayList<ApplyItem> predictionList;
}
