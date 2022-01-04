package com.liugs.tool;

import com.liugs.tool.drools.bo.RuleAttributeBo;
import org.junit.Test;
import org.kie.api.runtime.KieSession;

/**
 * @ClassName RuleAttributeTeset
 * @Description 规则属性测试
 * @Author liugs
 * @Date 2021/8/27 14:53:51
 */
public class RuleAttributeTester {

    @Test
    public void testAttribute() {
        //设置时间格式，规则文件指定的生效时间格式和这里的一致
        System.setProperty("drools.dateformat","yyyy-MM-dd HH:mm");

        //Fact对象(就是普通的JAVA对象)
        RuleAttributeBo attributeBo = new RuleAttributeBo();
        attributeBo.setName("eee");
        attributeBo.setAge(0);

        KieSession kieSession = KieUtil.getKieSession();

        //将条件FACT对象插入到工作内存
        kieSession.insert(attributeBo);

        //给指定的协议分组指定焦点
//        kieSession.getAgenda().getAgendaGroup("agenda_group_1").setFocus();

        //激活规则，有Drools框架自动进行规则匹配
        kieSession.fireAllRules();

        //关闭会话
        kieSession.dispose();

    }

}
