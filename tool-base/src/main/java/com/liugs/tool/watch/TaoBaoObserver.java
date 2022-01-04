package com.liugs.tool.watch;

import com.liugs.tool.base.Console;

import java.util.Observable;
import java.util.Observer;

/**
 * @ClassName TaoBaoObserver
 * @Description
 * @Author liugs
 * @Date 2021/7/11 17:15
 */
public class TaoBaoObserver implements Observer {

    @Override
    public void update(Observable o, Object arg) {
        String newProduct = (String)arg;

        Console.show("taobao接收到新产品信息：" + newProduct);
    }
}
