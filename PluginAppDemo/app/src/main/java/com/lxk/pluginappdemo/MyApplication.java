package com.lxk.pluginappdemo;

import android.app.Application;
import android.content.res.AssetManager;
import android.content.res.Resources;

/**
 * @author https://github.com/103style
 * @date 2020/5/5 23:13
 */
public class MyApplication extends Application {

    @Override
    public Resources getResources() {
        return PluginLoader.sPluginResources == null ? super.getResources() : PluginLoader.sPluginResources;
    }

    @Override
    public AssetManager getAssets() {
        return PluginLoader.sNewAssetManager == null ? super.getAssets() : PluginLoader.sNewAssetManager;
    }
}
