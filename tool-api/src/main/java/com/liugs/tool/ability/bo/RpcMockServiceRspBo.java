package com.liugs.tool.ability.bo;

import com.liugs.tool.constants.ToolRspBaseBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName RpcMockServiceRspBo
 * @Description rpc调用出参
 * @Author liugs
 * @Date 2021/7/2 9:53:08
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RpcMockServiceRspBo extends ToolRspBaseBo {

    private static final long serialVersionUID = -3197326699594501457L;

    /**
     * 编码，0成功，其它失败
     */
    private String code;

    /**
     * 成功或失败信息描述
     */
    private String message;

    /**
     * 对象json串
     */
    private String data;
}
