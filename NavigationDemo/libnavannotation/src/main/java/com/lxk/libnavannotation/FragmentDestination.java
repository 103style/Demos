package com.lxk.libnavannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * @author https://github.com/103style
 * @date 2020/10/20 16:20
 */
@Target(ElementType.TYPE)
public @interface FragmentDestination {

    String pageUrl();

    boolean needLogin() default false;

    boolean asStarter() default false;
}