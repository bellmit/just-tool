package com.liugs.tool.constants;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ToolBaseReqBo
 * @Description 基本入参
 * @Author liugs
 * @Date 2021/2/28 11:30:00
 */
@Data
public class ToolReqBaseBo implements Serializable {

    private static final long serialVersionUID = 3204410801791338574L;

    /**
     * 需要限制
     */
    private Integer needInterval;
}
