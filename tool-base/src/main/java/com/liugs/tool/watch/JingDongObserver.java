package com.liugs.tool.watch;

import com.liugs.tool.base.Console;

import java.util.Observable;
import java.util.Observer;

/**
 * @ClassName JingDongObserver
 * @Description
 * @Author liugs
 * @Date 2021/7/11 17:13
 */
public class JingDongObserver implements Observer {


    @Override
    public void update(Observable o, Object arg) {
        String newProduct = (String)arg;
        Console.show("Jingdong接受到新产品信息：" + newProduct);
    }
}
