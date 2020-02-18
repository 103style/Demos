package com.lxk.asmdemo;

/**
 * @author https://github.com/103style
 * @date 2020/2/18 15:49
 */
public class HelloWorld {

    public void sayHello() {
        try {
            Thread.sleep(2 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
