package com.liugs.tool.watch;

/**
 * @ClassName WatchTester
 * @Description
 * @Author liugs
 * @Date 2021/7/11 17:16
 */
public class WatchTester {

    public static void main(String[] args) {
        ProductList product = ProductList.getInstance();
        JingDongObserver jingdong = new JingDongObserver();
        TaoBaoObserver taobao = new TaoBaoObserver();

        product.addProductListObserver(jingdong);
        product.addProductListObserver(taobao);

        product.addProduct("暗影精灵7");
        product.addProduct("幻16");
    }
}
