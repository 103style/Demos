package com.lxk.motioneventdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private TestFrameLayout testFrameLayout;
    private TestView testView;

    private Button btReset, btIntercept, btGroupDown, btViewDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testFrameLayout = findViewById(R.id.tfl);
        testView = findViewById(R.id.tv);
        btReset = findViewById(R.id.bt_reset);
        btIntercept = findViewById(R.id.bt_intercept);
        btGroupDown = findViewById(R.id.bt_group_down);
        btViewDown = findViewById(R.id.bt_view_down);

        btReset.setOnClickListener(this);
        btIntercept.setOnClickListener(this);
        btGroupDown.setOnClickListener(this);
        btViewDown.setOnClickListener(this);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(TAG, "dispatchTouchEvent: ev.getAction() = " + EventHandler.handlerEvent(ev.getAction()));
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "onInterceptTouchEvent: ev.getAction() = " + EventHandler.handlerEvent(event.getAction()));
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_reset:
                TestFrameLayout.setIsIntercept(false);
                testFrameLayout.setOnClickListener(null);
                testView.setOnClickListener(null);
                break;
            case R.id.bt_intercept:
                TestFrameLayout.setIsIntercept(true);
                break;
            case R.id.bt_group_down:
                testFrameLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case R.id.bt_view_down:
                testView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
        }
    }
}
