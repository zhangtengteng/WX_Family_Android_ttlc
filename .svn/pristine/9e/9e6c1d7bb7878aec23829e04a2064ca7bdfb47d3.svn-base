package com.xweisoft.wx.family.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.xweisoft.wx.family.R;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  administrator
 * @version  [版本号, 2013-11-21]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class LoadDataLayout extends LinearLayout implements OnClickListener
{
    private Context mContext;
    
    /**
     * 加载栏目状态：正在加载数据
     */
    public static final int STATE_LOAD_DATA = 0;
    
    /**
     * 加载栏目状态：加载数据成功
     */
    public static final int STATE_LOAD_DATA_SUCCESS = 1;
    
    /**
     * 加载数据状态：没有数据
     */
    public static final int STATE_LOAD_DATA_NO_CONTENT = 2;
    
    /**
     * 加载栏目状态：加载数据失败
     */
    public static final int STATE_LOAD_DATA_FAILED = 3;
    
    /**
     * 加载更多数据状态：连接失败
     */
    public static final int STATE_NET_ERROR = 4;
    
    /**
     * 内容layout
     */
    private View mainLayout;
    
    /**
     * 正在加载栏目layout
     */
    private View loadingLayout;
    
    /**
     * 栏目加载失败
     */
    private View loadFailedLayout;
    
    public interface OnLoadFailedRefreshClickListener
    {
        public void onLoadFailedRefreshClickListener();
    }
    
    private OnLoadFailedRefreshClickListener onLoadFailedRefreshClickListener;
    
    public LoadDataLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
        createView();
    }
    
    public LoadDataLayout(Context context)
    {
        super(context);
        mContext = context;
        createView();
    }
    
    /**
     * [创建view]<BR>
     */
    private void createView()
    {
        inflate(mContext, R.layout.common_loading_data_layout, this);
        
        initViews();
        bindListener();
    }
    
    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.layout_load_data_failed)
        {
            //加载更多或加载更多失败后重新加载
            if (onLoadFailedRefreshClickListener != null)
            {
                onLoadFailedRefreshClickListener.onLoadFailedRefreshClickListener();
            }
        }
    }
    
    /**
     * [根据加载数据状态设置布局的可见性]<BR>
     * @param loadDataState 加载数据状态
     */
    public void showLayoutByLoadDataState(int loadDataState)
    {
        switch (loadDataState)
        {
            case STATE_LOAD_DATA:
                loadingLayout.setVisibility(View.VISIBLE);
                if (mainLayout != null)
                {
                    mainLayout.setVisibility(View.GONE);
                }
                loadFailedLayout.setVisibility(View.GONE);
                break;
            
            case STATE_LOAD_DATA_SUCCESS:
                loadingLayout.setVisibility(View.GONE);
                if (mainLayout != null)
                {
                    mainLayout.setVisibility(View.VISIBLE);
                }
                loadFailedLayout.setVisibility(View.GONE);
                break;
            
            case STATE_LOAD_DATA_NO_CONTENT:
            case STATE_LOAD_DATA_FAILED:
            case STATE_NET_ERROR:
                loadingLayout.setVisibility(View.GONE);
                if (mainLayout != null)
                {
                    mainLayout.setVisibility(View.GONE);
                }
                loadFailedLayout.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
    
    /**
     * [初始化控件]<BR>
     */
    protected void initViews()
    {
        loadingLayout = findViewById(R.id.layout_load_data);
        loadFailedLayout = (View) findViewById(R.id.layout_load_data_failed);
    }
    
    /**
     * [控件绑定事件]<BR>
     */
    protected void bindListener()
    {
        loadFailedLayout.setOnClickListener(this);
    }
    
    public void setOnLoadFailedRefreshClickListener(
            OnLoadFailedRefreshClickListener onLoadFailedRefreshClickListener)
    {
        this.onLoadFailedRefreshClickListener = onLoadFailedRefreshClickListener;
    }
    
    public void setMainLayout(View mainLayout)
    {
        this.mainLayout = mainLayout;
    }
}
