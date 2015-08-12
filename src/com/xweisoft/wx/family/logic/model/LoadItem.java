package com.xweisoft.wx.family.logic.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.xweisoft.wx.family.logic.request.JsonUtils;

/**
 * <一句话功能简述>
 * 启动界面列表对象
 * <功能详细描述>
 * @author  houfangfang
 * @version  [版本号, 2014-4-8]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class LoadItem
{
    
    /**
     * load图片url
     */
    public String imageUrl;
    
    public LoadItem(JSONObject object) throws JSONException
    {
        this.imageUrl = JsonUtils.getJsonStringValue(object, "cover_url");
    }
}
