package com.liugs.tool.ability.bo;

import com.liugs.tool.constants.ToolRspBaseBo;
import lombok.Data;

/**
 * @ClassName ValidateServiceRspBo
 * @Description 验证服务 出参
 * @Author liugs
 * @Date 2021/3/3 14:38:59
 */
@Data
public class ValidateServiceRspBo extends ToolRspBaseBo {

    private static final long serialVersionUID = 776534394037976463L;

    private String name;

    private int age;
}
