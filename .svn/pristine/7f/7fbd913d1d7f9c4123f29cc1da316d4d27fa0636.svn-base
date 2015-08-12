package com.xweisoft.wx.family.ui.contact.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.WXApplication;
import com.xweisoft.wx.family.logic.model.ContactItem;
import com.xweisoft.wx.family.ui.adapter.ListViewAdapter;
import com.xweisoft.wx.family.util.Util;
import com.xweisoft.wx.family.widget.BaseViewHolder;

/**
 * 通讯录列表适配器
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-7-20]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ContactsListAdapter extends ListViewAdapter<ContactItem>
{
    /**
     * 显示标题
     */
    private boolean showTitle = true;
    
    /**
     * <默认构造函数>
     */
    public ContactsListAdapter(Context context)
    {
        super(context);
    }
    
    public ContactsListAdapter(Context context, boolean showTitle)
    {
        super(context);
        this.showTitle = showTitle;
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        if (null == convertView)
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.contacts_listview_item,
                    null);
            holder.mRoleView = convertView.findViewById(R.id.contact_item_role_layout);
            holder.mRoleText = (TextView) convertView.findViewById(R.id.contact_item_role_text);
            holder.mHeaderImage = (ImageView) convertView.findViewById(R.id.contact_item_header);
            holder.mNameText = (TextView) convertView.findViewById(R.id.contact_item_name);
            holder.mAppellationText = (TextView) convertView.findViewById(R.id.contact_item_appellation);
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
        ContactItem item = mList.get(position);
        if (null != item)
        {
            if (showTitle)
            {
                String role = item.role;
                String prevRole = (position - 1) >= 0
                        && null != mList.get(position - 1) ? mList.get(position - 1).role
                        : "";
                if (!role.equals(prevRole))
                {
                    if ("1".equals(role))
                    {
                        holder.mRoleText.setText("老师");
                    }
                    else if ("2".equals(role))
                    {
                        holder.mRoleText.setText("家长");
                    }
                    holder.mRoleView.setVisibility(View.VISIBLE);
                }
                else
                {
                    holder.mRoleView.setVisibility(View.GONE);
                }
            }
            holder.mNameText.setText(Util.checkNull(item.name));
            holder.mAppellationText.setText(Util.checkNull(item.appellation));
            ImageLoader.getInstance().displayImage(item.portraitPath,
                    holder.mHeaderImage,
                    WXApplication.getInstance().optionsCircle);
        }
        return convertView;
    }
    
    /**
     * 组件复用类
     * <一句话功能简述>
     * <功能详细描述>
     * 
     * @author  poorgod
     * @version  [版本号, 2015-7-20]
     * @see  [相关类/方法]
     * @since  [产品/模块版本]
     */
    private class ViewHolder extends BaseViewHolder
    {
        /**
         * 头像
         */
        private ImageView mHeaderImage;
        
        /**
         * 名称
         */
        private TextView mNameText;
        
        /**
         * 称谓
         */
        private TextView mAppellationText;
        
        /**
         * 角色view
         */
        private View mRoleView;
        
        /**
         * 角色
         */
        private TextView mRoleText;
    }
    
}
