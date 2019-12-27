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
 * @date 2019/12/27 15:49
 * <p>
 * 可以竖直滑动的view
 */
public class VerticalScrollerView extends ViewGroup {
    /**
     * 记录上一次触摸时间的位置
     */
    private float lastX, lastY;
    private Scroller scroller;
    private VelocityTracker velocityTracker;

    private int mHeight, mContentHeight;

    public VerticalScrollerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalScrollerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        scroller = new Scroller(context);
        velocityTracker = VelocityTracker.obtain();
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int contentHeight = 0;
        int contentMaxWidth = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            int cWidth = layoutParams.getMarginEnd() + layoutParams.getMarginStart() + child.getMeasuredWidth();
            int cHeight = layoutParams.topMargin + layoutParams.bottomMargin + child.getMeasuredHeight();
            contentMaxWidth = Math.max(cWidth, contentMaxWidth);
            contentHeight += cHeight;
        }
        //设置测量宽高
        mHeight = heightSize;
        mContentHeight = contentHeight;
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : contentMaxWidth,
                heightMode == MeasureSpec.EXACTLY ? heightSize : contentHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int top = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            MarginLayoutParams layoutParams = (MarginLayoutParams) view.getLayoutParams();
            //从上到下依次布局
            view.layout(layoutParams.leftMargin, layoutParams.topMargin + top,
                    layoutParams.leftMargin + view.getMeasuredWidth(),
                    layoutParams.topMargin + top + view.getMeasuredHeight());
            top += view.getMeasuredHeight() + layoutParams.bottomMargin + layoutParams.topMargin;
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (Math.abs(y - lastY) > Math.abs(x - lastY)) {
                return true;
            }
        }
        lastX = x;
        lastY = y;
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = (int) (y - lastY);
                //跟随手指滑动
                scrollBy(0, -dy);
                break;
            case MotionEvent.ACTION_UP:
                int scrollY = getScrollY();
                if (scrollY < 0) {
                    smoothScrollBy(-scrollY);
                } else if (mContentHeight <= mHeight) {
                    smoothScrollBy(-scrollY);
                } else if (mContentHeight - scrollY < mHeight) {
                    smoothScrollBy(mContentHeight - scrollY - mHeight);
                } else {
                    //惯性滑动效果
                }
                break;
            default:
                break;
        }
        lastX = x;
        lastY = y;
        return true;
    }

    /**
     * 平滑滑动
     *
     * @param dy
     */
    private void smoothScrollBy(int dy) {
        scroller.startScroll(0, getScrollY(), 0, dy, 500);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }


}
