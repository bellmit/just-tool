package com.liugs.tool.interceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @ClassName InterceptorJdkProxy
 * @Description jdk动态代理使用拦截器
 * @Author liugs
 * @Date 2021/7/6 22:51
 */
public class InterceptorJdkProxy implements InvocationHandler {

    /** 被代理对象 */
    private Object target;
    /** 拦截器全限定名 */
    private String interceptorClass;

    public InterceptorJdkProxy(Object target, String interceptorClass) {
        this.target = target;
        this.interceptorClass = interceptorClass;
    }

    /**
     * 描述 绑定委托对象并返回一个代理占位
     * @param  target, interceptorClass
     * @return java.lang.Object
     * @author liugs
     * @date 2021/7/6 22:55
     */
    public static Object bind(Object target, String interceptorClass) {
        //取得代理对象
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(),
                new InterceptorJdkProxy(target, interceptorClass));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //没有拦截器直接反射原有方法
        if (interceptorClass == null) {
            return method.invoke(target, args);
        }

        Object result = null;
        //通过反射生成拦截器
        Interceptor interceptor = (Interceptor) Class.forName(interceptorClass).newInstance();
        //调用前置方法
        if (interceptor.before(proxy, target, method, args)) {
            //反射原有对象方法
            result = method.invoke(target, args);
        } else {
            //执行around方法
            interceptor.around(proxy, target, method, args);
        }
        //调用后置方法
        interceptor.after(proxy, target, method, args);
        return result;
    }
}
