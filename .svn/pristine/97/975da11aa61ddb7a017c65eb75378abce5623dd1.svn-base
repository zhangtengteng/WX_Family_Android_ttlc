package com.xweisoft.wx.family.ui.message.adpter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.logic.model.MessageItem;
import com.xweisoft.wx.family.ui.adapter.ListViewAdapter;
import com.xweisoft.wx.family.util.ListUtil;

/**
 * 即时消息适配器
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-5-12]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class NewMessageListAdapter extends ListViewAdapter<MessageItem>
{
    
    public NewMessageListAdapter(Context context)
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
            convertView = mInflater.inflate(R.layout.message_listview_item,
                    null);
            viewHolder.classTextView = (TextView) convertView.findViewById(R.id.message_listview_footer_class_textview);
            viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.message_listview_footer_name_textview);
            viewHolder.dateTextView = (TextView) convertView.findViewById(R.id.message_listview_footer_date_textview);
            viewHolder.contentTextView = (TextView) convertView.findViewById(R.id.message_listview_footer_content_textview);
            viewHolder.mTitleView = convertView.findViewById(R.id.message_list_item_title_layout);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (position != 0)
        {
            viewHolder.mTitleView.setVisibility(View.GONE);
        }
        else
        {
            viewHolder.mTitleView.setVisibility(View.VISIBLE);
        }
        MessageItem item = mList.get(position);
        if (null != item)
        {
            //            viewHolder.classTextView.setText(Util.checkNull(item.getClassName()));
            //            viewHolder.nameTextView.setText(Util.checkNull(item.getTitle()));
            //            viewHolder.dateTextView.setText(TimeUtil.formatPHPTime(item.getDate()));
            //            viewHolder.contentTextView.setText(TimeUtil.formatPHPTime(item.getContent()));
        }
        return convertView;
    }
    
    class ViewHolder
    {
        private View mTitleView;
        
        /**
         * 班级
         */
        TextView classTextView;
        
        /**
         * 姓名
         */
        TextView nameTextView;
        
        /**
         * 消息日期
         */
        TextView dateTextView;
        
        /**
        * 消息内容
        */
        TextView contentTextView;
        
    }
    
}