package com.xweisoft.wx.family.widget;


import android.content.Context;
import android.widget.Toast;

/**
 * 自定义Toast
 * <功能详细描述>
 * 
 * @author  yangchao
 * @version  [版本号, 2013-10-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MarketToast
{
	/**
	 * 显示3秒
	 */
	public static final int LENGTH_THREE = 3000;
	public static final int LENGTH_SHORT = Toast.LENGTH_SHORT;
	public static final int LENGTH_LONG = Toast.LENGTH_LONG;

	public static void showToast(Context context, int strID)
	{
		showToast(context, context.getString(strID), LENGTH_THREE);
	}

	public static void showToast(Context context, String message)
	{
		showToast(context, message, LENGTH_THREE);
	}

	public static void showToast(Context context, int strID, int howlong)
	{
		showToast(context, context.getString(strID), howlong);
	}

	/**
     * [显示toast]<BR>
     * 视角确认要将toast改为系统默认的样式。
     * @param context 上下文
     * @param message 显示信息
     * @param howlong  显示时间
     */
     public static void showToast(Context context, String message, int howlong)
     {
         Toast.makeText(context, message, Toast.LENGTH_SHORT).show(); 
     }
}
