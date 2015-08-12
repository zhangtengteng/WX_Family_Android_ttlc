package com.xweisoft.wx.family.ui.message.adpter;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.logic.model.MsgContentItem;
import com.xweisoft.wx.family.logic.model.MsgNoticeItem;
import com.xweisoft.wx.family.ui.adapter.ListViewAdapter;
import com.xweisoft.wx.family.util.DateTools;
import com.xweisoft.wx.family.util.Util;
import com.xweisoft.wx.family.widget.BaseViewHolder;

public class WorkListAdapter extends ListViewAdapter<MsgContentItem>
{
    
    public WorkListAdapter(Context context)
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
            convertView = mInflater.inflate(R.layout.work_list_item, null);
            holder.mDateView = convertView.findViewById(R.id.work_list_item_date_view);
            holder.mDateText = (TextView) convertView.findViewById(R.id.work_list_item_date_text);
            
            holder.mSubjectText = (TextView) convertView.findViewById(R.id.work_list_item_subject);
            holder.mTeacherText = (TextView) convertView.findViewById(R.id.work_list_item_teacher);
            holder.mTimeText = (TextView) convertView.findViewById(R.id.work_list_item_time);
            holder.mContentText = (TextView) convertView.findViewById(R.id.work_list_item_content);
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
            MsgNoticeItem msg = item.notice;
            if (null != msg)
            {
                if (position == 0)
                {
                    holder.mDateView.setVisibility(View.VISIBLE);
                }
                else
                {
                    if (null != mList.get(position - 1)
                            && null != mList.get(position - 1).notice)
                    {
                        if (DateTools.parseTimeMillis2Str(msg.publishTime,
                                DateTools.YYYY_MM_DD)
                                .equals(DateTools.parseTimeMillis2Str(mList.get(position - 1).notice.publishTime,
                                        DateTools.YYYY_MM_DD)))
                        {
                            holder.mDateView.setVisibility(View.GONE);
                        }
                        else
                        {
                            holder.mDateView.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        holder.mDateView.setVisibility(View.GONE);
                    }
                }
                holder.mSubjectText.setText(Util.checkNull(msg.title));
                holder.mTeacherText.setText(Util.checkNull(msg.sendUser));
                holder.mTimeText.setText(DateTools.parseTimeMillis2Str(msg.publishTime,
                        DateTools.HH_MM));
                
                holder.mDateText.setText(getWeek(msg.publishTime)
                        + "   "
                        + DateTools.parseTimeMillis2Str(msg.publishTime,
                                DateTools.YYYY_MM_DD));
                holder.mContentText.setText(Util.fromHtml(msg.content));
            }
        }
        return convertView;
    }
    
    private class ViewHolder extends BaseViewHolder
    {
        private TextView mSubjectText;
        
        private TextView mTeacherText;
        
        private TextView mTimeText;
        
        private View mDateView;
        
        private TextView mDateText;
        
        private TextView mContentText;
        
    }
    
    /**
     * 
     * <一句话功能简述>
     * <功能详细描述>
     * @param timeStamp
     * @return [参数说明]
     * 
     * @return String [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private static String getWeek(String timeStamp)
    {
        long time = Long.parseLong(timeStamp);
        int mydate = 0;
        String week = null;
        Calendar cd = Calendar.getInstance();
        cd.setTime(new Date(time));
        mydate = cd.get(Calendar.DAY_OF_WEEK);
        // 获取指定日期转换成星期几
        if (mydate == 1)
        {
            week = "周日";
        }
        else if (mydate == 2)
        {
            week = "周一";
        }
        else if (mydate == 3)
        {
            week = "周二";
        }
        else if (mydate == 4)
        {
            week = "周三";
        }
        else if (mydate == 5)
        {
            week = "周四";
        }
        else if (mydate == 6)
        {
            week = "周五";
        }
        else if (mydate == 7)
        {
            week = "周六";
        }
        return week;
    }
}
