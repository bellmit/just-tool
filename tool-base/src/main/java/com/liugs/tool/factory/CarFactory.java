package com.liugs.tool.factory;

import com.liugs.tool.base.Console;

/**
 * @ClassName CarFactory
 * @Description
 * @Author liugs
 * @Date 2021/7/11 17:33
 */
public class CarFactory implements Factory{

    @Override
    public String getType() {
        return "CAR";
    }

    @Override
    public void create() {
        Console.show("create a car");
    }


}
