package com.xweisoft.wx.family.ui.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FmSelectSchool extends Fragment
{
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        //		view = inflater.inflate(R.layout.ysh_prediction_right_content_listview, null);
        //		lv = (ListView) view.findViewById(R.id.lv);
        //		devices = new myItem[] {new myItem("秦淮区",true),new myItem("玄武区",false),
        //				new myItem("建邺区",false),new myItem("鼓楼区",false),new myItem("雨花台区",false),new myItem("浦口区",false),
        //				new myItem("六合区",false),new myItem("栖霞区",false),new myItem("江宁区",false),
        //				new myItem("溧水区",false),new myItem("高淳区",false),};
        
        String[] data = { "游府西街小学", "五老村小学", "三条巷小学", "南京航天航空附小", "南京芳草路小学",
                "南京天妃宫小学", "南京仙林外国语小学" };
        
        //为listview控件配置适配器
        
        return new View(getActivity());
    }
}
