package com.liugs.tool.bean.subelement;

import com.liugs.tool.base.Console;
import org.springframework.beans.factory.support.MethodReplacer;

import java.lang.reflect.Method;

/**
 * @ClassName StudentShowMeReplacer
 * @Description
 * @Author liugs
 * @Date 2021/8/20 15:56:01
 */
public class StudentShowMeReplacer implements MethodReplacer {

    @Override
    public Object reimplement(Object obj, Method method, Object[] args) throws Throwable {
        Console.show("i replaced student`s showme() method");
        return null;
    }
}
