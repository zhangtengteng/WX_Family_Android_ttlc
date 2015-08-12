package com.xweisoft.wx.family.util;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xweisoft.wx.family.R;

public class CommonTitleUtil
{
    
    /** <一句话功能简述>
     * 初始化所有activity公共标题栏布局
     * <功能详细描述>
     * @param activity  当前activity   
     * @param titleStr 中间标题栏文字
     * @param rightImageId 右边图片资源id,否则为0
     * @param isHiddenLeft   是否隐藏左边view
     * @param isHiddenRight 是否隐藏右边view
     * [参数说明]
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static void initCommonTitle(final Activity activity,
            String titleStr, int rightImageId, final boolean isHiddenLeft,
            boolean isHiddenRight)
    {
        //中间标题
        TextView titleView = (TextView) activity.findViewById(R.id.common_title_center_text);
        //左边布局
        LinearLayout leftBackLayout = (LinearLayout) activity.findViewById(R.id.common_left_back);
        final ImageView leftBackView = (ImageView) activity.findViewById(R.id.common_left_back_imageview);
        //右边布局
        RelativeLayout rightLayout = (RelativeLayout) activity.findViewById(R.id.common_title_right);
        ImageView rightImageView = (ImageView) activity.findViewById(R.id.common_title_right_imageview);
        if (!TextUtils.isEmpty(titleStr))
        {
            titleView.setText(titleStr);
        }
        //是否隐藏左边
        if (!isHiddenLeft)
        {
            leftBackView.setVisibility(View.VISIBLE);
            // 监听左边返回    
            leftBackLayout.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    activity.finish();
                }
            });
        }
        //是否隐藏右边view
        if (isHiddenRight)
        {
            rightLayout.setVisibility(View.INVISIBLE);
        }
        else
        {
            //图片
            if (rightImageId != 0)
            {
                rightLayout.setVisibility(View.VISIBLE);
                rightImageView.setVisibility(View.VISIBLE);
                rightImageView.setImageResource(rightImageId);
            }
        }
    }
    
    /** <一句话功能简述>
     * 初始化所有activity公共标题栏布局
     * <功能详细描述>
     * @param activity  当前activity   
     * @param titleStr 中间标题栏文字
     * @param rightText 右边文字
     * @param isHiddenLeft   是否隐藏左边view
     * @param isHiddenRight 是否隐藏右边view
     * [参数说明]
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static void initCommonTitle(final Activity activity,
            String titleStr, String rightText, final boolean isHiddenLeft,
            boolean isHiddenRight)
    {
        //中间标题
        TextView titleView = (TextView) activity.findViewById(R.id.common_title_center_text);
        //左边布局
        LinearLayout leftLayout = (LinearLayout) activity.findViewById(R.id.common_left_back);
        final ImageView leftBackView = (ImageView) activity.findViewById(R.id.common_left_back_imageview);
        //右边布局
        RelativeLayout rightLayout = (RelativeLayout) activity.findViewById(R.id.common_title_right);
        TextView rightTextView = (TextView) activity.findViewById(R.id.common_title_right_text);
        ImageView rightImageView = (ImageView) activity.findViewById(R.id.common_title_right_imageview);
        if (!TextUtils.isEmpty(titleStr))
        {
            titleView.setText(titleStr);
        }
        //是否隐藏左边
        if (!isHiddenLeft)
        {
            leftBackView.setVisibility(View.VISIBLE);
            // 监听左边返回    
            leftLayout.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    activity.finish();
                }
            });
        }
        //是否隐藏右边view
        if (isHiddenRight)
        {
            rightLayout.setVisibility(View.INVISIBLE);
        }
        else
        {
            if (!TextUtils.isEmpty(rightText))
            {
                rightLayout.setVisibility(View.VISIBLE);
                rightTextView.setVisibility(View.VISIBLE);
                rightImageView.setVisibility(View.GONE);
                rightTextView.setText(rightText);
            }
            else
            {
                rightLayout.setVisibility(View.INVISIBLE);
            }
        }
    }
    
    /** <一句话功能简述>
     * 初始化所有Fragment公共标题栏布局
     * <功能详细描述>
     * @param activity  当前activity   
     * @param titleStr 中间标题栏文字
     * @param rightImageId 右边图片资源id,否则为0
     * @param isHiddenLeft   是否隐藏左边view
     * @param isHiddenRight 是否隐藏右边view
     * [参数说明]
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static void initCommonTitle(View view, String titleStr,
            int rightImageId, final boolean isHiddenLeft, boolean isHiddenRight)
    {
        //中间标题
        TextView titleView = (TextView) view.findViewById(R.id.common_title_center_text);
        //左边布局
        LinearLayout leftBackLayout = (LinearLayout) view.findViewById(R.id.common_left_back);
        final ImageView leftBackView = (ImageView) view.findViewById(R.id.common_left_back_imageview);
        //右边布局
        RelativeLayout rightLayout = (RelativeLayout) view.findViewById(R.id.common_title_right);
        ImageView rightImageView = (ImageView) view.findViewById(R.id.common_title_right_imageview);
        if (!TextUtils.isEmpty(titleStr))
        {
            titleView.setText(titleStr);
        }
        //是否隐藏左边
        if (!isHiddenLeft)
        {
            leftBackView.setVisibility(View.VISIBLE);
        }
        //是否隐藏右边view
        if (isHiddenRight)
        {
            rightLayout.setVisibility(View.INVISIBLE);
        }
        else
        {
            //图片
            if (rightImageId != 0)
            {
                rightLayout.setVisibility(View.VISIBLE);
                rightImageView.setVisibility(View.VISIBLE);
                rightImageView.setImageResource(rightImageId);
            }
        }
    }
    
    /** <一句话功能简述>
     * 初始化所有Fragment公共标题栏布局
     * <功能详细描述>
     * @param activity  当前activity   
     * @param titleStr 中间标题栏文字
     * @param rightText 右边文字
     * @param isHiddenLeft   是否隐藏左边view
     * @param isHiddenRight 是否隐藏右边view
     * [参数说明]
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static void initCommonTitle(View view, String titleStr,
            String rightText, final boolean isHiddenLeft, boolean isHiddenRight)
    {
        //中间标题
        TextView titleView = (TextView) view.findViewById(R.id.common_title_center_text);
        //左边布局
        LinearLayout leftLayout = (LinearLayout) view.findViewById(R.id.common_left_back);
        final ImageView leftBackView = (ImageView) view.findViewById(R.id.common_left_back_imageview);
        //右边布局
        RelativeLayout rightLayout = (RelativeLayout) view.findViewById(R.id.common_title_right);
        TextView rightTextView = (TextView) view.findViewById(R.id.common_title_right_text);
        ImageView rightImageView = (ImageView) view.findViewById(R.id.common_title_right_imageview);
        if (!TextUtils.isEmpty(titleStr))
        {
            titleView.setText(titleStr);
        }
        //是否隐藏左边
        if (!isHiddenLeft)
        {
            leftBackView.setVisibility(View.VISIBLE);
        }
        //是否隐藏右边view
        if (isHiddenRight)
        {
            rightLayout.setVisibility(View.INVISIBLE);
        }
        else
        {
            if (!TextUtils.isEmpty(rightText))
            {
                rightLayout.setVisibility(View.VISIBLE);
                rightTextView.setVisibility(View.VISIBLE);
                rightImageView.setVisibility(View.GONE);
                rightTextView.setText(rightText);
            }
            else
            {
                rightLayout.setVisibility(View.INVISIBLE);
            }
        }
    }
}
