package com.xweisoft.wx.family.widget;

import java.util.Calendar;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

/**
 * <一句话功能简述>
 * 自定义日期控件对话框
 * <功能详细描述>
 * 
 * @author  houfangfang
 * @version  [版本号, 2014-3-4]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MyDatePickDialog extends DatePickerDialog implements
        OnDateChangedListener
{
    
    /**
     * 当前年
     */
    private int currentYear;
    
    /**
     * 当前月
     */
    private int currentMonth;
    
    /**
     * 当前月的天
     */
    private int currentDay;
    
    public MyDatePickDialog(Context context, OnDateSetListener callBack,
            int year, int monthOfYear, int dayOfMonth)
    {
        super(context, callBack, year, monthOfYear, dayOfMonth);
    }
    
    @Override
    protected void onStop()
    {
        //注释掉了super.onStop()，注释掉这个方法能达到返回键不调用onDateSet方法,防止多次调用接口
        //DatePickerDialog 源码当中，onStop()生命周期当中，会调用tryNotifyDateSet();
    }
    
    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day)
    {
        //以下方法只支持sdk3.0以前版本，以后版本需要另作处理
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH) + 1;
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        //输入大于当前年，则设置成当前年份
//        if (isAfterCurrentYear(view))
//        {
//            view.init(currentYear, month, day, this);
//        }      
    }
    
    /** <一句话功能简述>
     * 判断输入年份是否大于当前年
     * <功能详细描述>
     * @param tempView
     * @return [参数说明]
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private boolean isAfterCurrentYear(DatePicker tempView)
    {
        if (tempView.getYear() > currentYear)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
