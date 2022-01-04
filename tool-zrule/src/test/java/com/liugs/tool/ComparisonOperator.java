package com.liugs.tool;

import com.liugs.tool.drools.bo.ComparisonOperatorBo;
import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.Arrays;

/**
 * @ClassName ComparisonOperator
 * @Description 比较操作符测试
 * @Author liugs
 * @Date 2021/8/27 10:25:33
 */
public class ComparisonOperator {
    @Test
    public void testBookDiscount() {
        KieServices kieServices = KieServices.Factory.get();
        //获取Kie容器
        KieContainer kieContainer = kieServices.newKieClasspathContainer();
        //从Kie容器获取Kiesession
        KieSession kieSession = kieContainer.newKieSession();

        //Fact对象(就是普通的JAVA对象)
        ComparisonOperatorBo operatorBo = new ComparisonOperatorBo();
        String[] names = {"9966110", "232", "2323", "123a"};

        operatorBo.setName("23");
        operatorBo.setList(Arrays.asList(names));

        //将条件FACT对象插入到工作内存
        kieSession.insert(operatorBo);

        //通过规则过滤器实现只执行指定规则
        kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("comparison_operator_not_matches"));

        //关闭会话
        kieSession.dispose();
    }
}
