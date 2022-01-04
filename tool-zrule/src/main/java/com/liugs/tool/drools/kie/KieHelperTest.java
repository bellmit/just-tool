package com.liugs.tool.drools.kie;

import com.liugs.tool.drools.bo.Order;
import com.liugs.tool.drools.config.KieBaseTemplate;
import org.kie.api.KieBase;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName KieHelperTest
 * @Description
 * @Author liugs
 * @Date 2021/8/30 17:11:49
 */
public class KieHelperTest {

    @Autowired
    private KieBaseTemplate kieBaseTemplate;
    @Autowired
    private KieHelper kieHelper;

    public void kieHelperTest() {

        String newRuleContent = "";

        kieHelper.addContent(newRuleContent, ResourceType.DRL);

        KieBase kieBase = kieHelper.build();

        kieBaseTemplate.updateKieBase(kieBase);

        KieSession kieSession = kieBaseTemplate.getKieBase().newKieSession();

        Order order = new Order();
        order.setOrgPrice(34d);

        kieSession.insert(order);

        kieSession.fireAllRules();

        kieSession.dispose();
    }
}
