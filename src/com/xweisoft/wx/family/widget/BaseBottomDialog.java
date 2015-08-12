package com.xweisoft.wx.family.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.logic.global.GlobalVariable;
import com.xweisoft.wx.family.logic.initialize.InitManager;

/**
 * 底部弹出框
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  guoac
 * @version  [版本号, 2015-1-20]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public abstract class BaseBottomDialog extends Dialog
{
    /**
     * 弹出dialog的Activity
     */
    private Context context;
    
    /**
     * 布局文件
     */
    private int layoutId;
    
    /**
     * <默认构造函数>
     */
    public BaseBottomDialog(Context context, int layoutId)
    {
        super(context, R.style.bottom_dialog_window_style);
        this.context = context;
        this.layoutId = layoutId;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(layoutId);
        initView();
        bindListener();
    }
    
    /**
     * 初始化view
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    protected abstract void initView();
    
    /**
     * 设置监听事件
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    protected abstract void bindListener();
    
    /**
     * 自定义动画展示的Dialog
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void showDialog()
    {
        show();
        Window window = getWindow();
        
        if (0 == GlobalVariable.screenWidth)
        {
            InitManager.getInstance().initClient((Activity) context);
        }
        // 设置显示动画
        window.setWindowAnimations(R.style.bottom_dialog_style);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = GlobalVariable.screenHeight;
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = GlobalVariable.screenWidth;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        
        // 设置显示位置
        onWindowAttributesChanged(wl);
        // 设置点击外围解散
        setCanceledOnTouchOutside(true);
    }
    
}
