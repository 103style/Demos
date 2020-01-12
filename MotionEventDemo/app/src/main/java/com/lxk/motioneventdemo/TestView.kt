package com.lxk.motioneventdemo

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

/**
 * @author https://github.com/103style
 * @date  2020/1/12 16:11
 */
class TestView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        Log.e(TAG, "dispatchTouchEvent: ev.getAction() = " + EventHandler.handlerEvent(ev.action))
        val res = super.dispatchTouchEvent(ev)
        Log.i(TAG, "dispatchTouchEvent: return  $res")
        return res
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.e(TAG, "onTouchEvent: ev.getAction() = " + EventHandler.handlerEvent(event.action))
        val res = super.onTouchEvent(event)
        Log.i(TAG, "onTouchEvent: return  $res")
        return res
    }

    companion object {

        private val TAG = "TestView"
    }
}
