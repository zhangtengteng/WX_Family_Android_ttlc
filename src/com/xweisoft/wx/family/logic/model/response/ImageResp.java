/*
 * 文 件 名:  ImageResp.java
 * 描    述:  <描述>
 * 创 建 人:  李晨光
 * 创建时间:  2014年7月16日
 */
package com.xweisoft.wx.family.logic.model.response;

import com.google.gson.annotations.SerializedName;
import com.xweisoft.wx.family.logic.model.ImageItem;

/**
 * 图片请求
 * 
 * @author  李晨光
 * @version  [版本号, 2014年7月16日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ImageResp extends CommonResp
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 图片Item
     */
    @SerializedName("data")
    private ImageItem imageItem;

    /**
     * @return 返回 imageItem
     */
    public ImageItem getImageItem()
    {
        return imageItem;
    }

    /**
     * @param 对imageItem进行赋值
     */
    public void setImageItem(ImageItem imageItem)
    {
        this.imageItem = imageItem;
    }
}
