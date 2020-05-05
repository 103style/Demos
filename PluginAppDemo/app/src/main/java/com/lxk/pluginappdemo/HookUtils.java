package com.lxk.pluginappdemo;

import android.app.Activity;
import android.app.Instrumentation;
import android.os.Handler;
import android.util.Log;

import com.lxk.pluginappdemo.proxy.ProxyHandlerCallback;
import com.lxk.pluginappdemo.proxy.ProxyInstrumentation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
            field.set(activity, new ProxyInstrumentation(instrumentation));
            Log.e(TAG, "hookActivityInstrumentation Success!");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            Log.e(TAG, "hookActivityInstrumentation Failure!");
        }
    }

    public static void hookActivityThreadHandler() {
        try {
            Class actThread = Class.forName("android.app.ActivityThread");
            Method curThread = actThread.getDeclaredMethod("currentActivityThread");
            Object mActivityThread = curThread.invoke(null);
            Field handlerF = actThread.getDeclaredField("mH");
            handlerF.setAccessible(true);
            Handler handler = (Handler) handlerF.get(mActivityThread);
            Field callbackF = Handler.class.getDeclaredField("mCallback");
            callbackF.setAccessible(true);
            callbackF.set(handler, new ProxyHandlerCallback(handler));
            Log.e(TAG, "hookActivityThreadHandler Success!");
        } catch (ClassNotFoundException
                | NoSuchMethodException | NoSuchFieldException
                | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            Log.e(TAG, "hookActivityThreadHandler Failure!");
        }
    }


}
