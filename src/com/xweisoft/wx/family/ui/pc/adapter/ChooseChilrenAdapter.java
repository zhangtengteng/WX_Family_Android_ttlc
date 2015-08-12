package com.xweisoft.wx.family.ui.pc.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.WXApplication;
import com.xweisoft.wx.family.logic.global.GlobalVariable;
import com.xweisoft.wx.family.logic.initialize.InitManager;
import com.xweisoft.wx.family.logic.model.ChildrenItem;
import com.xweisoft.wx.family.ui.adapter.ListViewAdapter;
import com.xweisoft.wx.family.util.Util;

/**
 * 孩子列表适配器
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-5-20]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ChooseChilrenAdapter extends ListViewAdapter<ChildrenItem>
{
    
    private int selection = -1;
    
    public ChooseChilrenAdapter(Context context)
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
            convertView = mInflater.inflate(R.layout.choose_children_item, null);
            holder.mLayoutView = convertView.findViewById(R.id.choose_children_item_layout);
            holder.mHeaderImage = (ImageView) convertView.findViewById(R.id.choose_children_item_header);
            holder.mName = (TextView) convertView.findViewById(R.id.choose_children_item_name);
            holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.choose_children_item_checkbox);
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
        if (GlobalVariable.screenWidth == 0)
        {
            InitManager.getInstance().getDisplay((Activity) mContext);
        }
        LayoutParams params = (LayoutParams) holder.mLayoutView.getLayoutParams();
        params.width = GlobalVariable.screenWidth / 2;
        holder.mLayoutView.setLayoutParams(params);
        if (position == selection)
        {
            holder.mCheckBox.setChecked(true);
        }
        else
        {
            holder.mCheckBox.setChecked(false);
        }
        holder.mCheckBox.setTag(position);
        holder.mHeaderImage.setTag(holder.mCheckBox);
        holder.mHeaderImage.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                CheckBox checkBox = (CheckBox) v.getTag();
                if (checkBox.isChecked())
                {
                    checkBox.setChecked(false);
                }
                else
                {
                    checkBox.setChecked(true);
                }
            }
        });
        holder.mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked)
            {
                if (isChecked)
                {
                    selection = (Integer) buttonView.getTag();
                    notifyDataSetChanged();
                }
                else
                {
                    selection = -1;
                }
            }
        });
        
        //针对只有一个孩子的时候，默认选中
        int size = getCount();
        if (size == 1)
        {
            holder.mCheckBox.setChecked(true);
            selection = 0;
        }
        
        ChildrenItem item = mList.get(position);
        if (null != item)
        {
            holder.mName.setText(Util.checkNull(item.studentName));
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
     * @version  [版本号, 2015-5-20]
     * @see  [相关类/方法]
     * @since  [产品/模块版本]
     */
    private class ViewHolder
    {
        /**
         * 头像   
         */
        private ImageView mHeaderImage;
        
        /**
         * checkbox(单选)
         */
        private CheckBox mCheckBox;
        
        /**
         * 名称
         */
        private TextView mName;
        
        /**
         * item布局
         */
        private View mLayoutView;
    }
    
    public ChildrenItem getSelection()
    {
        if (selection != -1)
        {
            return mList.get(selection);
        }
        return null;
    }
}
