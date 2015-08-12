package com.xweisoft.wx.family.ui.pc;

import java.io.File;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.WXApplication;
import com.xweisoft.wx.family.logic.global.GlobalConstant;
import com.xweisoft.wx.family.logic.global.GlobalVariable;
import com.xweisoft.wx.family.logic.global.HttpAddressProperties;
import com.xweisoft.wx.family.logic.global.NetWorkCodes.CommontErrorCodes;
import com.xweisoft.wx.family.logic.global.NetWorkCodes.CommontSuccessCodes;
import com.xweisoft.wx.family.logic.model.ChildrenItem;
import com.xweisoft.wx.family.logic.model.VersionItem;
import com.xweisoft.wx.family.logic.model.response.CommonResp;
import com.xweisoft.wx.family.logic.model.response.UploadFileResp;
import com.xweisoft.wx.family.logic.model.response.VersionResp;
import com.xweisoft.wx.family.logic.request.DownClientApkRequest;
import com.xweisoft.wx.family.service.upload.IHttpListener;
import com.xweisoft.wx.family.service.upload.UploadItem;
import com.xweisoft.wx.family.ui.BaseFragment;
import com.xweisoft.wx.family.ui.setting.AboutUsActivity;
import com.xweisoft.wx.family.ui.setting.FeedbackActivity;
import com.xweisoft.wx.family.ui.setting.ModifyPasswordActivity;
import com.xweisoft.wx.family.ui.setting.SetMessageActivity;
import com.xweisoft.wx.family.ui.setting.SettingActivity;
import com.xweisoft.wx.family.util.CommonTitleUtil;
import com.xweisoft.wx.family.util.FileHelper;
import com.xweisoft.wx.family.util.HttpRequestUtil;
import com.xweisoft.wx.family.util.ListUtil;
import com.xweisoft.wx.family.util.LoginUtil;
import com.xweisoft.wx.family.util.ProgressUtil;
import com.xweisoft.wx.family.util.StringUtil;
import com.xweisoft.wx.family.util.Util;
import com.xweisoft.wx.family.widget.CommonDialog;
import com.xweisoft.wx.family.widget.CommonDialog.OnButtonClickListener;
import com.xweisoft.wx.family.widget.NetHandler;

