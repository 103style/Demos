package com.lxk.motioneventdemo.test

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout
import com.lxk.motioneventdemo.EventHandler

/**
 * @author https://github.com/103style
 * @date 2020/1/12 16:12
 */
class TestLayout : FrameLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        Log.e(TAG, System.currentTimeMillis().toString() + "  dispatchTouchEvent: action = " + EventHandler.handlerEvent(ev.action))
        return if (ev.action == MotionEvent.ACTION_MOVE) {
            true
        } else super.dispatchTouchEvent(ev)//                        ||
        //                ev.getAction() == MotionEvent.ACTION_UP
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        Log.e(TAG, System.currentTimeMillis().toString() + "  onInterceptTouchEvent: action = " + EventHandler.handlerEvent(ev.action))
        //        if (
        //                ev.getAction() == MotionEvent.ACTION_MOVE
        //                        ||
        //                ev.getAction() == MotionEvent.ACTION_UP
        //        ) {
        //            return true;
        //        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.e(TAG, System.currentTimeMillis().toString() + "  onTouchEvent: action = " + EventHandler.handlerEvent(event.action))
        return super.onTouchEvent(event)
    }

    companion object {
        private val TAG = TestLayout::class.java.simpleName
    }
}