package com.xweisoft.wx.family.ui.message.adpter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.logic.model.MsgContentItem;
import com.xweisoft.wx.family.logic.model.MsgNoticeItem;
import com.xweisoft.wx.family.ui.adapter.ListViewAdapter;
import com.xweisoft.wx.family.ui.message.view.CancelDialog;
import com.xweisoft.wx.family.ui.message.view.CancelDialog.OnConfirmClickListener;
import com.xweisoft.wx.family.util.DateTools;
import com.xweisoft.wx.family.util.Util;
import com.xweisoft.wx.family.widget.BaseViewHolder;

/**
 * 通知列表适配器
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-7-16]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class NotificationListAdapter extends ListViewAdapter<MsgContentItem>
{
    
    public NotificationListAdapter(Context context)
    {
        super(context);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        if (null == convertView)
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.wx_notification_list_item,
                    null);
            holder.mDayText = (TextView) convertView.findViewById(R.id.notification_item_day);
            holder.mMonthText = (TextView) convertView.findViewById(R.id.notification_item_month);
            holder.mTitleText = (TextView) convertView.findViewById(R.id.notification_item_title);
            holder.mTeacherText = (TextView) convertView.findViewById(R.id.notification_item_teacher);
            holder.mContentText = (TextView) convertView.findViewById(R.id.notification_item_content);
            holder.mDeleteImage = (ImageView) convertView.findViewById(R.id.notification_item_delete);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        if (null == mList)
        {
            return convertView;
        }
        MsgContentItem item = mList.get(position);
        if (null != item)
        {
            MsgNoticeItem noticeItem = item.notice;
            if (null != noticeItem)
            {
                holder.mTitleText.setText(Util.checkNull(noticeItem.title));
                holder.mContentText.setText(Util.fromHtml(noticeItem.content));
                holder.mTeacherText.setText("发布者："
                        + Util.checkNull(noticeItem.sendUser));
                holder.mDayText.setText(DateTools.parseTimeMillis2StrArray(noticeItem.publishTime)[1]);
                holder.mMonthText.setText(DateTools.parseTimeMillis2StrArray(noticeItem.publishTime)[0]
                        + "月");
            }
        }
        holder.mDeleteImage.setTag(position);
        holder.mDeleteImage.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(final View v)
            {
                CancelDialog dialog = new CancelDialog(mContext, "提示",
                        "确认删除通知消息", new OnConfirmClickListener()
                        {
                            
                            @Override
                            public void onConfirmClick()
                            {
                                int position = (Integer) v.getTag();
                                mList.remove(position);
                                notifyDataSetChanged();
                            }
                        });
                dialog.showDialog();
            }
        });
        return convertView;
    }
    
    /**
     * 组件复用类
     * <一句话功能简述>
     * <功能详细描述>
     * 
     * @author  poorgod
     * @version  [版本号, 2015-7-16]
     * @see  [相关类/方法]
     * @since  [产品/模块版本]
     */
    private class ViewHolder extends BaseViewHolder
    {
        /**
         * 天
         */
        private TextView mDayText;
        
        /**
         * 月
         */
        private TextView mMonthText;
        
        /**
         * 标题
         */
        private TextView mTitleText;
        
        /**
         * 内容
         */
        private TextView mContentText;
        
        /**
         * 发布者
         */
        private TextView mTeacherText;
        
        /**
         * 删除
         */
        private ImageView mDeleteImage;
    }
}
