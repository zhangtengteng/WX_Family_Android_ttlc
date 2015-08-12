package com.xweisoft.wx.family.util;

import java.util.Locale;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.logic.global.NetWorkCodes;
import com.xweisoft.wx.family.widget.MarketToast;

/**
 * <一句话功能简述>
 * 错误处理类
 * 
 * @author  administrator
 * @version  [版本号, 2013-10-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ErrorMsgUtil
{
    
    /**
     * 显示服务器端返回的错误提示信息，如果返回的为空，则提示提示对应的操作失败信息，请重试
     * @param mContext 上下文
     * @param operDesc 操作失败描述
     * @param operCode 操作失败编码
     */
    public static void showErrorMsg(Context context, String operDesc, int operCode)
    {
        showErrorMsg(context, operDesc, operCode, R.string.system_error);
    }
    
    /**
     * 显示服务器端返回的错误提示信息，如果返回的为空，则提示提示对应的操作失败信息，请重试
     * @param mContext 上下文
     * @param operDesc 操作失败描述
     * @param operCode 操作失败编码
     * @param emptyErrorMsgResID 错误提示信息返回的为空时，提示对应的操作失败信息对应的资源ID
     */
    public static void showErrorMsg(Context context, String operDesc, int operCode, int emptyErrorMsgResID)
    {
        switch(operCode)
        {
            case NetWorkCodes.CommontErrorCodes.NETWORK_ERROR:
                MarketToast.showToast(context, R.string.network_error, Toast.LENGTH_SHORT);
                return ;
            case NetWorkCodes.CommontErrorCodes.NETWORK_TIMEOUT_ERROR:
                MarketToast.showToast(context, R.string.network_timeout, Toast.LENGTH_SHORT);
                return ;
                
        }
        
        if (TextUtils.isEmpty(operDesc))
        {
            Locale.setDefault(Locale.ENGLISH);
            android.content.res.Configuration config = new android.content.res.Configuration();
            config.locale = Locale.ENGLISH;
            context.getApplicationContext().getResources().updateConfiguration(config,context.getResources().getDisplayMetrics());
            MarketToast.showToast(context, context.getResources().getString(emptyErrorMsgResID, operCode));
        }
        else
        {
            switch(operCode)
            {
                case NetWorkCodes.CommontErrorCodes.OTHER_ERROR:
                    //与NetWorkErrorCodes.CommontErrorCodes.SYSTEM_ERROR一样，显示系统错误
                    MarketToast.showToast(context, R.string.system_error, Toast.LENGTH_SHORT);
                    break;
                default :
                    MarketToast.showToast(context, operDesc);
            }
        }
    }
    
    /**
     * 本地提示错误信息
     * @param mContext 上下文
     * @param operDesc 操作失败描述
     * @param emptyErrorMsgResID 操作失败编码   提示对应的操作失败信息对应的资源ID
     */
    public static void showErrorMsg(Context context, int operCode, int emptyErrorMsgResID)
    {
        switch(operCode)
        {
            case NetWorkCodes.CommontErrorCodes.NETWORK_ERROR:
                MarketToast.showToast(context, R.string.network_error, Toast.LENGTH_SHORT);
                break ;
            case NetWorkCodes.CommontErrorCodes.NETWORK_TIMEOUT_ERROR:
                MarketToast.showToast(context, R.string.network_timeout, Toast.LENGTH_SHORT);
                break ;
            default :
                MarketToast.showToast(context, context.getResources().getString(emptyErrorMsgResID, operCode));
        }
    }
}
