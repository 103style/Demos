package com.lxk.asmdemo;

/**
 * @author xiaoke.luo@tcl.com 2020/2/24 12:25
 */
public class TestClassLoader extends ClassLoader {

    public TestClassLoader() {
    }

    public Class<?> defineClassForName(String name, byte[] data) {
        return this.defineClass(name, data, 0, data.length);
    }
}
