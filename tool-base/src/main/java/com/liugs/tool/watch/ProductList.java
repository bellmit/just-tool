package com.liugs.tool.watch;

import com.liugs.tool.base.Console;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * @ClassName ProductList
 * @Description
 * @Author liugs
 * @Date 2021/7/11 17:04
 */
public class ProductList extends Observable {

    private List<String> productList = null;

    private static ProductList instance;

    private ProductList() {}

    /**
     * 获取唯一实例
     * @return
     */
    public static ProductList getInstance() {
        if (instance == null) {
            instance = new ProductList();
            instance.productList = new ArrayList<>();
        }
        return instance;
    }

    /**
     * 新增观察者
     * @param observer
     */
    public void addProductListObserver(Observer observer) {
        this.addObserver(observer);
    }

    /**
     * 添加产品
     * @param newProduct
     */
    public void addProduct(String newProduct) {
        productList.add(newProduct);
        Console.show("产品列表新增商品：" + newProduct);
        //设置被观察对象发生变化
        this.setChanged();
        //通知观察者，并传递信息
        this.notifyObservers(newProduct);
    }
}
