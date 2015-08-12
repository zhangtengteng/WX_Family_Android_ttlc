package com.xweisoft.wx.family.ui.message.adpter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.logic.model.ApplyItem;
import com.xweisoft.wx.family.ui.adapter.ListViewAdapter;
import com.xweisoft.wx.family.util.DateTools;
import com.xweisoft.wx.family.util.Util;

/**
 * 预报名列表适配器
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-5-19]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ApplyListAdapter extends ListViewAdapter<ApplyItem>
{
    
    public ApplyListAdapter(Context context)
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
            convertView = mInflater.inflate(R.layout.apply_list_item, null);
            holder.mStudentText = (TextView) convertView.findViewById(R.id.apply_item_name);
            holder.mNumberText = (TextView) convertView.findViewById(R.id.apply_item_number);
            holder.mSchoolText = (TextView) convertView.findViewById(R.id.apply_item_school);
            holder.mTimeText = (TextView) convertView.findViewById(R.id.apply_item_time);
            holder.mStatusText = (TextView) convertView.findViewById(R.id.apply_item_status);
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
        ApplyItem item = mList.get(position);
        if (null != item)
        {
            holder.mStudentText.setText(Util.checkNull(item.studentName));
            holder.mNumberText.setText(Util.checkNull(item.predictionId));
            holder.mSchoolText.setText(Util.checkNull(item.schoolName));
            if ("1".equals(item.status))
            {
                holder.mStatusText.setText("已通过");
            }
            else if ("2".equals(item.status))
            {
                holder.mStatusText.setText("未通过");
            }
            else if ("3".equals(item.status))
            {
                holder.mStatusText.setText("审核中");
            }
            if (TextUtils.isEmpty(item.approveTime))
            {
                holder.mTimeText.setVisibility(View.GONE);
            }
            else
            {
                holder.mTimeText.setVisibility(View.VISIBLE);
                holder.mTimeText.setText(DateTools.parseTimeMillis2Str(item.approveTime,
                        DateTools.YYYY_MM_DD));
            }
        }
        return convertView;
    }
    
    /**
     * 复用组件
     * <一句话功能简述>
     * <功能详细描述>
     * 
     * @author  poorgod
     * @version  [版本号, 2015-5-19]
     * @see  [相关类/方法]
     * @since  [产品/模块版本]
     */
    private class ViewHolder
    {
        /**
         * 学生
         */
        private TextView mStudentText;
        
        /**
         * 预报名编号
         */
        private TextView mNumberText;
        
        /**
         * 学校名称
         */
        private TextView mSchoolText;
        
        /**
         * 时间
         */
        private TextView mTimeText;
        
        /**
         * 状态
         */
        private TextView mStatusText;
    }
}
