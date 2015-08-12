package com.xweisoft.wx.family.util;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.xweisoft.wx.family.logic.global.GlobalVariable;

/**
 * <一句话功能简述>
 * 自定义滑动效果
 * 
 * @author  administrator
 * @version  [版本号, 2013-10-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class Slider extends ViewGroup
{
    /**
     * 滑动状态：停止
     */
    public static final int TOUCH_STATE_REST = 0;
    
    /**
     * 日志TAG
     */
    private static final String TAG = "===Slider===";

    /**
     * 临界滑动速度，超过此值时滑动视图
     */
    private static final int SNAP_VELOCITY = 300;

    /**
     * 临界滑动距离，超过此值时滑动视图
     */
    private static final int SNAP_OFFSET = GlobalVariable.screenWidth / 3;

    /**
     * 滑动状态：正在滑动
     */
    private static final int TOUCH_STATE_SCROLLING = 1;

    /**
     * 子窗口的横向显示比例
     */
    private int scaleX = 1;

    /**
     * 滑动状态
     */
    private int mTouchState;

    /**
     * 当前显示的视图编号
     */
    private int mCurScreen;

    /**
     * 最小滑动距离
     */
    private int mTouchSlop;

    /**
     * 实际滚动宽度
     */
    private int mActualWidth;

    /**
     * 记录上次滑动结束的横坐标位置
     */
    private float mLastMotionX;

    /**
     * 记录上次滑动结束的纵坐标位置
     */
    private float mLastMotionY;

    /**
     * 记录上次ACTION_DOWN事件触发时的横坐标位置
     */
    private float mLastDownX;

    /**
     * 是否边缘检测
     */
    private boolean isCheckFringed = false;

    /**
     * 是否可以滑动
     */
    private boolean isTouchabled = true;

    /**
     * 滚动视图
     */
    private Scroller mScroller;

    /**
     * 滑动速度追踪器
     */
    private VelocityTracker mVelocityTracker;

    /**
     * 滑动监听
     */
    private Handler mHandler = null;
    
    /**
     * 构造函数
     * @param context  上下文
     * @param attrs  属性
     */
    public Slider(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }
    
    /**
     * 构造函数
     * @param context  上下文
     * @param attrs  属性
     * @param defStyle  类型
     */
    public Slider(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        mScroller = new Scroller(context);
        mCurScreen = 0;
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop() * 3;
        mTouchState = TOUCH_STATE_REST;
    }


    /**
     * 设置是否可以滑动
     * 
     * @param isTouchable  是否滑动
     */
    public void setTouchable(boolean isTouchable)
    {
        this.isTouchabled = isTouchable;
    }

    /**
     * 设置到达边缘时是否有弹性
     * 
     * @param isCheckFringe  是否到达边缘
     */
    public void setCheckFringe(boolean isCheckFringe)
    {
        this.isCheckFringed = isCheckFringe;
    }

    /**
     * 设置子窗口横向显示比例
     * @param scaleX  比列
     */
    public void setScaleX(int scaleX)
    {
        this.scaleX = scaleX;
    }

    /**
     * 设置mHandler
     * 
     * @param handler  处理
     */
    public void setHandler(Handler handler)
    {
        this.mHandler = handler;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        // 解决Slider动态添加视图不刷新的问题：暂时不对changed做判断
        // 左边离第一个子视图开始的距离
        int childLeft = 0;

        for (int i = 0; i < getChildCount(); i++)
        {
            final View childView = getChildAt(i);

            if (childView.getVisibility() != View.GONE)
            {
                final int childWidth = childView.getMeasuredWidth()
                        / scaleX;
                if (scaleX == 1)
                {
                    childView.layout(childLeft, 0, childLeft + childWidth,
                            childView.getMeasuredHeight());
                }
                else if (mCurScreen == i - 1)
                {
                    childView.layout(childLeft - 30, 0, childLeft
                            + childWidth, childView.getMeasuredHeight());
                }
                else if (mCurScreen == i + 1)
                {
                    childView.layout(childLeft + 30, 0, childLeft
                            + childWidth + 100,
                            childView.getMeasuredHeight());
                }
                else
                {
                    childView.layout(childLeft, 0, childLeft + childWidth
                            + 100, childView.getMeasuredHeight());
                }

                childLeft += childWidth;
            }
        }

        mActualWidth = childLeft;
        
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int width = MeasureSpec.getSize(widthMeasureSpec);
        // final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        // if (widthMode != MeasureSpec.EXACTLY)
        // {
        // throw new IllegalStateException(
        // "Slider only can run at EXACTLY mode!");
        // }
        // final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        // if (heightMode != MeasureSpec.EXACTLY)
        // {
        // throw new IllegalStateException(
        // "Slider only can run at EXACTLY mode!");
        // }
        // 子窗口赋予与Slider同样的宽和高
        for (int i = 0; i < getChildCount(); i++)
        {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        }

        scrollTo(mCurScreen * width / scaleX, 0);
    }

    @Override
    public void computeScroll()
    {
        if (mScroller.computeScrollOffset())
        {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (!isTouchabled)
        {
            return false;
        }

        if (mVelocityTracker == null)
        {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        final float x = event.getX();
        final float y = event.getY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished())
                {
                    mScroller.abortAnimation();
                }
                mLastMotionX = x;
                mLastMotionY = y;
                mLastDownX = x;

                // 将ACTION_DOWN事件传出
                if (mHandler != null)
                {
                    mHandler.sendEmptyMessage(10000);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                int deltaX = (int) (mLastMotionX - x) / scaleX;
                int deltaY = (int) (mLastMotionY - y) / scaleX;
                int checkX = getScrollX() + deltaX;
                mLastMotionX = x;
                mLastMotionY = y;

                // 判断是否滑到头
                if (isCheckFringed
                        && (checkX <= 0 || checkX >= mActualWidth - getWidth()))
                {
                    return true;
                }

                scrollBy(deltaX, 0);

//                // 将ACTION_MOVE事件传出
//                Message msg = mHandler.obtainMessage(10002);
//                msg.arg1 = deltaX;
//                if(mHandler != null)
//                {
//                    mHandler.sendMessage(msg);
//                }
                LogX.getInstance().i(TAG, deltaX + "");
                LogX.getInstance().i(TAG, deltaY + "");
                break;

            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000);
                int velocityX = (int) mVelocityTracker.getXVelocity();
                int mLastChild = getChildCount() - 1;
                int offsetX = (int) (x - mLastDownX) / scaleX;

                if ((velocityX > SNAP_VELOCITY || offsetX > SNAP_OFFSET)
                        && mCurScreen > 0)
                {
                    // 滑动速度足够向左滚动视图
                    snapToScreen(mCurScreen - 1);
                }
                else if ((velocityX < -SNAP_VELOCITY || offsetX < -SNAP_OFFSET)
                        && mCurScreen < mLastChild)
                {
                    // 滑动速度足够向右滚动视图
                    snapToScreen(mCurScreen + 1);
                }
                else
                {
                    // snapToDestination();
                    snapToScreen(mCurScreen);
                }

                if (mVelocityTracker != null)
                {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                mTouchState = TOUCH_STATE_REST;
                break;

            case MotionEvent.ACTION_CANCEL:
                mTouchState = TOUCH_STATE_REST;
                break;
            default:
                    break;
        }

        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE)
                && (mTouchState != TOUCH_STATE_REST))
        {
            return true;
        }

        final float x = ev.getX();
        final float y = ev.getY();

        switch (action)
        {
            case MotionEvent.ACTION_MOVE:
                final int xDiff = (int) Math.abs(mLastMotionX - x);
                if (xDiff > mTouchSlop)
                {
                    mTouchState = TOUCH_STATE_SCROLLING;
                }
                break;

            case MotionEvent.ACTION_DOWN:
                mLastMotionX = x;
                mLastMotionY = y;
                mLastDownX = x;

                if (mScroller.isFinished())
                {
                    mTouchState = TOUCH_STATE_REST;
                    // 将ACTION_DOWN事件传出
                    if (mHandler != null)
                    {
                        mHandler.sendEmptyMessage(10000);
                    }
                }
                else
                {
                    mTouchState = TOUCH_STATE_SCROLLING;
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_UP:
                mTouchState = TOUCH_STATE_REST;
                break;
            default:
                break;
        }

        return mTouchState != TOUCH_STATE_REST;
    }

    /**
     * 滑动速度不够，滚回当前视图
     */
    public void snapToDestination()
    {
        final int screenWidth = getWidth();
        if (screenWidth != 0)
        {
        	 final int destScreen = (getScrollX() + screenWidth / 2) / screenWidth;
             snapToScreen(destScreen);
        }
     
    }

    /**
     * 滑动到指定视图
     * 
     * @param whichScreen
     *            视图编号
     */
    public void snapToScreen(int whichScreen)
    {
        // 获得正确的视图编号
        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
        if (getScrollX() != (whichScreen * getWidth() / scaleX))
        {
            final int delta = whichScreen * getWidth() / scaleX - getScrollX();
            mScroller.startScroll(getScrollX(), 0, delta, 0,
                    Math.abs(delta) * 2);

            // 将ACTION_UP事件传出
            if (mHandler != null && mCurScreen != whichScreen)
            {
                mHandler.sendEmptyMessage(10001);
            }
            mCurScreen = whichScreen;
            invalidate();
        }
    }

    /**
     * 设置当期视图到指定视图
     * 
     * @param whichScreen
     *            视图编号
     */
    public void setToScreen(int whichScreen)
    {
        whichScreen = Math.max(0, Math.max(whichScreen, getChildCount() - 1));
        mCurScreen = whichScreen;
        scrollTo(whichScreen * getWidth() / scaleX, 0);
    }

    /**
     * 返回当前视图编号
     * 
     * @return mCurScreen 当前视图编号
     */
    public int getCurScreen()
    {
        return mCurScreen;
    }
}
