package com.liugs.tool.ability.impl;

import com.alibaba.fastjson.JSON;
import com.liugs.tool.ability.DroolsService;
import com.liugs.tool.ability.bo.DroolsDataBO;
import com.liugs.tool.ability.bo.DroolsReqBO;
import com.liugs.tool.ability.bo.DroolsRspBO;
import com.liugs.tool.base.Console;
import com.liugs.tool.util.ToolRspUtil;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieBase;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @ClassName DroolsServiceImpl
 * @Description 规则测试
 * @Author liugs
 * @Date 2022/1/19 17:55
 */
@Slf4j
@Service("droolsService")
public class DroolsServiceImpl implements DroolsService {

    private KieBase kieBase;

    public DroolsServiceImpl(KieBase kieBase) {
        this.kieBase = kieBase;
    }

    @Override
    public DroolsRspBO trigger(DroolsReqBO reqBO) {
        log.debug("Ultraman trigger：balance type");
        DroolsRspBO retBO = ToolRspUtil.getSuccessRspBo(DroolsRspBO.class);
        DroolsDataBO dataBO = new DroolsDataBO();

        triggerVirgo(reqBO, dataBO);


        BeanUtils.copyProperties(reqBO, retBO);

        return retBO;
    }

    /**
     * 描述
     * @param objects
     * @return void
     * @author liugs
     * @date 2022/1/20 14:48
     */
    private void triggerVirgo(Object... objects) {
        KieSession kieSession = kieBase.newKieSession();

        kieSession.insert(objects[0]);
        kieSession.setGlobal("dataBO", objects[1]);

        kieSession.fireAllRules();

        for (Object obj : kieSession.getObjects(new ClassObjectFilter(objects[0].getClass()))) {
            if (100000 == ((DroolsReqBO) obj).getValue()) {
                Console.show("SUCCESS");
            }
        }
        log.info("Global对象：{}", JSON.toJSONString(objects[1]));
        kieSession.dispose();
    }


}
