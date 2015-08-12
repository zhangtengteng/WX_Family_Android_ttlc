package com.xweisoft.wx.family.widget;

import java.util.ArrayList;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.ui.adapter.ListViewAdapter;

import android.content.Context;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class CommonPopupWindow<T> extends MyPopupWindow
{
    
    private ListView listView;
    
    private ListViewAdapter<T> adapter;
    
    private ArrayList<T> list;
    
    private OnItemClickListener onItemClickListener;
    
    public CommonPopupWindow(Context mContext, ListViewAdapter<T> adapter,
            ArrayList<T> list, OnItemClickListener onItemClickListener)
    {
        super(mContext, R.layout.ysh_listview_pop);
        this.adapter = adapter;
        this.list = list;
        this.onItemClickListener = onItemClickListener;
        initCommonPopupWindow();
        initViews();
        bindLisener();
    }
    
    @Override
    public void initViews()
    {
        listView = (ListView) view.findViewById(R.id.dialog_listview);
        if (null != adapter && null != list)
        {
            adapter.setList(list);
            adapter.setListView(listView);
            listView.setAdapter(adapter);
        }
    }
    
    @Override
    public void bindLisener()
    {
        if (null != onItemClickListener)
        {
            listView.setOnItemClickListener(onItemClickListener);
        }
    }
    
}
