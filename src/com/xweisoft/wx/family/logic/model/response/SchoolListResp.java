package com.xweisoft.wx.family.logic.model.response;

import java.util.ArrayList;

import com.xweisoft.wx.family.logic.model.SchoolItem;

/**
 * 学校解析类
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-5-20]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class SchoolListResp extends CommonResp
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -5714878123363289487L;
    
    /**
     * 学校集合
     */
    public ArrayList<SchoolItem> schoolInfoList;
}
