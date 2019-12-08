package com.lxk.ipcdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import aidl.BookManagerActivity;
import contentprovider.ProviderActivity;
import messenger.MessengerActivity;
import socket.SocketTestActivity;

/**
 * @author https://github.com/103style
 * @date 2019/12/8 18:02
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.bt_messenger).setOnClickListener(this);
        findViewById(R.id.bt_aidl).setOnClickListener(this);
        findViewById(R.id.bt_content_provider).setOnClickListener(this);
        findViewById(R.id.bt_socket).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Class c = null;
        switch (v.getId()) {
            case R.id.bt_messenger:
                c = MessengerActivity.class;
                break;
            case R.id.bt_aidl:
                c = BookManagerActivity.class;
                break;
            case R.id.bt_content_provider:
                c = ProviderActivity.class;
                break;
            case R.id.bt_socket:
                c = SocketTestActivity.class;
                break;
        }
        if (c != null) {
            startActivity(new Intent(this, c));
        }
    }
}
