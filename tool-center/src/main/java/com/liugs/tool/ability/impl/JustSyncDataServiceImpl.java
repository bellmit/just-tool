package com.liugs.tool.ability.impl;

import com.alibaba.fastjson.JSON;
import com.liugs.tool.ability.JustSyncDataService;
import com.liugs.tool.ability.bo.JustSyncDataServiceReqBo;
import com.liugs.tool.ability.bo.JustSyncDataServiceRspBo;
import com.liugs.tool.async.JustAsyncDataService;
import com.liugs.tool.util.ToolRspUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

/**
 * @ClassName JustSyncDataServiceImpl
 * @Description 数据同步实现类
 * @Author liugs
 * @Date 2021/6/15 15:15:56
 */
@Slf4j
@EnableAsync
@Service("justSyncDataService")
public class JustSyncDataServiceImpl implements JustSyncDataService {

    @Autowired
    private JustAsyncDataService justAsyncDataService;

    @Override
    public JustSyncDataServiceRspBo dealSync(JustSyncDataServiceReqBo reqBo) {
        if (log.isDebugEnabled()) {
            log.debug("=========data sync begin:{}", JSON.toJSONString(reqBo));
        }

        justAsyncDataService.async(reqBo);

        JustSyncDataServiceRspBo retBo = ToolRspUtil.getSuccessRspBo(JustSyncDataServiceRspBo.class);
        return retBo;
    }
}
