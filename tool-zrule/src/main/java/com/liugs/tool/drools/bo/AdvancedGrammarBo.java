package com.liugs.tool.drools.bo;

import com.liugs.tool.base.Console;
import lombok.Data;

/**
 * @ClassName AdvancedGrammarBo
 * @Description 高级语法测试BO
 * @Author liugs
 * @Date 2021/8/27 16:42:35
 */
@Data
public class AdvancedGrammarBo {
    private String desc;

    public void show() {
        Console.show("AdvancedGrammarBo.show(): " + this.desc);
    }
}
