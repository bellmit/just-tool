package com.liugs.tool;

import com.alibaba.fastjson.JSON;
import com.liugs.tool.base.Console;
import com.liugs.tool.constants.ToolRspBaseBo;
import com.liugs.tool.drools.bo.InnerMethodBo;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

/**
 * @ClassName InnerMethodTester
 * @Description 内置方法测试
 * @Author liugs
 * @Date 2021/8/27 12:06:31
 */
public class InnerMethodTester {

    @Test
    public void run() {
        KieServices kieServices = KieServices.Factory.get();
        //获取Kie容器
        KieContainer kieContainer = kieServices.newKieClasspathContainer();
        //从Kie容器获取Kiesession
        KieSession kieSession = kieContainer.newKieSession();

        //Fact对象(就是普通的JAVA对象)
        InnerMethodBo innerMethodBo = new InnerMethodBo();
        innerMethodBo.setAge(9);
        innerMethodBo.setScore(79);

        ToolRspBaseBo retBo = new ToolRspBaseBo();

        //将条件FACT对象插入到工作内存
        kieSession.insert(innerMethodBo);
        kieSession.insert(retBo);

        //通过规则过滤器实现只执行指定规则
        kieSession.fireAllRules();

        //关闭会话
        kieSession.dispose();

        Console.show(JSON.toJSONString(retBo));
    }

}
