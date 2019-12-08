package com.lxk.motioneventdemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.lxk.motioneventdemo.test.TestActivity;

/**
 * @author https://github.com/103style
 * @date 2019/11/27 22:00
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private TestLinearLayout testLinearLayout;
    private TestView testView;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testLinearLayout = findViewById(R.id.tll);
        testView = findViewById(R.id.tv);

        findViewById(R.id.bt_reset).setOnClickListener(this);
        findViewById(R.id.bt_view_down).setOnClickListener(this);
        findViewById(R.id.bt_group_down).setOnClickListener(this);
        findViewById(R.id.bt_intercept_all).setOnClickListener(this);
        findViewById(R.id.bt_intercept_down).setOnClickListener(this);
        findViewById(R.id.bt_intercept_move).setOnClickListener(this);
        findViewById(R.id.bt_intercept_up).setOnClickListener(this);
        findViewById(R.id.bt_view_clickable).setOnClickListener(this);
        findViewById(R.id.bt_view_enable).setOnClickListener(this);

        findViewById(R.id.bt_test).setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_reset:
                TestLinearLayout.setIsIntercept(false);
                TestLinearLayout.setInterceptEvent(-1024);
                testLinearLayout.setOnClickListener(null);
                testView.setOnClickListener(null);
                testLinearLayout.setClickable(false);
                testView.setClickable(false);
                break;
            case R.id.bt_view_down:
                testView.setClickable(true);
                testView.setOnClickListener(onClickListener);
                break;
            case R.id.bt_group_down:
                testLinearLayout.setClickable(true);
                testLinearLayout.setOnClickListener(onClickListener);
                break;
            case R.id.bt_intercept_all:
                testLinearLayout.setClickable(false);
                TestLinearLayout.setIsIntercept(true);
                break;
            case R.id.bt_intercept_down:
                testLinearLayout.setClickable(false);
                TestLinearLayout.setInterceptEvent(MotionEvent.ACTION_DOWN);
                break;
            case R.id.bt_intercept_move:
                testLinearLayout.setClickable(false);
                TestLinearLayout.setInterceptEvent(MotionEvent.ACTION_MOVE);
                break;
            case R.id.bt_intercept_up:
                testLinearLayout.setClickable(false);
                TestLinearLayout.setInterceptEvent(MotionEvent.ACTION_UP);
                break;
            case R.id.bt_view_clickable:
                testView.setClickable(true);
                break;
            case R.id.bt_view_enable:
                testView.setEnabled(false);
                break;
            case R.id.bt_test:
                startActivity(new Intent(this, TestActivity.class));
            default:
                break;
        }
    }
}
