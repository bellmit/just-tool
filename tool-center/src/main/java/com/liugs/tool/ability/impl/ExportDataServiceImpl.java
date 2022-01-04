package com.liugs.tool.ability.impl;

import com.liugs.tool.ability.ExportDataService;
import com.liugs.tool.ability.bo.ExportDataServiceReqBo;
import com.liugs.tool.ability.bo.ExportDataServiceRspBo;
import com.liugs.tool.config.AsyncExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

/**
 * @ClassName ExprotDataServiceImpl
 * @Description export data service impl
 * @Author liugs
 * @Date 2021/2/27 18:09:13
 */
@Service("exportDataService")
@EnableAsync
public class ExportDataServiceImpl implements ExportDataService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AsyncExport asyncExport;

    @Override
    public ExportDataServiceRspBo exportData(ExportDataServiceReqBo reqBo) {
        ExportDataServiceRspBo retBo = new ExportDataServiceRspBo();
        asyncExport.asyncExport(reqBo);

        retBo.setRespDesc("SUCCESS");
        retBo.setRespCode("0000");
        return retBo;
    }
}
