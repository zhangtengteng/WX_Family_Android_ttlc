package com.xweisoft.wx.family.widget;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.logic.global.GlobalConstant;
import com.xweisoft.wx.family.logic.model.MessageItem;
import com.xweisoft.wx.family.service.database.ChatMessageDBHelper;
import com.xweisoft.wx.family.service.download.DownloadFileTask;
import com.xweisoft.wx.family.service.download.DownloadItem;
import com.xweisoft.wx.family.service.download.FileDownloadManager;
import com.xweisoft.wx.family.util.LoginUtil;
import com.xweisoft.wx.family.util.Util;

/**
 * 聊天界面 图片 异步加载类
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  gac
 * @version  [版本号, 2013-8-30]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@SuppressLint("HandlerLeak")
public class AsyncImage extends ImageView
{
    
    private DownloadItem item;
    
    private Context mContext;
    
    private MessageItem mItem;
    
    // 消息数据表帮助类
    private ChatMessageDBHelper mDBHelper;
    
    RuntimeException mException;
    
    public AsyncImage(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }
    
    private Handler handler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case DownloadItem.FXZ_DOWNLOAD_SCUUCSS:
                    if (null != mItem)
                    {
                        if (!TextUtils.isEmpty(mItem.msgLocalPath)
                                && new File(mItem.msgLocalPath).exists())
                        {
                            setResource(BitmapFactory.decodeFile(mItem.msgLocalPath));
                        }
                    }
                    else
                    {
                        setDefaultImage();
                    }
                    break;
                default:
                    break;
            }
        };
    };
    
    private void init(Context context, AttributeSet attrs)
    {
        mContext = context;
        mDBHelper = new ChatMessageDBHelper(mContext,
                LoginUtil.getUserId(context));
    }
    
    public void loadImage(MessageItem item)
    {
        this.mItem = item;
        setDefaultImage();
        if (null == item || null == mDBHelper)
        {
            return;
        }
        String msgId = item.messageId;
        String path = item.msgLocalPath;
        String url = item.text;
        if (TextUtils.isEmpty(path))
        {
            if (!TextUtils.isEmpty(url))
            {
                File thumb = Util.makeDirs(GlobalConstant.IMAGE_CACHE_DIR);
                String tName = thumb.getAbsolutePath() + File.separator
                        + "tmb_" + System.currentTimeMillis() + ".jpg";
                item.msgLocalPath = tName;
                download(msgId, url, tName);
                mDBHelper.updateMessageResource(msgId, item);
            }
            else
            {
                setDefaultImage();
            }
        }
        else
        {
            if (new File(path).exists())
            {
                setResource(BitmapFactory.decodeFile(path));
            }
            else
            {
                download(msgId, url, item.msgLocalPath);
                //                setDefaultImage();
            }
        }
    }
    
    private void setDefaultImage()
    {
        setImageResource(R.drawable.wx_default_image);
    };
    
    private void setResource(Bitmap bitmap)
    {
        if (null != bitmap)
        {
            setImageBitmap(zoomSmall(bitmap, Util.dpToPixel(mContext, 100)));
        }
        else
        {
            setDefaultImage();
        }
    }
    
    /**
     * 文件下载
     * <一句话功能简述>
     * <功能详细描述>
     * @param path 文件下载的服务器地址
     * @param name [下载生成的文件名]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private void download(String id, String path, String name)
    {
        item = new DownloadItem();
        
        //断点下载只针对视频文件
        
        item.handler = handler;
        item.id = id;
        item.filePath = name;
        item.context = mContext;
        item.downloadServer = path;
        FileDownloadManager.getInstance().addDownloadTask(item,
                DownloadFileTask.GET);
    }
    
    /**
     * 放缩图片
     * <一句话功能简述>
     * <功能详细描述>
     * @param bitmap 
     * @param size 放缩大小 float类型
     * @return [参数说明]
     * 
     * @return Bitmap [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static Bitmap zoomSmall(Bitmap bitmap, float size)
    {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float s = (float) size / bitmap.getWidth() > 1 ? 1 : (float) size
                / bitmap.getWidth();
        Matrix matrix = new Matrix();
        matrix.postScale(s, s);
        Bitmap newbmp = Bitmap.createBitmap(bitmap,
                0,
                0,
                width,
                height,
                matrix,
                true);
        return newbmp;
    }
}
