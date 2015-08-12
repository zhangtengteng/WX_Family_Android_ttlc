package com.xweisoft.wx.family.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.logic.global.GlobalVariable;
import com.xweisoft.wx.family.logic.initialize.InitManager;
import com.xweisoft.wx.family.util.Util;

/**
 * 对话框的基类
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  gac
 * @version  [版本号, 2014-7-7]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public abstract class BaseDialog extends Dialog
{
    
    /**
     * 布局id
     */
    protected int layoutId;
    
    /**
     * 上下文
     */
    protected Context mContext;
    
    public BaseDialog(Context context, int layoutId)
    {
        super(context, R.style.base_dialog);
        mContext = context;
        this.layoutId = layoutId;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(this.layoutId);
        initViews();
        bindLinsenter();
    }
    
    /**
     * 获取dialog的views
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    protected abstract void initViews();
    
    /**
     * 绑定监听事件
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    protected abstract void bindLinsenter();
    
    /**
     * 显示对话框
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void showDialog()
    {
        show();
        Window dialogWindow = getWindow();
        if (0 == GlobalVariable.screenWidth)
        {
            InitManager.getInstance().initClient((Activity) mContext);
        }
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = GlobalVariable.screenWidth
                - Util.dpToPixel(mContext, 30);
        dialogWindow.setAttributes(params);
    }
    
}
