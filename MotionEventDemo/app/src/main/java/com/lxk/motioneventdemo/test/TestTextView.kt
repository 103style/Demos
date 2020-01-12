package com.lxk.motioneventdemo.test

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView
import com.lxk.motioneventdemo.EventHandler

/**
 * @author https://github.com/103style
 * @date 2020/1/12 16:13
 */
class TestTextView : AppCompatTextView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        Log.e(TAG, System.currentTimeMillis().toString() + "  dispatchTouchEvent: action = " + EventHandler.handlerEvent(event.action))
        return super.dispatchTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.e(TAG, System.currentTimeMillis().toString() + "  onTouchEvent: action = " + EventHandler.handlerEvent(event.action))
        return true
    }

    companion object {
        private val TAG = "TestTextView"
    }
}
