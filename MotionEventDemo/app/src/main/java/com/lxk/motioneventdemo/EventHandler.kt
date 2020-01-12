package com.lxk.motioneventdemo

import android.view.MotionEvent

/**
 * @author https://github.com/103style
 * @date 2020/1/12 15:59
 */
object EventHandler {

    fun handlerEvent(action: Int): String {
        return when (action) {
            MotionEvent.ACTION_DOWN -> "ACTION_DOWN"
            MotionEvent.ACTION_UP -> "ACTION_UP"
            MotionEvent.ACTION_MOVE -> "ACTION_MOVE"
            MotionEvent.ACTION_CANCEL -> "ACTION_CANCEL"
            else -> action.toString()
        }
    }
}
