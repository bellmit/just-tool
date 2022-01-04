package com.liugs.tool.interceptor;

import com.liugs.tool.proxy.HelloWord;
import com.liugs.tool.proxy.HelloWordInterface;

/**
 * @ClassName InterceptorTester
 * @Description
 * @Author liugs
 * @Date 2021.7.6 23:01
 */
public class InterceptorTester {

    public static void main(String[] args) {
        HelloWordInterface proxy = (HelloWordInterface) InterceptorJdkProxy.bind(new HelloWord(),
                "com.liugs.tool.interceptor.InterceptorImpl");
        proxy.sayHello();
    }
}
