package com.xweisoft.wx.family.logic.model.response;

import java.io.Serializable;

/**
 * 公共响应参数
 * @author  yangchao
 * @version  [版本号, 2013-10-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CommonResp implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 返回码
     */
    private String code = "";
    
    /**
     * 返回消息
     */
    private String message = "";
    
    public String getCode()
    {
        return code;
    }
    
    public void setCode(String code)
    {
        this.code = code;
    }
    
    public String getMessage()
    {
        return message;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }
    
}
