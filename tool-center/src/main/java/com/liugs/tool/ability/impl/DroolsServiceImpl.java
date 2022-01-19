package com.liugs.tool.ability.impl;

import com.liugs.tool.ability.DroolsService;
import com.liugs.tool.ability.bo.DroolsReqBO;
import com.liugs.tool.ability.bo.DroolsRspBO;
import com.liugs.tool.util.ToolRspUtil;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @ClassName DroolsServiceImpl
 * @Description 规则测试
 * @Author liugs
 * @Date 2022/1/19 17:55
 */
@Service("droolsService")
public class DroolsServiceImpl implements DroolsService {

    private KieBase kieBase;

    public DroolsServiceImpl(KieBase kieBase) {
        this.kieBase = kieBase;
    }

    @Override
    public DroolsRspBO trigger(DroolsReqBO reqBO) {
        DroolsRspBO retBO = ToolRspUtil.getSuccessRspBo(DroolsRspBO.class);
        KieSession kieSession = kieBase.newKieSession();
        kieSession.insert(reqBO);
        kieSession.fireAllRules();
        kieSession.dispose();

        BeanUtils.copyProperties(reqBO, retBO);
        return retBO;
    }
}
