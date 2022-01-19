package com.liugs.tool.ability;

import com.liugs.tool.ability.bo.DroolsReqBO;
import com.liugs.tool.ability.bo.DroolsRspBO;

/**
 * @ClassName DroolsService
 * @Description 规则测试
 * @Author liugs
 * @Date 2022/1/19 17:54
 */
public interface DroolsService {

    /**
     * 描述 触发
     * @param reqBO
     * @return com.liugs.tool.ability.bo.DroolsRspBO
     * @author liugs
     * @date 2022/1/19 17:55
     */
    DroolsRspBO trigger(DroolsReqBO reqBO);
}
