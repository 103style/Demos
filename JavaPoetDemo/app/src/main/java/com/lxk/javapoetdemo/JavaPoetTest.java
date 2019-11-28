package com.lxk.javapoetdemo;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import com.squareup.javapoet.WildcardTypeName;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Modifier;


/**
 * @author https://github.com/103style
 * @date 2019/11/28 10:14
 */
public class JavaPoetTest {

    /**
     * 生成的类名
     */
    private String generateClassName = "JavaPoetGenerateClass";

    private String classDir = "./app/src/main/java/";

    private String elementData = "elementData";
    /**
     * 对应的包名
     */
    private String packageName;

    private String staticName = "SINGLE";

    /**
     * 初始化参数
     */
    private void init() {
        packageName = JavaPoetTest.class.getPackage().getName();
    }

    /**
     * 开始生成java代码
     */
    public void run() {
        init();
        TypeSpec typeSpec = getTypeSpec();
        JavaFile javaFile = JavaFile.builder(packageName, typeSpec).build();
        System.out.println(javaFile.toString());
        try {
            javaFile.writeTo(new File(classDir));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private TypeSpec getTypeSpec() {
        TypeSpec.Builder typeSpeBuilder = getClassTypeBuilder();

        //添加成员变量
        addFields(typeSpeBuilder);

        //添加静态代码块
        typeSpeBuilder.addStaticBlock(getCodeBlock());
        //添加初始化代码块 {}
        typeSpeBuilder.addInitializerBlock(CodeBlock.builder().build());

        //添加方法
        addMethods(typeSpeBuilder);

        //添加内部类
        addInnerClass(typeSpeBuilder);

        return typeSpeBuilder.build();
    }

    /**
     * 构建类名
     * public class JavaPoetGenerateClass<T> extends ArrayList<T>
     * implements Serializable,Comparable<JavaPoetGenerateClass<? extends T>> {}
     */
    private TypeSpec.Builder getClassTypeBuilder() {
        return TypeSpec
                //class  JavaPoetGenerateClass
                .classBuilder(generateClassName)
                //public class JavaPoetGenerateClass
                .addModifiers(Modifier.PUBLIC)
                //public class JavaPoetGenerateClass<T>
                .addTypeVariable(TypeVariableName.get("T"))
                //extends ArrayList<T>
                .superclass(ParameterizedTypeName.get(ClassName.get(ArrayList.class), TypeVariableName.get("T")))
                //implements Serializable
                .addSuperinterface(Serializable.class)
                //implements Comparable<JavaPoetGenerateClass<? extends T>>
                .addSuperinterface(
                        //Comparable<JavaPoetGenerateClass<? extends T>>
                        ParameterizedTypeName.get(
                                ClassName.get(Comparable.class),
                                //JavaPoetGenerateClass<? extends T>
                                ParameterizedTypeName.get(
                                        ClassName.get(packageName, generateClassName),
                                        WildcardTypeName.subtypeOf(TypeVariableName.get("T"))
                                )
                        )
                )
                .addJavadoc("@author https://github.com/103style\n")
                .addJavadoc("JavaPoet generate class\n");
    }

    /**
     * 添加静态代码块
     * <p>
     * $T 是类型替换, 一般用于 ("$T foo", List.class) => List foo. $T 的好处在于 JavaPoet 会自动帮你补全文件开头的 import.
     * 如果直接写 ("List foo") 虽然也能生成 List foo， 但是最终的 java 文件就不会自动帮你添加 import java.util.List.
     * <p>
     * $L 是字面量替换, 比如 ("abc$L123", "FOO") => abcFOO123. 也就是直接替换.
     * <p>
     * $S 是字符串替换, 比如: ("$S.length()", "foo") => "foo".length() 注意 $S 是将参数替换为了一个带双引号的字符串.
     * 免去了手写 "\"foo\".length()" 中转义 (\") 的麻烦.
     * <p>
     * $N 是名称替换, 比如你之前定义了一个函数 MethodSpec methodSpec = MethodSpec.methodBuilder("foo").build();
     * 现在你可以通过 $N 获取这个函数的名称 ("$N", methodSpec) => foo.
     */
    private CodeBlock getCodeBlock() {
        return CodeBlock.builder()
                //System.loadLibrary("test.so");
                .add("System.loadLibrary($S);\n", "test.so")
                //staticName = "staticName";
                .add("$L = $S;\n", staticName, staticName)
                .build();
    }

    /**
     * 添加成员变量
     */
    private void addFields(TypeSpec.Builder typeSpeBuilder) {
        //private static final String TAG = generateClassName;
        typeSpeBuilder.addField(
                FieldSpec.builder(
                        String.class, "TAG", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                        .initializer("$S", generateClassName)
                        .build()
        );

        // private static final int DEFAULT_CAPACITY = 10;
        typeSpeBuilder.addField(
                FieldSpec.builder(int.class, "DEFAULT_CAPACITY")
                        .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                        .initializer("$L", 10).build()
        );

        //private static final String staticName;
        typeSpeBuilder.addField(
                FieldSpec.builder(String.class, staticName)
                        .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                        .build()
        );

        //private int size;
        typeSpeBuilder.addField(
                FieldSpec.builder(int.class, "size", Modifier.PRIVATE).build()
        );

        //transient Object[] elementData;
        typeSpeBuilder.addField(
                FieldSpec.builder(ArrayTypeName.of(Object.class), elementData)
                        .addModifiers(Modifier.TRANSIENT).build()
        );

        //private T t;
        typeSpeBuilder.addField(
                FieldSpec.builder(TypeVariableName.get("T"), "t", Modifier.PRIVATE).build()
        );

        //private List<T> mParameterizedField;
        typeSpeBuilder.addField(
                FieldSpec.builder(
                        ParameterizedTypeName.get(ClassName.get(List.class), TypeVariableName.get("T")),
                        "mParameterizedField",
                        Modifier.PRIVATE)
                        .build()
        );

        //private List<? extends T> mWildcardField;
        typeSpeBuilder.addField(
                FieldSpec.builder(
                        ParameterizedTypeName.get(
                                ClassName.get(List.class),
                                WildcardTypeName.subtypeOf(TypeVariableName.get("T"))
                        ),
                        "mWildcardField",
                        Modifier.PRIVATE)
                        .build()
        );
    }

    /**
     * 添加 构造方法 自定义方法  重写的方法
     */
    private void addMethods(TypeSpec.Builder typeSpeBuilder) {
        //public generateClassName(){}
        typeSpeBuilder.addMethod(
                MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).build()
        );

        //public generateClassName(int initialCapacity){this.elementData =  new Object[initialCapacity];}
        typeSpeBuilder.addMethod(
                MethodSpec.constructorBuilder()
                        .addParameter(ParameterSpec.builder(int.class, "initialCapacity").build())
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("this.elementData =  new Object[$L]", "initialCapacity")
                        .build()
        );

        //@Override public int size(){return size;}
        typeSpeBuilder.addMethod(
                MethodSpec.methodBuilder("size")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(int.class)
                        .addStatement("return $L", "size")
                        .build()
        );

        //添加重写的 compareTo 方法
        addMethodCompareTo(typeSpeBuilder);

        //添加示例方法
        typeSpeBuilder.addMethod(
                MethodSpec.methodBuilder("examples")
                        .addTypeVariable(TypeVariableName.get("T"))
                        .addModifiers(Modifier.PUBLIC)
                        .returns(int.class)
                        .addParameter(String.class, "string")
                        .addParameter(TypeVariableName.get("T"), "t")
                        .addParameter(ParameterizedTypeName.get(ClassName.get(Map.class),
                                ClassName.get(Integer.class),
                                WildcardTypeName.subtypeOf(TypeVariableName.get("T"))),
                                "map")
                        .addException(IOException.class)
                        .addException(RuntimeException.class)
                        .addCode(code())
                        .build()
        );
    }

    /**
     * 添加重写的 compareTo 方法
     */
    private void addMethodCompareTo(TypeSpec.Builder typeSpeBuilder) {
        //@Override
        //public int compareTo(generateClassName<? extends T> o){}
        typeSpeBuilder.addMethod(
                MethodSpec.methodBuilder("compareTo")
                        .addAnnotation(AnnotationSpec.builder(Override.class).build())
                        .addModifiers(Modifier.PUBLIC)
                        .returns(int.class)
                        .addParameter(
                                ParameterSpec.builder(
                                        ParameterizedTypeName.get(
                                                ClassName.get(packageName, generateClassName),
                                                WildcardTypeName.subtypeOf(TypeVariableName.get("T"))
                                        ),
                                        "o"
                                ).build()
                        )
                        .addStatement("int res")
                        // if... else if... else...
                        .beginControlFlow("if(size() > o.size())")
                        .addStatement("res = 1")
                        .nextControlFlow("else if(size() == o.size())")
                        .addStatement("res = 0")
                        .nextControlFlow("else")
                        .addStatement("res = -1")
                        .endControlFlow()
                        .addStatement("return res")
                        .build()
        );
    }

    /**
     * 示例
     */
    private CodeBlock code() {
        return CodeBlock.builder()
                .addStatement("int foo = 1")
                .addStatement("$T bar = $S", String.class, "a string")

                // Object obj = new HashMap<Integer, ? extends T>(5);
                .addStatement("$T obj = new $T(5)",
                        Object.class, ParameterizedTypeName.get(ClassName.get(HashMap.class),
                                ClassName.get(Integer.class),
                                ParameterizedTypeName.get(
                                        ClassName.get(packageName, generateClassName),
                                        WildcardTypeName.subtypeOf(TypeVariableName.get("T"))
                                )
                        )
                )
                .addStatement("$L",
                        TypeSpec.anonymousClassBuilder("")
                                .superclass(Runnable.class)
                                .addMethod(MethodSpec.methodBuilder("run")
                                        .addModifiers(Modifier.PUBLIC)
                                        .addAnnotation(Override.class)
                                        .returns(TypeName.VOID)
                                        .build())
                                .build())

                // for
                .beginControlFlow("for (int i = 0; i < 5; i++)")
                .addStatement("System.out.println(i)")
                .endControlFlow()

                // while
                .beginControlFlow("while (true)")
                .addStatement("break")
                .endControlFlow()

                // do... while
                .beginControlFlow("do")
                .addStatement("break")
                .endControlFlow("while (false)")

                // if... else if... else...
                .beginControlFlow("if (false)")
                .addStatement("int step1")
                .nextControlFlow("else if (false)")
                .addStatement("int step2")
                .nextControlFlow("else")
                .addStatement("int step3")
                .endControlFlow()

                // try... catch... finally
                .beginControlFlow("try")
                .addStatement("throw new $T()", NullPointerException.class)
                .nextControlFlow("catch ($T e)", Exception.class)
                .addStatement("e.printStackTrace()")
                .nextControlFlow("finally")
                .addStatement("System.out.println($S)", "finally run!")
                .endControlFlow()

                .addStatement("return 0")
                .build();
    }

    /**
     * 添加内部类
     */
    private void addInnerClass(TypeSpec.Builder typeSpeBuilder) {
        typeSpeBuilder.addType(TypeSpec.classBuilder("InnerClass").addModifiers(Modifier.PUBLIC, Modifier.STATIC).build());
        typeSpeBuilder.addType(
                TypeSpec.interfaceBuilder("InnerInterface")
                        .addModifiers(Modifier.PUBLIC)
                        .addMethod(
                                MethodSpec.methodBuilder("onClickListener")
                                        .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                                        .addJavadoc("点击事件回调\n")
                                        .build())
                        .build()
        );
    }

}
