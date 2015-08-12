package com.xweisoft.wx.family.ui.message.adpter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.logic.model.AreaItem;
import com.xweisoft.wx.family.logic.model.SchoolItem;
import com.xweisoft.wx.family.ui.adapter.ListViewAdapter;
import com.xweisoft.wx.family.util.Util;

public class SchoolListAdapter extends ListViewAdapter<SchoolItem>
{
    
    public SchoolListAdapter(Context context)
    {
        super(context);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Viewholder holder = null;
        if (null == convertView)
        {
            holder = new Viewholder();
            convertView = mInflater.inflate(R.layout.apply_select_school_item,
                    null);
            holder.mNameText = (TextView) convertView.findViewById(R.id.apply_select_school_item_text);
            convertView.setTag(holder);
        }
        else
        {
            holder = (Viewholder) convertView.getTag();
        }
        if (null == mList)
        {
            return convertView;
        }
        SchoolItem item = mList.get(position);
        if (null != item)
        {
            holder.mNameText.setText(Util.checkNull(item.schoolName));
        }
        return convertView;
    }
    
    private class Viewholder
    {
        private TextView mNameText;
    }
}
