package com.xweisoft.wx.family.logic.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.xweisoft.wx.family.logic.model.AreaItem;

/**
 * 地区解析类
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-5-20]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class AreaListResp extends CommonResp
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -1644078746680465223L;
    
    /**
     * 地区集合
     */
    @SerializedName("addressInfo")
    public ArrayList<AreaItem> areaList;
    
}
