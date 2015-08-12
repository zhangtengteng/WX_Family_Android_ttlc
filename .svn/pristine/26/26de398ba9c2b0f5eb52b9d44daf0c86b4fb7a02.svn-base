package com.xweisoft.wx.family.ui.photo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.logic.global.GlobalConstant;
import com.xweisoft.wx.family.ui.BaseActivity;
import com.xweisoft.wx.family.util.CommonTitleUtil;
import com.xweisoft.wx.family.util.LogX;
import com.xweisoft.wx.family.util.Util;
import com.xweisoft.wx.family.widget.ScaleImageView;

/**
 * 图片压缩处理类
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  guoac
 * @version  [版本号, 2015-1-21]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@SuppressLint("HandlerLeak")
public class CompressPhotoActivity extends BaseActivity
{
    
    private Bundle bundle;
    
    /**
     * 文件路径 
     */
    private String filePath;
    
    /**
     * 放大缩小图片
     */
    private ScaleImageView mImageView;
    
    /**
     * 标题布局
     */
    private View mTitleView;
    
    /**
     * 是否删除
     */
    private boolean isDelete = true;
    
    /**
     * 标题是否 在显示
     */
    private boolean isShowing = true;
    
    /**
     * 标题栏右侧布局
     */
    private View rightView = null;
    
    /**
     * 更新UI
     */
    private Handler handler = new Handler()
    {
        
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 0:
                    if (null == BitmapFactory.decodeFile(filePath))
                    {
                        handler.postDelayed(new Runnable()
                        {
                            
                            @Override
                            public void run()
                            {
                                Message message = new Message();
                                message.what = 0;
                                handler.sendMessage(message);
                            }
                        }, 1000);
                        return;
                    }
                    mImageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
                    break;
                default:
                    break;
            }
        }
    };
    
    @SuppressWarnings("deprecation")
    private void getBundle()
    {
        bundle = getIntent().getExtras();
        if (bundle.getString("path") == null)
        {
            Uri uri = Uri.parse(bundle.getString("uri"));
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
            if (null != actualimagecursor)
            {
                int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                actualimagecursor.moveToFirst();
                String img_path = actualimagecursor.getString(actual_image_column_index);
                filePath = new File(img_path).getAbsolutePath();
                if (actualimagecursor != null)
                {
                    actualimagecursor.close();
                    actualimagecursor = null;
                }
            }
            else
            {
                String path = bundle.getString("uri");
                if (path.startsWith("file://"))
                {
                    path = path.replace("file://", "");
                }
                filePath = path;
            }
        }
        else
        {
            filePath = bundle.getString("path");
        }
        new PhotoTask(filePath).start();
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayout());
        initViews();
        bindListener();
        getBundle();
    }
    
    @Override
    public void initViews()
    {
        CommonTitleUtil.initCommonTitle(this, "图片预览", "完成", false, false);
        mTitleView = findViewById(R.id.compress_title_view);
        rightView = findViewById(R.id.common_title_right);
        mImageView = (ScaleImageView) findViewById(R.id.compress_image);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void bindListener()
    {
        mImageView.setDetector(new GestureDetector(new MySimpleGesture()));
        rightView.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                isDelete = false;
                //来自爆料发布界面
                //将数据返回给上一activity
                Intent intent = new Intent();
                intent.putExtra("picPath", filePath);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
    
    @Override
    public int getActivityLayout()
    {
        return R.layout.ysh_compress_photo;
    }
    
    private class MySimpleGesture extends SimpleOnGestureListener
    {
        
        public boolean onDoubleTap(MotionEvent e)
        {
            mImageView.maxZoomTo((int) e.getX(), (int) e.getY());
            mImageView.cutting();
            return true;
        }
        
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e)
        {
            hideOrShowView();
            return true;
        }
    }
    
    /**
     * 
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private void hideOrShowView()
    {
        if (isShowing)
        {
            isShowing = false;
            mTitleView.setVisibility(View.GONE);
        }
        else
        {
            isShowing = true;
            mTitleView.setVisibility(View.VISIBLE);
        }
    }
    
    @Override
    protected void onDestroy()
    {
        if (isDelete)
        {
            if (null != filePath && new File(filePath).exists())
            {
                new File(filePath).delete();
            }
        }
        super.onDestroy();
    }
    
    /**
     * 
     * 继承thread 压缩图片处理
     * <一句话功能简述>
     * <功能详细描述>
     * 
     * @author  gac
     * @version  [版本号, 2013-5-16]
     * @see  [相关类/方法]
     * @since  [产品/模块版本]
     */
    private class PhotoTask extends Thread
    {
        
        private String path;
        
        public PhotoTask(String filePath)
        {
            super();
            this.path = filePath;
        }
        
        @Override
        public void run()
        {
            super.run();
            int degree = 0;
            try
            {
                // 从指定路径下读取图片，并获取其EXIF信息
                ExifInterface exifInterface = new ExifInterface(path);
                // 获取图片的旋转信息
                int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);
                switch (orientation)
                {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            
            BufferedOutputStream bos = null;
            Bitmap icon = null;
            try
            {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(path, options);//此时返回bm为空
                float percent = options.outHeight > options.outWidth ? options.outHeight / 960f
                        : options.outWidth / 960f;
                if (percent > 3)
                {
                    options.inSampleSize = 4;
                }
                else if (percent > 2 && percent < 3)
                {
                    options.inSampleSize = 3;
                }
                else if (percent < 1)
                {
                    percent = 1;
                }
                //                if (percent < 1)
                //                {
                //                    percent = 1;
                //                }
                //                else
                //                {
                //                    options.inSampleSize = 2;
                //                }
                int width = (int) (options.outWidth / percent);
                int height = (int) (options.outHeight / percent);
                icon = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                //初始化画布 绘制的图像到icon上  
                Canvas canvas = new Canvas(icon);
                //建立画笔  
                Paint photoPaint = new Paint();
                //获取跟清晰的图像采样  
                photoPaint.setDither(true);
                //过滤一些  
                photoPaint.setFilterBitmap(true);
                options.inJustDecodeBounds = false;
                Bitmap prePhoto = BitmapFactory.decodeFile(path, options);
                if (percent >= 1)
                {
                    prePhoto = Bitmap.createScaledBitmap(prePhoto,
                            width,
                            height,
                            true);
                    canvas.drawBitmap(prePhoto, 0, 0, photoPaint);
                    
                    if (bundle.getString("path") == null)
                    {
                        String picName = System.currentTimeMillis() + ".jpg";
                        File file = Util.makeDirs(GlobalConstant.IMAGE_CACHE_DIR);
                        File picFile = new File(file, picName);
                        filePath = picFile.getAbsolutePath();
                        bos = new BufferedOutputStream(new FileOutputStream(
                                picFile));
                    }
                    else
                    {
                        bos = new BufferedOutputStream(new FileOutputStream(
                                path));
                    }
                    //                    int quaility = (int) (100 / percent > 80 ? 80
                    //                            : 100 / percent);
                    //                    prePhoto.compress(CompressFormat.JPEG, quaility, bos);
                    if (degree != 0)
                    {
                        // 根据旋转角度，生成旋转矩阵
                        Matrix matrix = new Matrix();
                        matrix.postRotate(degree);
                        // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
                        prePhoto = Bitmap.createBitmap(prePhoto,
                                0,
                                0,
                                width,
                                height,
                                matrix,
                                true);
                    }
                    else
                    {
                        int quaility = (int) (100 / percent > 80 ? 80
                                : 100 / percent);
                        prePhoto.compress(CompressFormat.JPEG, quaility, bos);
                    }
                    int quaility = (int) (100 / percent > 80 ? 80
                            : 100 / percent);
                    prePhoto.compress(CompressFormat.JPEG, quaility, bos);
                    bos.flush();
                    if (prePhoto != null && !prePhoto.isRecycled())
                    {
                        prePhoto.recycle();
                        prePhoto = null;
                        System.gc();
                    }
                }
            }
            catch (IOException e)
            {
                LogX.getInstance().e(Util.makeLogTag(getClass()), e.toString());
            }
            catch (Exception e)
            {
                LogX.getInstance().e(Util.makeLogTag(getClass()), e.toString());
            }
            finally
            {
                if (bos != null)
                {
                    try
                    {
                        bos.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                if (icon != null && !icon.isRecycled())
                {
                    icon.recycle();
                    icon = null;
                    System.gc();
                }
            }
            Message message = new Message();
            message.what = 0;
            handler.sendMessage(message);
        }
    }
    
}
