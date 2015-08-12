package com.xweisoft.wx.family.ui.message.adpter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.logic.model.AreaItem;
import com.xweisoft.wx.family.ui.adapter.ListViewAdapter;
import com.xweisoft.wx.family.util.Util;

public class AreaListAdapter extends ListViewAdapter<AreaItem>
{
    
    private int selection = 0;
    
    public AreaListAdapter(Context context)
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
            convertView = mInflater.inflate(R.layout.apply_select_area_item,
                    null);
            holder.mItemLayout = convertView.findViewById(R.id.apply_select_area_item_layout);
            holder.mNameText = (TextView) convertView.findViewById(R.id.apply_select_area_item_text);
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
        if (position == selection)
        {
            holder.mItemLayout.setBackgroundResource(R.drawable.wx_apply_area_selected_bg);
            holder.mNameText.setTextColor(mContext.getResources()
                    .getColor(R.color.violet_8B6BBE_color));
        }
        else
        {
            holder.mItemLayout.setBackgroundResource(R.drawable.wx_transparent);
            holder.mNameText.setTextColor(mContext.getResources()
                    .getColor(R.color.white_color));
        }
        AreaItem item = mList.get(position);
        if (null != item)
        {
            holder.mNameText.setText(Util.checkNull(item.addressName));
        }
        return convertView;
    }
    
    private class Viewholder
    {
        private TextView mNameText;
        
        private View mItemLayout;
        
    }
    
    public int getSelection()
    {
        return selection;
    }
    
    public void setSelection(int selection)
    {
        this.selection = selection;
        notifyDataSetChanged();
    }
    
}
