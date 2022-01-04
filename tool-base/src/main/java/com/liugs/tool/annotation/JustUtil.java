package com.liugs.tool.annotation;

import com.liugs.tool.base.Console;

import java.util.Arrays;

/**
 * @ClassName JustUtil
 * @Description
 * @Author liugs
 * @Date 2021/7/16 17:07:22
 */
public class JustUtil {

    public static void main(String[] args) {

        Class<JustAnnotationTest> annotationClass = JustAnnotationTest.class;
        boolean annotationPresent;
        Console.show(annotationPresent = annotationClass.isAnnotationPresent(JustAnnotation.class));
        if (annotationPresent) {
            JustAnnotation justAnnotation = annotationClass.getAnnotation(JustAnnotation.class);
            Console.show(justAnnotation.value());
            Console.show(Arrays.asList(justAnnotation.params()));
        }

    }
}
