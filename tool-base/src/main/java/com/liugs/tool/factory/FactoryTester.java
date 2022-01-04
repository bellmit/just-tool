package com.liugs.tool.factory;

/**
 * @ClassName FactoryTester
 * @Description
 * @Author liugs
 * @Date 2021/7/11 17:36
 */
public class FactoryTester {
    public static void main(String[] args) {
        FactoryManager manager = new FactoryManager();
        Factory factory = manager.getFactory("CAR");
        factory.create();

        factory = manager.getFactory("PHONE");
        factory.create();
    }
}
