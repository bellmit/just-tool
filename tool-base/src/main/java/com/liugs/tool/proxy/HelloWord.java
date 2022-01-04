package com.liugs.tool.proxy;

import com.liugs.tool.base.Console;

/**
 * @ClassName HelloWord
 * @Description 你好 世界
 * @Author liugs
 * @Date 2021/7/6 13:48:24
 */
public class HelloWord implements HelloWordInterface{

    public void syHello() {
        Console.show("hello word");
    }

    @Override
    public void sayHello() {
        Console.show("Say hello!!!");
    }
}
