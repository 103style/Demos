package com.lxk.libnavcomplier;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.auto.service.AutoService;
import com.lxk.libnavannotation.ActivityDestination;
import com.lxk.libnavannotation.FragmentDestination;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

/**
 * @author https://github.com/103style
 * @date 2020/10/20 16:23
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"com.lxk.libnavannotation.FragmentDestination",
        "com.lxk.libnavannotation.ActivityDestination"})
public class NavProcessor extends AbstractProcessor {
    private static final String OUTPUT_FILE_NAME = "destination.json";
    private Messager messager;
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> fragmentElements = roundEnv.getElementsAnnotatedWith(FragmentDestination.class);
        Set<? extends Element> activityElements = roundEnv.getElementsAnnotatedWith(ActivityDestination.class);
        if (!fragmentElements.isEmpty() || !activityElements.isEmpty()) {
            HashMap<String, JSONObject> destMap = new HashMap<>();
            //处理注解 获取对应的值 存入map中
            handleDestination(fragmentElements, FragmentDestination.class, destMap);
            handleDestination(activityElements, ActivityDestination.class, destMap);
            //生成json文件 放到 asserts 目录下
            makeFileToAsserts(destMap);
        }
        return true;
    }

    /**
     * 将解析的内容 生成json文件 放到 asserts 目录下
     */
    private void makeFileToAsserts(HashMap<String, JSONObject> destMap) {
        //app/src/main/asserts
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try {
            //../NavigationDemo/app/build/intermediates/javac/debug/classes/destination.json
            FileObject resource = filer.createResource(StandardLocation.CLASS_OUTPUT, "", OUTPUT_FILE_NAME);
            String resourcePath = resource.toUri().getPath();
            messager.printMessage(Diagnostic.Kind.NOTE, "resourcePath = " + resourcePath);

            String appPath = resourcePath.substring(0, resourcePath.indexOf("app") + 4);
            String assertsPath = appPath + "src/main/asserts/";
            File file = new File(assertsPath);
            if (!file.exists()) {
                file.mkdir();
            }

            File outPutFile = new File(file, OUTPUT_FILE_NAME);
            if (outPutFile.exists()) {
                outPutFile.delete();
            }
            outPutFile.createNewFile();

            String content = JSON.toJSONString(destMap);
            fos = new FileOutputStream(outPutFile);
            osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            osw.write(content);
            osw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (osw != null) {
                    osw.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleDestination(Set<? extends Element> elements, Class<? extends Annotation> annotationClaz, HashMap<String, JSONObject> destMap) {
        for (Element element : elements) {
            TypeElement typeElement = (TypeElement) element;
            String clzName = typeElement.getQualifiedName().toString();
            int id = Math.abs(clzName.hashCode());
            String pageUrl = null;
            boolean needLogin = false, asStarter = false, isFragment = false;

            //获取注解的值
            Annotation annotation = element.getAnnotation(annotationClaz);
            if (annotation instanceof FragmentDestination) {
                FragmentDestination dest = (FragmentDestination) annotation;
                pageUrl = dest.pageUrl();
                needLogin = dest.needLogin();
                asStarter = dest.asStarter();
                isFragment = true;
            } else if (annotation instanceof ActivityDestination) {
                ActivityDestination dest = (ActivityDestination) annotation;
                pageUrl = dest.pageUrl();
                needLogin = dest.needLogin();
                asStarter = dest.asStarter();
            }

            if (destMap.containsKey(pageUrl)) {
                messager.printMessage(Diagnostic.Kind.ERROR, "不同的页面不允许使用相同的pageUrl：" + clzName);
            } else {
                JSONObject object = new JSONObject();
                object.put("id", id);
                object.put("needLogin", needLogin);
                object.put("asStarter", asStarter);
                object.put("pageUrl", pageUrl);
                object.put("className", clzName);
                object.put("isFragment", isFragment);
                destMap.put(pageUrl, object);
            }
        }
    }
}
