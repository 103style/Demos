package com.lxk.pluginappdemo.proxy;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.lxk.pluginappdemo.HookUtils;
import com.lxk.pluginappdemo.ProxyActivity;

import java.lang.reflect.Method;

/**
 * @author https://github.com/103style
 * @date 2020/5/5 21:59
 */
public class ProxyInstrumentation extends Instrumentation {
    private Instrumentation instrumentation;

    public ProxyInstrumentation(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token,
                                            Activity target, Intent intent, int requestCode,
                                            Bundle options) {
        StringBuilder sb = new StringBuilder();
        sb.append("who = [").append(who).append("], ")
                .append("contextThread = [").append(contextThread).append("], ")
                .append("token = [").append(token).append("], ")
                .append("target = [").append(target).append("], ")
                .append("intent = [").append(intent).append("], ")
                .append("requestCode = [").append(requestCode).append("], ")
                .append("options = [").append(options).append("]");
        Log.e(HookUtils.TAG, "ProxyInstrumentation  execStartActivity info = " + who.toString());


        Intent proxyIntent = new Intent(target, ProxyActivity.class);
        proxyIntent.putExtra(ProxyActivity.TARGET_COMPONENT, intent);
        intent = proxyIntent;

        try {
            String methodName = "execStartActivity";
            Method execStartActivity = Instrumentation.class.getDeclaredMethod(methodName,
                    Context.class, IBinder.class, IBinder.class, Activity.class, Intent.class,
                    int.class, Bundle.class);
            return (ActivityResult) execStartActivity.invoke(instrumentation, who, contextThread, token, target, intent, requestCode, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
