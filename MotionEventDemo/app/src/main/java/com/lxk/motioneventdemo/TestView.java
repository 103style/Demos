package com.lxk.motioneventdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @author https://github.com/103style
 * @date 2019/11/27 23:09
 */
public class TestView extends View {

    private static final String TAG = "TestView";

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(TAG, "dispatchTouchEvent: ev.getAction() = " + EventHandler.handlerEvent(ev.getAction()));
        boolean res = super.dispatchTouchEvent(ev);
        Log.i(TAG, "dispatchTouchEvent: return  " + res);
        return res;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "onTouchEvent: ev.getAction() = " + EventHandler.handlerEvent(event.getAction()));
        boolean res = super.onTouchEvent(event);
        Log.i(TAG, "onTouchEvent: return  " + res);
        return res;
    }
}
