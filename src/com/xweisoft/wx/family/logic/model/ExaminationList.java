package com.xweisoft.wx.family.logic.model;

/**
 * 学科对象
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-5-19]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ExaminationList
{
    /**
    * 1 上学期 2 下学期
    */
    public String semester;
    
    /**
     * 考次名称
     */
    public String examinationName;
    
    /**
     * 考次id
     */
    public String examinationId;
    

    
    public int schoolYear;
    
    /**
     * 开始时间
     */
    public  long  startTime;
    
    
    /**
     * 结束时间
     */
    public  long  endTime;
    
    /**
     * 学校编号
     */
    public  long  schoolId;
    
    
    
    
}
