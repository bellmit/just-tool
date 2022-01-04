package com.liugs.tool.bean.subelement;

/**
 * @ClassName GetBeanTest
 * @Description
 * @Author liugs
 * @Date 2021/8/20 15:30:54
 */
public abstract class GetBeanTest {

    public void showMe() {
        this.getBean().showMe();
    }

    protected abstract User getBean();
}
