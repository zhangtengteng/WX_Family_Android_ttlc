/*
 * 文 件 名:  SettingActivity.java
 * 描    述:  设置页面
 * 创 建 人:  李晨光
 * 创建时间:  2014年6月18日
 */
package com.xweisoft.wx.family.ui.setting;

import java.io.File;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.logic.global.GlobalConstant;
import com.xweisoft.wx.family.logic.global.GlobalVariable;
import com.xweisoft.wx.family.logic.global.HttpAddressProperties;
import com.xweisoft.wx.family.logic.global.NetWorkCodes.CommontErrorCodes;
import com.xweisoft.wx.family.logic.global.NetWorkCodes.CommontSuccessCodes;
import com.xweisoft.wx.family.logic.model.VersionItem;
import com.xweisoft.wx.family.logic.model.response.VersionResp;
import com.xweisoft.wx.family.logic.request.DownClientApkRequest;
import com.xweisoft.wx.family.ui.BaseActivity;
import com.xweisoft.wx.family.util.CommonTitleUtil;
import com.xweisoft.wx.family.util.FileHelper;
import com.xweisoft.wx.family.util.HttpRequestUtil;
import com.xweisoft.wx.family.util.ProgressUtil;
import com.xweisoft.wx.family.util.StringUtil;
import com.xweisoft.wx.family.widget.CommonDialog;
import com.xweisoft.wx.family.widget.CommonDialog.OnButtonClickListener;
import com.xweisoft.wx.family.widget.NetHandler;

