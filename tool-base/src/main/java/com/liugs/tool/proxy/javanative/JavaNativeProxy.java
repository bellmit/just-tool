package com.liugs.tool.proxy.javanative;

import com.liugs.tool.base.Console;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @ClassName java
 * @Description java原生代理
 * @Author liugs
 * @Date 2021/7/6 22:25
 */
public class JavaNativeProxy implements InvocationHandler {

    //真实对象
    private Object target = null;

    /**
     * 描述 建立代理对象和真实对象的代理关系，并返回代理对象
     * @param target
     * @return java.lang.Object
     * @author liugs
     * @date 2021/7/6 22:28
     */
    public Object bind(Object target) {
        this.target = target;
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }

    /**
     * 描述
     * @param proxy                 代理对象
     * @param method                当前调度方法
     * @param args                  当前方法参数
     * @return java.lang.Object     代理结果返回
     * @author liugs
     * @date 2021/7/6 22:34
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Console.show("进入代理方法");
        Object obj = method.invoke(target, args);
        Console.show("调用真实对象后");
        return obj;
    }
}
