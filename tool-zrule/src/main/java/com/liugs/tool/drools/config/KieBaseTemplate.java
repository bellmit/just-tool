package com.liugs.tool.drools.config;

import lombok.Data;
import org.kie.api.KieBase;

/**
 * @ClassName KieBaseTemplate
 * @Description
 * @Author liugs
 * @Date 2021/8/30 17:08:08
 */
@Data
public class KieBaseTemplate {

    private KieBase kieBase;

    public KieBaseTemplate(KieBase kieBase) {
        this.kieBase = kieBase;
    }

    public void updateKieBase(KieBase kieBase) {
        this.kieBase = kieBase;
    }
}
