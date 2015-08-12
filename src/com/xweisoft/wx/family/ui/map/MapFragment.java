package com.xweisoft.wx.family.ui.map;

import java.util.ArrayList;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.ui.BaseFragment;
import com.xweisoft.wx.family.ui.map.adapter.MyPagerAdapter;
import com.xweisoft.wx.family.util.LogX;

/**
 * 
 * @author Administrator
 * 
 */
public class MapFragment extends BaseFragment implements OnClickListener {

	ViewPager pager = null;
	@SuppressWarnings("deprecation")
	LocalActivityManager manager = null;
	private LinearLayout llMap;
	private LinearLayout llList;



	@Override
	public void initViews() {
		llMap = (LinearLayout) getActivity().findViewById(R.id.ll_map_map);
		llList = (LinearLayout) getActivity().findViewById(R.id.ll_map_list);
	}

	@Override
	public void bindListener() {
		llMap.setOnClickListener(this);
		llList.setOnClickListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLayouView = inflater.inflate(R.layout.map_fragment, container, false);
		return mLayouView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		manager = new LocalActivityManager(getActivity(), true);
		manager.dispatchCreate(savedInstanceState);
		initViews();
		initPagerViewer();
		bindListener();
	}

	/**
	 * 初始化PageViewer
	 */
	private void initPagerViewer() {
		pager = (ViewPager) getActivity().findViewById(R.id.vp_myorder);
		final ArrayList<View> list = new ArrayList<View>();

		Intent intent = new Intent(getActivity(), LocationDemo.class);
		list.add(getView("0", intent));
		Intent intent2 = new Intent(getActivity(), ListActivity.class);
		list.add(getView("1", intent2));
		pager.setAdapter(new MyPagerAdapter(list));
		pager.setCurrentItem(0);
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				System.out.println("onPageSelected=" + position);
				if (position == 0) {
				}

				if (position == 1) {
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		// pager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/**
	 * 通过activity获取视图
	 * 
	 * @param id
	 * @param intent
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private View getView(String id, Intent intent) {
		return manager.startActivity(id, intent).getDecorView();
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		// 地图
		case R.id.ll_map_map:
			System.out.println("ll_map_map is onClick ~");
			pager.setCurrentItem(0);
			break;
		// 列表
		case R.id.ll_map_list:
			pager.setCurrentItem(1);
			System.out.println("ll_map_list is onClick ~");
			break;
		default:
			break;
		}
	}
}
