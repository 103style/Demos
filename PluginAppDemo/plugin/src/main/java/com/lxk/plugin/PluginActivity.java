package com.lxk.plugin;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author https://github.com/103style
 * @date 2020/5/5 21:52
 */
public class PluginActivity extends AppCompatActivity {

    @Override
    public Resources getResources() {
        return getApplication() != null && getApplication().getResources() != null ? getApplication().getResources() : super.getResources();
    }

    @Override
    public AssetManager getAssets() {
        return getApplication() != null && getApplication().getAssets() != null ? getApplication().getAssets() : super.getAssets();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin);
    }
}
