package com.liugs.tool.acpect.aop;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ToolTransactionAspect
 * @Description 事务配置
 * @Author liugs
 * @Date 2021/3/3 15:54:31
 */
@Aspect
@Configuration
@Order(-100)
public class ToolTransactionAspect {

    /**
     * 定义切点路径
     */
    private static final String AOP_POINTCUT_EXPRESSION = "execution(* com.liugs.tool.busi.impl..*(..))";

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public TransactionInterceptor txAdvice() {
        // 事务管理规则，承载需要进行事务管理的方法名（模糊匹配）及设置的事务管理属性
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();

        // 设置第一个事务管理的模式（适用于“增删改”）
        RuleBasedTransactionAttribute requireRule = new RuleBasedTransactionAttribute();
        // 当抛出设置的对应异常后，进行事务回滚（此处设置为“Exception”级别）
        requireRule.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Throwable.class)));
        // 设置隔离级别（存在事务则加入其中，不存在则新建事务）
        requireRule.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        // 设置传播行为（读已提交的数据）
        requireRule.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);

        // 设置第二个事务管理的模式（适用于“查”）
        RuleBasedTransactionAttribute readOnlyRule = new RuleBasedTransactionAttribute();
        // 当抛出设置的对应异常后，进行事务回滚（此处设置为“Exception”级别）
        readOnlyRule.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Throwable.class)));
        // 设置隔离级别（存在事务则挂起该事务，执行当前逻辑，结束后再恢复上下文事务）
        readOnlyRule.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);
        // 设置传播行为（读已提交的数据）
        readOnlyRule.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        // 设置事务是否“只读”（非必需，只是声明该事务中不会进行修改数据库的操作，可减轻由事务造成的数据库压力，属于性能优化的推荐配置）
        readOnlyRule.setReadOnly(true);

        // 建立一个map，用来储存要需要进行事务管理的方法名（模糊匹配）
        Map<String, TransactionAttribute> txMap = new HashMap<>(16);
        txMap.put("create*", requireRule);
        txMap.put("register*", requireRule);
        txMap.put("update*", requireRule);
        txMap.put("delete*", requireRule);
        txMap.put("insert*", requireRule);
        txMap.put("add*", requireRule);
        txMap.put("modify*", requireRule);

        txMap.put("save*", requireRule);
        txMap.put("cancel*", requireRule);
        txMap.put("change*", requireRule);
        txMap.put("notify*", requireRule);
        txMap.put("break*", requireRule);
        txMap.put("marking*", requireRule);
        txMap.put("execute*", requireRule);

        txMap.put("set*", requireRule);
        txMap.put("record*", requireRule);
        txMap.put("log*", requireRule);
        txMap.put("deal*", requireRule);
        txMap.put("complete*", requireRule);
        txMap.put("batchDelete*", requireRule);
        txMap.put("batchInsert*", requireRule);
        txMap.put("on*", requireRule);

        txMap.put("query*", readOnlyRule);
        txMap.put("*", readOnlyRule);

        // 注入设置好的map
        source.setNameMap(txMap);
        // 实例化事务拦截器
        return new TransactionInterceptor(transactionManager, source);
    }

    /**
     * 描述 利用AspectJExpressionPointcut设置切面
     * @return org.springframework.aop.Advisor
     * @author liugs
     * @date 2021/3/3 16:00:10
     */
    @Bean
    public Advisor txAdviceAdvisor() {
        // 声明切点要切入的面
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        // 设置需要被拦截的路径
        pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
        // 设置切面和配置好的事务管理
        return new DefaultPointcutAdvisor(pointcut, txAdvice());
    }
}
