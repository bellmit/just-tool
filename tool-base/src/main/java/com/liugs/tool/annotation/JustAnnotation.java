package com.liugs.tool.annotation;

import java.lang.annotation.*;

/**
 * @author Liuner
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JustAnnotation {
    String value();

    String[] params() default {""};
}