/**
 * 设置页面
 * 
 * @author  李晨光
 * @version  [版本号, 2014年6月18日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class SettingActivity extends BaseActivity implements OnClickListener
{
    
    private View aboutusView = null;
    
    private View feedbackView = null;
    
    private View clearView = null;
    
    private View upgrateView = null;
    
    private ProgressDialog progressDialog = null;
    
    /**
     * 通知更新下载进度条的标识
     */
    public static final int UPDATE_PROGRESSBAR = 1001;
    
    /**
     * 升级Handler
     */
    private Handler updateHandler = new NetHandler()
    {
        
        @Override
        public void onSuccess(String errMsg, Message msg)
        {
            if (null != msg.obj && msg.obj instanceof VersionResp)
            {
                VersionResp resp = (VersionResp) msg.obj;
                dealVersionUpdate(resp);
            }
        }
        
        @Override
        public void onFailed(String errCode, String errMsg, Message msg)
        {
            showToast(errMsg);
        }
    };
    
    /**
     * 下载客户端
     */
    private Handler downClientHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case CommontSuccessCodes.SUCCESS:
                    // 下载成功后进行客户端更新
                    closeProgressDialog();
                    Intent it = (Intent) msg.obj;
                    startActivity(it);
                    return;
                    
                case UPDATE_PROGRESSBAR:
                    // 更新下载进度条
                    progressDialog.setMessage(getString(R.string.update_downloading_percent,
                            msg.arg1 + "%"));
                    return;
                    
                case CommontErrorCodes.SYSTEM_ERROR:
                case CommontErrorCodes.OTHER_ERROR:
                    closeProgressDialog();
                    showToast(getString(R.string.system_error));
                    break;
                case CommontErrorCodes.NETWORK_ERROR:
                    closeProgressDialog();
                    showToast(getString(R.string.network_error));
                    break;
                case CommontErrorCodes.NETWORK_TIMEOUT_ERROR:
                    closeProgressDialog();
                    showToast(getString(R.string.network_timeout));
                    break;
                default:
                    closeProgressDialog();
                    showToast(getString(R.string.update_client_fail));
                    break;
            }
        };
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayout());
        initViews();
        bindListener();
        
    }
    
    @Override
    public void initViews()
    {
        CommonTitleUtil.initCommonTitle(this,
                getString(R.string.ysh_setting),
                null,
                false,
                true);
        aboutusView = findViewById(R.id.aboutus_view);
        feedbackView = findViewById(R.id.feedback_view);
        clearView = findViewById(R.id.clear_view);
        upgrateView = findViewById(R.id.upgrate_view);
    }
    
    @Override
    public void bindListener()
    {
        aboutusView.setOnClickListener(this);
        feedbackView.setOnClickListener(this);
        clearView.setOnClickListener(this);
        upgrateView.setOnClickListener(this);
    }
    
    @Override
    public int getActivityLayout()
    {
        return R.layout.wx_setting_activity;
    }
    
    @Override
    public void onClick(View v)
    {
        if (v == aboutusView)
        {
            Intent intent = new Intent(this, AboutUsActivity.class);
            startActivity(intent);
        }
        else if (v == upgrateView)
        {
            ProgressUtil.showProgressDialog(this,
                    getString(R.string.ysh_update_message));
            sendUpdateRequest();
        }
        else if (v == feedbackView)
        {
            Intent intent = new Intent(this, FeedbackActivity.class);
            startActivity(intent);
        }
        else if (v == clearView)
        {
            String cachePath = GlobalConstant.FILE_CACHE_DIR;
            //删除缓存
            try
            {
                FileHelper.deleteDir(new File(cachePath));
            }
            catch (Exception e)
            {
                
            }
            showToast("清除成功");
        }
    }
    
    /**
     * 版本检测
     * @see [类、类#方法、类#成员]
     */
    private void sendUpdateRequest()
    {
        Map<String, String> param = HttpRequestUtil.getCommonParams("clientUpdate");
        param.put("version", GlobalVariable.versionCode + "");
        param.put("appType", "2");
        //1家长端，2教师端
        param.put("platform", "1");
        HttpRequestUtil.sendHttpPostCommonRequest(this,
                HttpAddressProperties.SERVICE_URL,
                param,
                VersionResp.class,
                updateHandler);
    }
    
    /** <一句话功能简述>
     * 处理版本升级返回数据
     * <功能详细描述>
     * @param rsp [参数说明]
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private void dealVersionUpdate(VersionResp resp)
    {
        if (null != resp)
        {
            String errCode = resp.getCode();
            String errMsg = resp.getMessage();
            VersionItem item = resp.getVersionItem();
            if (errCode.equals("200"))
            {
                if (null != item)
                {
                    int versionCode = Integer.valueOf(item.getVersionCode());
                    String apkUrl = item.getApkUrl();
                    if (StringUtil.isEmpty(apkUrl))
                    {
                        Toast.makeText(mContext,
                                R.string.set_version_update_latest_toast,
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (versionCode <= 0)
                    {
                        Toast.makeText(mContext,
                                R.string.set_version_update_latest_toast,
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (versionCode <= GlobalVariable.versionCode)
                    {
                        Toast.makeText(mContext,
                                R.string.set_version_update_latest_toast,
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    
                    //弹出对话框
                    updateDialog(mContext, downClientHandler, item);
                }
            }
            else
            {
                showToast(errMsg);
            }
        }
    }
    
    /**
     * 升级对话框
     * @param mContext
     * @param downClientHandler
     * @param updateType
     * @param description
     * @see [类、类#方法、类#成员]
     */
    private void updateDialog(Context mContext, Handler downClientHandler,
            final VersionItem item)
    {
        String versionName = item.getVersionName();
        
        String title = "";
        if (!StringUtil.isEmpty(versionName))
        {
            title = getString(R.string.ysh_new_version) + "\t\tv"
                    + item.getVersionName();
        }
        else
        {
            title = getString(R.string.ysh_new_version);
        }
        
        CommonDialog dialog = new CommonDialog(mContext, title,
                item.getDescription(), new OnButtonClickListener()
                {
                    
                    @Override
                    public void onConfirmClick()
                    {
                        downClient(item.getApkUrl(), item.getVersionName());
                    }
                    
                    @Override
                    public void onCancelClick()
                    {
                        
                    }
                });
        dialog.showDialog();
    }
    
    /**
     * 发送请求下载更新客户端
     */
    protected void downClient(final String downloadUrl, final String versionName)
    {
        progressDialog = new ProgressDialog(GlobalVariable.currentActivity);
        progressDialog.setTitle(R.string.update_client_title);
        progressDialog.setMessage(getString(R.string.update_downloading_percent,
                "0%"));
        progressDialog.setOnKeyListener(GlobalVariable.onKeyListener);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        new Thread()
        {
            public void run()
            {
                DownClientApkRequest downClientApkRequest = new DownClientApkRequest(
                        mContext, downClientHandler, downloadUrl, versionName);
                try
                {
                    downClientApkRequest.connetionProcess();
                }
                catch (Exception e)
                {
                    //如果获取更新数据包失败,则结束
                    showToast(getResources().getString(R.string.update_client_fail));
                }
            };
        }.start();
    }
    
    /**
     * 关闭对话框
     */
    protected void closeProgressDialog()
    {
        if (progressDialog != null)
        {
            progressDialog.dismiss();
        }
    }
    
}
