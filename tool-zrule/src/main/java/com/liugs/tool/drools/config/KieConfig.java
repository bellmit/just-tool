package com.liugs.tool.drools.config;

import org.kie.internal.utils.KieHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName KieConfig
 * @Description kie配置
 * @Author liugs
 * @Date 2021/8/30 15:16:07
 */
@Configuration
public class KieConfig {

    @Bean
    public KieHelper kieHelper() {
        return new KieHelper();
    }

    @Bean
    public RuleTemplate ruleDataBo() {
        //
        RuleTemplate ruleDataBo = new RuleTemplate("");
        return ruleDataBo;
    }
}
