package com.liugs.tool.ability.bo;

import com.liugs.tool.constants.ToolRspBaseBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName DroolsRspBO
 * @Description 规则测试 出参
 * @Author liugs
 * @Date 2022/1/19 17:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DroolsRspBO extends ToolRspBaseBo {

    private static final long serialVersionUID = -5914369478986219491L;

    private String target;
}
