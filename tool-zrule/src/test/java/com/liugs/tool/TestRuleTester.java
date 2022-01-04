package com.liugs.tool;

import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.drools.core.io.impl.UrlResource;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName TestRuleTester
 * @Description
 * @Author liugs
 * @Date 2021/9/9 14:32:32
 */
public class TestRuleTester {

    @Test
    public void testRule() throws IOException {
        String url = "https://cqdev-pay-center.oss-cn-beijing.aliyuncs.com/tool-api-1.0.0.jar?OSSAccessKeyId=&Expires=1631180524&Signature=naxbczgwtyNfYDWcOaH7Qv4FEmw%3D";

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

        RuleTestOrderBo ruleTestOrderBo = new RuleTestOrderBo();
        ruleTestOrderBo.setFee(111L);
        session.insert(ruleTestOrderBo);
        session.fireAllRules();
        session.dispose();
    }

    @Test
    public void testRuleLocal() {
        //设置时间格式，规则文件指定的生效时间格式和这里的一致
        System.setProperty("drools.dateformat","yyyy-MM-dd HH:mm");

        //Fact对象(就是普通的JAVA对象)
        RuleTestOrderBo dataBo = new RuleTestOrderBo();
        dataBo.setFee(88L);

        KieSession kieSession = KieUtil.getKieSession();

        //将条件FACT对象插入到工作内存
        kieSession.insert(dataBo);

        //给指定的协议分组指定焦点
//        kieSession.getAgenda().getAgendaGroup("agenda_group_1").setFocus();

        //激活规则，有Drools框架自动进行规则匹配
        kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("test_rule_1"));

        //关闭会话
        kieSession.dispose();
    }
}
