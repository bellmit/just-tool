package com.liugs.tool.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName FactoryManager
 * @Description
 * @Author liugs
 * @Date 2021/7/11 17:30
 */
public class FactoryManager {

    private static Map<String, Factory> factoryMap = new HashMap<>(1);

    public void register(Factory factory) {
        factoryMap.put(factory.getType(), factory);
    }

    public Factory getFactory(String type) {
        return factoryMap.get(type);
    }

    public FactoryManager() {
        CarFactory car = new CarFactory();
        PhoneFactory phone = new PhoneFactory();
        register(car);
        register(phone);
    }
}
