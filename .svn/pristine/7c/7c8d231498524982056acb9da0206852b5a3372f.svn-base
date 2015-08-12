/*
 * 文 件 名:  DeletePicturePopupWindow.java
 * 描    述:  删除图片
 * 创 建 人:  李晨光
 * 创建时间:  2014年7月24日
 */
package com.xweisoft.wx.family.widget;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.xweisoft.wx.family.R;

/**
 * 删除图片
 * 
 * @author  李晨光
 * @version  [版本号, 2014年7月24日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class DeletePicturePopupWindow extends MyPopupWindow
{
    /**
     * 拍摄图片，本地图片，取消
     */
    private View camaraPicture, localPicture;
    
    /**
     * click事件
     */
    private OnClickListener listener;
    
    /**
     * <默认构造函数>
     * @param layoutId 内容布局id
     */
    public DeletePicturePopupWindow(Context mContext, int layoutId,
            OnClickListener listener)
    {
        super(mContext, layoutId);
        this.listener = listener;
        initPopupWindow();
        initViews();
        bindLisener();
    }
    
    @Override
    public void initViews()
    {
        camaraPicture = view.findViewById(R.id.delete_picture);
        localPicture = view.findViewById(R.id.cancel_delete);
    }
    
    @Override
    public void bindLisener()
    {
        camaraPicture.setOnClickListener(listener);
        localPicture.setOnClickListener(listener);
    }
}
