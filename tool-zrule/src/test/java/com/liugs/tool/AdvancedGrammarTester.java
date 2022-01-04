package com.liugs.tool;

import com.alibaba.fastjson.JSON;
import com.liugs.tool.base.Console;
import com.liugs.tool.drools.bo.AdvancedGrammarBo;
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName AdvancedGrammarTester
 * @Description 高级语法测试类
 * @Author liugs
 * @Date 2021/8/27 16:40:42
 */
public class AdvancedGrammarTester {
    @Test
    public void testAdvanceGrammar() {
        //设置时间格式，规则文件指定的生效时间格式和这里的一致
        System.setProperty("drools.dateformat","yyyy-MM-dd HH:mm");
        //Fact对象(就是普通的JAVA对象)
        AdvancedGrammarBo dataBo = new AdvancedGrammarBo();
        dataBo.setDesc("nihao");

        KieSession kieSession = KieUtil.getKieSession();
        kieSession.setGlobal("dataBo", new AdvancedGrammarBo());
        kieSession.setGlobal("count", 5);

        List list = new ArrayList();
        kieSession.setGlobal("globalList", list);

        //将条件FACT对象插入到工作内存
        kieSession.insert(dataBo);

        //调用规则文件中的查询
        QueryResults result = kieSession.getQueryResults("query_test_1");
        Console.show(result.size());
        if (result.size() > 0) {
            for (QueryResultsRow row : result) {
                AdvancedGrammarBo bo = (AdvancedGrammarBo) row.get("$model");
                Console.show(JSON.toJSONString(bo));
            }
        }

        //调用规则文件中的查询
        QueryResults result1 = kieSession.getQueryResults("query_test_2", "hello");
        Console.show(result1.size());
        if (result1.size() > 0) {
            for (QueryResultsRow row : result1) {
                AdvancedGrammarBo bo = (AdvancedGrammarBo) row.get("$model");
                Console.show(JSON.toJSONString(bo));
            }
        }


        //激活规则，有Drools框架自动进行规则匹配
        kieSession.fireAllRules();

        //关闭会话
        kieSession.dispose();
    }
}