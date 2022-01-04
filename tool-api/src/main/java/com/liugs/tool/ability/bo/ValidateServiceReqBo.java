package com.liugs.tool.ability.bo;


import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ValidateServiceReqBo
 * @Description 验证服务 入参
 * @Author liugs
 * @Date 2021/3/3 14:33:00
 */
@Data
public class ValidateServiceReqBo implements Serializable {

    private static final long serialVersionUID = -5227744237371257254L;

    private String name;

    private Integer age;

    private Long fee;
}
