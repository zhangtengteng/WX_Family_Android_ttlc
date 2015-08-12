package com.xweisoft.wx.family.ui.message;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.R.mipmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.logic.global.GlobalConstant;
import com.xweisoft.wx.family.logic.global.HttpAddressProperties;
import com.xweisoft.wx.family.logic.model.MessageItem;
import com.xweisoft.wx.family.logic.model.MsgContentItem;
import com.xweisoft.wx.family.logic.model.MsgNoticeItem;
import com.xweisoft.wx.family.logic.model.response.MessageInfoResp;
import com.xweisoft.wx.family.ui.BaseActivity;
import com.xweisoft.wx.family.util.CommonTitleUtil;
import com.xweisoft.wx.family.util.DateTools;
import com.xweisoft.wx.family.util.HttpRequestUtil;
import com.xweisoft.wx.family.util.ProgressUtil;
import com.xweisoft.wx.family.util.StringUtil;
import com.xweisoft.wx.family.widget.NetHandler;

public class NotificationDetailActivity extends BaseActivity
{
    /**
     * 
     */
    private String type;
    
    /**
     * 富文本内容str
     */
    String contentStr = "";
    
    /**
     * 内容
     */
    private WebView mWebView;
    
    /**
     *  消息对象
     */
    private MsgNoticeItem mItem;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayout());
        getBundle();
        initViews();
    }
    
    @Override
    public void initViews()
    {
        CommonTitleUtil.initCommonTitle(this, "通知详情", null, false, true);
        
        mWebView = (WebView) findViewById(R.id.message_detail_webview);
        //注册webview与js交互的监听,"JS"名字自定义，必须确保js调用的名字与此处一致
        mWebView.clearCache(false);
        mWebView.getSettings().setJavaScriptEnabled(true);
        initData();
    }
    
    @Override
    public void bindListener()
    {
        //取消webview 的长安选中效果
        mWebView.setOnLongClickListener(new WebView.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                return true;
            }
        });
        //屏蔽webview禁用 按数字时 打电话功能
        mWebView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                if (url.indexOf("tel:") < 0)
                {
                    view.loadUrl(url);
                }
                return true;
            }
        });
    }
    
    /**
     * 更新UI数据
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private void initData()
    {
        if (null != mItem)
        {
            //给内容增加字体颜色为灰色#585858
            String content = mItem.content;
            if (!StringUtil.isEmpty(content))
            {
                //webview加载时，不需要使用fromHtml方法，设置setText时需要。
                //否则导致没有换行
                contentStr = dealImgStr(content);
            }
            mWebView.loadDataWithBaseURL("file:///android_asset/",
                    getHeadHtml()
                            + "<body style=\"padding:0;margin:0;\"><div><div style=\"padding-left:10px;padding-right:10px;\">"
                            + getBodyHtml()
                            + "</div>"
                            + "<div style=\"color:#585858;font-size:14px;padding-top:5px;padding-left:10px;padding-right:10px;line-height:30px;\">"
                            + contentStr + "</div></div>" + "</body></html>",
                    "text/html",
                    "UTF-8",
                    "");
        }
    }
    
    /** <一句话功能简述>
     * 处理服务器返回的富文本中<p标签未增加宽度限制属性
     * <功能详细描述>
     * @param imgStr
     * @return [参数说明]
     * 
     * @return String [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("rawtypes")
    private String dealPStr(String imgStr)
    {
        if (imgStr.contains("<p"))
        {
            HashMap<String, String> map = new HashMap<String, String>();
            //正则匹配img标签下的src属性，只保留src属性，其余属性全部去除
            Pattern p = Pattern.compile("<p(.+?)>");
            Matcher m = p.matcher(imgStr);
            while (m.find())
            {
                String s = m.group();
                Pattern p1 = Pattern.compile("style=\"(.+?)\"");
                Matcher m1 = p1.matcher(s);
                while (m1.find())
                {
                    String replaceStr = "<p " + ">";
                    //修改如果<img>前是<p>，这不替换为<p>,否则会导致图片标签被剔除
                    if (!TextUtils.isEmpty(s) && s.contains("<p><img"))
                    {
                        break;
                    }
                    map.put(s, replaceStr);
                }
            }
            Set set = map.keySet();
            Iterator iter = set.iterator();
            while (iter.hasNext())
            {
                String key = (String) iter.next();
                imgStr = imgStr.replace(key, map.get(key));
            }
        }
        return imgStr;
        
    }
    
    /** <一句话功能简述>
     * 处理服务器返回的富文本中<span标签未增加宽度限制属性
     * <功能详细描述>
     * @param imgStr
     * @return [参数说明]
     * 
     * @return String [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("rawtypes")
    private String dealSpanStr(String imgStr)
    {
        if (imgStr.contains("<span"))
        {
            HashMap<String, String> map = new HashMap<String, String>();
            //正则匹配img标签下的src属性，只保留src属性，其余属性全部去除
            Pattern p = Pattern.compile("<span(.+?)>");
            Matcher m = p.matcher(imgStr);
            while (m.find())
            {
                String s = m.group();
                Pattern p1 = Pattern.compile("style=\"(.+?)\"");
                Matcher m1 = p1.matcher(s);
                while (m1.find())
                {
                    String replaceStr = "<span " + ">";
                    map.put(s, replaceStr);
                }
            }
            Set set = map.keySet();
            Iterator iter = set.iterator();
            while (iter.hasNext())
            {
                String key = (String) iter.next();
                imgStr = imgStr.replace(key, map.get(key));
            }
        }
        return imgStr;
        
    }
    
    /** <一句话功能简述>
     * 处理服务器返回的富文本中img标签未增加宽度限制属性
     * <功能详细描述>
     * @param imgStr
     * @return [参数说明]
     * 
     * @return String [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("rawtypes")
    private String dealImgStr(String imgStr)
    {
        if (imgStr.contains("<img"))
        {
            HashMap<String, String> map = new HashMap<String, String>();
            //正则匹配img标签下的src属性，只保留src属性，其余属性全部去除
            Pattern p = Pattern.compile("<[Ii]mg(.+?)/>");
            Matcher m = p.matcher(imgStr);
            while (m.find())
            {
                String s = m.group();
                Pattern p1 = Pattern.compile("[Ss]rc=\"(.+?)\"");
                Matcher m1 = p1.matcher(s);
                while (m1.find())
                {
                    String replaceStr = "<img style=\"max-width:100%;\" "
                            + m1.group() + "/>";
                    map.put(s, replaceStr);
                }
            }
            Set set = map.keySet();
            Iterator iter = set.iterator();
            while (iter.hasNext())
            {
                String key = (String) iter.next();
                imgStr = imgStr.replace(key, map.get(key));
            }
        }
        return imgStr;
        
    }
    
    /** <一句话功能简述>
     * 头html
     * <功能详细描述>
     * @return [参数说明]
     * @return String [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private String getHeadHtml()
    {
        StringBuffer bodyHtmlBuffer = new StringBuffer();
        bodyHtmlBuffer.append("<html>");
        bodyHtmlBuffer.append("<head>");
        bodyHtmlBuffer.append("<meta charset=\"utf-8\" />");
        bodyHtmlBuffer.append("<script>");
        bodyHtmlBuffer.append("function commentList(){ window.JS.commentList();}");
        bodyHtmlBuffer.append("</script>");
        bodyHtmlBuffer.append("</head>");
        return bodyHtmlBuffer.toString();
    }
    
    /** <一句话功能简述>
     * 拼装网址频道顶部html
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private String getBodyHtml()
    {
        StringBuffer bodyHtmlBuffer = new StringBuffer();
        if (null != mItem)
        {
            bodyHtmlBuffer.append("<div style=\"height:20px;\">");
            // 作者
            bodyHtmlBuffer.append("<div style=\"width:90%;height:20px;float:left;color:#454545;text-overflow:ellipsis;white-space:nowrap;overflow:hidden;margin-top:10px;text-align:left;font-size:16px;\">");
            bodyHtmlBuffer.append(mItem.title);
            bodyHtmlBuffer.append("</div>");
            bodyHtmlBuffer.append("</div>");
            
            //标题
            bodyHtmlBuffer.append("<div style=\"word-wrap:break-word;width:100%;text-align:left;float:left;margin-top:5px;margin-bottom:10px;color:#a7a7a7;font-size:14px;\">");
            bodyHtmlBuffer.append(DateTools.parseTimeMillis2Str(mItem.publishTime,
                    DateTools.YYYY_MM_DD));
            bodyHtmlBuffer.append("</div></br>");
            
            //分割线
            //            bodyHtmlBuffer.append("<div style=\"width:100%;text-align:center;margin-bottom:5px;\">");
            //            bodyHtmlBuffer.append("<img style=\"max-width:100%;height:1px;\" src=\"images/news_detail_divider_line.png\"/>");
            //            bodyHtmlBuffer.append("</div>");
        }
        return bodyHtmlBuffer.toString();
    }
    
    private void getBundle()
    {
        type = getIntent().getStringExtra("type");
        mItem = (MsgNoticeItem) getIntent().getSerializableExtra("item");
    }
    
    @Override
    public int getActivityLayout()
    {
        return R.layout.message_detail_activity;
    }
    
}
