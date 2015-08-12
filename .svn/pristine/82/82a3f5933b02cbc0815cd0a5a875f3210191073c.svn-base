package com.xweisoft.wx.family.widget;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.logic.global.GlobalConstant;
import com.xweisoft.wx.family.util.LogX;

/**
 * <一句话功能简述>
 * 自定义的webview,点击内嵌网址可以返回上一级页面
 * 
 * @author  administrator
 * @version  [版本号, 2013-10-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class GoBackWebView extends WebView
{
    
    private Context context;
    
    /**
     * 进度条对话框
     */
    private ProgressDialog progressDialog;
    
    /**
     * 下载进度条
     */
    private ProgressDialog dialog;
    
    /**
     * 显示进度对话框  
     */
    private static final int PROGRESS_DIALOG_SHOW = 0;
    
    /**
     * 隐藏进度对话框
     */
    private static final int PROGRESS_DIALOG_HIDE = 1;
    
    private WebSettings settings;
    
    private Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            //定义一个Handler，用于处理下载线程与UI间通讯
            if (!Thread.currentThread().isInterrupted())
            {
                switch (msg.what)
                {
                    case PROGRESS_DIALOG_SHOW:
                        if (null != progressDialog)
                        {
                            progressDialog.show();//显示进度对话框         
                        }
                        break;
                    case PROGRESS_DIALOG_HIDE:
                        if (null != progressDialog)
                        {
                            progressDialog.hide();//隐藏进度对话框，不可使用dismiss()、cancel(),否则再次调用show()时，显示的对话框小圆圈不会动。
                        }
                        closeProgressDialog();
                        break;
                }
            }
            super.handleMessage(msg);
        }
    };
    
    private WebViewClient webViewClient = new WebViewClient()
    {
        //重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
        public boolean shouldOverrideUrlLoading(final WebView view,
                final String url)
        {
            loadurl(view, url);//载入网页
            
            return true;
        }
        
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                SslError error)
        {
            handler.proceed(); // 接受所有网站的证书
        }
        
        public void onReceivedError(WebView view, int errorCode,
                String description, String failingUrl)
        {
            //加载出错时直接finish
            //-2：网络断开  -10加载出错
            if (errorCode == -10)
            {
                ((Activity) context).finish();
            }
        }
        
        @Override
        public void onPageFinished(WebView view, String url)
        {
            view.requestFocus();
            super.onPageFinished(view, url);
        };
        
    };
    
    private WebChromeClient webChromeClient = new WebChromeClient()
    {
        public void onProgressChanged(WebView view, int progress)
        {
            //载入进度改变而触发 
            if (progress == 100)
            {
                handler.sendEmptyMessage(PROGRESS_DIALOG_HIDE);//如果全部载入,隐藏进度对话框
            }
            super.onProgressChanged(view, progress);
        }
    };
    
    public GoBackWebView(Context context)
    {
        super(context);
        this.context = context;
        createView(context);
    }
    
    public GoBackWebView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.context = context;
        createView(context);
    }
    
    public GoBackWebView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        this.context = context;
        createView(context);
    }
    
    /**
     * 
     * [创建view]<BR>
     * @param context
     */
    private void createView(Context context)
    {
        this.context = context;
        //        createProgressDialog();
        
        settings = this.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        this.setScrollBarStyle(0);
        this.setWebViewClient(webViewClient);
        this.setWebChromeClient(webChromeClient);
        this.setDownloadListener(new MyWebViewDownLoadListener());
    }
    
    /**
     * 
     * [载入网页]<BR>
     * @param url 请求地址
     */
    public void loadurl(String url)
    {
        loadurl(this, url);
    }
    
    /**
     * 
     * [载入网页]<BR>
     * @param view webview
     * @param url 请求地址
     */
    private void loadurl(final WebView view, final String url)
    {
        //拦截电话号码
        if (!TextUtils.isEmpty(url) && url.startsWith("tel:"))
        {
            return;
        }
        //解决健康咨询中标题按下返回页面无法加载问题
        if (!TextUtils.isEmpty(url) && url.startsWith("cloudjs://p"))
        {
            ((Activity) context).finish();
            return;
        }
        handler.post(new Runnable()
        {
            
            @Override
            public void run()
            {
                handler.sendEmptyMessage(PROGRESS_DIALOG_SHOW);
                view.loadUrl(url);//载入网页
            }
        });
    }
    
    /**
     * 
     * [创建进度条对话框]<BR>
     */
    private void createProgressDialog()
    {
        if (progressDialog == null)
        {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(context.getString(R.string.loading));
        }
    }
    
    /**
     * 关闭进度条对话框
     */
    public void closeProgressDialog()
    {
        if (null != progressDialog && progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
        progressDialog = null;
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.canGoBack())
        {
            this.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    private class MyWebViewDownLoadListener implements DownloadListener
    {
        
        @Override
        public void onDownloadStart(String url, String userAgent,
                String contentDisposition, String mimetype, long contentLength)
        {
            //            Uri uri = Uri.parse(url);
            //            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            //            context.startActivity(intent);
            new DownloadTask().execute(new String[] { url });
        }
    }
    
    private class DownloadTask extends AsyncTask<String, Void, String>
    {
        
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setMessage("院线通下载中...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }
        
        @Override
        protected String doInBackground(String... params)
        {
            String imageUrl = params[0];
            HttpURLConnection con = null;
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            BufferedInputStream bis = null;
            File installDir = null;
            File apkFile = null;
            try
            {
                URL url = new URL(imageUrl);
                con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(5 * 1000);
                con.setReadTimeout(15 * 1000);
                //                con.setDoInput(true);
                //                con.setDoOutput(true);
                bis = new BufferedInputStream(con.getInputStream());
                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                        && hasExternalStoragePermission(context))
                {
                    installDir = new File(GlobalConstant.DOWNLOAD_PATH_SD);
                }
                if (installDir == null)
                {
                    String dir = null;
                    if (null != Environment.getExternalStorageDirectory()
                            .getParentFile()
                            && null != Environment.getExternalStorageDirectory()
                                    .getParentFile()
                                    .listFiles())
                    {
                        dir = Environment.getExternalStorageDirectory()
                                .getParentFile()
                                .listFiles()[0]
                                + "/" + context.getPackageName() + "/cache/";
                    }
                    if (dir == null)
                    {
                        Toast.makeText(context,
                                "抱歉！没有sd卡无法更新升级",
                                Toast.LENGTH_SHORT).show();
                        return "";
                    }
                    installDir = new File(dir);
                }
                apkFile = new File(installDir, "YXT_Android.apk");
                if (!installDir.exists())
                {
                    installDir.mkdirs();
                }
                if (apkFile.exists())
                {
                    apkFile.delete();
                }
                fos = new FileOutputStream(apkFile);
                bos = new BufferedOutputStream(fos);
                byte[] b = new byte[1024];
                int length;
                while ((length = bis.read(b)) != -1)
                {
                    bos.write(b, 0, length);
                    bos.flush();
                }
            }
            catch (Exception e)
            {
                LogX.getInstance().e("=========", e.toString());
            }
            finally
            {
                try
                {
                    if (bis != null)
                    {
                        bis.close();
                    }
                    if (bos != null)
                    {
                        bos.close();
                    }
                    if (con != null)
                    {
                        con.disconnect();
                    }
                }
                catch (IOException e)
                {
                    LogX.getInstance().e("==========", e.toString());
                }
            }
            return apkFile != null ? apkFile.getPath() : "";
        }
        
        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            dialog.cancel();
            if (!TextUtils.isEmpty(result) && new File(result).exists())
            {
                closeProgressDialog();
                Toast.makeText(context,
                        "文件下载成功，保存在：" + result,
                        Toast.LENGTH_SHORT).show();
            }
            else
            {
                
                Toast.makeText(context, "下载失败，请稍后重试", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
    
    private static boolean hasExternalStoragePermission(Context context)
    {
        int perm = context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
        return perm == PackageManager.PERMISSION_GRANTED;
    }
}
