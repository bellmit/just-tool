package com.liugs.tool.bean.subelement;

import com.liugs.tool.base.Console;

/**
 * @ClassName User
 * @Description
 * @Author liugs
 * @Date 2021/8/20 15:27:17
 */
public class User {

    protected String name;

    public User(String name) {
        this.name = name;
    }

    public void showMe() {
        Console.show("i`m user");
    }

}
