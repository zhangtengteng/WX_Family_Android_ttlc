package com.xweisoft.wx.family.logic.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 用户实体类
 * 
 * @author  李晨光
 * @version  [版本号, 2014年6月30日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class UserItem implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户id
     */
    public String userId;
    
    /**
     * 家长性别
     */
    public String parentSex;
    
    /**
     * 登录手机号
     */
    public String phone;
    
    /**
     * 登录密码
     */
    public String password;
    
    /**
     * 孩子集合
     */
    public ArrayList<ChildrenItem> children;
    
}
