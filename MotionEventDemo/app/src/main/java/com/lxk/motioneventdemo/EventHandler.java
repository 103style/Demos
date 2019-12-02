package com.lxk.motioneventdemo;

import android.view.MotionEvent;

/**
 * @author https://github.com/103style
 * @date 2019/11/27 23:24
 */
public class EventHandler {

    public static String handlerEvent(int action) {
        String res = "";
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                res = "ACTION_DOWN";
                break;
            case MotionEvent.ACTION_UP:
                res = "ACTION_UP";
                break;
            case MotionEvent.ACTION_MOVE:
                res = "ACTION_MOVE";
                break;
            case MotionEvent.ACTION_CANCEL:
                res = "ACTION_CANCEL";
                break;
            default:
                res = String.valueOf(action);

        }
        return res;
    }
}
