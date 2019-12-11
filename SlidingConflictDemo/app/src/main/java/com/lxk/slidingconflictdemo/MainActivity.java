package com.lxk.slidingconflictdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.lxk.slidingconflictdemo.home.HomeActivity;
import com.lxk.slidingconflictdemo.recyclerview.RecyclerViewActivity;

/**
 * @author https://github.com/103style
 * @date 2019/12/11 20:29
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.bt_homepage).setOnClickListener(this);
        findViewById(R.id.bt_recycler_view).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_homepage:
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                break;
            case R.id.bt_recycler_view:
                startActivity(new Intent(MainActivity.this, RecyclerViewActivity.class));
                break;
            default:
                break;
        }
    }
}
