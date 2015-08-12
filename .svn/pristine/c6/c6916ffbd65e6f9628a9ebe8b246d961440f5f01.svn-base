package com.xweisoft.wx.family.logic.request;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.xweisoft.wx.family.logic.global.NetWorkCodes;
import com.xweisoft.wx.family.logic.global.NetWorkCodes.CommontSuccessCodes;
import com.xweisoft.wx.family.logic.model.LoadItem;
import com.xweisoft.wx.family.logic.model.response.LoadResp;
import com.xweisoft.wx.family.util.LogX;

/**
 * <一句话功能简述>
 * 启动界面请求类
 * <功能详细描述>
 * @author  houfangfang
 * @version  [版本号, 2014-2-12]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class LoadRequest extends JsonRequest
{
    /**
     * 日志标记
     */
    private static final String TAG = "===LoadRequest===";
    
    /**
     * 请求处理类
     */
    private Handler handler;
    
    /**
     * 请求响应类
     */
    private LoadResp resp;
    
    public LoadRequest(Context context, Handler handler, String httpUrl)
    {
        super(context, handler, httpUrl);
        this.handler = handler;
    }
    
    @Override
    protected String createRequestPara()
    {
        return null;
    }
    
    @Override
    protected void parseJsonResponse(String json)
    {
        resp = new LoadResp();
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            String str = JsonUtils.getJsonStringValue(jsonObject, "android");
            JSONObject object = new JSONObject(str);
            String jsonStr = JsonUtils.getJsonStringValue(object, "data");
            JSONObject jsonObj = new JSONObject(jsonStr);
            LoadItem loadItem = new LoadItem(jsonObj);
            resp.loadItem = loadItem;
            handler.sendMessage(handler.obtainMessage(CommontSuccessCodes.SUCCESS,
                    resp));
        }
        catch (NumberFormatException e)
        {
            LogX.getInstance().e(TAG, e.toString());
            handler.sendEmptyMessage(NetWorkCodes.CommontErrorCodes.OTHER_ERROR);
        }
        catch (JSONException e)
        {
            LogX.getInstance().e(TAG, e.toString());
            handler.sendEmptyMessage(NetWorkCodes.CommontErrorCodes.OTHER_ERROR);
        }
    }
    
}
