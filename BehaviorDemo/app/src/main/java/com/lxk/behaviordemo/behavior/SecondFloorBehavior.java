package com.lxk.behaviordemo.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.lxk.behaviordemo.BuildConfig;

/**
 * @author https://github.com/103style
 * @date 2020/11/20 13:37
 */
public class SecondFloorBehavior extends CoordinatorLayout.Behavior<View> {

    private static final String TAG = "SecondFloorBehavior";

    View.OnLayoutChangeListener layoutChangeListener;

    boolean showSecondFloor;

    public SecondFloorBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull MotionEvent ev) {
        return !showSecondFloor;
    }

    @Override
    public boolean onTouchEvent(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull MotionEvent ev) {

        //还没有开始拖动就收到了DOWN之外的事件，直接忽略
        if (!dragging && ev.getAction() != MotionEvent.ACTION_DOWN) {
            return true;
        }
        //是否已消费该事件
        boolean handled = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_CANCEL:
                if (!dragging) {
                    handled = true;
                }
                break;
            case MotionEvent.ACTION_DOWN:
                handleEventDown();
                break;
            case MotionEvent.ACTION_MOVE:
                handleEventMove();
                break;
            case MotionEvent.ACTION_UP:
                handleEventUp();
                break;
        }

        if (ev.getAction() == MotionEvent.ACTION_CANCEL
                && "resetTouchBehaviors".equals(Thread.currentThread().getStackTrace()[3].getMethodName()) || handled) {
            return true;
        }
        return parent.getChildAt(2).dispatchTouchEvent(ev);
    }

    private void handleEventUp() {
    }

    private void handleEventMove() {

    }

    private void handleEventDown() {

    }

    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent, @NonNull View child, int layoutDirection) {
        if (layoutChangeListener == null) {
            layoutChangeListener = new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    layoutChange((ViewGroup) v);
                }
            };
            parent.addOnLayoutChangeListener(layoutChangeListener);
        }
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    private void layoutChange(ViewGroup v) {
        View head = v.getChildAt(0);
        View secondFloor = v.getChildAt(1);
        View firstFloor = v.getChildAt(2);

        head.layout(head.getLeft(), firstFloor.getTop() - head.getHeight(), head.getRight(), firstFloor.getTop());
        secondFloor.layout(secondFloor.getLeft(), head.getTop() - secondFloor.getHeight(), secondFloor.getRight(), head.getTop());
    }


    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return directTargetChild == coordinatorLayout.getChildAt(2);
    }


    //手指是否在拖动中
    private boolean dragging = false;
    //已下拉的距离
    private float pullDownOffset = 0F;
    //是否已经开始下拉
    private boolean pullDownStarted = false;

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                               @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed,
                               int dyUnconsumed, int type, @NonNull int[] consumed) {
        if (dyUnconsumed < 0 && dragging && !pullDownStarted) {
            pullDownStarted = true;
            pullDownOffset = dyUnconsumed;
        }
    }
}
