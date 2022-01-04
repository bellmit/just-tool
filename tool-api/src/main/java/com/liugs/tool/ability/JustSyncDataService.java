package com.liugs.tool.ability;

import com.liugs.tool.ability.bo.JustSyncDataServiceReqBo;
import com.liugs.tool.ability.bo.JustSyncDataServiceRspBo;

/**
 * @ClassName JustSyncDataService
 * @Description 数据同步
 * @Author liugs
 * @Date 2021/6/15 15:15:03
 */
public interface JustSyncDataService {


    /**
     * 描述 处理数据同步
     * @param reqBo
     * @return com.liugs.tool.ability.bo.JustSyncDataServiceRspBo
     * @author liugs
     * @date 2021/6/15 15:15:34
     */
    JustSyncDataServiceRspBo dealSync(JustSyncDataServiceReqBo reqBo);
}
