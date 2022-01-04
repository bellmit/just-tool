package test;

import com.liugs.tool.base.Console;

/**
 * @ClassName LoaderTest
 * @Description
 * @Author liugs
 * @Date 2021/9/3 15:50:55
 */
public class LoaderTest {

    public void hello() {
        Console.show("v1.0.0");
        Console.show("当前使用的类加载器是：" + getClass().getClassLoader());
    }

}
