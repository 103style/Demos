package com.lxk.motioneventdemo.test;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.lxk.motioneventdemo.EventHandler;

/**
 * @author https://github.com/103style
 * @date 2019/12/8 20:11
 */
public class TestTextView extends AppCompatTextView {
    private static final String TAG = "TestTextView";

    public TestTextView(Context context) {
        super(context);
    }

    public TestTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TestTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.e(TAG, System.currentTimeMillis() + "  dispatchTouchEvent: action = " + EventHandler.handlerEvent(event.getAction()));
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, System.currentTimeMillis() + "  onTouchEvent: action = " + EventHandler.handlerEvent(event.getAction()));
        return true;
    }
}
