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
public class HorizontalScrollerView extends ViewGroup {
    /**
     * 平滑滑动用
     */
    private Scroller scroller;
    /**
     * 计算滑动速度用
     */
    private VelocityTracker velocityTracker;
    /**
     * 记录上一次触摸时间的位置
     */
    private float lastX, lastY, lastInterceptX, lastInterceptY;
    /**
     * 子控件的宽高和个数
     */
    private int mChildWidth, mChildHeight, mChildSize;

    /**
     * 当前的子View索引变化
     */
    private OnChangeListener onChangeListener;

    /**
     * 当前的子View索引
     */
    private int childIndex = 0;

    public HorizontalScrollerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalScrollerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        scroller = new Scroller(context);
        velocityTracker = VelocityTracker.obtain();
    }

    /**
     * 设置子View索引变化回调
     */
    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.onChangeListener = onChangeListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //默认都一样大 占满父布局
        mChildSize = getChildCount();
        for (int i = 0; i < mChildSize; i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
        mChildWidth = widthSize;
        mChildHeight = heightSize;

        //设置测量宽高
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            //从左到右依次布局
            view.layout(left, 0, left + mChildWidth, mChildHeight);
            left += mChildWidth;
        }
    }

    //外部拦截法
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
                //水平滑动距离大于竖直滑动
                intercept = Math.abs(dx) > Math.abs(dy) + 50;
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


//    // 内部拦截法
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                if (!scroller.isFinished()) {
//                    scroller.abortAnimation();
//                    return true;
//                }
//                return false;
//            case MotionEvent.ACTION_MOVE:
//            case MotionEvent.ACTION_UP:
//            default:
//                return true;
//        }
//    }

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
                //跟随手指滑动
                scrollBy(-dx, 0);
                break;
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                //计算1s内的速度
                velocityTracker.computeCurrentVelocity(1000);
                //获取水平的滑动速度
                float xVelocity = velocityTracker.getXVelocity();

                if (Math.abs(xVelocity) > 50) {
                    childIndex = xVelocity > 0 ? childIndex - 1 : childIndex + 1;
                } else {
                    childIndex = (scrollX + mChildWidth / 2) / mChildWidth;
                }
                childIndex = Math.max(0, Math.min(childIndex, mChildSize - 1));

                if (onChangeListener != null) {
                    onChangeListener.indexChange(childIndex);
                }
                //计算还需滑动到整个child的偏移
                int sx = childIndex * mChildWidth - scrollX;
                //通过Scroller来平滑滑动
                smoothScrollBy(sx);

                //清除
                velocityTracker.clear();
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
     * @param dx
     */
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

    /**
     * 更新当前可见子View的索引
     */
    public void updateChildIndex(int pos) {
        if (!scroller.isFinished()) {
            scroller.abortAnimation();
        }
        smoothScrollBy(pos * mChildWidth - getScrollX());
        if (onChangeListener != null) {
            onChangeListener.indexChange(pos);
        }
    }

    public interface OnChangeListener {
        /**
         * 索引变化
         *
         * @param index 索引
         */
        void indexChange(int index);
    }

}
