package com.xweisoft.wx.family.ui.message;

import java.util.ArrayList;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.WXApplication;
import com.xweisoft.wx.family.logic.global.HttpAddressProperties;
import com.xweisoft.wx.family.logic.model.ApplyItem;
import com.xweisoft.wx.family.logic.model.response.ApplyListResp;
import com.xweisoft.wx.family.ui.BaseActivity;
import com.xweisoft.wx.family.ui.message.adpter.ApplyListAdapter;
import com.xweisoft.wx.family.util.CommonTitleUtil;
import com.xweisoft.wx.family.util.HttpRequestUtil;
import com.xweisoft.wx.family.util.ListUtil;
import com.xweisoft.wx.family.widget.NetHandler;

/**
 * 预报名
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-5-19]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ApplyActivity extends BaseActivity
{
    /**
     * 添加预报名
     */
    private View mAddView;
    
    /**
     * 记录为空view
     */
    private View mEmptyLayout;
    
    /**
     * 记录不为空view
     */
    private View mListLayout;
    
    /**
     * 列表
     */
    private ListView mListView;
    
    /**
     * 预报名集合
     */
    private ArrayList<ApplyItem> mList = new ArrayList<ApplyItem>();
    
    /**
     * 列表适配器
     */
    private ApplyListAdapter mAdapter;
    
    private Handler handler = new NetHandler(false)
    {
        
        @Override
        public void onSuccess(String errMsg, Message msg)
        {
            ApplyListResp resp = null;
            if (null != msg.obj && msg.obj instanceof ApplyListResp)
            {
                resp = (ApplyListResp) msg.obj;
                mListLayout.setVisibility(View.GONE);
                mEmptyLayout.setVisibility(View.GONE);
                if (ListUtil.isEmpty(resp.predictionList))
                {
                    mEmptyLayout.setVisibility(View.VISIBLE);
                }
                else
                {
                    mListLayout.setVisibility(View.VISIBLE);
                    mList.clear();
                    mList.addAll(resp.predictionList);
                    mAdapter.setList(mList);
                }
            }
        }
        
        @Override
        public void onFailed(String errCode, String errMsg, Message msg)
        {
            showToast(errMsg);
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayout());
        initViews();
        bindListener();
        sendRequest();
        WXApplication.getInstance().addFinishActivity(this);
    }
    
    @Override
    public void initViews()
    {
        CommonTitleUtil.initCommonTitle(this,
                getString(R.string.ysh_prediction_info_register),
                null,
                false,
                true);
        mAddView = findViewById(R.id.apply_add_layout);
        mEmptyLayout = findViewById(R.id.apply_empty_layout);
        mListLayout = findViewById(R.id.apply_list_layout);
        mListView = (ListView) findViewById(R.id.apply_listview);
        
        mAdapter = new ApplyListAdapter(mContext);
        mAdapter.setList(mList);
        mListView.setAdapter(mAdapter);
    }
    
    @Override
    public void bindListener()
    {
        mAddView.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mContext, ApplySelectActivity.class);
                startActivity(intent);
            }
        });
    }
    
    @Override
    public int getActivityLayout()
    {
        return R.layout.apply_activity;
    }
    
    /**
     * 发送预报名列表信息请求
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private void sendRequest()
    {
        Map<String, String> param = HttpRequestUtil.getCommonParams(HttpAddressProperties.APPLY_LIST);
        param.put("offset", "0");
        param.put("max", "100");
        HttpRequestUtil.sendHttpPostCommonRequest(mContext,
                HttpAddressProperties.SERVICE_URL,
                param,
                ApplyListResp.class,
                handler);
    }
}