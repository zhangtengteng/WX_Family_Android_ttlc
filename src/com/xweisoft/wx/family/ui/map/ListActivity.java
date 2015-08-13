package com.xweisoft.wx.family.ui.map;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.ui.BaseActivity;
import com.xweisoft.wx.family.ui.map.adapter.MapListAdapter;

public class ListActivity extends BaseActivity{
	private ListView listView;
	private MapListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_fragment_list_activity);
		initViews();
		bindListener();
	}
	@Override
	public void initViews() {
		listView = (ListView) findViewById(R.id.lv_map_list);
		adapter = new MapListAdapter(getApplicationContext());
		listView.setAdapter(adapter);
	}

	@Override
	public void bindListener() {
		
	}

	@Override
	public int getActivityLayout() {
		return 0;
	}

}
