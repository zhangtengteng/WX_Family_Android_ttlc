package com.xweisoft.wx.family.widget;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.http.SslError;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.xweisoft.wx.family.R;

/**
 * <一句话功能简述>
 * 自定义的webview
 * 
 * @author  administrator
 * @version  [版本号, 2013-10-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MyWebView extends WebView
{
    
    private Context context;
    
    private Activity activity;
    
    /**
     * 进度条对话框
     */
    private ProgressDialog progressDialog;
    
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
            MarketToast.showToast(context,
                    R.string.load_failed,
                    Toast.LENGTH_SHORT);
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
    
    public MyWebView(Context context)
    {
        super(context);
        createView(context);
    }
    
    public MyWebView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        createView(context);
    }
    
    public MyWebView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
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
        createProgressDialog();
        
        settings = this.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //让图片自适应屏幕
        //        settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        this.setScrollBarStyle(0);
        this.setWebViewClient(webViewClient);
        this.setWebChromeClient(webChromeClient);
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
    
    /** <一句话功能简述>
     * 用于webview与activity之间跳转使用
     * <功能详细描述>
     * @param activity
     * @param url [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void loadurl(Activity activity, String url)
    {
        this.activity = activity;
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
        //点击服务条款链接
        if (!TextUtils.isEmpty(url) && url.startsWith("provision://"))
        {
            //            Intent intent = new Intent(context,
            //                    RegisterServiceTermActivity.class);
            //            context.startActivity(intent);
            return;
        }
        //点击注册
        if (!TextUtils.isEmpty(url) && url.startsWith("discuz://"))
        {
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
        //		new Thread()
        //        {
        //            public void run()
        //            {
        //                handler.sendEmptyMessage(PROGRESS_DIALOG_SHOW);
        //                view.loadUrl(url);//载入网页
        //            }
        //        }.start();
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
    
}
