package com.liugs.tool;

import com.liugs.tool.base.Console;
import com.liugs.tool.drools.bo.Order;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

/**
 * @ClassName BoolDiscountTester
 * @Description 图书折扣规则测试
 * @Author liugs
 * @Date 2021/8/23 16:11:35
 */
public class BookDiscountTester {

    @Test
   public void testBookDiscount() {
        KieServices kieServices = KieServices.Factory.get();
        //获取Kie容器
        KieContainer kieContainer = kieServices.newKieClasspathContainer();
        //从Kie容器获取Kiesession
        KieSession kieSession = kieContainer.newKieSession();

        //Fact对象(就是普通的JAVA对象)
        Order order = new Order();
        Order order2 = new Order();
        order.setOrgPrice(200d);

        //将之插入到工作内存
        kieSession.insert(order);
        kieSession.insert(order2);

        //激活规则，有Drools框架自动进行规则匹配
        kieSession.fireAllRules();

        //关闭会话
        kieSession.dispose();

        Console.show("匹配后的到的价格：" + order.getRealPrice());
   }
}

