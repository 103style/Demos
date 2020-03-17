package com.lxk.gradleplugindemo;

import android.util.Log;

/**
 * @author https://github.com/103style
 * @date 2020/3/17 17:11
 * 通过 ASM Bytecode outline 获取字节码
 */
public class Test {

    public void time() {
        long t = System.currentTimeMillis();
    }

    public void cost() {
        long t = System.currentTimeMillis();
        Log.e("TAG", "clickTest:  time(ms) cost = " + (System.currentTimeMillis() - t) + ", class name = " + this.getClass());
    }

}
