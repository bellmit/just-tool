package com.liugs.tool;

import com.space.project.Person;
import org.drools.core.io.impl.UrlResource;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.io.InputStream;

/**
 * @ClassName PersonTest
 * @Description
 * @Author liugs
 * @Date 2021/9/6 16:14:16
 */
public class PersonTest {

    @Test
    public void test1() throws Exception{

        String url = "https://cqdev-pay-center.oss-cn-beijing.aliyuncs.com/project-1.0.0.jar?OSSAccessKeyId=&Expires=1631092021&Signature=wcs0WpBTcv6W3MX%2BE0OYUqU%2FOBI%3D";

        KieServices kieServices = KieServices.Factory.get();

        //通过Resource资源对象加载jar包
        UrlResource resource = (UrlResource) kieServices.getResources().newUrlResource(url);

        //将资源转换为输入流，通过此输入流可以读取jar包数据
        InputStream inputStream = resource.getInputStream();

        //创建仓库对象，仓库对象中保存Drools的规则信息
        KieRepository repository = kieServices.getRepository();

        //通过输入流读取maven仓库中的jar包数据，包装成KieModule模块添加到仓库中
        KieModule kieModule = repository.addKieModule(kieServices.getResources().newInputStreamResource(inputStream));

        //基于KieModule模块创建容器对象，从容器中可以获取session会话
        KieContainer kieContainer = kieServices.newKieContainer(kieModule.getReleaseId());
        KieSession session = kieContainer.newKieSession();

        Person person = new Person();
        person.setOrgPrice(10L);
        session.insert(person);

        session.fireAllRules();
        session.dispose();
    }
}
