package com.xweisoft.wx.family.widget;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.WXApplication;
import com.xweisoft.wx.family.logic.global.NetWorkCodes.CommontErrorCodes;
import com.xweisoft.wx.family.logic.global.NetWorkCodes.CommontSuccessCodes;
import com.xweisoft.wx.family.logic.model.response.CommonResp;
import com.xweisoft.wx.family.util.ProgressUtil;

/**
 * 网络请求通用处理类
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-5-12]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public abstract class NetHandler extends Handler
{
    private Context mContext = null;
    
    /**
     * 请求成功
     */
    private static final String SUCCESS = "200";
    
    /**
     * 登录过期
     */
    private static final String LOGIN_EXPIRE = "4004";
    
    /**
     * Token为空
     */
    private static final String TOKEN_NULL = "token_null";
    
    /**
     * 是否toast提示
     */
    private boolean isToast = true;
    
    public NetHandler()
    {
        initToast();
    }
    
    public NetHandler(boolean isToast)
    {
        this.isToast = isToast;
        initToast();
    }
    
    private void initToast()
    {
        mContext = WXApplication.getInstance();
        if (null == mContext)
        {
            isToast = false;
        }
    }
    
    @Override
    public void handleMessage(Message msg)
    {
        ProgressUtil.dismissProgressDialog();
        onResponse();
        switch (msg.what)
        {
            case CommontSuccessCodes.SUCCESS:
                if (null != msg.obj)
                {
                    String errCode = ((CommonResp) msg.obj).getCode();
                    String errMsg = ((CommonResp) msg.obj).getMessage();
                    if (errCode.equals(SUCCESS))
                    {
                        onSuccess(errMsg, msg);
                    }
                    else
                    {
                        if (errCode.equals(LOGIN_EXPIRE)
                                || TOKEN_NULL.equals(TokenNull(errCode)))
                        {
                            // 提示重新登录
                            onJumpTo();
                        }
                        else
                        {
                            onFailed(errCode, errMsg, msg);
                        }
                    }
                }
                else
                {
                    if (isToast)
                    {
                        Toast.makeText(mContext,
                                R.string.system_error,
                                Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case CommontErrorCodes.NETWORK_TIMEOUT_ERROR:
                netTimeout();
                if (isToast)
                {
                    Toast.makeText(mContext,
                            R.string.network_timeout,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case CommontErrorCodes.NETWORK_ERROR:
                networkErr();
                if (isToast)
                {
                    Toast.makeText(mContext,
                            R.string.network_error,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case CommontErrorCodes.OTHER_ERROR:
                otherErr();
                if (isToast)
                {
                    Toast.makeText(mContext,
                            R.string.system_error,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
    
    /**
     * 接口响应成功
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void onResponse()
    {
        
    };
    
    /**
     * token失效，登录跳转
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void onJumpTo()
    {
        
    };
    
    /**
     * 接口请求成功（接口返回200响应码）
     * <一句话功能简述>
     * <功能详细描述>
     * @param msg [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public abstract void onSuccess(String errMsg, Message msg);
    
    /**
     * 接口请求失败（接口返回非200响应码，不包含登录过期的其他情况）
     * <一句话功能简述>
     * <功能详细描述>
     * @param msg [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public abstract void onFailed(String errCode, String errMsg, Message msg);
    
    /**
     * 网络请求超时
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void netTimeout()
    {
        
    }
    
    /**
     * 网络请求出错
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void networkErr()
    {
        
    }
    
    /**
     * 其他错误
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void otherErr()
    {
        
    }
    
    /**
     * 判断token失效的返回码
     * <一句话功能简述>
     * <功能详细描述>
     * @param errCode
     * @return [参数说明]
     * 
     * @return String [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private String TokenNull(String errCode)
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("1009", TOKEN_NULL);
        map.put("1041", TOKEN_NULL);
        map.put("1011", TOKEN_NULL);
        map.put("1021", TOKEN_NULL);
        map.put("1027", TOKEN_NULL);
        map.put("1031", TOKEN_NULL);
        map.put("1036", TOKEN_NULL);
        map.put("2012", TOKEN_NULL);
        map.put("3029", TOKEN_NULL);
        errCode = map.get(errCode);
        if (null == errCode)
        {
            errCode = "";
        }
        return errCode;
    }
}
