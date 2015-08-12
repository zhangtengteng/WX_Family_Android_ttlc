package com.xweisoft.wx.family.ui;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

/**
 * Fragment基类
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-2-6]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public abstract class BaseFragment extends Fragment
{
    /**
     * 上下文
     */
    protected Context mContext;
    
    /**
     * 图片加载类
     */
    protected ImageLoader imageLoader;
    
    protected View mLayouView;
    
    private Toast mToast;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        imageLoader = ImageLoader.getInstance();
    }
    
    /**
     * 显示Toast提示信息
     * @param message
     * @see [类、类#方法、类#成员]
     */
    public void showToast(String message)
    {
        if (mToast == null)
        {
            mToast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        }
        else
        {
            mToast.setText(message);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
    
    /**
     * 初始化控件    
     */
    public abstract void initViews();
    
    /**
     * 绑定控件事件    
     */
    public abstract void bindListener();
    
}
