package com.xweisoft.wx.family.util;

import java.io.FileInputStream;
import java.io.InputStream;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.xweisoft.wx.family.logic.global.GlobalVariable;

/**
 * <一句话功能简述>
 * 资源包管理类 
 * 
 * @author  administrator
 * @version  [版本号, 2013-10-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ResourceUtil
{
    /**
     * 日志名
     */
    private static final String TAG = "ResourceUtil";

    /**
     * 图片缩放
     */
    private static BitmapFactory.Options sBitmapOptions;
    
    /**
     * 从工程资源或者SD卡中加载图片资源
     * 
     * @param fileName SD卡中存放的资源路径
     * @return 图片
     */
    public static Bitmap loadImageResource(int fileName)
    {
        Bitmap bitmap = null;
        InputStream is = null;
        FileInputStream fis = null;
        try
        {
        	bitmap = BitmapFactory.decodeResource(GlobalVariable.currentActivity.getResources(), fileName); 
        }
        catch (Exception e)
        {
            LogX.getInstance().e(TAG,"::loadImageResource: loadImageResource error " + e.toString());
        }
        finally
        {
            try
            {
                if (is != null)
                {
                    is.close();
                }
                if (fis != null)
                {
                    fis.close();
                }
            }
            catch (Exception e)
            {
                LogX.getInstance().e(TAG, "::loadImageResource: is close Exception. " + e.toString());
            }
            finally
            {
                is = null;
                fis = null;
            }
        }
        return bitmap;
    }

    /**
     * 按比例缩放图片到指定的高度
     * 
     * @param bmp 指定的图片
     * @param height 指定的高度
     * @return 缩放后的图片
     */
    public static Bitmap zoomImage(Bitmap bmp, int height)
    {
        if (bmp == null)
        {
            return null;
        }
        int formerlyHeight = bmp.getHeight();
        int formerlyWidth = bmp.getWidth();
        if (formerlyHeight == height)
        {
            return bmp;
        }
        // 缩放的比例
        float scale = (float) height / (float) formerlyHeight;

        Matrix matrix = new Matrix();
        matrix.postScale(0.99f,
            scale);
        Bitmap resizeBitmap = Bitmap.createBitmap(bmp,
            0,
            0,
            formerlyWidth,
            formerlyHeight,
            matrix,
            true);
        return resizeBitmap;
    }

    /**
     * 提供通用的操作等待的ProgressDialog对象
     * 
     * @param context 上下文
     * @param message 提示信息，可为null，为null时使用默认提示信息
     * @param listener 该监听为用户按返回时的事件处理
     * @return ProgressDialog 对话框
     */
    public static ProgressDialog getWaitProgressDialog(Context context,
        String message, OnCancelListener listener)
    {
        // 创建等待提示框
        ProgressDialog progressDialog = new ProgressDialog(context);
        // 设置进度条风格，风格为圆形，旋转的
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        // 设置ProgressDialog 提示信息
        progressDialog.setMessage(message);

        // 设置ProgressDialog 的进度条是否不明确
        progressDialog.setIndeterminate(false);

        // 设置ProgressDialog 是否可以按退回按键取消
        progressDialog.setCancelable(false);

        if (listener != null)
        {
            progressDialog.setOnCancelListener(listener);
        }

        return progressDialog;
    }

    /**
     * 等比例压缩图片<BR>
     * [功能详细描述]
     * 
     * @param data 路径或文件
     * @param w 需要改变宽 如果不确定就传一个
     * @param h 需要改变的高
     * @param isScale true只压缩 false根据宽度拉伸或压缩
     * @return Bitmap
     */
    public static synchronized Bitmap zoomBitmapWithWidth(byte[] data, int w,
        int h, boolean isScale)
    {
        LogX.getInstance().d(TAG,
            "zoomBitmap w=" + w + " h= " + h);

        if (w < 1 && h < 1)
        {
            return null;
        }

        sBitmapOptions = new BitmapFactory.Options();

        if (data != null)
        {
            try
            {
                int sampleSize = 1;

                // Compute the closest power-of-two scale factor
                // and pass that to sBitmapOptionsCache.inSampleSize, which
                // will
                // result in faster decoding and better quality
                sBitmapOptions.inJustDecodeBounds = true;

                BitmapFactory.decodeByteArray(data,
                    0,
                    data.length,
                    sBitmapOptions);

                Bitmap b = null;

                LogX.getInstance().d(TAG,
                    "sBitmapOptions w=" + sBitmapOptions.outWidth + " h = "
                        + sBitmapOptions.outHeight);

                // 如果是拉伸就直接返回了
                if (w >= sBitmapOptions.outWidth && isScale
                    && h >= sBitmapOptions.outHeight)
                {

                    Bitmap tempBmp = BitmapFactory.decodeByteArray(data,
                        0,
                        data.length);

                    if (tempBmp != null)
                    {
                        b = tempBmp.copy(Bitmap.Config.ARGB_8888,
                            true);
                        if (tempBmp != b)
                        {
                            tempBmp.recycle();
                        }
                    }
                    return b;
                }

                // 需要交换
                if ((sBitmapOptions.outWidth < sBitmapOptions.outHeight && w > h)
                    || (sBitmapOptions.outWidth > sBitmapOptions.outHeight && w < h))
                {
                    int temp = w;
                    w = h;
                    h = temp;
                }

                // 计算图片压缩后的高
                if ((h > 0 && (sBitmapOptions.outHeight / h) > (sBitmapOptions.outWidth / w))
                    || w < 1)
                {
                    // 重新计算宽
                    w = h * sBitmapOptions.outWidth / sBitmapOptions.outHeight;
                }
                else
                {
                    // 重新计算高
                    h = w * sBitmapOptions.outHeight / sBitmapOptions.outWidth;
                }

                int nextWidth = sBitmapOptions.outWidth >> 1;
                int nextHeight = sBitmapOptions.outHeight >> 1;
                while (nextWidth > w && nextHeight > h)
                {
                    sampleSize <<= 1;
                    nextWidth >>= 1;
                    nextHeight >>= 1;
                }

                sBitmapOptions.inSampleSize = sampleSize;
                sBitmapOptions.inJustDecodeBounds = false;

                b = BitmapFactory.decodeByteArray(data,
                    0,
                    data.length,
                    sBitmapOptions);
                if (b != null)
                {
                    // finally rescale to exactly the size we need
                    if (sBitmapOptions.outWidth != w
                        || sBitmapOptions.outHeight != h)
                    {
                        Bitmap tmp = Bitmap.createScaledBitmap(b,
                            w,
                            h,
                            true);
                        if (tmp != b)
                        {
                            b.recycle();
                        }
                        b = tmp;
                    }
                }

                return b;
            }
            catch (Exception e)
            {
                LogX.getInstance().e(TAG, e.toString());
            }
        }
        return null;
    }
}
