package com.xweisoft.wx.family.logic.model;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

/**
 * 考试对象
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-5-19]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class HistroyExamItem
{
    
    /**
     * 考试编号
     */
    public String examinationId;
    
    /**
     * 考试名称
     */
    public String examinationName;
    
    /**
     * 1 上学期 2 下学期
     */
    public String semester;
    
    /**
     * 学科成绩集合
     */
    
    @SerializedName("subjectScores")
    public ArrayList<SubjectItem> list=new ArrayList<SubjectItem>();
    
}
