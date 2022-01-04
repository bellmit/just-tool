package com.liugs.tool.proxy;

import com.liugs.tool.proxy.HelloWord;
import com.liugs.tool.proxy.ccglib.CglibProxyExample;
import com.liugs.tool.proxy.javanative.JavaNativeProxy;

/**
 * @ClassName CglibTest
 * @Description CGLIB test
 * @Author liugs
 * @Date 2021/7/6 13:57:28
 */
public class ProxyTester {
    public static void main(String[] args) {
        testCglib();

        testNative();
    }
    /**
     * 描述 CGLIB代理测试
     * @return void
     * @author liugs
     * @date 2021/7/6 22:27
     */
    public static void testCglib() {
        CglibProxyExample cglibExample = new CglibProxyExample();
        //绑定关系
        HelloWord helloWord = (HelloWord) cglibExample.getProxy(HelloWord.class);
        helloWord.syHello();
    }

    /**
     * 描述 jdk动态代理
     * @return void
     * @author liugs
     * @date 2021/7/6 22:37
     */
    public static void testNative() {
        JavaNativeProxy javaNativeProxy = new JavaNativeProxy();
        HelloWordInterface proxy = (HelloWordInterface) javaNativeProxy.bind(new HelloWord());
        proxy.sayHello();
    }
}
