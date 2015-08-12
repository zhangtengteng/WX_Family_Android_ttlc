package com.xweisoft.wx.family.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.logic.model.PopuItem;

/**
 * <一句话功能简述>
 *  对话框列表适配器
 * <功能详细描述> 
 * @author  houfangfang
 * @version  [版本号, 2014-6-23]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MyPopWindowListAdapter extends ListViewAdapter<PopuItem>
{
    
    /**
     * <默认构造函数>
     */
    public MyPopWindowListAdapter(Context context)
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
            convertView = mInflater.inflate(R.layout.ysh_my_popwindow_list_item,
                    null);
            holder.mNameView = (TextView) convertView.findViewById(R.id.my_popwindow_list_item_name_textview);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mList != null && mList.size() > position)
        {
            String name = mList.get(position).getName();
            holder.mNameView.setText(name);
        }
        return convertView;
    }
    
    /**
     * 列表组件复用类
     * <一句话功能简述>
     * <功能详细描述>
     * 
     * @author  gac
     * @version  [版本号, 2014-6-20]
     * @see  [相关类/方法]
     * @since  [产品/模块版本]
     */
    private class ViewHolder
    {
        
        /**
         * 名称
         */
        private TextView mNameView;
        
    }
}
