package com.liugs.tool.proxy.ccglib;

import com.liugs.tool.base.Console;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @ClassName cglibExample
 * @Description CGLIB代理
 * @Author liugs
 * @Date 2021/7/6 13:49:32
 */
public class CglibProxyExample implements MethodInterceptor {

    /**
     * 描述 获取代理对象
     * @param cls
     * @return java.lang.Object
     * @author liugs
     * @date 2021/7/6 13:51:10
     */
    public Object getProxy(Class cls) {
        //CGLIB enhancer 增强对象
        Enhancer enhancer = new Enhancer();
        //设置增强类型
        enhancer.setSuperclass(cls);
        //定义代理逻辑对象为当前对象，要求当前对象实现MethodInterceptor方法
        enhancer.setCallback(this);
        //生产并返回代理对象
        return enhancer.create();
    }


    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Console.show("调用真实方法前");
        Object result = methodProxy.invokeSuper(obj,args);
        Console.show("调用真实方法后");
        return result;
    }
}
