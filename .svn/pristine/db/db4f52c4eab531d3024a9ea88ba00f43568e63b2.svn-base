package com.xweisoft.wx.family.ui.message.view;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.logic.global.HttpAddressProperties;
import com.xweisoft.wx.family.logic.model.AreaItem;
import com.xweisoft.wx.family.logic.model.SchoolItem;
import com.xweisoft.wx.family.logic.model.response.SchoolListResp;
import com.xweisoft.wx.family.ui.message.adpter.AreaListAdapter;
import com.xweisoft.wx.family.ui.message.adpter.SchoolListAdapter;
import com.xweisoft.wx.family.util.HttpRequestUtil;
import com.xweisoft.wx.family.util.ListUtil;
import com.xweisoft.wx.family.widget.BasePopWindow;
import com.xweisoft.wx.family.widget.NetHandler;

public class SelectSchoolPopuWindow extends BasePopWindow
{
    /**
     * 
     */
    private ListView mAreaListView;
    
    /**
     * 
     */
    private ListView mSchoolListView;
    
    /**
     * 
     */
    private AreaListAdapter mAreaAdapter;
    
    /**
     * 
     */
    private SchoolListAdapter mSchoolAdapter;
    
    /**
     * 
     */
    private OnSchoolClickListener onSchoolClickListener;
    
    /**
     * 
     */
    private ArrayList<AreaItem> mAreaList;
    
    /**
     * 
     */
    private ArrayList<SchoolItem> mSchoolList = new ArrayList<SchoolItem>();
    
    /**
     * 学校请求响应处理
     */
    private Handler handler = new NetHandler(false)
    {
        
        @Override
        public void onSuccess(String errMsg, Message msg)
        {
            SchoolListResp resp = null;
            if (null != msg.obj && msg.obj instanceof SchoolListResp)
            {
                resp = (SchoolListResp) msg.obj;
                if (!ListUtil.isEmpty(resp.schoolInfoList))
                {
                    mSchoolList.addAll(resp.schoolInfoList);
                }
                mSchoolAdapter.setList(mSchoolList);
            }
        }
        
        @Override
        public void onFailed(String errCode, String errMsg, Message msg)
        {
        }
    };
    
    private int type;
    
    public SelectSchoolPopuWindow(Context mContext)
    {
        super(mContext, R.layout.apply_select_school_popuwindow);
        initPopupWindow();
        initViews();
        bindLisener();
    }
    
    @Override
    public void initViews()
    {
        popupWindow.setAnimationStyle(R.style.left_popuwindow_style);
        mAreaListView = (ListView) view.findViewById(R.id.apply_select_area_listview);
        mSchoolListView = (ListView) view.findViewById(R.id.apply_select_school_listview);
        mAreaAdapter = new AreaListAdapter(mContext);
        mAreaAdapter.setList(mAreaList);
        mAreaListView.setAdapter(mAreaAdapter);
        
        mSchoolAdapter = new SchoolListAdapter(mContext);
        mSchoolAdapter.setList(mSchoolList);
        mSchoolListView.setAdapter(mSchoolAdapter);
    }
    
    public void setList(ArrayList<AreaItem> list, int type)
    {
        this.type = type;
        if (ListUtil.isEmpty(list))
        {
            dismissWindow();
        }
        else
        {
            mAreaList = list;
            mAreaAdapter.setList(list);
            setSchoolList(0);
        }
    }
    
    @Override
    public void bindLisener()
    {
        mAreaListView.setOnItemClickListener(new OnItemClickListener()
        {
            
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id)
            {
                if (position >= 0 && position < mAreaList.size()
                        && null != mAreaList.get(position))
                {
                    setSchoolList(position);
                }
            }
        });
        mSchoolListView.setOnItemClickListener(new OnItemClickListener()
        {
            
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id)
            {
                dismissWindow();
                if (null != onSchoolClickListener
                        && null != mSchoolList.get(position))
                {
                    onSchoolClickListener.onClick(mSchoolList.get(position).schoolId);
                }
            }
        });
    }
    
    public OnSchoolClickListener getOnSchoolClickListener()
    {
        return onSchoolClickListener;
    }
    
    public void setOnSchoolClickListener(
            OnSchoolClickListener onSchoolClickListener)
    {
        this.onSchoolClickListener = onSchoolClickListener;
    }
    
    public interface OnSchoolClickListener
    {
        void onClick(String schoolId);
    }
    
    private void setSchoolList(int position)
    {
        if (null != mAreaList.get(position))
        {
            Map<String, String> param = HttpRequestUtil.getCommonParams(HttpAddressProperties.AREA_SCHOOL_LIST);
            param.put("offset", "0");
            param.put("max", "100");
            param.put("addressCode", mAreaList.get(position).addressCode);
            param.put("schoolType", "" + type);
            HttpRequestUtil.sendHttpPostCommonRequest(mContext,
                    HttpAddressProperties.SERVICE_URL,
                    param,
                    SchoolListResp.class,
                    handler);
        }
        mSchoolList.clear();
        mSchoolAdapter.setList(mSchoolList);
        mAreaAdapter.setSelection(position);
    }
}
