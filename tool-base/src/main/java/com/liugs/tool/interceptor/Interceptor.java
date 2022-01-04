package com.liugs.tool.interceptor;

import java.lang.reflect.Method;

/**
 * @ClassName Interceptor
 * @Description 拦截器
 * @Author liugs
 * @Date 2021/7/6 22:43
 */
public interface Interceptor {
    boolean before(Object proxy, Object target, Method method, Object[] args);

    void around(Object proxy, Object target, Method method, Object[] args);

    void after(Object proxy, Object target, Method method, Object[] args);


}
