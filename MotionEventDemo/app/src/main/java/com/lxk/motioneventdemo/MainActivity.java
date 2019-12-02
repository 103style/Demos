package com.lxk.motioneventdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author https://github.com/103style
 * @date 2019/11/27 22:00
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private TestLinearLayout testLinearLayout;
    private TestView testView;

    private Button btReset, btInterceptAll, btGroupSetOnClick, btViewSetOnClick, btInterceptDown,
            btInterceptMove, btInterceptUp;
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
        btReset = findViewById(R.id.bt_reset);
        btInterceptAll = findViewById(R.id.bt_intercept_all);
        btGroupSetOnClick = findViewById(R.id.bt_group_down);
        btViewSetOnClick = findViewById(R.id.bt_view_down);
        btInterceptDown = findViewById(R.id.bt_intercept_down);
        btInterceptMove = findViewById(R.id.bt_intercept_move);
        btInterceptUp = findViewById(R.id.bt_intercept_up);

        btReset.setOnClickListener(this);
        btInterceptAll.setOnClickListener(this);
        btGroupSetOnClick.setOnClickListener(this);
        btViewSetOnClick.setOnClickListener(this);
        btInterceptDown.setOnClickListener(this);
        btInterceptMove.setOnClickListener(this);
        btInterceptUp.setOnClickListener(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(TAG, "dispatchTouchEvent: ev.getAction() = " + EventHandler.handlerEvent(ev.getAction()));
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "onTouchEvent: ev.getAction() = " + EventHandler.handlerEvent(event.getAction()));
        return super.onTouchEvent(event);
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
            default:
                break;
        }
    }
}
