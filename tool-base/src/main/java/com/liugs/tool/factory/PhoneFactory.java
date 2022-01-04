package com.liugs.tool.factory;

import com.liugs.tool.base.Console;

/**
 * @ClassName PhoneFactory
 * @Description
 * @Author liugs
 * @Date 2021/7/11 17:35
 */
public class PhoneFactory implements Factory{

    @Override
    public String getType() {
        return "PHONE";
    }

    @Override
    public void create() {
        Console.show("create phone");
    }
}
