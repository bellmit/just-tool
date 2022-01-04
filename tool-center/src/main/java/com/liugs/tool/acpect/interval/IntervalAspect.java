package com.liugs.tool.acpect.interval;

import com.alibaba.fastjson.JSON;
import com.liugs.tool.constants.RespConstants;
import com.liugs.tool.constants.ToolReqBaseBo;
import com.liugs.tool.constants.ToolRspBaseBo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @ClassName IntervalAspect
 * @Description 限制切面
 * @Author liugs
 * @Date 2021/12/8 14:36
 */
@Aspect
@Component
@Slf4j
public class IntervalAspect {

    private static final String NEED_INTERVAL_ASPECT = "1";


    /** 以 @Interval 注解为切点 */
    @Pointcut("@annotation(com.liugs.tool.aspect.Interval)")
    public void interval() {
    }


    @Before("interval()")
    public void doBefore(JoinPoint joinPoint) {
        // nothing
    }

    @After("interval()")
    public void doAfter() {
        // nothing
    }

    @Around("interval()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 打印一下请求日志

        log.info("========== 请求开始: 入参: {} =================================== ", proceedingJoinPoint.getArgs());
        ToolReqBaseBo recordReqBO = JSON.parseObject(JSON.toJSONString(proceedingJoinPoint.getArgs()[0]), ToolReqBaseBo.class);

        if (null != recordReqBO && NEED_INTERVAL_ASPECT.equals(recordReqBO.getNeedInterval())) {
            // 调用接口判断是否过期
        }

        Object result = proceedingJoinPoint.proceed();

        if (null != recordReqBO && NEED_INTERVAL_ASPECT.equals(recordReqBO.getNeedInterval())) {
            //如果接口失败了的，将记录置为无效
            if (result instanceof ToolRspBaseBo) {
                Field field = result.getClass().getDeclaredField("code");
                field.setAccessible(true);
                String code = field.get(result).toString();

                if (!RespConstants.RESP_CODE_SUCCESS.equals(code)) {
                    // 将记录置为失效
                }
            }
        }
        return result;
    }
}
