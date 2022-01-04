package com.liugs.tool.interceptor;

import com.liugs.tool.base.Console;

import java.lang.reflect.Method;

/**
 * @ClassName InterceptorImpl
 * @Description 拦截器实现类
 * @Author liugs
 * @Date 2021/7/6 22:46
 */
public class InterceptorImpl implements Interceptor{

    @Override
    public boolean before(Object proxy, Object target, Method method, Object[] args) {
        Console.show("反射方法前逻辑");
        //不发射被代理对象原有方法
        return false;
    }

    @Override
    public void around(Object proxy, Object target, Method method, Object[] args) {
        Console.show("取代了被代理对象原有方法");
    }

    @Override
    public void after(Object proxy, Object target, Method method, Object[] args) {
        Console.show("方法方法后逻辑");
    }
}
