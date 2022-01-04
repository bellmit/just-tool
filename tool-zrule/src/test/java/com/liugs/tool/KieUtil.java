package com.liugs.tool;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

/**
 * @ClassName KieUtil
 * @Description
 * @Author liugs
 * @Date 2021/8/27 14:58:46
 */
public class KieUtil {

    private static KieSession kieSession;

    public static KieSession getKieSession() {
        if (kieSession == null) {
            KieServices kieServices = KieServices.Factory.get();
            //获取Kie容器
            KieContainer kieContainer = kieServices.newKieClasspathContainer();
            //从Kie容器获取Kiesession
            kieSession = kieContainer.newKieSession();
        }
        return kieSession;
    }


}
