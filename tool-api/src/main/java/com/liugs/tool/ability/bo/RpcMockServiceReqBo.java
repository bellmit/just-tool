package com.liugs.tool.ability.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName RpcMockServiceReqBo
 * @Description rpc调用入参
 * @Author liugs
 * @Date 2021/7/2 9:52:31
 */
@Data
public class RpcMockServiceReqBo implements Serializable {

    private static final long serialVersionUID = 6964489117432807188L;

    /**
     * 服务全类名
     */
    private String interClass;

    /**
     * 要调用的服务的方法名
     */
    private String method;

    /**
     * 服务请求对象
     */
    private Object data;
}
