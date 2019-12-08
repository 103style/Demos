package com.lxk.motioneventdemo.test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author https://github.com/103style
 * @date 2019/12/8 20:10
 */
public class TestLayout extends FrameLayout {
    public TestLayout(@NonNull Context context) {
        super(context);
    }

    public TestLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TestLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (
                ev.getAction() == MotionEvent.ACTION_MOVE
//                        ||
//                ev.getAction() == MotionEvent.ACTION_UP
        ) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        if (
//                ev.getAction() == MotionEvent.ACTION_MOVE
//                        ||
//                ev.getAction() == MotionEvent.ACTION_UP
//        ) {
//            return true;
//        }
        return super.onInterceptTouchEvent(ev);
    }
}