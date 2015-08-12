package com.xweisoft.wx.family.widget.swipe;

import com.xweisoft.wx.family.widget.BaseViewHolder;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.WrapperListAdapter;

/**
 * 
 * @author baoyz
 * @date 2014-8-24
 * 
 */
public class SwipeMenuAdapter implements WrapperListAdapter,
        OnSwipeItemClickListener
{
    
    private ListAdapter mAdapter;
    
    private Context mContext;
    
    private OnMenuItemClickListener onMenuItemClickListener;
    
    public SwipeMenuAdapter(Context context, ListAdapter adapter)
    {
        mAdapter = adapter;
        mContext = context;
    }
    
    @Override
    public int getCount()
    {
        return mAdapter.getCount();
    }
    
    @Override
    public Object getItem(int position)
    {
        return mAdapter.getItem(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        return mAdapter.getItemId(position);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        SwipeMenuLayout layout = null;
        if (convertView == null)
        {
            View contentView = mAdapter.getView(position, convertView, parent);
            SwipeMenu menu = new SwipeMenu(mContext);
            menu.setViewType(mAdapter.getItemViewType(position));
            createMenu(menu);
            SwipeMenuView menuView = new SwipeMenuView(menu);
            menuView.setOnSwipeItemClickListener(this);
            layout = new SwipeMenuLayout(contentView, menuView, null, null);
            layout.setPosition(position);
            if (null != contentView.getTag())
            {
                layout.setAvailable(((BaseViewHolder) contentView.getTag()).isAvailable);
            }
        }
        else
        {
            layout = (SwipeMenuLayout) convertView;
            layout.closeMenu();
            layout.setPosition(position);
            View view = mAdapter.getView(position,
                    layout.getContentView(),
                    parent);
            if (null != view.getTag())
            {
                layout.setAvailable(((BaseViewHolder) view.getTag()).isAvailable);
            }
        }
        return layout;
    }
    
    public void createMenu(SwipeMenu menu)
    {
    }
    
    @Override
    public void onItemClick(SwipeMenuView view, SwipeMenu menu, int index)
    {
        if (onMenuItemClickListener != null)
        {
            onMenuItemClickListener.onMenuItemClick(view.getPosition(),
                    menu,
                    index);
        }
    }
    
    public void setOnMenuItemClickListener(
            OnMenuItemClickListener onMenuItemClickListener)
    {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }
    
    @Override
    public void registerDataSetObserver(DataSetObserver observer)
    {
        mAdapter.registerDataSetObserver(observer);
    }
    
    @Override
    public void unregisterDataSetObserver(DataSetObserver observer)
    {
        mAdapter.unregisterDataSetObserver(observer);
    }
    
    @Override
    public boolean areAllItemsEnabled()
    {
        return mAdapter.areAllItemsEnabled();
    }
    
    @Override
    public boolean isEnabled(int position)
    {
        return mAdapter.isEnabled(position);
    }
    
    @Override
    public boolean hasStableIds()
    {
        return mAdapter.hasStableIds();
    }
    
    @Override
    public int getItemViewType(int position)
    {
        return mAdapter.getItemViewType(position);
    }
    
    @Override
    public int getViewTypeCount()
    {
        return mAdapter.getViewTypeCount();
    }
    
    @Override
    public boolean isEmpty()
    {
        return mAdapter.isEmpty();
    }
    
    @Override
    public ListAdapter getWrappedAdapter()
    {
        return mAdapter;
    }
    
}