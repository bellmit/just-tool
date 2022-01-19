package com.liugs.tool.ability.bo;

import com.liugs.tool.constants.ToolReqBaseBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName DroolsReqBO
 * @Description 规则测试 入参
 * @Author liugs
 * @Date 2022/1/19 17:53
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DroolsReqBO extends ToolReqBaseBo {

    private static final long serialVersionUID = -7243192598999595633L;

    private Integer value;

    private String target;
}
