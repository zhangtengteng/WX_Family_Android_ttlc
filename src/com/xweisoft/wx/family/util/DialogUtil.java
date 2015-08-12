/*
 * 文 件 名:  DialogUtil.java
 * 描    述:  <描述>
 * 创 建 人:  李晨光
 * 创建时间:  2014年4月3日
 */
package com.xweisoft.wx.family.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * 对话框工具类
 * 
 * @author  李晨光
 * @version  [版本号, 2014年4月3日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class DialogUtil
{  
   /**
    * 含有两个按钮的对话框
    * @param context 上下文
    * @param title 标题
    * @param message 对话框中的内容
    * @param posText 一个按钮提示语
    * @param negText 第二个按钮提示语
    * @param posTodo 单击第一个按钮要做的事情
    * @param negTodo 单击第二个按钮要做的事情
    * @see [类、类#方法、类#成员]
    */
    public static void createDialog(Context context,String title, String message, String posText, String negText, final PosTodo posTodo, final NegTodo negTodo)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(posText, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                posTodo.todo();
            }
        });
        builder.setNegativeButton(negText, new DialogInterface.OnClickListener()
        {
            
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                negTodo.todo();
            }
        });
        builder.create().show();
    }
    
    /**
     * 含有一个按钮的对话框
     * @param context 上下文
     * @param title 标题
     * @param message 对话框中的内容
     * @param posText 一个按钮提示语
     * @param negText 第二个按钮提示语
     * @param posTodo 单击第一个按钮要做的事情
     * @param negTodo 单击第二个按钮要做的事情
     * @see [类、类#方法、类#成员]
     */
     public static void createDialog(Context context,String title, String message, String posText, final PosTodo posTodo)
     {
         AlertDialog.Builder builder = new AlertDialog.Builder(context);
         builder.setTitle(title);
         builder.setMessage(message);
         builder.setPositiveButton(posText, new DialogInterface.OnClickListener()
         {
             @Override
             public void onClick(DialogInterface dialog, int which)
             {
                 dialog.dismiss();
                 posTodo.todo();
             }
         });
         builder.create().show();
     }
    
    /**
     * 
     * 弹出对话框后单击确定后要做的事情
     * 
     * @author  李晨光
     * @version  [版本号, 2014年4月3日]
     * @see  [相关类/方法]
     * @since  [产品/模块版本]
     */
    public interface PosTodo
    {
        public void todo();
    }
    
    /**
     * 单击对话框取消按钮后要做的事情
     * 
     * @author  李晨光
     * @version  [版本号, 2014年4月3日]
     * @see  [相关类/方法]
     * @since  [产品/模块版本]
     */
    public interface NegTodo
    {
        public void todo();
    }
}
