package com.lxk.windowdemo;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import static android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;


/**
 * @author https://github.com/103style
 * @date 2020/1/13 21:18
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.test).setOnClickListener(v -> testShowWindow());

        checkPermission();
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(MainActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 10);
            }
        }
    }


    private void testShowWindow() {
        //加载一个自定义布局
        View windowView = LayoutInflater.from(this).inflate(R.layout.window_test, null);
        //配置布局参数
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                TYPE_APPLICATION_OVERLAY, 0, PixelFormat.TRANSPARENT);
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
        layoutParams.gravity = Gravity.START | Gravity.TOP;
        layoutParams.x = 160;
        layoutParams.y = 320;
        WindowManager windowManager = getWindowManager();
        windowManager.addView(windowView, layoutParams);

        windowView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                layoutParams.x = (int) event.getRawX() - windowView.getWidth() / 2;
                layoutParams.y = (int) event.getRawY() - windowView.getHeight() / 2;
                windowManager.updateViewLayout(windowView, layoutParams);
            }
            return true;
        });
        windowView.findViewById(R.id.test).setOnClickListener(v -> {
            windowManager.removeView(windowView);
        });
    }
}
