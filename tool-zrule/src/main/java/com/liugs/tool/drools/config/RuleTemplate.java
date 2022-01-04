package com.liugs.tool.drools.config;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName Rule
 * @Description 规则实体
 * @Author liugs
 * @Date 2021/8/30 15:20:12
 */
@Data
public class RuleTemplate implements Serializable {

    private static final long serialVersionUID = -3943334987859767502L;

    public RuleTemplate(String ruleString) {
        this.ruleString = ruleString;
    }

    private String ruleString;
}
