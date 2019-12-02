package com.lxk.motioneventdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author https://github.com/103style
 * @date 2019/11/27 22:03
 */
public class TestLinearLayout extends LinearLayout {

    private static final String TAG = "TestLinearLayout";

    private static boolean isIntercept = false;

    private static int intercept_event = -1024;

    public TestLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public static void setIsIntercept(boolean intercept) {
        isIntercept = intercept;
    }

    public static void setInterceptEvent(int event) {
        intercept_event = event;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e(TAG, "onInterceptTouchEvent: ev.getAction() = " + EventHandler.handlerEvent(ev.getAction())
                + ", isIntercept = " + isIntercept);
        if (isIntercept) {
            return true;
        }

        if (ev.getAction() == intercept_event) {
            Log.e(TAG, "intercept event = " + EventHandler.handlerEvent(ev.getAction()));
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(TAG, "dispatchTouchEvent: ev.getAction() = " + EventHandler.handlerEvent(ev.getAction()));
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "onTouchEvent: ev.getAction() = " + EventHandler.handlerEvent(event.getAction()));
        return super.onTouchEvent(event);
    }
}
