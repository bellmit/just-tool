package com.liugs.tool;

import com.liugs.tool.base.Console;
import com.liugs.tool.constants.RespConstants;
import com.liugs.tool.drools.bo.Order;
import com.liugs.tool.exception.ToolBusinessException;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.junit.Test;
import org.kie.api.definition.KiePackage;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;

import java.nio.charset.StandardCharsets;
import java.util.Collection;

/**
 * @ClassName LoadRuleFromString
 * @Description 从字符串动态加载规则
 * @Author liugs
 * @Date 2021/8/30 14:14:19
 */
public class LoadRuleFromString {

    private static KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

    private static Collection<KiePackage> kiePackages;

    private static InternalKnowledgeBase internalKBase = KnowledgeBaseFactory.newKnowledgeBase();

    private static KieSession kieSession;

    @Test
    public void loadRuleFromString() {
        //初始化
        initDrools();
        //初始化fact对象
        initFactObject();
        //触发规则
        fireRules();
    }

    /**
     * 描述 初始化 Drools
     * @param
     * @return void
     * @author liugs
     * @date 2021/8/30 14:24:53
     */
    private void initDrools() {
        String myRule = "package rules;\n" +
                "import com.liugs.tool.drools.bo.Order\n" +
                "import com.liugs.tool.base.Console\n" +
                "\n" +
                "rule \"book_discount_1\"\n" +
                "    when\n" +
                "        $order:Order(orgPrice < 100)\n" +
                "    then\n" +
                "        $order.setRealPrice($order.getOrgPrice());\n" +
                "        Console.show(\"成功匹配到规则一：真实价格：\" + $order.getRealPrice());\n" +
                "end \n" +
                "rule \"book_discount_2\"\n" +
                "    when\n" +
                "        $order:Order(100 <= orgPrice && orgPrice < 200)\n" +
                "    then\n" +
                "        $order.setRealPrice($order.getOrgPrice() - 20);\n" +
                "        Console.show(\"成功匹配到规则二：真实价格：\" + $order.getRealPrice());\n" +
                "end";
        //从字符串加载规则
        Resource kieResource = ResourceFactory.newByteArrayResource(myRule.getBytes(StandardCharsets.UTF_8));

        //将资源加入到builder并编译
        knowledgeBuilder.add(kieResource, ResourceType.DRL);

        //检查生成器是否有错误
        if (knowledgeBuilder.hasErrors()) {
            Console.show(knowledgeBuilder.getErrors());
            throw new ToolBusinessException(RespConstants.RESP_CODE_FAILED, knowledgeBuilder.getErrors().toString());
        }

        //获取已编译的包
        kiePackages = knowledgeBuilder.getKnowledgePackages();

        //遍历kiePackages，打印包名和规则名称
        kiePackages.forEach(packageItem -> packageItem.getRules().forEach(ruleItem -> {
                Console.show(ruleItem.getPackageName());
                Console.show(ruleItem.getName());
            }));

        //将包添加到知识库（部署知识包）
        internalKBase.addPackages(kiePackages);

        //获取KieSession
        kieSession = internalKBase.newKieSession();

        //移除指定规则
        internalKBase.removeRule("rules", "book_discount_1");
    }

    /**
     * 描述 触发规则
     * @param
     * @return void
     * @author liugs
     * @date 2021/8/30 14:38:55
     */
    private void fireRules() {
        kieSession.fireAllRules();
        kieSession.dispose();
    }

    /**
     * 描述 初始化fact对象
     * @param
     * @return void
     * @author liugs
     * @date 2021/8/30 14:37:51
     */
    private void initFactObject() {
        Order order = new Order();
        order.setOrgPrice(10d);
        kieSession.insert(order);
    }
}