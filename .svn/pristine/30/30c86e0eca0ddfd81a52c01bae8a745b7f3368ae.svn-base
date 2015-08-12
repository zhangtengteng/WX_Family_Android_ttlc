package com.xweisoft.wx.family.widget;

import com.xweisoft.wx.family.R;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * <一句话功能简述>
 * 点击上传图片还是视频弹出对话框
 * <功能详细描述>
 * 
 * @author  houfangfang
 * @version  [版本号, 2014-3-13]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class UploadPicturePopupWindow extends MyPopupWindow
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
    public UploadPicturePopupWindow(Context mContext, int layoutId,
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
        camaraPicture = view.findViewById(R.id.camara_picture);
        localPicture = view.findViewById(R.id.select_picture);
    }
    
    @Override
    public void bindLisener()
    {
        camaraPicture.setOnClickListener(listener);
        localPicture.setOnClickListener(listener);
    }
}
