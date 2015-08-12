package com.xweisoft.wx.family.util.async;

import android.os.AsyncTask;

/**
 * <一句话功能简述>
 * 异步任务工具类
 * 
 * @author  administrator
 * @version  [版本号, 2013-10-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class AsyncTaskUtils
{
    /**
     * 
     * 异步操作<BR>
     * 
     * @param callable 异步执行接口,在后台线程运行
     * @param callback 异步执行结果接口，在主线程运行
     */
    public static <T> void doAsync(final Callable<T> callable, final Callback<T> callback){
        new AsyncTask<Void, Void, T>()
        {

            @Override
            protected T doInBackground(Void... params)
            {
                try
                {
                    return callable.call();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                return null;
            }
            
            @Override
            protected void onPostExecute(T result) {  
                callback.onCallback(result);  
            } 
        }.execute((Void[])null);
    }
}
