package com.lxk.plugin;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author https://github.com/103style
 * @date 2020/3/17 16:04
 */
public class TestClassVisitor extends ClassVisitor implements Opcodes {

    private String mClassName;

    public TestClassVisitor(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);

    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.mClassName = name;
        super.visit(version, access, name, signature, superName, interfaces);
        System.out.println("visit name = " + mClassName);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        if (this.mClassName.contains("GradlePluginASMTestActivity")) {
            if ("clickTest".equals(name)) {
                //修改 clickTest方法的逻辑
                System.out.println("TestClassVisitor : change method ----> " + name);
                return new TestMethodVisitor(mv);
            }
        }
        return mv;
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}
