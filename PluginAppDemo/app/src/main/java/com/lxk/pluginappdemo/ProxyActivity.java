package com.lxk.pluginappdemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author https://github.com/103style
 * @date 2020/5/5 22:48
 * 占坑的activity
 */
public class ProxyActivity extends AppCompatActivity {

    public static final String TARGET_COMPONENT = "TARGET_COMPONENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prozy);
    }
}
