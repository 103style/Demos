package com.lxk.slidingconflictdemo.test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author https://github.com/103style
 * @date 2019/12/15 23:45
 */
public class TestView extends View {

    private float lastEventX, lastEventY;

    public TestView(Context context) {
        super(context);
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float x = ev.getX();
        float y = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = x - lastEventX;
                float dy = y - lastEventY;
//                if(父容器需要处理){
//                    getParent().requestDisallowInterceptTouchEvent(false);
//                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        lastEventX = x;
        lastEventY = y;
        return super.dispatchTouchEvent(ev);
    }
}
