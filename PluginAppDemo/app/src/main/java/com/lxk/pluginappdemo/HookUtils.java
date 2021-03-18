package com.lxk.pluginappdemo;

import android.app.Activity;
import android.app.Instrumentation;
import android.util.Log;

import com.lxk.pluginappdemo.proxy.ProxyInstrumentation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author https://github.com/103style
 * @date 2020/5/5 21:55
 */
public class HookUtils {
    public static final String TAG = "HookUtils";

    public static void hookActivityInstrumentation(Activity activity) {
        try {
            Field field = Activity.class.getDeclaredField("mInstrumentation");
            field.setAccessible(true);
            Instrumentation instrumentation = (Instrumentation) field.get(activity);
            ProxyInstrumentation proxyInstrumentation = new ProxyInstrumentation(instrumentation);
            field.set(activity, proxyInstrumentation);
            Log.e(TAG, "hookActivityInstrumentation Success!");
            hookActivityThreadInstrumentation();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            Log.e(TAG, "hookActivityInstrumentation Failure!");
        }
    }

    private static void hookActivityThreadInstrumentation() {
        try {
            Class actThread = Class.forName("android.app.ActivityThread");
            Method curThread = actThread.getDeclaredMethod("currentActivityThread");
            Object mActivityThread = curThread.invoke(null);

            Field field = actThread.getDeclaredField("mInstrumentation");
            field.setAccessible(true);
            Instrumentation instrumentation = (Instrumentation) field.get(mActivityThread);
            field.set(mActivityThread, new ProxyInstrumentation(instrumentation));
            Log.e(TAG, "hookActivityThreadInstrumentation Success!");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "hookActivityThreadInstrumentation Failure!");
        }
    }


}
