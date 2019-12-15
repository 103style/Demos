package com.lxk.motioneventdemo.test;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lxk.motioneventdemo.EventHandler;

/**
 * @author https://github.com/103style
 * @date 2019/12/8 20:10
 */
public class TestLayout extends FrameLayout {
    private static final String TAG = TestLayout.class.getSimpleName();

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
        Log.e(TAG, System.currentTimeMillis() + "  dispatchTouchEvent: action = " + EventHandler.handlerEvent(ev.getAction()));
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
        Log.e(TAG, System.currentTimeMillis() + "  onInterceptTouchEvent: action = " + EventHandler.handlerEvent(ev.getAction()));
//        if (
//                ev.getAction() == MotionEvent.ACTION_MOVE
//                        ||
//                ev.getAction() == MotionEvent.ACTION_UP
//        ) {
//            return true;
//        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, System.currentTimeMillis() + "  onTouchEvent: action = " + EventHandler.handlerEvent(event.getAction()));
        return super.onTouchEvent(event);
    }
}