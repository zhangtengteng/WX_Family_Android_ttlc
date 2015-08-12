package com.xweisoft.wx.family.ui.message;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.WXApplication;
import com.xweisoft.wx.family.logic.global.GlobalConstant;
import com.xweisoft.wx.family.logic.global.HttpAddressProperties;
import com.xweisoft.wx.family.logic.model.ContactItem;
import com.xweisoft.wx.family.logic.model.MessageItem;
import com.xweisoft.wx.family.logic.model.MessageItem.MessageType;
import com.xweisoft.wx.family.logic.model.SocketMsgItem;
import com.xweisoft.wx.family.logic.model.response.UploadFileResp;
import com.xweisoft.wx.family.mina.ClientUtil;
import com.xweisoft.wx.family.service.database.ChatMessageDBHelper;
import com.xweisoft.wx.family.service.database.ContactsDBHelper;
import com.xweisoft.wx.family.service.database.SessionMessageDBHelper;
import com.xweisoft.wx.family.service.upload.FileUploadManager;
import com.xweisoft.wx.family.service.upload.IHttpListener;
import com.xweisoft.wx.family.service.upload.UploadItem;
import com.xweisoft.wx.family.ui.BaseActivity;
import com.xweisoft.wx.family.ui.message.adpter.MessageChatListAdapter;
import com.xweisoft.wx.family.ui.message.view.ChatListView;
import com.xweisoft.wx.family.ui.message.view.ChatListView.IXListViewListener;
import com.xweisoft.wx.family.ui.photo.CompressPhotoActivity;
import com.xweisoft.wx.family.util.CommonTitleUtil;
import com.xweisoft.wx.family.util.HttpRequestUtil;
import com.xweisoft.wx.family.util.ListUtil;
import com.xweisoft.wx.family.util.LoginUtil;
import com.xweisoft.wx.family.util.Util;
import com.xweisoft.wx.family.widget.PhotoChooseDialog;

