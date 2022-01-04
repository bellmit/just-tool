package com.liugs.tool.drools.config;

import org.kie.api.KieBase;
import org.kie.api.io.ResourceType;
import org.kie.internal.utils.KieHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;

/**
 * @ClassName KieBaseConfig
 * @Description
 * @Author liugs
 * @Date 2021/8/30 17:04:49
 */
public class KieBaseConfig {

    @Autowired
    private KieHelper kieHelper;
    @Autowired
    private RuleTemplate ruleDataBo;

    @Bean
    @PostConstruct
    public KieBaseTemplate kieBase() {
        kieHelper.addContent(ruleDataBo.getRuleString(), ResourceType.DRL);
        //编译
        KieBase kieBase = kieHelper.build();

        return new KieBaseTemplate(kieBase);
    }
}
