package com.xweisoft.wx.family.widget;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.logic.model.PopuItem;
import com.xweisoft.wx.family.ui.adapter.MyPopWindowListAdapter;

/**
 * <一句话功能简述>
 * popupwindow的父类窗体,弹出在界面底部
 * <功能详细描述>
 * @author  houfangfang
 * @version  [版本号, 2014-3-13]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MyPopWindow extends BasePopWindow
{
    
    /**
     * listviewItem点击事件
     */
    private OnItemClickListener itemClickListener;
    
    private ListView mListView;
    
    private ArrayList<PopuItem> mList = new ArrayList<PopuItem>();
    
    private View mBottomLayout;
    
    /**
     * 对话框列表适配器
     */
    private MyPopWindowListAdapter myPopWindowListAdapter;
    
    /**
     * <默认构造函数>
     * @param layoutId 内容布局id
     */
    public MyPopWindow(Context mContext, int layoutId,
            OnItemClickListener itemClickListener, ArrayList<PopuItem> mList)
    {
        super(mContext, layoutId);
        this.itemClickListener = itemClickListener;
        this.mList = mList;
        initPopupWindow();
        initViews();
        bindLisener();
        initAdapter();
    }
    
    private void initAdapter()
    {
        myPopWindowListAdapter = new MyPopWindowListAdapter(mContext);
        myPopWindowListAdapter.setList(mList);
        mListView.setAdapter(myPopWindowListAdapter);
        myPopWindowListAdapter.notifyDataSetChanged();
    }
    
    @Override
    public void initViews()
    {
        mListView = (ListView) view.findViewById(R.id.my_popwindow_listview);
        mBottomLayout = view.findViewById(R.id.my_popwindow_bottom_layout);
    }
    
    @Override
    public void bindLisener()
    {
        mListView.setOnItemClickListener(itemClickListener);
        mBottomLayout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MyPopWindow.this.dismissWindow();
            }
        });
    }
    
}
