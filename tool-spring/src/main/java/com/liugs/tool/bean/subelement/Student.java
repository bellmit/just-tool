package com.liugs.tool.bean.subelement;

import com.liugs.tool.base.Console;

/**
 * @ClassName Student
 * @Description
 * @Author liugs
 * @Date 2021/8/20 15:29:53
 */
public class Student extends User {

    public Student(String name) {
        super(name);
    }

    @Override
    public void showMe() {
        Console.show("i`m a student, my name is " + name);
    }
}
