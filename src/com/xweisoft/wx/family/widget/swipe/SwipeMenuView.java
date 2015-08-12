package com.xweisoft.wx.family.widget.swipe;

import java.util.List;

import com.xweisoft.wx.family.util.Util;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * @author baoyz
 * @date 2014-8-23
 * 
 */
public class SwipeMenuView extends LinearLayout implements OnClickListener
{
    
    private ListView mListView;
    
    private SwipeMenuLayout mLayout;
    
    private SwipeMenu mMenu;
    
    private OnSwipeItemClickListener onItemClickListener;
    
    private int position;
    
    public int getPosition()
    {
        return position;
    }
    
    public void setPosition(int position)
    {
        this.position = position;
    }
    
    public SwipeMenuView(SwipeMenu menu)
    {
        super(menu.getContext());
        mMenu = menu;
        List<SwipeMenuItem> items = menu.getMenuItems();
        int id = 0;
        for (SwipeMenuItem item : items)
        {
            addItem(item, id++);
        }
    }
    
    public SwipeMenuView(SwipeMenu menu, ListView listView)
    {
        super(menu.getContext());
        mListView = listView;
        mMenu = menu;
        List<SwipeMenuItem> items = menu.getMenuItems();
        int id = 0;
        for (SwipeMenuItem item : items)
        {
            addItem(item, id++);
        }
    }
    
    private void addItem(SwipeMenuItem item, int id)
    {
        LayoutParams params = new LayoutParams(item.getWidth(),
                item.getHeight());
        LinearLayout parent = new LinearLayout(getContext());
        parent.setId(id);
        parent.setGravity(Gravity.CENTER);
        parent.setOrientation(LinearLayout.VERTICAL);
        parent.setLayoutParams(params);
        parent.setBackgroundDrawable(item.getBackground());
        parent.setOnClickListener(this);
        addView(parent);
        
        if (item.getIcon() != null)
        {
            parent.addView(createIcon(item));
        }
        if (!TextUtils.isEmpty(item.getTitle()))
        {
            parent.addView(createTitle(item));
        }
        
    }
    
    private ImageView createIcon(SwipeMenuItem item)
    {
        ImageView iv = new ImageView(getContext());
        iv.setLayoutParams(new LayoutParams(Util.dpToPixel(getContext(), 25),
                Util.dpToPixel(getContext(), 25)));
        iv.setImageDrawable(item.getIcon());
        return iv;
    }
    
    private TextView createTitle(SwipeMenuItem item)
    {
        TextView tv = new TextView(getContext());
        tv.setText(item.getTitle());
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(item.getTitleSize());
        tv.setTextColor(item.getTitleColor());
        return tv;
    }
    
    @Override
    public void onClick(View v)
    {
        if (onItemClickListener != null && mLayout.isOpen())
        {
            onItemClickListener.onItemClick(this, mMenu, v.getId());
        }
    }
    
    public OnSwipeItemClickListener getOnSwipeItemClickListener()
    {
        return onItemClickListener;
    }
    
    public void setOnSwipeItemClickListener(
            OnSwipeItemClickListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;
    }
    
    public void setLayout(SwipeMenuLayout mLayout)
    {
        this.mLayout = mLayout;
    }
}
