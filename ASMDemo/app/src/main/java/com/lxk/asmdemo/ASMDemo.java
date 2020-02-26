package com.lxk.asmdemo;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.InvocationTargetException;

/**
 * @author https://github.com/103style
 * @date 2020/2/24 10:12
 */
public class ASMDemo {
    static ClassWriter createClassWriter(String classname) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        //基于jdk 1.8版本声明类 public class classname{}
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, classname, null, "java/lang/Object", null);
        //声明无参构造函数 public classname(){}
        MethodVisitor constructor = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        constructor.visitVarInsn(Opcodes.ALOAD, 0);
        //执行init方法
        constructor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        //从当前方法返回 void
        constructor.visitInsn(Opcodes.RETURN);
        constructor.visitMaxs(1, 1);
        constructor.visitEnd();
        return cw;
    }

    static byte[] createVoidMethod(String classname, String message) {
        //
        ClassWriter cw = createClassWriter(classname.replace(".", "/"));

        //创建 public void run(){} 方法
        MethodVisitor runMethod = cw.visitMethod(Opcodes.ACC_PUBLIC, "run", "()V", null, null);
        //获取 System.out 对象,
        runMethod.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        //将int, float或String型常量值从常量池中推送至栈顶
        runMethod.visitLdcInsn(message);
        //执行println方法
        runMethod.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        runMethod.visitInsn(Opcodes.RETURN);
        runMethod.visitMaxs(1, 1);
        runMethod.visitEnd();
        return cw.toByteArray();
    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        String className = "com.lxk.asmdemo.HelloWorld";
        byte[] classData = createVoidMethod(className, "Hello from ASM");
        Class<?> clazz = new TestClassLoader().defineClassForName(className, classData);
        clazz.getMethods()[0].invoke(clazz.newInstance());
    }
}
