package com.lxk.asmdemo;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

/**
 * @author xiaoke.luo@tcl.com 2020/2/18 16:00
 */
public class CostTime {

    public static void main(String[] args) {
        try {
            modifyHelloWorldClass();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void modifyHelloWorldClass() throws Exception {
        String className = HelloWorld.class.getName();
        System.out.println(className);
        String classFilePath = "E:\\Demos\\ASMDemo\\app\\src\\main\\java\\com\\lxk\\asmdemo\\HelloWorld.class";
        String classFileChangePath = "E:\\Demos\\ASMDemo\\app\\src\\main\\java\\com\\lxk\\asmdemo\\HelloWorldChange.class";
        InputStream is = new FileInputStream(classFilePath);
        ClassReader reader = new ClassReader(is);
        ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS);
        // TODO: 2020/2/18
//        ClassVisitor change = new ChangeVisitor(writer);
//        reader.accept(change, ClassReader.EXPAND_FRAMES);

//        Class c = new MyClassLoader().defineClass(className, writer.toByteArray());
//        Object personObj = c.newInstance();
//        Method nameMethod = c.getDeclaredMethod("sayHello", null);
//        nameMethod.invoke(personObj, null);

        byte[] code = writer.toByteArray();

        FileOutputStream fos = new FileOutputStream(classFileChangePath);
        fos.write(code);
        fos.close();
    }

}
