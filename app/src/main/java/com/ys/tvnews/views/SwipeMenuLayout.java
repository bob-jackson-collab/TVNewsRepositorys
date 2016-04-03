package com.ys.tvnews.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.ys.tvnews.utils.ConvertUtils;
import com.ys.tvnews.utils.ScreenUtil;

/**
 * Created by admin on 2016/1/20.
 */
public class SwipeMenuLayout extends LinearLayout
{
    /**
     * 关闭
     */
    public static final int SLIDE_STATUS_OFF = 0;
    /**
     * 开始滚动
     */
    public static final int SLIDE_STATUS_START_SCROLL = 1;
    /**
     * 已经打开
     */
    public static final int SLIDE_STATUS_ON = 2;
    private static final int TAN = 2;
    /**
     * 滚动类
     */
    private Scroller mScroller;
    /**
     * 屏幕尺寸类
     */
    private int[] screenSize;
    /**
     * 主内容区域
     */
    private ViewGroup mContent;
    /**
     * 菜单区域
     */
    private ViewGroup mMenu;
    /**
     * 默认的宽度
     */
    private int mMenuWidth = 120;

    private int lastX,lastY;

    public SwipeMenuLayout(Context context)
    {
        this(context,null);
    }

    public SwipeMenuLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs,0);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SwipeMenuLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context)
    {
        screenSize = ScreenUtil.getScreenSize(context);
        mMenuWidth = ConvertUtils.dp2px(context, mMenuWidth);
        this.setOrientation(LinearLayout.HORIZONTAL);
        mScroller = new Scroller(context);
    }

    boolean isOnce = false;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        if (!isOnce)
        {
            mContent = (ViewGroup) getChildAt(0);
            mContent.getLayoutParams().width = screenSize[0];
            mMenu = (ViewGroup) getChildAt(1);
            mMenu.getLayoutParams().width = mMenuWidth;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 设置默认的显示
     */
    public void shrink()
    {
        Log.e("----------",getScrollX() + "");
        if (getScrollX() != 0)
        {
            smoothScrollTo(0, 0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        /**
         * 重置上次滑动的View
         */
        if (onSlidingListener != null) {
            onSlidingListener.onSliding(this, SLIDE_STATUS_START_SCROLL);
        }
        int x = (int) event.getX();
        int y = (int) event.getY();
        int scrollX = getScrollX();
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
                /**
                 * 如果没有结束则停止动画
                 */
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            }
            case MotionEvent.ACTION_MOVE:
            {
                int deltaX = x - lastX;
                int deltaY = y - lastY;
                if (Math.abs(deltaX) < Math.abs(deltaY) * TAN) {
                    break;
                }
                int newScrollX = scrollX - deltaX;
                if (deltaX != 0) {
                    if (newScrollX < 0) {
                        newScrollX = 0;
                    } else if (newScrollX > mMenuWidth) {
                        newScrollX = mMenuWidth;
                    }
                    this.scrollTo(newScrollX, 0);
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                int mX = 0;
                if (scrollX - mMenuWidth * 0.75f > 0) {
                    mX = mMenuWidth;
                }
                this.smoothScrollTo(mX, 0);
                if (onSlidingListener != null) {
                    onSlidingListener.onSliding(this, mX == 0 ? SLIDE_STATUS_OFF : SLIDE_STATUS_ON);
                }
                break;
            }
        }
        lastX = x;
        lastY = y;
        return super.onTouchEvent(event);
    }

    /**
     * 换换滚动到的位置
     * @param x
     * @param y
     */
    private void smoothScrollTo(int x, int y)
    {
        int scrollX = getScrollX();
        int dealtX = x - scrollX;
        /**
         * 1.startX
         * 2.startY
         * 3.endX
         * 4.endY
         * 5.duration
         */
        mScroller.startScroll(scrollX,0,dealtX,0,Math.abs(dealtX) * 3);
        invalidate();
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

    public void setOnSlidingListener(OnSlidingListener onSlidingListener)
    {
        this.onSlidingListener = onSlidingListener;
    }

    private OnSlidingListener onSlidingListener;

    public interface OnSlidingListener
    {
        void onSliding(View view, int state);
    }


}
