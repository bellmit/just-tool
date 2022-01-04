package com.liugs.tool.ability;

import com.liugs.tool.ability.bo.ExportDataServiceReqBo;
import com.liugs.tool.ability.bo.ExportDataServiceRspBo;

/**
 * @ClassName ExportDataService
 * @Description data export
 * @Author liugs
 * @Date 2021/2/27 18:04:23
 */
public interface ExportDataService {

    /**
     * 描述  data export
     * @param reqBo
     * @return com.liugs.tool.ability.bo.ExportDataServiceRspBo
     * @author liugs
     * @date 2021/2/27 18:07:56
     */
    ExportDataServiceRspBo exportData(ExportDataServiceReqBo reqBo);
}
