package com.lxk.compiler;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

/**
 * @author https://github.com/103style
 * @date 2019/12/7 16:32
 */
public class LogUtils {

    private static Messager mMessager;

    public static void init(Messager messager) {
        mMessager = messager;
    }

    public static void e(String s) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, s);
    }

    public static void i(String s) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, s);
    }

    public static void w(String s) {
        mMessager.printMessage(Diagnostic.Kind.WARNING, s);
    }

    public static void o(String s) {
        mMessager.printMessage(Diagnostic.Kind.OTHER, s);
    }

}
