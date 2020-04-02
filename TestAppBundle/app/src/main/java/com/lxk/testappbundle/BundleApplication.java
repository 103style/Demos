package com.lxk.testappbundle;

import android.app.Application;
import android.content.Context;

import com.google.android.play.core.splitcompat.SplitCompat;

/**
 * @author https://github.com/103style
 * @date 2020/4/2 11:28
 */
public class BundleApplication extends Application {

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        SplitCompat.install(this);
    }

}
