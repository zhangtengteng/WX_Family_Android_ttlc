package com.xweisoft.wx.family.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.xweisoft.wx.family.R;

/**
 * 打电话的Dialog
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  gac
 * @version  [版本号, 2014-7-7]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CommonDialog extends BaseDialog implements View.OnClickListener
{
    /**
     * 对话框标题
     */
    private TextView mTitleView;
    
    /**
     * 对话框提示内容
     */
    private TextView mContentView;
    
    /**
     * 对话框标题
     */
    private TextView mCancelTextView;
    
    /**
     * 对话框提示内容
     */
    private TextView mConfirmTextView;
    
    private String title;
    
    private String content;
    
    private String cancel;
    
    private String confirm;
    
    private OnButtonClickListener onButtonClickListener;
    
    private Context mContext;
    
    private boolean titleGone;
    
    private boolean contentGone;
    
    private int titleGravity = -1;
    
    private int contentGravity = -1;
    
    public static final int GRAVITY_DEFAULT = -1;
    
    public CommonDialog(Context context,
            OnButtonClickListener onButtonClickListener)
    {
        super(context, R.layout.apply_cancel_dialog);
        mContext = context;
        this.onButtonClickListener = onButtonClickListener;
    }
    
    public CommonDialog(Context context, String title, String content,
            OnButtonClickListener onButtonClickListener)
    {
        super(context, R.layout.apply_cancel_dialog);
        this.title = title;
        this.content = content;
        mContext = context;
        this.onButtonClickListener = onButtonClickListener;
    }
    
    public CommonDialog(Context context, String title, String content,
            String cancel, String confirm,
            OnButtonClickListener onButtonClickListener)
    {
        super(context, R.layout.apply_cancel_dialog);
        this.title = title;
        this.content = content;
        this.cancel = cancel;
        this.confirm = confirm;
        mContext = context;
        this.onButtonClickListener = onButtonClickListener;
    }
    
    public void setTextGravity(int titleGravity, int contentGravity)
    {
        this.titleGravity = titleGravity;
        this.contentGravity = contentGravity;
    }
    
    public void setTextGone(boolean titleGone, boolean contentGone)
    {
        this.titleGone = titleGone;
        this.contentGone = contentGone;
    }
    
    @Override
    protected void initViews()
    {
        mCancelTextView = (TextView) findViewById(R.id.apply_cancel_dialog_cancel);
        mConfirmTextView = (TextView) findViewById(R.id.apply_cancel_dialog_confirm);
        mTitleView = (TextView) findViewById(R.id.apply_cancel_dialog_title);
        mContentView = (TextView) findViewById(R.id.apply_cancel_dialog_content);
        if (!TextUtils.isEmpty(title))
        {
            mTitleView.setText(title);
        }
        if (!TextUtils.isEmpty(content))
        {
            mContentView.setText(content);
        }
        if (!TextUtils.isEmpty(cancel))
        {
            mCancelTextView.setText(cancel);
        }
        if (!TextUtils.isEmpty(confirm))
        {
            mConfirmTextView.setText(confirm);
        }
        if (titleGone || contentGone)
        {
            findViewById(R.id.apply_cancel_dialog_divider_line).setVisibility(View.GONE);
        }
        if (titleGone)
        {
            mTitleView.setVisibility(View.GONE);
        }
        if (contentGone)
        {
            mContentView.setVisibility(View.GONE);
        }
        
        if (titleGravity != -1)
        {
            mTitleView.setGravity(titleGravity);
        }
        if (contentGravity != -1)
        {
            mContentView.setGravity(contentGravity);
        }
    }
    
    @Override
    protected void bindLinsenter()
    {
        mCancelTextView.setOnClickListener(this);
        mConfirmTextView.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v)
    {
        dismiss();
        switch (v.getId())
        {
            case R.id.apply_cancel_dialog_cancel:
                if (null != onButtonClickListener)
                {
                    onButtonClickListener.onCancelClick();
                }
                break;
            case R.id.apply_cancel_dialog_confirm:
                if (null != onButtonClickListener)
                {
                    onButtonClickListener.onConfirmClick();
                }
                break;
            default:
                break;
        }
    }
    
    /**
     * 
     * 弹出对话框后单击确定后要做的事情
     * 
     * @author  李晨光
     * @version  [版本号, 2014年4月3日]
     * @see  [相关类/方法]
     * @since  [产品/模块版本]
     */
    public interface OnButtonClickListener
    {
        public void onCancelClick();
        
        public void onConfirmClick();
    }
    
    /**
     * 取消back键
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void cancelBackKey()
    {
        setOnKeyListener(new OnKeyListener()
        {
            
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                    KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_SEARCH
                        || keyCode == KeyEvent.KEYCODE_BACK)
                {
                    return true;
                }
                else
                {
                    return false; //默认返回 false
                }
            }
        });
    }
    
}
