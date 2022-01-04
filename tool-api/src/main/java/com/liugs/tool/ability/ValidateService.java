package com.liugs.tool.ability;

import com.liugs.tool.ability.bo.ValidateServiceReqBo;
import com.liugs.tool.ability.bo.ValidateServiceRspBo;

/**
 * @ClassName ValidateService
 * @Description 验证服务
 * @Author liugs
 * @Date 2021/3/3 14:39:53
 */
public interface ValidateService {

    /**
     * 描述 验证服务
     * @param reqBo
     * @return com.liugs.tool.ability.bo.ValidateServiceRspBo
     * @author liugs
     * @date 2021/3/3 14:40:13
     */
    ValidateServiceRspBo validate(ValidateServiceReqBo reqBo);



    ValidateServiceRspBo validateRule(ValidateServiceReqBo reqBo);
}
