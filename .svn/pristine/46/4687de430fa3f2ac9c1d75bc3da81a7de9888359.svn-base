package com.xweisoft.wx.family.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class InsideViewPager extends ViewPager
{
    
    /** the last x position */
    private float lastX;
    
    /** if the first swipe was from left to right (->), dont listen to swipes from the right */
    private boolean slidingLeft;
    
    /** if the first swipe was from right to left (<-), dont listen to swipes from the left */
    private boolean slidingRight;
    
    private Context context;
    
    private GestureDetector gestureScanner;
    
    /**
     * 上一次y坐标的位置
     */
    private float mLastMotionY;
    
    public InsideViewPager(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);
        this.context = context;
    }
    
    public InsideViewPager(final Context context)
    {
        super(context);
        this.context = context;
    }
    
    public void setDetector(GestureDetector dectector)
    {
        gestureScanner = dectector;
    }
    
    @Override
    public boolean onTouchEvent(final MotionEvent ev)
    {
        if (gestureScanner != null)
        {
            gestureScanner.onTouchEvent(ev);
        }
        final int action = ev.getAction();
        final float x = ev.getX();
        final float y = ev.getY();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                
                // Disallow parent ViewPager to intercept touch events.
                this.getParent().requestDisallowInterceptTouchEvent(true);
                
                // save the current x position
                this.lastX = ev.getX();
                mLastMotionY = y;
                break;
            
            case MotionEvent.ACTION_UP:
                // Allow parent ViewPager to intercept touch events.
                this.getParent().requestDisallowInterceptTouchEvent(false);
                
                // save the current x position
                this.lastX = ev.getX();
                
                // reset swipe actions
                this.slidingLeft = false;
                this.slidingRight = false;
                
                break;
            
            case MotionEvent.ACTION_MOVE:
                final float dx = x - lastX;
                final float xDiff = Math.abs(dx);
                final float yDiff = Math.abs(y - mLastMotionY);
                if (yDiff > xDiff)
                {
                    this.getParent().requestDisallowInterceptTouchEvent(false);
                }
                else
                {
                    this.getParent().requestDisallowInterceptTouchEvent(true);
                }
                /*
                 * if this is the first item, scrolling from left to
                 * right should navigate in the surrounding ViewPager
                 */
                if (this.getCurrentItem() == 0)
                {
                    // swiping from left to right (->)?
                    if (this.lastX <= ev.getX() && !this.slidingRight)
                    {
                        // make the parent touch interception active -> parent pager can swipe
                        //                        this.getParent()
                        //                                .requestDisallowInterceptTouchEvent(false);
                    }
                    else
                    {
                        /*
                         * if the first swipe was from right to left, dont listen to swipes
                         * from left to right. this fixes glitches where the user first swipes
                         * right, then left and the scrolling state gets reset
                         */
                        this.slidingRight = true;
                        
                        // save the current x position
                        this.lastX = ev.getX();
                        this.getParent()
                                .requestDisallowInterceptTouchEvent(true);
                    }
                }
                else
                /*
                 * if this is the last item, scrolling from right to
                 * left should navigate in the surrounding ViewPager
                 */
                if (this.getCurrentItem() == this.getAdapter().getCount() - 1)
                {
                    // swiping from right to left (<-)?
                    if (this.lastX >= ev.getX() && !this.slidingLeft)
                    {
                        // make the parent touch interception active -> parent pager can swipe
                        this.getParent()
                                .requestDisallowInterceptTouchEvent(false);
                    }
                    else
                    {
                        /*
                         * if the first swipe was from left to right, dont listen to swipes
                         * from right to left. this fixes glitches where the user first swipes
                         * left, then right and the scrolling state gets reset
                         */
                        this.slidingLeft = true;
                        
                        // save the current x position
                        this.lastX = ev.getX();
                        this.getParent()
                                .requestDisallowInterceptTouchEvent(true);
                    }
                }
                
                break;
        }
        super.onTouchEvent(ev);
        return true;
    }
}