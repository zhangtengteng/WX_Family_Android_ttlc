package com.xweisoft.wx.family.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  administrator
 * @version  [版本号, 2013-10-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MarqueeTextView extends TextView
{
	private static final int DEFAULT_SPAN = 2;

    private static final byte MARQUEE_STOPPED = 0x0;
    private static final byte MARQUEE_STARTING = 0x1;
    private static final byte MARQUEE_RUNNING = 0x2;

    private static final int MESSAGE_START = 0x1;
    private static final int MESSAGE_TICK = 0x2;
    private static final int MESSAGE_RESTART = 0x3;
	
    /**
     * true 是从右至左，  false 从左至右
     */
    public boolean isFromRight2Left = true;
    
    private byte mStatus = MARQUEE_STOPPED;

    protected int span = DEFAULT_SPAN;

	/**
	 * 文字的横坐标
	 */
	protected float x = 0f;

	/**
	 * 文字的纵坐标
	 */
	private float y = 0f;

	/**
	 * 文本长度
	 */
	protected float textWidth = 0;

	/**
	 * 刷新频率 默认200
	 */
	private int frequency = 400;

	/**
	 * 滚动延时
	 */
	private int delay = 100;
	
	/**
	 * 绘图样式
	 */
	protected Paint paint = null;

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case MESSAGE_START:
				mStatus = MARQUEE_RUNNING;
				tick();
				break;
			case MESSAGE_TICK:
				tick();
				break;
			case MESSAGE_RESTART:
				if (mStatus == MARQUEE_RUNNING)
				{
					start();
				}
				break;
			}
		}
	};

    /**
     * 构造函数
     * @param context    context对象
     */
    public MarqueeTextView(Context context)
    {
        super(context);
    }

    /**
     * 构造函数
     * @param context   context对象
     * @param attrs     AttributeSet对象
     */
    public MarqueeTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    /**
     * 构造函数
     * @param context       context对象
     * @param attrs         AttributeSet对象
     * @param defStyle      风格定义
     */
    public MarqueeTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }
	
	/**
	 * [开始滚动]<BR>
	 */
	public void start()
	{
		if (!isFromRight2Left && MARQUEE_STOPPED != mStatus)
		{
			this.x = getWidth() - textWidth;
		}
		
		mStatus = MARQUEE_STARTING;
		invalidate();
		handler.sendEmptyMessageDelayed(MESSAGE_START, delay);
	}

	/**
	 * [停止滚动]<BR>
	 */
	private void stop()
	{
		mStatus = MARQUEE_STOPPED;
		handler.removeMessages(MESSAGE_START);
		handler.removeMessages(MESSAGE_RESTART);
		handler.removeMessages(MESSAGE_TICK);
	}

	/**
	 * [刷新频率]<BR>
	 */
	void tick()
	{
		if (mStatus != MARQUEE_RUNNING)
		{
			return;
		}

		handler.removeMessages(MESSAGE_TICK);
		handler.sendEmptyMessageDelayed(MESSAGE_TICK, frequency);

		invalidate();
	}

	/**
	 * 文本初始化，每次更改文本内容或者文本效果等之后都需要重新初始化一下<BR>
	 * 
	 * @param text 显示文本
	 * @param marqueeMethod 滚动方式 true 是从右至左，  false 从左至右
	 */
	public void init(String text, boolean marqueeMethod)
	{
		// 判空操作
		if (TextUtils.isEmpty(text))
		{
			return;
		}

		this.setText(text);
		//        measure(MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),     
		//				 MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY)); 

		// 获得paint对象
		paint = getPaint();

		// 变量初始化
		this.isFromRight2Left = marqueeMethod;
		this.textWidth = paint.measureText(text);

		if (isFromRight2Left)
		{
			this.x = 0;
		}
		this.y = getTextSize() + getPaddingTop();
		
		// 参数设置
		paint.setColor(getTextColors().getDefaultColor());
	}

	/**
	 * 初始化滚动<BR>
	 * @param text 显示字符串
	 * @param marqueeMethod 滚动方式
	 * @param span 滚动进度
	 */
	public void init(String text, boolean marqueeMethod, int span)
	{
		this.span = span;
		init(text, marqueeMethod);
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus)
	{
		super.onWindowFocusChanged(hasWindowFocus);

		if (hasWindowFocus)
		{
			start();
		}
		else
		{
			stop();
		}
	}

	/**
	 * 重写函数，文本框的绘制
	 * @param canvas   画布对象
	 */
	@Override
	public void onDraw(Canvas canvas)
	{
		// 判空操作
		if (TextUtils.isEmpty(getText()))
		{
			return;
		}

		// 如果开始滚动，并且文本宽度大于可视宽度
		if (MARQUEE_RUNNING == mStatus && paint != null)
		{
			if (this.isFromRight2Left)
			{
				canvas.drawText(getText().toString(), x, y, paint);

				x -= span;

				// 如果超出可视区域则重置位置
				if (x + textWidth <= 0)
				{
					setNextText();
					x = getWidth();
				}
			}
			else
			{
				canvas.drawText(getText().toString(), x, y, paint);

				x += span;

				// 如果超出可视区域则重置位置
				if (x >= getWidth())
				{
					setNextText();
					x = -textWidth;
				}
			}
		}
		else
		{
			canvas.drawText(getText().toString(), x, y, paint);
		}
	}
	
	/**
	 * [显示下一条文本，如果只有一条文本，则显示当前文本]<BR>
	 */
	private void setNextText()
	{
		String text = getNextText();
		// 判空操作
		if (TextUtils.isEmpty(text))
		{
			return;
		}

		this.setText(text);
		this.textWidth = paint.measureText(text);
	}
	
	/**
	 * [获取下一条文本]<BR>
	 * @return string
	 */
	protected String getNextText()
	{
		return "";
	}
}
