package com.xweisoft.wx.family.ui.message;

import java.util.ArrayList;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.WXApplication;
import com.xweisoft.wx.family.logic.global.HttpAddressProperties;
import com.xweisoft.wx.family.logic.model.ApplyInfoItem;
import com.xweisoft.wx.family.logic.model.AreaItem;
import com.xweisoft.wx.family.logic.model.response.AreaListResp;
import com.xweisoft.wx.family.ui.BaseActivity;
import com.xweisoft.wx.family.ui.message.view.SelectSchoolPopuWindow;
import com.xweisoft.wx.family.ui.message.view.SelectSchoolPopuWindow.OnSchoolClickListener;
import com.xweisoft.wx.family.util.CommonTitleUtil;
import com.xweisoft.wx.family.util.HttpRequestUtil;
import com.xweisoft.wx.family.util.ListUtil;
import com.xweisoft.wx.family.widget.NetHandler;

/**
 * 预报名选择学校
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-5-20]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ApplySelectActivity extends BaseActivity implements
        OnClickListener
{
    /**
     * 小学
     */
    private ImageView mPrimaryImage;
    
    /**
     * 初中
     */
    private ImageView mJuniorImage;
    
    /**
     * 学校选择框
     */
    private SelectSchoolPopuWindow mPopuWindow;
    
    private int type;
    
    /**
     * 区域集合
     */
    private ArrayList<AreaItem> mList = new ArrayList<AreaItem>();
    
    /**
     * 区域请求响应处理
     */
    private Handler handler = new NetHandler(false)
    {
        
        @Override
        public void onSuccess(String errMsg, Message msg)
        {
            AreaListResp resp = null;
            if (null != msg.obj && msg.obj instanceof AreaListResp)
            {
                resp = (AreaListResp) msg.obj;
                if (!ListUtil.isEmpty(resp.areaList))
                {
                    mList.clear();
                    mList.addAll(resp.areaList);
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
        WXApplication.getInstance().addFinishActivity(this);
        sendRequest();
    }
    
    @Override
    public void initViews()
    {
        CommonTitleUtil.initCommonTitle(this,
                getString(R.string.add_prediction_info),
                null,
                false,
                true);
        mPrimaryImage = (ImageView) findViewById(R.id.apply_select_primary);
        mJuniorImage = (ImageView) findViewById(R.id.apply_select_junior);
        mPopuWindow = new SelectSchoolPopuWindow(mContext);
    }
    
    @Override
    public void bindListener()
    {
        mPrimaryImage.setOnClickListener(this);
        mJuniorImage.setOnClickListener(this);
        mPopuWindow.setOnSchoolClickListener(new OnSchoolClickListener()
        {
            
            @Override
            public void onClick(String schoolId)
            {
                try
                {
                    ApplyInfoItem item = new ApplyInfoItem();
                    item.schoolId = Integer.valueOf(schoolId);
                    item.schoolType = type;
                    item.gradeId = 1;//写死，目前不知道什么用途
                    WXApplication.getInstance().setApplyInfoItem(item);
                    Intent intent = new Intent(mContext,
                            ApplyStudentInfoActivity.class);
                    startActivity(intent);
                }
                catch (Exception e)
                {
                }
            }
        });
    }
    
    @Override
    public int getActivityLayout()
    {
        return R.layout.apply_select_activity;
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.apply_select_primary:
                type = 2;
                break;
            case R.id.apply_select_junior:
                type = 4;
                break;
            default:
                break;
        }
        mPopuWindow.setList(mList, type);
        mPopuWindow.showAsDropDown(findViewById(R.id.common_title_layout));
    }
    
    /**
     * 地区接口请求
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private void sendRequest()
    {
        Map<String, String> param = HttpRequestUtil.getCommonParams(HttpAddressProperties.AREA_LIST);
        param.put("offset", "0");
        param.put("max", "100");
        HttpRequestUtil.sendHttpPostCommonRequest(mContext,
                HttpAddressProperties.SERVICE_URL,
                param,
                AreaListResp.class,
                handler);
    }
}
