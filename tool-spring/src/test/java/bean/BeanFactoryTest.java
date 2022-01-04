package bean;

import com.liugs.tool.base.Console;
import com.liugs.tool.bean.MyFirstBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName BeanFactoryTest
 * @Description
 * @Author liugs
 * @Date 2021/8/11 14:42:52
 */
@SuppressWarnings("deprecation")
public class BeanFactoryTest {

    @Test
    public void start() {
        testSimpleLoad();

        testResource();
    }

    public void testSimpleLoad() {
        BeanFactory bf = new XmlBeanFactory(new ClassPathResource("MyFirstBean.xml"));
        MyFirstBean myFirstBean = (MyFirstBean) bf.getBean("myFirstBean");
        Console.show(myFirstBean.getTestStr());
    }

    public void testResource() {
        Resource resource = new ClassPathResource("MyFirstBean.xml");
        Console.show(resource.getFilename());
        try {
            InputStream inputStream = resource.getInputStream();
            byte[] bytes = new byte[1024];
            inputStream.read(bytes);
            Console.show(new String(bytes));
        } catch (IOException e) {
            Console.show(e.getMessage());
        }



    }
}
