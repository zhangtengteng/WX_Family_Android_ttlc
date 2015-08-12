package com.xweisoft.wx.family.service.httpClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.params.CookieSpecPNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.text.TextUtils;

/**
 * 网络请求公共类
 * <功能详细描述>
 * 
 * @author  administrator
 * @version  [版本号, 2014年3月12日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class NetUtil
{
    public static final int POST = 0;
    
    public static final int GET = 1;
    
    private static DefaultHttpClient client;
    
    public static DefaultHttpClient getHttpClient()
    {
        if (client != null)
        {
            return client;
        }
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params,
                HTTP.DEFAULT_CONTENT_CHARSET);
        HttpProtocolParams.setUseExpectContinue(params, true);
        SchemeRegistry schReg = new SchemeRegistry();
        schReg.register(new Scheme("http",
                PlainSocketFactory.getSocketFactory(), 80));
        schReg.register(new Scheme("https",
                SSLSocketFactory.getSocketFactory(), 443));
        ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
                params, schReg);
        client = new DefaultHttpClient(conMgr, params);
        CookieStore cookieStore = new BasicCookieStore();
        client.setCookieStore(cookieStore);
        CookieSpecFactory csf = new CookieSpecFactory()
        {
            public CookieSpec newInstance(HttpParams params)
            {
                return new BrowserCompatSpec()
                {
                    @Override
                    public void validate(Cookie cookie, CookieOrigin origin)
                            throws MalformedCookieException
                    {
                        
                    }
                };
            }
        };
        client.getCookieSpecs().register("oschina", csf);
        client.getParams().setParameter(ClientPNames.COOKIE_POLICY, "oschina");
        client.getParams().setParameter(CookieSpecPNames.SINGLE_COOKIE_HEADER,
                true);
        return client;
        
    }
    
    /**
     * 
     * 访问给定的url
     * @param url
     * @param method：指定访问的方式，GET或�?POST
     * @param params：url中需要封装的参数string 或int
     * @return
     * @throws IOException 
     * */
    public static String openUrl(String url, int method, String params) throws IOException
    {
        HttpResponse response;
        if (method == POST)
        {
            HttpPost httppost = new HttpPost(url);
            StringEntity entity = new StringEntity(params);
            httppost.setEntity(entity);
            response = HttpManager.execute(httppost);
        }
        else
        {
            HttpGet httpGet = new HttpGet(url);
            response = HttpManager.execute(httpGet);
        }
        HttpEntity rentity = response.getEntity();
        StringBuffer sb = null;
        if (rentity != null)
        {
            InputStream in = rentity.getContent();
            Scanner scanner = new Scanner(in);
            sb = new StringBuffer();
            while (scanner.hasNext())
            {
                sb.append(scanner.nextLine());
            }
            in.close();
            scanner.close();
            return new String(sb);
        }
        else
        {
            return "{\"resultCode\":\"-1\",\"resultDesc\":\"请求失败。\"}";
        }
    }
    
    /**
     * 
     * 访问给定的url
     * @param url 请求地址
     * @param bodyStr：post请求，同时存在请求体
     * @return success failed
     * @throws IOException 
     * */
    public static String openPostBodyUrl(String url, String bodyStr)
            throws IOException
    {
        HttpResponse response;
        HttpPost httppost = new HttpPost(url);
        StringEntity entity = new StringEntity(bodyStr, HTTP.UTF_8);
        httppost.setEntity(entity);
        response = HttpManager.execute(httppost);
        HttpEntity rentity = response.getEntity();
        StringBuffer sb = null;
        if (rentity != null)
        {
            InputStream in = rentity.getContent();
            Scanner scanner = new Scanner(in);
            sb = new StringBuffer();
            while (scanner.hasNext())
            {
                sb.append(scanner.nextLine());
            }
            in.close();
            scanner.close();
            return new String(sb);
        }
        else
        {
            return "{\"resultCode\":\"-1\",\"resultDesc\":\"请求失败。\"}";
        }
    }
    
    /**
     * 
     * 访问给定的url
     * @param url
     * @param method：指定访问的方式，GET或�?POST
     * @param params：url中需要封装的参数string 或int
     * @return
     * @throws IOException 
     * */
    public static String openHttpUrl(String url, String method,
            Map<String, Object> params) throws IOException
    {
        //DefaultHttpClient client=getHttpClient();
        HttpResponse response;
        if (method.equalsIgnoreCase("POST"))
        {
            System.out.println(url);
            HttpPost httppost = new HttpPost(url);
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            for (String key : params.keySet())
            {
                if (params.get(key) != null)
                {
                    formparams.add(new BasicNameValuePair(key, params.get(key)
                            .toString()));
                }
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams,
                    "UTF-8");
            httppost.setEntity(entity);
            response = HttpManager.execute(httppost);
        }
        else
        {
            if (params != null && params.size() > 0)
            {
                if (url.indexOf("?") != -1)
                {
                    url = url + "&" + encodeUrl(params);
                }
                else
                {
                    url = url + "?" + encodeUrl(params);
                }
            }
            System.out.println(url);
            HttpGet httpGet = new HttpGet(url);
            response = HttpManager.execute(httpGet);
            //response=client.execute(httpGet);
        }
        HttpEntity rentity = response.getEntity();
        StringBuffer sb = null;
        if (rentity != null)
        {
            InputStream in = rentity.getContent();
            Scanner scanner = new Scanner(in);
            sb = new StringBuffer();
            while (scanner.hasNext())
            {
                sb.append(scanner.nextLine());
            }
            in.close();
            scanner.close();
            return new String(sb);
        }
        else
        {
            return "{\"resultCode\":\"-1\",\"resultDesc\":\"请求失败。\"}";
        }
    }
    
    /**
     * 
     * 访问给定的url
     * @param url 请求地址
     * @param bodyStr：post请求，同时存在请求体
     * @return success failed
     * @throws IOException 
     * */
    public static String openPostBodyUrl(String url,
            Map<String, Object> headParams, String bodyStr) throws IOException
    {
        HttpResponse response;
        HttpPost httppost = new HttpPost(url);
        StringEntity entity = new StringEntity(bodyStr, HTTP.UTF_8);
        httppost.setEntity(entity);
        
        Iterator<Entry<String, Object>> iterator = headParams.entrySet()
                .iterator();
        Entry<String, Object> e = null;
        while (iterator.hasNext())
        {
            e = iterator.next();
            httppost.addHeader(e.getKey(), e.getValue().toString());
        }
        response = HttpManager.execute(httppost);
        HttpEntity rentity = response.getEntity();
        StringBuffer sb = null;
        if (rentity != null)
        {
            InputStream in = rentity.getContent();
            Scanner scanner = new Scanner(in);
            sb = new StringBuffer();
            while (scanner.hasNext())
            {
                sb.append(scanner.nextLine());
            }
            in.close();
            scanner.close();
            return new String(sb);
        }
        else
        {
            //{"resultCode":"1000","resultDesc":"成功"}
            return "{\"resultCode\":\"-1\",\"resultDesc\":\"请求失败。\"}";
        }
    }
    
    /**
     * 
     * 访问给定的url
     * @param url 请求地址
     * @param bodyStr：post请求，同时存在请求体
     * @return success failed
     * @throws IOException 
     * */
    public static HttpEntity requestPostBodyUrl(String url, String bodyStr) throws Exception, Error
    {
        try
        {
            HttpPost httppost = new HttpPost(url);
            if (!TextUtils.isEmpty(bodyStr))
            {
                StringEntity entity = new StringEntity(bodyStr, HTTP.UTF_8);
                httppost.setEntity(entity);
            }
            HttpResponse response = HttpManager.execute(httppost);
            HttpEntity rentity = response.getEntity();
//            InputStream in = rentity.getContent();
            return rentity;
        }
        catch (Exception e)
        {
            throw new Error();
        }
    }
    
    /**
     * 
     * 访问给定的url
     * @param url
     * @param method：指定访问的方式，GET或�?POST
     * @param params：url中需要封装的参数string 或int
     * @return
     * @throws IOException 
     * */
    public static HttpEntity downloadUpdateUrl(String url) throws IOException
    {
        try
        {
            HttpResponse response;
            HttpGet httpGet = new HttpGet(url);
            response = HttpManager.execute(httpGet);
            HttpEntity entity = response.getEntity();
            return entity;
        }
        catch (Exception e)
        {
            throw new Error();
        }
    }
    
    /**
     *
     * @param params
     * @return
     */
    public static String encodeUrl(Map<String, Object> params)
    {
        if (params == null || params.size() == 0)
            return "";
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String key : params.keySet())
        {
            if (first)
                first = false;
            else
                sb.append("&");
            
            if (params.get(key) != null)
            {
                sb.append(key + "=" + params.get(key).toString());
            }
        }
        return sb.toString();
    }
}