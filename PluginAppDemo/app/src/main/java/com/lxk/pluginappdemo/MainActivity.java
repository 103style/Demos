package com.lxk.pluginappdemo;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author https://github.com/103style
 * @date 2020/5/5 21:52
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.bt_load).setOnClickListener(v -> loadPlugin());
        findViewById(R.id.bt_go).setOnClickListener(v -> gotoPlugin());
    }

    private void loadPlugin() {
        HookUtils.hookActivityInstrumentation(this);
        PluginLoader.loadPluginClass(this, getClassLoader());
        PluginLoader.addResource(this);
    }

    private void gotoPlugin() {
        Intent intent = new Intent();

        intent.setComponent(new ComponentName("com.lxk.plugin", "com.lxk.plugin.PluginActivity"));
        startActivity(intent);
    }
}
