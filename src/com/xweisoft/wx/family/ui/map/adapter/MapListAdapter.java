package com.xweisoft.wx.family.ui.map.adapter;

import com.xweisoft.wx.family.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MapListAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;

	public MapListAdapter(Context context) {
		this.context = context;
		inflater = inflater.from(context);
	}

	@Override
	public int getCount() {
		return 10;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		view = inflater.inflate(R.layout.map_fragment_list_item, null);
		return view;
	}

}
