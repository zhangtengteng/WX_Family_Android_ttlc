package com.xweisoft.wx.family.ui.pc;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.WXApplication;
import com.xweisoft.wx.family.logic.global.GlobalConstant;
import com.xweisoft.wx.family.logic.global.HttpAddressProperties;
import com.xweisoft.wx.family.logic.model.ChildrenItem;
import com.xweisoft.wx.family.logic.model.UserItem;
import com.xweisoft.wx.family.logic.model.response.UploadFileResp;
import com.xweisoft.wx.family.service.upload.FileUploadManager;
import com.xweisoft.wx.family.service.upload.IHttpListener;
import com.xweisoft.wx.family.service.upload.UploadItem;
import com.xweisoft.wx.family.ui.BaseActivity;
import com.xweisoft.wx.family.ui.photo.CompressPhotoActivity;
import com.xweisoft.wx.family.util.CommonTitleUtil;
import com.xweisoft.wx.family.util.ListUtil;
import com.xweisoft.wx.family.util.ProgressUtil;
import com.xweisoft.wx.family.util.Util;
import com.xweisoft.wx.family.widget.PhotoChooseDialog;

/**
 * 修改头像
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-5-12]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class UpdateIconActivity extends BaseActivity implements OnClickListener
{
    
    /**
     * 头像
     */
    private ImageView mHeaderImage;
    
    /**
     * 图片选择框
     */
    private PhotoChooseDialog mPhotoChooseDialog;
    
    /**
     * 名称
     */
    private TextView mNameText;
    
    /**
     * 电话
     */
    private TextView mPhoneText;
    
    /**
     * 班级
     */
    private TextView mClassText;
    
    /**
     * 角色布局
     */
    private View mAppellationView;
    
    /**
     * 角色
     */
    private TextView mAppellationText;
    
    /**
     * 性别
     */
    private TextView mSexText;
    
    /**
     * 相册选取图片请求码
     */
    private final int LOCAL_PHOTO = 1001;
    
    /**
     * 拍照请求码
     */
    private final int CAMERA_PHOTO = 1002;
    
    /**
     * 图片处理压缩
     */
    private final int COMPRESS_PHOTO = 1003;
    
    /**
     * 修改监护角色
     */
    private final int MODIFY_CALL = 1004;
    
    /**
     * 图片 类型
     */
    private static final String TYPE_PHOTO = "image/*";
    
    /**
     * 调用系统相机 指定输出文件 
     */
    private static File PHOTO_FILE;
    
    /**
     * 调用系统相机 指定输出文件的 Uri
     */
    private static Uri PHOTO_URI;
    
    /**
     * 通知更新下载进度条的标识
     */
    public static final int UPDATE_PROGRESSBAR = 1001;
    
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
                getResources().getString(R.string.ysh_personalzl),
                null,
                false,
                true);
        mNameText = (TextView) findViewById(R.id.person_info_name);
        mPhoneText = (TextView) findViewById(R.id.person_info_phone);
        mSexText = (TextView) findViewById(R.id.person_info_sex);
        mClassText = (TextView) findViewById(R.id.person_info_class);
        mAppellationView = findViewById(R.id.person_info_appellation_view);
        mAppellationText = (TextView) findViewById(R.id.person_info_appellation);
        mHeaderImage = (ImageView) findViewById(R.id.person_info_header);
        
        //选择相机还是本地
        mPhotoChooseDialog = new PhotoChooseDialog(mContext,
                dialogClickListener);
        ChildrenItem childrenItem = WXApplication.getInstance().selectedItem;
        if (null != childrenItem)
        {
            imageLoader.displayImage(childrenItem.portraitPath,
                    mHeaderImage,
                    WXApplication.getInstance().optionsCircle);
            if (TextUtils.isEmpty(childrenItem.appellation))
            {
                mNameText.setText(Util.checkNull(childrenItem.studentName));
                
            }
            else
            {
                mAppellationText.setText(childrenItem.appellation);
                mNameText.setText(Util.checkNull(childrenItem.studentName)
                        + "(" + childrenItem.appellation + ")");
            }
            mClassText.setText(Util.checkNull(childrenItem.classinfoName));
        }
        UserItem item = WXApplication.getInstance().loginUserItem;
        
        if (null != item)
        {
            mPhoneText.setText(Util.checkNull(item.phone));
            mAppellationText.setText(Util.checkNull(childrenItem.appellation));
            if ("1".equals(item.parentSex))
            {
                mSexText.setText("男");
            }
            else if ("2".equals(item.parentSex))
            {
                mSexText.setText("女");
            }
        }
        
    }
    
    @Override
    public void bindListener()
    {
        mAppellationView.setOnClickListener(this);
        mHeaderImage.setOnClickListener(this);
    }
    
    /**
     * 选择框监听事件
     */
    private OnClickListener dialogClickListener = new OnClickListener()
    {
        
        @Override
        public void onClick(View v)
        {
            mPhotoChooseDialog.dismiss();
            switch (v.getId())
            {
                case R.id.photo_choose_camera:
                    cameraPhoto();
                    break;
                case R.id.photo_choose_local:
                    selectPhoto();
                    break;
                case R.id.photo_choose_cancel:
                    break;
                default:
                    break;
            }
        }
    };
    
    /**
     * 相机拍照图片
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private void cameraPhoto()
    {
        if (!Util.isHasSDcard())
        {
            showToast("");
            return;
        }
        File file = Util.makeDirs(GlobalConstant.IMAGE_CACHE_DIR);
        PHOTO_FILE = new File(file, System.currentTimeMillis() + ".jpg");
        PHOTO_URI = Uri.fromFile(PHOTO_FILE);
        Intent intent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        // 设置系统相机拍照后文件的输出位置
        intent.putExtra(MediaStore.EXTRA_OUTPUT, PHOTO_URI);
        startActivityForResult(intent, CAMERA_PHOTO);
    }
    
    /**
     * 调用系统相册 
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private void selectPhoto()
    {
        Intent intent = null;
        //根据版本号不同使用不同的Action. 4.4系统 直接进入相册界面
        if (Build.VERSION.SDK_INT < 19)
        {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType(TYPE_PHOTO);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intent, "选择图片"),
                    LOCAL_PHOTO);
        }
        else
        {
            intent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, LOCAL_PHOTO);
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = null;
        Intent intent = null;
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == CAMERA_PHOTO)
            {
                bundle = new Bundle();
                bundle.putString("path", PHOTO_FILE.getAbsolutePath());
                bundle.putString("uri", PHOTO_URI.toString());
                intent = new Intent(mContext, CompressPhotoActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, COMPRESS_PHOTO);
            }
            else if (requestCode == LOCAL_PHOTO)
            {
                // 获取系统相册图片的uri
                if (null != data)
                {
                    Uri originalUri = data.getData();
                    bundle = new Bundle();
                    bundle.putString("uri", originalUri.toString());
                    intent = new Intent(mContext, CompressPhotoActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, COMPRESS_PHOTO);
                }
            }
            else if (requestCode == COMPRESS_PHOTO)
            {
                if (null != data)
                {
                    String path = data.getStringExtra("picPath");
                    if (!TextUtils.isEmpty(path))
                    {
                        ProgressUtil.showProgressDialog(mContext, "图片上传中...");
                        ProgressUtil.isUploadCancel = false;
                        UploadItem item = new UploadItem();
                        item.handler = headerHandler;
                        item.filePath = new String[] { path };
                        item.uploadServer = "http://"
                                + WXApplication.getInstance().getRequestUrl()
                                + HttpAddressProperties.UPLOAD_HEADER_URL;
                        FileUploadManager.getInstance(mContext)
                                .addUploadTask(item);
                    }
                }
            }
            else if (requestCode == MODIFY_CALL)
            {
                ChildrenItem item = WXApplication.getInstance().selectedItem;
                if (null != item)
                {
                    String appellation = data.getStringExtra("call");
                    if (TextUtils.isEmpty(appellation))
                    {
                        mNameText.setText(Util.checkNull(item.studentName));
                    }
                    else
                    {
                        mNameText.setText(Util.checkNull(item.studentName)
                                + "(" + appellation + ")");
                    }
                    mAppellationText.setText(Util.checkNull(appellation));
                }
            }
        }
    }
    
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
                                
                                if (null != WXApplication.getInstance().selectedItem)
                                {
                                    WXApplication.getInstance().selectedItem.portraitPath = resp.resourceList.get(0).resourcePath;
                                }
                                imageLoader.displayImage(resp.resourceList.get(0).resourcePath,
                                        mHeaderImage,
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
    public int getActivityLayout()
    {
        return R.layout.wx_person_info_activity;
    }
    
    /**
     * 获取传递数据
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.person_info_appellation_view:
                Intent intent = new Intent(mContext, ControlRoleActivity.class);
                startActivityForResult(intent, MODIFY_CALL);
                break;
            case R.id.person_info_header:
                mPhotoChooseDialog.showDialog();
                break;
            default:
                break;
        }
    }
    
}
