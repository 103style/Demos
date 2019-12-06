package com.lxk.annotationdemo;

import java.io.File;
import java.io.IOException;

/**
 * @author xiaoke.luo@tcl.com 2019/12/6 14:03
 */
public class Test {
    public static void main(String[] args) throws IOException {
        File file = new File("tt.txt");
        if (!file.exists()){
            file.createNewFile();
        }
    }
}
