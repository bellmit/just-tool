package com.liugs.tool.aspect;

import java.lang.annotation.*;

/**
 * @ClassName Interval
 * @Description 间隔控制
 * @Author liugs
 * @Date 2021/12/8 14:34
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface Interval {

}