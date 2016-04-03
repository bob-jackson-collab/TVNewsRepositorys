package com.ys.tvnews.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by admin on 2016/1/20.
 */
public class SwipeLayoutList extends ListView {

    private SwipeMenuLayout mSwipeMenuLayout;

    public SwipeLayoutList(Context context)
    {
        super(context);
    }

    public SwipeLayoutList(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public SwipeLayoutList(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                int x = (int) ev.getX();
                int y = (int) ev.getY();
                int position = pointToPosition(x,y);
                if (position != INVALID_POSITION)
                {
                    mSwipeMenuLayout = (SwipeMenuLayout) getChildAt(position - getFirstVisiblePosition());
                }
                break;
        }
        if (mSwipeMenuLayout != null)
        {
            mSwipeMenuLayout.onTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }
}
