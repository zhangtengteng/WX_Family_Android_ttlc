package com.xweisoft.wx.family.service.http;

import android.content.Context;

/**
 * <一句话功能简述>
 *更新请求回调接口
 * 
 * @author  administrator
 * @version  [版本号, 2013-10-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface IUpgradeCallback extends IDownloadCallback
{

    /**
     * 设置升级进度变化回调函数
     * @param getLength      下载大小
     * @param totalLength    文件大小
     */
    void onProgressChanged(long getLength, long totalLength);

    /**
     * 用户取消升级回调 函数
     */
    void canceledCallback();

    /**
     * 开始下载升级包通知回调
     */
    void startDownloadCallback();

    /**
     * 升级包下载完成回调通知接口
     * @param context 上下文
     */
    void successDownloadCallback(final Context context);
}
