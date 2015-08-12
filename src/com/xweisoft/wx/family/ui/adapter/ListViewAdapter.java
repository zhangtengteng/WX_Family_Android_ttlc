package com.xweisoft.wx.family.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  yangchao
 * @version  [版本号, 2013-10-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public abstract class ListViewAdapter<T> extends BaseAdapter
{
	protected ArrayList<T> mList;
	protected Context mContext;
	protected ListView mListView;
	protected LayoutInflater mInflater;
	
	protected int mTotalCount = -1;
	 /**
     * 当前选中的position,默认第一条
     */
	protected int currentSelectedPosition = 0;
	/**
	 * [构造简要说明]
	 * @param context 上下文
	 */
	public ListViewAdapter(Context context)
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
	
	public ListView getListView()
	{
		return mListView;
	}
	
	public void setListView(ListView listView)
	{
		mListView = listView;
	}

	public void setTotalCount(int totalCount)
	{
		mTotalCount = totalCount;
	}
	
	public  void setCurrentSelectedPosition(int currentSelectedPosition)
	{
	    this.currentSelectedPosition = currentSelectedPosition;
	}
}
