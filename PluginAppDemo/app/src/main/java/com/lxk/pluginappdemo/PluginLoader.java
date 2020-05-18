package com.lxk.pluginappdemo;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;

/**
 * @author https://github.com/103style
 * @date 2020/5/5 22:57
 */
public class PluginLoader {
    public static Resources sPluginResources;
    public static AssetManager sNewAssetManager;
    public static String resPath;

    public static void addResource(Context context) {
        //利用反射创建一个新的AssetManager
        try {
            sNewAssetManager = AssetManager.class.getConstructor().newInstance();
            //利用反射获取addAssetPath方法
            Method mAddAssetPath = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            mAddAssetPath.setAccessible(true);
            //利用反射调用addAssetPath方法加载外部的资源（SD卡）
            if (((Integer) mAddAssetPath.invoke(sNewAssetManager, resPath)) == 0) {
                throw new IllegalStateException("Could not create new AssetManager");
            }
            sPluginResources = new Resources(sNewAssetManager,
                    context.getResources().getDisplayMetrics(),
                    context.getResources().getConfiguration());

            Log.d(HookUtils.TAG, "资源加载完毕");
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    public static void loadPluginClass(Context context, ClassLoader hostClassLoader) {
        //下载插件 移动到package下的目录
        String localPath = copyFile(context);
        if (TextUtils.isEmpty(localPath)) {
            Log.e(HookUtils.TAG, "PluginLoader copy plugin error!");
        }
        resPath = localPath;
        String dexOpt = context.getDir("dexopt", 0).getAbsolutePath();
        DexClassLoader pluginClassLoader = new DexClassLoader(localPath, dexOpt, null, hostClassLoader);

        //合并类文件
        try {
            //获取当前的类
            Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
            pathListField.setAccessible(true);
            Object pathList = pathListField.get(hostClassLoader);
            Field dexElementsFiled = pathList.getClass().getDeclaredField("dexElements");
            dexElementsFiled.setAccessible(true);
            Object[] dexElements = (Object[]) dexElementsFiled.get(pathList);

            //获取插件中的类
            Object[] pluginDexElements = getDexElements(pluginClassLoader);

            //创建一个数组
            Object[] finalArray = (Object[]) Array.newInstance(dexElements.getClass().getComponentType(),
                    dexElements.length + pluginDexElements.length);
            //合并插件和应用内的类
            System.arraycopy(pluginDexElements, 0, finalArray, 0, pluginDexElements.length);
            System.arraycopy(dexElements, 0, finalArray, pluginDexElements.length, dexElements.length);
            //把新数组替换掉原先的数组
            dexElementsFiled.set(pathList, finalArray);
            Log.e(HookUtils.TAG, "PluginLoader load plugin success!");
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            Log.e(HookUtils.TAG, "PluginLoader load plugin failure!");
        }
    }

    /**
     * 获取当前类加载器的所有dexElements
     */
    private static Object[] getDexElements(ClassLoader classLoader) {
        try {
            Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
            pathListField.setAccessible(true);
            Object pathList = pathListField.get(classLoader);
            Field dexElementsFiled = pathList.getClass().getDeclaredField("dexElements");
            dexElementsFiled.setAccessible(true);
            return (Object[]) dexElementsFiled.get(pathList);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String copyFile(Context context) {
        String path = context.getFilesDir().getAbsolutePath() + File.separator + "plugin.apk";
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = context.getAssets().open("plugin.apk");
            outputStream = new BufferedOutputStream(new FileOutputStream(path));
            byte[] temp = new byte[1024];
            int len;
            while ((len = (inputStream.read(temp))) != -1) {
                outputStream.write(temp, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            path = "";
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }
}
