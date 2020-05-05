package com.lxk.pluginappdemo.proxy;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.lxk.pluginappdemo.HookUtils;
import com.lxk.pluginappdemo.ProxyActivity;

import java.lang.reflect.Field;

/**
 * @author https://github.com/103style
 * @date 2020/5/5 22:05
 */
public class ProxyHandlerCallback implements Handler.Callback {
    private Handler mH;

    public ProxyHandlerCallback(Handler mH) {
        this.mH = mH;
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        Log.e(HookUtils.TAG, "ProxyHandlerCallback handleMessage: " + msg);
        if (msg.what == 100) {
            try {
                Object obj = msg.obj;
                Field intentField = obj.getClass().getDeclaredField("intent");
                intentField.setAccessible(true);
                Intent intent = (Intent) intentField.get(obj);
                if (intent != null && intent.hasExtra(ProxyActivity.TARGET_COMPONENT)) {
                    Intent targetIntent = intent.getParcelableExtra(ProxyActivity.TARGET_COMPONENT);
                    intent.setComponent(targetIntent.getComponent());
                    Log.e(HookUtils.TAG, "ProxyHandlerCallback targetIntent = "
                            + targetIntent.toString());
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        mH.handleMessage(msg);
        return true;
    }
}
