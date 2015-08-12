package com.xweisoft.wx.family.ui.view;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.xweisoft.wx.family.R;

public class LeftFragment extends Fragment implements OnItemClickListener
{
    
    private boolean isFirstOnClick = true;
    
    /** view - 主界面创建的视图 */
    private View view;
    
    /** 左边布局 - 设备类型 */
    private ListView lv = null;
    
    /** 数组 - 设备类型 */
    private myItem[] devices;
    
    /** 适配器 */
    
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        //		view = inflater.inflate(R.layout.ysh_prediction_left_list, null);
        //		lv = (ListView) view.findViewById(R.id.lv);
        devices = new myItem[] { new myItem("秦淮区", true),
                new myItem("玄武区", false), new myItem("建邺区", false),
                new myItem("鼓楼区", false), new myItem("雨花台区", false),
                new myItem("浦口区", false), new myItem("六合区", false),
                new myItem("栖霞区", false), new myItem("江宁区", false),
                new myItem("溧水区", false), new myItem("高淳区", false), };
        //为listview控件配置适配器
        //		adapter = new PredictionLeftListViewAdapter(getActivity(), devices);
        //		lv.setAdapter(adapter);	
        lv.setOnItemClickListener(this);
        return view;
    }
    
    public class myItem
    {
        public String name;
        
        public boolean isSelected;
        
        public myItem(String name, boolean isSelected)
        {
            super();
            this.name = name;
            this.isSelected = isSelected;
        }
    }
    
    FmSelectSchool fmSelectSchool = new FmSelectSchool();
    
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
            long arg3)
    {
        for (int i = 0; i < devices.length; i++)
        {
            if (i == position)
            {
                devices[i].isSelected = true;
            }
            else
            {
                devices[i].isSelected = false;
            }
        }
        
        if (isFirstOnClick)
        {
            isFirstOnClick = false;
        }
        
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.right, fmSelectSchool);
        ft.commit();// 提交
    }
}
