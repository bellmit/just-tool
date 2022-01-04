package bean;

import com.liugs.tool.bean.subelement.GetBeanTest;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @ClassName Main
 * @Description
 * @Author liugs
 * @Date 2021/8/20 15:32:50
 */
public class Main {

    @Test
    public void test() {
        lookupMethodTest();
    }

    public void lookupMethodTest() {
        ApplicationContext factory = new ClassPathXmlApplicationContext("Sublement.xml");
        GetBeanTest getBeanTest = (GetBeanTest) factory.getBean("getBeanTest");
        getBeanTest.showMe();
    }
}
