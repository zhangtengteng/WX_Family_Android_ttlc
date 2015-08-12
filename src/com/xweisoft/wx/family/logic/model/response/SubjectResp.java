package com.xweisoft.wx.family.logic.model.response;

import java.util.ArrayList;

import com.xweisoft.wx.family.logic.model.Examination;
import com.xweisoft.wx.family.logic.model.ExaminationList;
import com.xweisoft.wx.family.logic.model.SubjectItem;
import com.xweisoft.wx.family.logic.model.SubjectScore;

/**
 * 当前考试的列表
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  
 * @see  
 * @since  [产品/模块版本]
 */
public class SubjectResp extends CommonResp
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -1204798873191725972L;
    
    /**
     * 学科成绩集合
     */
    public ArrayList<SubjectScore>subjectScores;
    
}
