package com.nostra13.universalimageloader.core.display;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.util.Util;

/**
 * 圆形图片显示
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-7-15]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CircleBitmapDisplayer implements BitmapDisplayer
{
    
    @Override
    public void display(Bitmap bitmap, ImageAware imageAware,
            LoadedFrom loadedFrom)
    {
        bitmap = Util.toRoundBitmap(bitmap);
        if (null != bitmap)
        {
//            imageAware.setBackgroundResource(R.drawable.wx_transparent);
            imageAware.setImageBitmap(bitmap);
        }
    }
    
}
