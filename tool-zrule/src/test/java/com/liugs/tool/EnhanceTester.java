package com.liugs.tool;

import com.liugs.tool.drools.bo.EnhanceTestBo;
import org.junit.Test;
import org.kie.api.runtime.KieSession;

/**
 * @ClassName EnhanceTester
 * @Description
 * @Author liugs
 * @Date 2021/8/30 10:33:17
 */
public class EnhanceTester {
    @Test
    public void test() {
        //设置时间格式，规则文件指定的生效时间格式和这里的一致
        System.setProperty("drools.dateformat","yyyy-MM-dd HH:mm");

        KieSession kieSession = KieUtil.getKieSession();

        EnhanceTestBo testBo = new EnhanceTestBo();
        testBo.setTestField("1");

        kieSession.insert(testBo);

        kieSession.fireAllRules();

        kieSession.dispose();
    }
}
