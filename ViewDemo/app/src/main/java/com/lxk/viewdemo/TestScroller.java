package com.lxk.viewdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Scroller;

import androidx.annotation.Nullable;

/**
 * created by 103style  2019/11/20 20:51
 */
public class TestScroller extends View {
    private static final String TAG = "TestScroller";

    Scroller mScroller;

    public TestScroller(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    public void smoothScrollTo(int destX, int destY) {
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        int deltaX = destX - scrollX;
        int deltaY = destY - scrollY;
        mScroller.startScroll(scrollX, scrollY, deltaX, deltaY, 1000);
        invalidate();
    }
//    int direction = -1;
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mScroller.startScroll(((int) getX()), ((int) getY()), ((int) getX())*direction,
//                        ((int) getY())*direction);//开始位移，真正开始是在下面的invalidate
//                direction*=-1;//改变方向
//                invalidate();//开始执行位移
//                break;
//        }
//        return super.onTouchEvent(event);
//    }
}