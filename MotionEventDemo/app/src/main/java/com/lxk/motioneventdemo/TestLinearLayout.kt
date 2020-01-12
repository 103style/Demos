package com.lxk.motioneventdemo

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.LinearLayout

/**
 * @author https://github.com/103style
 * @date 2020/1/12 16:11
 */
class TestLinearLayout(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val res: Boolean
        Log.e(TAG, "onInterceptTouchEvent: ev.getAction() = " + EventHandler.handlerEvent(ev.action)
                + ", isIntercept = " + isIntercept)
        if (isIntercept) {
            res = true
            Log.i(TAG, "onInterceptTouchEvent: return $res")
            return true
        }

        if (ev.action == intercept_event) {
            Log.e(TAG, "intercept event = " + EventHandler.handlerEvent(ev.action))
            res = true
            Log.i(TAG, "onInterceptTouchEvent: return $res")
            return true
        }
        res = super.onInterceptTouchEvent(ev)
        Log.i(TAG, "onInterceptTouchEvent: return $res")
        return res
    }

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

        private val TAG = "TestLinearLayout"

        private var isIntercept = false

        private var intercept_event = -1024

        fun setIsIntercept(intercept: Boolean) {
            isIntercept = intercept
        }

        fun setInterceptEvent(event: Int) {
            intercept_event = event
        }
    }
}
