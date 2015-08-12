package com.xweisoft.wx.family.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.GridView;

/**
 * gridView适配器 基类
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  gac
 * @version  [版本号, 2014-6-18]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public abstract class GridViewAdapter<T> extends BaseAdapter
{
    /**
     * 数据结合
     */
    protected ArrayList<T> mList;
    
    /**
     * 上下文
     */
    protected Context mContext;
    
    /**
     * GridView
     */
    protected GridView mGridView;
    
    /**
     * 加载布局文件
     */
    protected LayoutInflater mInflater;
    
    /**
     * 总数量
     */
    protected int mTotalCount = -1;
    
    /**
    * 当前选中的position,默认第一条
    */
    protected int currentSelectedPosition = 0;
    
    /**
     * <默认构造函数>
     */
    public GridViewAdapter(Context context)
    {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }
    
    @Override
    public int getCount()
    {
        if (mList != null)
        {
            return mList.size();
        }
        else
        {
            return 0;
        }
    }
    
    @Override
    public Object getItem(int position)
    {
        return mList == null ? null : mList.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    
    /**
     * 设置数据
     * @param list 数据集合
     */
    public void setList(ArrayList<T> list)
    {
        this.mList = list;
        notifyDataSetChanged();
    }
    
    public ArrayList<T> getList()
    {
        return mList;
    }
    
    /**
     * [一句话功能简述]<BR>
     * [功能详细描述]
     * @param list 数据集合
     */
    public void setList(T[] list)
    {
        ArrayList<T> arrayList = new ArrayList<T>(list.length);
        for (T t : list)
        {
            arrayList.add(t);
        }
        setList(arrayList);
    }
    
    public GridView getListView()
    {
        return mGridView;
    }
    
    public void setGridView(GridView gridView)
    {
        mGridView = gridView;
    }
    
    public void setTotalCount(int totalCount)
    {
        mTotalCount = totalCount;
    }
    
    public void setCurrentSelectedPosition(int currentSelectedPosition)
    {
        this.currentSelectedPosition = currentSelectedPosition;
    }
    
}