/**
 * 即时通讯聊天页面
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-7-21]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ChatMessageActivity extends BaseActivity
{
    
    // 分页后页码（因为消息显示为按时间由近及远，所以页码非正常分页页码）
    private int pageNum = 1;
    
    /**
     * 通讯录对象
     */
    private ContactItem mItem;
    
    /**
     * 通讯录数据库帮助类
     */
    private ContactsDBHelper mContactsDBHelper;
    
    /**
     * 图片 按钮
     */
    private ImageView mPictureImage;
    
    /**
     * 发送 按钮
     */
    private TextView mSendText;
    
    /**
     * 输入框
     */
    private EditText mInputEditText;
    
    /**
     * 列表适配器
     */
    private MessageChatListAdapter mAdapter;
    
    /**
     *下拉listView 
     */
    private ChatListView mListView;
    
    /**
     * 图片选择框
     */
    private PhotoChooseDialog mPhotoChooseDialog;
    
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
     * 消息集合
     */
    private ArrayList<MessageItem> mList = new ArrayList<MessageItem>();
    
    /**
     * 即时消息数据库帮助类
     */
    private ChatMessageDBHelper mChatDBHelper;
    
    /**
     * 会话消息数据库帮助类
     */
    private SessionMessageDBHelper mSessionDBHelper;
    
    /**
     * 
     */
    private String inputText;
    
    /**
     * 
     */
    private NotificationManager mNotificationManager;
    
    private boolean isGroup;
    
    /**
     * 广播接收器 用于更新数据
     */
    private BroadcastReceiver msgReceiver = new BroadcastReceiver()
    {
        
        @Override
        public void onReceive(Context context, Intent intent)
        {
            MessageItem item = (MessageItem) intent.getSerializableExtra("item");
            boolean refresh = false;
            if (null != item)
            {
                if (isGroup)
                {
                    if (WXApplication.getInstance().groupId.equals(item.groupId))
                    {
                        refresh = true;
                    }
                }
                else
                {
                    if (item.userId.equals(mItem.userId))
                    {
                        refresh = true;
                    }
                }
                if (refresh)
                {
                    mList.add(item);
                    mChatDBHelper.updateReadByMsgId(item.messageId);
                    mAdapter.setList(mList);
                    setListViewSelection();
                }
            }
        }
    };
    
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
    
    private Handler uploadHandler = new Handler()
    {
        
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case UploadItem.UPLOAD_SCUUCSS:
                    UploadFileResp resp = null;
                    if (null != msg.obj && msg.obj instanceof UploadFileResp)
                    {
                        resp = (UploadFileResp) msg.obj;
                        if ("200".equals(resp.getCode()))
                        {
                            if (!ListUtil.isEmpty(resp.resourceList)
                                    && null != resp.resourceList.get(0))
                            {
                                MessageItem item = new MessageItem();
                                item.text = resp.resourceList.get(0).resourcePath;
                                mChatDBHelper.updateMessageResource(resp.msgId,
                                        item);
                                sendIMMessage(MessageType.IMAGE,
                                        resp.msgId,
                                        resp.resourceList.get(0).resourcePath);
                            }
                        }
                        else
                        {
                            showToast("发送失败");
                        }
                    }
                    else
                    {
                        showToast("发送失败");
                    }
                    break;
                case IHttpListener.HTTP_FAILED:
                    showToast("发送失败");
                    break;
                case IHttpListener.HTTP_SER_ERROR:
                    showToast("发送失败");
                    break;
                default:
                    break;
            }
            
        }
        
    };
    
    private Handler downloadHandler = new Handler()
    {
        
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
        }
        
    };
    
    private Handler updateHandler = new Handler()
    {
        
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            ArrayList<MessageItem> list = null;
            if (isGroup)
            {
                list = mChatDBHelper.queryGroupMsgByPage(mItem.userId, pageNum);
            }
            else
            {
                list = mChatDBHelper.queryMsgByPage(mItem.userId,
                        mItem.studentId,
                        pageNum);
            }
            if (ListUtil.isEmpty(list))
            {
                mListView.setPullRefreshEnable(false);
            }
            else
            {
                if (list.size() <= ChatMessageDBHelper.PAGE_SIZE)
                {
                    mListView.setPullRefreshEnable(false);
                }
                mList.addAll(0, list);
                mAdapter.setList(mList);
                mListView.setSelection(list.size());
            }
        }
        
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayout());
        getBundle(getIntent().getStringExtra("userId"),
                getIntent().getStringExtra("studentId"));
        initViews();
        bindListener();
        registerReceiver(msgReceiver, new IntentFilter(
                GlobalConstant.NOTIFICATION_MSG_RECEIVER));
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
    
    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        getBundle(intent.getStringExtra("userId"),
                intent.getStringExtra("studentId"));
        initViews();
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        try
        {
            if (isGroup)
            {
                mList = mChatDBHelper.queryGroupMsgByPage(mItem.userId, pageNum);
                mChatDBHelper.updateGroupAllRead(mItem.userId);
                mNotificationManager.cancel(ClientUtil.NOTIFICATION_GROUP_MESSAGE);
            }
            else
            {
                mNotificationManager.cancel(Integer.parseInt(mItem.userId));
                mList = mChatDBHelper.queryMsgByPage(mItem.userId,
                        mItem.studentId,
                        pageNum);
                mChatDBHelper.updateAllRead(mItem.userId, mItem.studentId);
            }
            mAdapter.setList(mList);
            setListViewSelection();
        }
        catch (Exception e)
        {
        }
    }
    
    @Override
    public void initViews()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(mItem.name);
        if (!TextUtils.isEmpty(mItem.appellation))
        {
            buffer.append("(" + mItem.appellation + ")");
        }
        CommonTitleUtil.initCommonTitle(this,
                buffer.toString(),
                null,
                false,
                true);
        
        mPhotoChooseDialog = new PhotoChooseDialog(mContext,
                dialogClickListener);
        mPictureImage = (ImageView) findViewById(R.id.message_chat_picture);
        mInputEditText = (EditText) findViewById(R.id.message_chat_edit);
        mSendText = (TextView) findViewById(R.id.message_chat_send);
        
        mListView = (ChatListView) findViewById(R.id.message_chat_listview);
        mListView.setPullLoadEnable(false);
        
        mAdapter = new MessageChatListAdapter(mContext);
        mAdapter.setListView(mListView);
        mAdapter.setList(mList);
        mListView.setAdapter(mAdapter);
        mListView.setSelection(mAdapter.getCount());
        
        mChatDBHelper = new ChatMessageDBHelper(mContext,
                LoginUtil.getUserId(mContext));
        mSessionDBHelper = new SessionMessageDBHelper(mContext,
                LoginUtil.getUserId(mContext));
    }
    
    @Override
    public void bindListener()
    {
        mListView.setXListViewListener(new IXListViewListener()
        {
            
            @Override
            public void onRefresh()
            {
                pageNum++;
                updateHandler.postDelayed(new Runnable()
                {
                    
                    @Override
                    public void run()
                    {
                        updateHandler.sendEmptyMessage(0);
                        mListView.stopRefresh();
                    }
                }, 1500);
            }
            
            @Override
            public void onLoadMore()
            {
                
            }
        });
        mInputEditText.setOnFocusChangeListener(new OnFocusChangeListener()
        {
            
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus)
                {
                    setListViewSelection();
                }
            }
        });
        mInputEditText.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                setListViewSelection();
            }
        });
        mSendText.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if (TextUtils.isEmpty(mInputEditText.getText()))
                {
                    showToast("输入内容不能为空");
                    return;
                }
                inputText = mInputEditText.getText().toString().trim();
                String msgId = insertMessage(MessageType.TEXT, null, null);
                sendIMMessage(MessageType.TEXT, msgId, inputText);
                inputText = null;
            }
        });
        
        mPictureImage.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                mPhotoChooseDialog.showDialog();
            }
        });
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(msgReceiver);
    }
    
    @Override
    public int getActivityLayout()
    {
        return R.layout.wx_message_chat_activity;
    }
    
    private void getBundle(String userId, String studentId)
    {
        mContactsDBHelper = new ContactsDBHelper(mContext,
                LoginUtil.getUserId(mContext));
        if ("group".equals(getIntent().getStringExtra("group")))
        {
            isGroup = true;
            mItem = new ContactItem();
            mItem.name = "群组";
            mItem.userId = WXApplication.getInstance().groupId;
        }
        else
        {
            isGroup = false;
            mItem = mContactsDBHelper.queryContactById(userId, studentId);
        }
        if (null == mItem)
        {
            mItem = new ContactItem();
            finish();
        }
    }
    
    /**
     * 设置聊天消息最底端
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private void setListViewSelection()
    {
        if (!ListUtil.isEmpty(mList))
        {
            if (mList.size() > 0)
            {
                mListView.setSelection(mList.size() - 1);
            }
        }
    }
    
    /**
     * 发送消息
     * <一句话功能简述>
     * <功能详细描述>
     * @param type [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private String insertMessage(MessageType type, String localPath,
            String thumbPath)
    {
        String msgId = UUID.randomUUID().toString();
        MessageItem msgItem = getMessageItem(type, msgId);
        if (isGroup)
        {
            msgItem.groupId = mItem.userId;
        }
        else
        {
            msgItem.userId = mItem.userId;
        }
        msgItem.isFrom = "1";
        msgItem.isRead = "1";
        msgItem.thumbLocalPath = thumbPath;
        msgItem.msgLocalPath = localPath;
        msgItem.recTime = System.currentTimeMillis() + "";
        msgItem.text = inputText;
        mChatDBHelper.insert(msgItem);
        if (isGroup)
        {
            mSessionDBHelper.insertGroupSession(msgItem);
        }
        else
        {
            mSessionDBHelper.insert(msgItem);
        }
        mList.add(msgItem);
        mAdapter.setList(mList);
        mInputEditText.setText("");
        mListView.setSelection(mList.size() - 1);
        return msgId;
    }
    
    /**
     * 发送消息
     * <一句话功能简述>
     * <功能详细描述>
     * @param type [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private void sendIMMessage(MessageType type, String msgId, String text)
    {
        SocketMsgItem item = new SocketMsgItem();
        if (isGroup)
        {
            item.funcid = "img";
        }
        else
        {
            item.funcid = "im";
        }
        item.from = LoginUtil.getUserId(mContext);
        item.to = mItem.userId;
        MessageItem msgItem = getMessageItem(type, msgId);
        msgItem.text = text;
        item.content = new Gson().toJson(msgItem);
        item.msgid = msgId;
        if (null != WXApplication.getInstance().minaManager)
        {
            WXApplication.getInstance().minaManager.sendMessage(new Gson().toJson(item));
            WXApplication.getInstance().getImMsgIds().add(msgId);
        }
    }
    
    /**
     * 获取公共属性的messageItem
     * <一句话功能简述>
     * <功能详细描述>
     * @param type
     * @param msgId
     * @return [参数说明]
     * 
     * @return MessageItem [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private MessageItem getMessageItem(MessageType type, String msgId)
    {
        MessageItem msgItem = new MessageItem();
        msgItem.messageId = msgId;
        msgItem.from = LoginUtil.getStudentId(mContext);
        msgItem.to = mItem.studentId;
        if (isGroup)
        {
            msgItem.groupId = WXApplication.getInstance().groupId;
        }
        if (type == MessageType.TEXT)
        {
            msgItem.messageBodyType = "1";
        }
        else if (type == MessageType.AUDIO)
        {
            msgItem.messageBodyType = "2";
        }
        else if (type == MessageType.VIDEO)
        {
            msgItem.messageBodyType = "3";
        }
        else if (type == MessageType.IMAGE)
        {
            msgItem.messageBodyType = "4";
        }
        else if (type == MessageType.GPS)
        {
            
        }
        return msgItem;
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
                        Map<String, String> params = HttpRequestUtil.getCommonParams("");
                        String msgId = insertMessage(MessageType.IMAGE,
                                path,
                                null);
                        params.put("fileType", "1");//文件类型 1 图片 2 音频 3 视频
                        UploadItem item = new UploadItem();
                        item.handler = uploadHandler;
                        item.params = params;
                        item.filePath = new String[] { path };
                        item.msgId = msgId;
                        item.uploadServer = "http://"
                                + WXApplication.getInstance().getRequestUrl()
                                + HttpAddressProperties.UPLOAD_FILE_URL;
                        FileUploadManager.getInstance(mContext)
                                .addUploadTask(item);
                    }
                }
            }
        }
    }
}
