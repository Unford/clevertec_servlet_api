package ru.clevertec.course.web.controller.filter;

import javax.servlet.DispatcherType;
import javax.servlet.annotation.WebInitParam;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OrderedWebFilter {
    WebInitParam[] initParams() default {};


    String filterName() default "";

    String[] servletNames() default {};


    String[] value() default {};


    String[] urlPatterns() default {};


    DispatcherType[] dispatcherTypes() default {DispatcherType.REQUEST};


    boolean asyncSupported() default false;

    int order() default Integer.MAX_VALUE;
}