/**
 * 个人中心
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-5-11]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class PersonCenterFragment extends BaseFragment implements
        OnClickListener
{
    
    private ProgressDialog progressDialog = null;
    
    private LinearLayout userLayout;
    
    /**
     * 关于我们
     */
    private LinearLayout myguanyueview;
    
    /**
     * 反馈
     */
    private LinearLayout userfankui;
    
    /**
     * 用户头像
     */
    private ImageView headerImageView = null;
    
    /**
     * 切换baby
     */
    private View changeBabyView;
    
    /**
     * 监护角色
     */
    private View mycontrolView = null;
    
    /**
     * 修改密码
     */
    private View mymodifyView = null;
    
    /**
     * 设置
     */
    private View settingView = null;
    
    /**
     * 清除缓存
     * 
     * */
    private LinearLayout myclearviews;
    
    /**
     * 消息设置
     * 
     * */
    private LinearLayout messages;
    
    /**
     * 检查版本更新
     * 
     * */
    private LinearLayout mychageupdateview;
    
    /**
     * 退出登录
     */
    private View signOutView = null;
    
    /**
     * 称谓
     */
    private TextView mNameTextView;
    
    /**
     * 监护角色
     */
    private TextView mCallTextView;
    
    /**
     * 修改监护角色
     */
    private final int MODIFY_CALL = 1004;
    
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
    
    /**
     * 上传图片
     */
    @SuppressLint("HandlerLeak")
    private Handler headerHandler = new Handler()
    {
        
        @Override
        public void handleMessage(Message msg)
        {
            
            super.handleMessage(msg);
            switch (msg.what)
            {
                case UploadItem.UPLOAD_SCUUCSS:
                    ProgressUtil.dismissProgressDialog();
                    UploadFileResp resp = null;
                    if (null != msg.obj && msg.obj instanceof UploadFileResp)
                    {
                        resp = (UploadFileResp) msg.obj;
                        if ("200".equals(resp.getCode()))
                        {
                            if (!ListUtil.isEmpty(resp.resourceList)
                                    && null != resp.resourceList.get(0))
                            {
                                imageLoader.displayImage(resp.resourceList.get(0).resourcePath,
                                        headerImageView,
                                        WXApplication.getInstance().optionsCircle);
                            }
                        }
                        else
                        {
                            showToast("头像上传失败");
                        }
                    }
                    else
                    {
                        showToast("头像上传失败");
                    }
                    break;
                case IHttpListener.HTTP_FAILED:
                    ProgressUtil.dismissProgressDialog();
                    if (ProgressUtil.isUploadCancel)
                    {
                        return;
                    }
                    Toast.makeText(mContext,
                            R.string.network_error,
                            Toast.LENGTH_SHORT).show();
                    break;
                case IHttpListener.HTTP_SER_ERROR:
                    ProgressUtil.dismissProgressDialog();
                    if (ProgressUtil.isUploadCancel)
                    {
                        return;
                    }
                    Toast.makeText(mContext,
                            R.string.system_error,
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            
        }
    };
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        mLayouView = inflater.inflate(R.layout.wx_personal_center_activity,
                container,
                false);
        initViews();
        bindListener();
        return mLayouView;
    }
    
    @Override
    public void initViews()
    {
        CommonTitleUtil.initCommonTitle(mLayouView,
                getResources().getString(R.string.ysh_personal_center),
                null,
                true,
                true);
        headerImageView = (ImageView) mLayouView.findViewById(R.id.userheader_imageview);
        
        userLayout = (LinearLayout) mLayouView.findViewById(R.id.user_layout);
        
        changeBabyView = mLayouView.findViewById(R.id.myswitch_view);
        mymodifyView = mLayouView.findViewById(R.id.mymodify_view);
        mycontrolView = mLayouView.findViewById(R.id.mycontrol_view);
        settingView = mLayouView.findViewById(R.id.setting_view);
        //清除缓存
        myclearviews = (LinearLayout) mLayouView.findViewById(R.id.my_clear_views);
        //检查版本更新
        mychageupdateview = (LinearLayout) mLayouView.findViewById(R.id.my_chageupdate_view);
        //关于我们
        
        //反馈
        userfankui = (LinearLayout) mLayouView.findViewById(R.id.fankui_view);
        
        //消息
        messages = (LinearLayout) mLayouView.findViewById(R.id.mymessage_view);
        
        myguanyueview = (LinearLayout) mLayouView.findViewById(R.id.sign_guanyu_view);
        signOutView = mLayouView.findViewById(R.id.sign_out_view);
        mNameTextView = (TextView) mLayouView.findViewById(R.id.username_textview);
        mCallTextView = (TextView) mLayouView.findViewById(R.id.tv_control_name);
        
    }
    
    @Override
    public void onResume()
    {
        super.onResume();
        ChildrenItem item = WXApplication.getInstance().selectedItem;
        if (null != item)
        {
            imageLoader.displayImage(item.portraitPath,
                    headerImageView,
                    WXApplication.getInstance().optionsCircle);
            if (TextUtils.isEmpty(item.appellation))
            {
                mNameTextView.setText(Util.checkNull(item.studentName));
            }
            else
            {
                mNameTextView.setText(Util.checkNull(item.studentName) + "("
                        + item.appellation + ")");
            }
            mCallTextView.setText(Util.checkNull(item.appellation));
        }
    }
    
    @Override
    public void bindListener()
    {
        userLayout.setOnClickListener(this);
        changeBabyView.setOnClickListener(this);
        mycontrolView.setOnClickListener(this);
        mymodifyView.setOnClickListener(this);
        signOutView.setOnClickListener(this);
        settingView.setOnClickListener(this);
        headerImageView.setOnClickListener(this);
        myclearviews.setOnClickListener(this);
        mychageupdateview.setOnClickListener(this);
        myguanyueview.setOnClickListener(this);
        userfankui.setOnClickListener(this);
        messages.setOnClickListener(this);
        
    }
    
    @Override
    public void onClick(View v)
    {
        if (v == mycontrolView)
        {
            if (null != WXApplication.getInstance().loginUserItem
                    && !ListUtil.isEmpty(WXApplication.getInstance().loginUserItem.children))
            {
                Intent intent = new Intent(mContext, ControlRoleActivity.class);
                startActivityForResult(intent, MODIFY_CALL);
            }
        }
        else if (v == mymodifyView)
        {
            Intent intent = new Intent(mContext, ModifyPasswordActivity.class);
            startActivity(intent);
        }
        else if (v == userLayout)
        {
            //修改头像
            Intent intent = new Intent(mContext, UpdateIconActivity.class);
            startActivity(intent);
        }
        else if (v == userfankui)
        {
            //意见反馈
            Intent intent = new Intent(mContext, FeedbackActivity.class);
            startActivity(intent);
        }
        else if (v == messages)
        {
            //消息设置
            Intent intent = new Intent(mContext, SetMessageActivity.class);
            startActivity(intent);
        }
        else if (v == settingView)
        {
            Intent intent = new Intent(mContext, SettingActivity.class);
            startActivity(intent);
        }
        else if (v == myguanyueview)
        {
            Intent intent = new Intent(mContext, AboutUsActivity.class);
            startActivity(intent);
            
        }
        else if (v == myclearviews)
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
        else if (v == mychageupdateview)
        {
            
            //检测版本更新 
            ProgressUtil.showProgressDialog(mContext,
                    getString(R.string.ysh_update_message));
            sendUpdateRequest();
        }
     
        else if (v == signOutView)
        {
            if (null != WXApplication.getInstance().getMinaManager())
            {
                WXApplication.getInstance().getMinaManager().disconnect();
            }
            HttpRequestUtil.sendHttpGetCommonRequest(mContext,
                    HttpAddressProperties.LOGOUT_URL,
                    CommonResp.class,
                    new Handler());
            LoginUtil.logout(getActivity());
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
        HttpRequestUtil.sendHttpPostCommonRequest(mContext,
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
