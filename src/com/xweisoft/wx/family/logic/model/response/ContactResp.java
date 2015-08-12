package com.xweisoft.wx.family.logic.model.response;

import java.util.ArrayList;

import com.xweisoft.wx.family.logic.model.ContactItem;

/**
 * 通讯录
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-7-23]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ContactResp extends CommonResp
{
    
    private static final long serialVersionUID = -830760141557151744L;
    
    /**
     * 家长集合
     */
    public ArrayList<ContactItem> parentContacts;
    
    /**
     * 教师集合
     */
    public ArrayList<ContactItem> teacherContacts;
}
