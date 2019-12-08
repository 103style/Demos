package com.lxk.compiler;

import com.google.auto.service.AutoService;
import com.lxk.annotation.RvAdapter;

import java.util.Iterator;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

/**
 * @author https://github.com/103style
 * @date 2019/12/7 16:30
 */
@AutoService(RvAdapter.class)
public class AnnotationProcessor extends AbstractProcessor {

    private Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        LogUtils.init(processingEnvironment.getMessager());
        mFiler = processingEnvironment.getFiler();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        LogUtils.i("process: start");
        Set<RvAdapter> elements = (Set<RvAdapter>) roundEnvironment.getElementsAnnotatedWith(RvAdapter.class);
        if (elements == null || elements.size() == 0) {
            return false;
        }

        Iterator<RvAdapter> iterator = elements.iterator();
        while (iterator.hasNext()) {
            create(iterator.next());
        }
        return false;
    }

    private void create(RvAdapter next) {

    }
}
