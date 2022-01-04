package com.liugs.tool.ability.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName JustSyncDataServiceReqBo
 * @Description 数据同步入参
 * @Author liugs
 * @Date 2021/6/15 15:12:05
 */
@Data
public class JustSyncDataServiceReqBo implements Serializable {

    private static final long serialVersionUID = -9022740213903952409L;

    /**
     * 商户ID
     */
    private String merchantId;
    /**
     * 是否启用多线程
     */
    private Boolean isMultiThread;
}
