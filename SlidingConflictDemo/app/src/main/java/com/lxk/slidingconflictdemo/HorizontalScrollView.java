package com.lxk.slidingconflictdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * @author https://github.com/103style
 * @date 2019/12/23 22:20
 */
public class HorizontalScrollView extends ViewGroup {

    private Scroller scroller;
    private VelocityTracker velocityTracker;
    private float lastX, lastY, lastInterceptX, lastInterceptY;
    private int mChildWidth, mChildHeight;
    private int mChildSize;
    private onIndexChangeListener onIndexChangeListener;
    private int childIndex = 0;

    public HorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setOnIndexChangeListener(HorizontalScrollView.onIndexChangeListener onIndexChangeListener) {
        this.onIndexChangeListener = onIndexChangeListener;
    }

    private void init(Context context) {
        scroller = new Scroller(context);
        velocityTracker = VelocityTracker.obtain();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //默认都一样大 占满父布局
        mChildSize = getChildCount();
        for (int i = 0; i < mChildSize; i++) {
            View view = getChildAt(i);
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
            mChildWidth = Math.max(view.getMeasuredWidth(), mChildWidth);
            mChildHeight = Math.max(view.getMeasuredHeight(), mChildHeight);
        }
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : mChildWidth,
                heightMode == MeasureSpec.EXACTLY ? heightSize : mChildHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            view.layout(left, 0, left + mChildWidth, mChildHeight);
            left += mChildWidth;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept;
        float x = ev.getX();
        float y = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercept = false;
                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                    intercept = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = x - lastInterceptX;
                float dy = y - lastInterceptY;
                intercept = Math.abs(dx) > Math.abs(dy);
                break;
            case MotionEvent.ACTION_UP:
            default:
                intercept = false;
                break;
        }
        lastX = x;
        lastY = y;

        lastInterceptX = x;
        lastInterceptY = y;

//        return false;
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        velocityTracker.addMovement(event);
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) (x - lastX);
                scrollBy(-dx, 0);
                break;
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                velocityTracker.computeCurrentVelocity(1000);
                float xVelocity = velocityTracker.getXVelocity();
                if (Math.abs(xVelocity) > 50) {
                    childIndex = xVelocity > 0 ? childIndex - 1 : childIndex + 1;
                } else {
                    childIndex = (scrollX + mChildWidth / 2) / mChildWidth;
                }
                childIndex = Math.max(0, Math.min(childIndex, mChildSize - 1));
                if (onIndexChangeListener != null) {
                    onIndexChangeListener.indexChange(childIndex);
                }
                int sx = childIndex * mChildWidth - scrollX;
                smoothScrollBy(sx);
                velocityTracker.clear();
                break;
            default:
                break;
        }
        lastX = x;
        lastY = y;
        return true;
    }

    private void smoothScrollBy(int dx) {
        scroller.startScroll(getScrollX(), 0, dx, 0, 500);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }

    public void updateChildIndex(int pos) {
        if (!scroller.isFinished()) {
            scroller.abortAnimation();
        }
        smoothScrollBy(pos * mChildWidth - getScrollX());
        if (onIndexChangeListener != null) {
            onIndexChangeListener.indexChange(pos);
        }
    }

    public interface onIndexChangeListener {
        void indexChange(int index);
    }

}
