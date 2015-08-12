package com.xweisoft.wx.family.ui.message.adpter;

import java.util.Calendar;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.WXApplication;
import com.xweisoft.wx.family.logic.model.MsgContentItem;
import com.xweisoft.wx.family.logic.model.MsgNoticeItem;
import com.xweisoft.wx.family.ui.adapter.ListViewAdapter;
import com.xweisoft.wx.family.util.DateTools;
import com.xweisoft.wx.family.util.ListUtil;
import com.xweisoft.wx.family.util.Util;
import com.xweisoft.wx.family.widget.BaseViewHolder;

/**
 * 消息中心列表适配器
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-7-15]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MessageFragmentListAdapter extends ListViewAdapter<MsgContentItem>
{
    public MessageFragmentListAdapter(Context context)
    {
        super(context);
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if (ListUtil.isEmpty(mList) || mList.size() <= position)
        {
            return null;
        }
        ViewHolder viewHolder = null;
        if (null == convertView)
        {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.message_listview_header_item,
                    null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.message_listview_item_imageview);
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.message_listview_item_title_textview);
            viewHolder.dateTextView = (TextView) convertView.findViewById(R.id.message_listview_item_date_textview);
            viewHolder.contentTextView = (TextView) convertView.findViewById(R.id.message_listview_item_content_textview);
            viewHolder.mContentView = convertView.findViewById(R.id.message_listview_item_text_layout);
            viewHolder.unReadCountText = (TextView) convertView.findViewById(R.id.message_listview_item_unread_text);
            viewHolder.mDividerView = convertView.findViewById(R.id.message_item_header_divider_view);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mDividerView.setVisibility(View.GONE);
        if (position == 0)
        {
            viewHolder.imageView.setImageResource(R.drawable.img_system_icon);
            viewHolder.titleTextView.setText("系统消息");
        }
        else if (position == 1)
        {
            viewHolder.imageView.setImageResource(R.drawable.wx_message_school_icon);
            viewHolder.titleTextView.setText("学校通知");
        }
        else if (position == 2)
        {
            viewHolder.imageView.setImageResource(R.drawable.wx_message_class_icon);
            viewHolder.titleTextView.setText("班级通知");
        }
        else if (position == 3)
        {
            viewHolder.imageView.setImageResource(R.drawable.wx_message_work_icon);
            viewHolder.titleTextView.setText("班级作业");
            viewHolder.mDividerView.setVisibility(View.VISIBLE);
        }
        //        else if (position == 4)
        //        {
        //            viewHolder.imageView.setImageResource(R.drawable.wx_message_apply_icon);
        //            viewHolder.titleTextView.setText("预报名登记");
        //        }
        viewHolder.unReadCountText.setVisibility(View.GONE);
        MsgContentItem item = mList.get(position);
        if (null != item)
        {
            if (item.unReadCount > 0)
            {
                viewHolder.unReadCountText.setVisibility(View.VISIBLE);
            }
            viewHolder.unReadCountText.setText(item.unReadCount + "");
            if (position >= 4)
            {
                Calendar calendar = DateTools.parseTimeMillis2Calendar(item.time);
                if (null != calendar)
                {
                    viewHolder.dateTextView.setText((calendar.get(Calendar.MONTH) + 1)
                            + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日");
                }
                if (TextUtils.isEmpty(item.groupId))
                {
                    if (!TextUtils.isEmpty(item.userAppellation))
                    {
                        viewHolder.titleTextView.setText(Util.checkNull(item.userName)
                                + "（"
                                + Util.checkNull(item.userAppellation)
                                + "）");
                    }
                    else
                    {
                        viewHolder.titleTextView.setText(Util.checkNull(item.userName));
                    }
                    ImageLoader.getInstance().displayImage(item.header,
                            viewHolder.imageView,
                            WXApplication.getInstance().optionsCircle);
                }
                else
                {
                    viewHolder.titleTextView.setText("群组消息");
                    viewHolder.imageView.setImageResource(R.drawable.wx_message_group_icon);
                }
                if ("1".equals(item.type))
                {
                    viewHolder.contentTextView.setText(Util.checkNull(item.text));
                }
                else if ("4".equals(item.type))
                {
                    viewHolder.contentTextView.setText("[图片]");
                }
            }
            else
            {
                MsgNoticeItem msg = item.notice;
                if (null != msg)
                {
                    Calendar calendar = DateTools.parseTimeMillis2Calendar(msg.publishTime);
                    viewHolder.dateTextView.setText((calendar.get(Calendar.MONTH) + 1)
                            + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日");
                    String content = Util.checkNull(msg.content);
                    viewHolder.contentTextView.setText(Util.fromHtml(content)
                            .toString());
                }
                else
                {
                    viewHolder.contentTextView.setText("");
                }
            }
        }
        if (position < 4)
        {
            viewHolder.isAvailable = false;
        }
        else
        {
            viewHolder.isAvailable = true;
        }
        return convertView;
    }
    
    /**
     * 组件复用类
     * <一句话功能简述>
     * <功能详细描述>
     * 
     * @author  poorgod
     * @version  [版本号, 2015-7-15]
     * @see  [相关类/方法]
     * @since  [产品/模块版本]
     */
    public class ViewHolder extends BaseViewHolder
    {
        ImageView imageView;
        
        /**
         * 消息标题
         */
        TextView titleTextView;
        
        /**
         * 消息日期
         */
        TextView dateTextView;
        
        /**
        * 消息内容
        */
        TextView contentTextView;
        
        /**
         * 未读消息
         */
        TextView unReadCountText;
        
        /**
         * 内容布局
         */
        View mContentView;
        
        /**
         * 通知、消息分割线
         */
        View mDividerView;
    }
}
