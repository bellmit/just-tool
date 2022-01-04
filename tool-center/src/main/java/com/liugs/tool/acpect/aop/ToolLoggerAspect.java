package com.liugs.tool.acpect.aop;

import com.alibaba.dubbo.rpc.RpcContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liugs.tool.constants.RespConstants;
import com.liugs.tool.constants.ToolRspBaseBo;
import com.liugs.tool.exception.ToolBusinessException;
import com.taobao.eagleeye.EagleEye;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;


/**
 * @ClassName ToolLoggerAspect
 * @Description TOOL 日志切面
 * @Author liugs
 * @Date 2021/3/3 16:02:16
 */
@Aspect
@Component
@Order(100)
public class ToolLoggerAspect {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    /**
     * 分隔符
     */
    private static final String SEPARATOR = "|";
    /**
     * 需要排除的方法名称
     */
    private static final String EXCLUDE_METHODS = "subscribe";
    /**
     * 类与方法名的连接符
     */
    private static final String CLASS_METHOD_SEPARATOR = ".";
    /**
     * 开始标识
     */
    private static final String START_IDENTIFICATION = "START";
    /**
     * 异常标识
     */
    private static final String ERROR_IDENTIFICATION = "ERROR";
    /**
     * 结束标识
     */
    private static final String END_IDENTIFICATION = "END";

    private static final Logger atomLogger = LoggerFactory.getLogger(ToolLoggerAspect.class);

    private static final String TRACE_ID = "traceId";

    @Pointcut("(execution(* com.liugs.tool.ability.impl..*(..)))" +
            " || (execution(* com.liugs.tool.busi.impl..*(..)))" +
            " || (execution(* com.liugs.tool.comb.impl..*(..)))")
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Map<String, String> hiddenParm = RpcContext.getContext().getAttachments();
        String traceId = "";
        if (hiddenParm.containsKey(TRACE_ID)) {
            traceId = hiddenParm.get(TRACE_ID);
        } else {
            traceId = EagleEye.getTraceId();
            hiddenParm.put(TRACE_ID, traceId);
        }
        // 打印请求日志
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        Class<?> rspClass = method.getReturnType();
        Object rspObj = null;
        String[] infos = this.printBusiAopStartLog(pjp, traceId);
        try {
            rspObj = pjp.proceed();
            this.putCodeToRsp(rspObj, RespConstants.RESP_CODE_SUCCESS, RespConstants.RESP_DESC_SUCCESS);
        } catch (ToolBusinessException e) {
            if (atomLogger.isDebugEnabled()) {
                e.printStackTrace();
            }
            rspObj = rspClass.newInstance();
            String msgCode = e.getMsgCode();
            String desc = e.getMessage();
            if (StringUtils.isEmpty(msgCode)) {
                msgCode = RespConstants.RESP_CODE_FAILED;
            }
            if (StringUtils.isEmpty(desc)) {
                desc = RespConstants.RESP_DESC_FAILED;
            }
            this.printBusiAopErrorLog(infos, e);
            this.putCodeToRsp(rspObj, msgCode, desc);
        } catch (Throwable e) {
            if (!rspClass.isInterface()) {
                if (atomLogger.isDebugEnabled()) {
                    e.printStackTrace();
                }
                this.printBusiAopErrorLog(infos, e);
                rspObj = rspClass.newInstance();
                this.putCodeToRsp(rspObj, RespConstants.RESP_CODE_FAILED, "服务调用异常:" + e.getMessage());
            } else {
                //接口，无法创建实例，反给外层去处理
                throw e;
            }
        }
        this.printBusiAopEndLog(infos, rspObj);
        return rspObj;
    }

    /**
     * 描述  打印业务层异常日志
     * @param infos
     * @param e
     * @return void
     * @author liugs
     * @date 2021/3/3 16:21:02
     */
    private void printBusiAopErrorLog(String[] infos, Throwable e) {
        if (infos == null) {
            return;
        }
        Long nowTime = System.currentTimeMillis();
        // 修改标识为结束标识
        infos[1] = ERROR_IDENTIFICATION;
        // 添加结束时间
        infos[3] = nowTime.toString();
        // 异常信息
        if (null != this.getThrowableStrRep(e)) {
            infos[5] = this.getThrowableStrRep(e);
        }
        // 打印接口服务异常日志
        atomLogger.error(getLogInfoByStrings(infos));
    }

    private void putCodeToRsp(Object object, String code, String desc) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        this.putCodeToRsp(object, code, desc, false);
    }

    private void putCodeToRsp(Object object, String code, String desc, boolean fource) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        if (object instanceof ToolRspBaseBo) {
            Field field = ToolRspBaseBo.class.getDeclaredField("respCode");
            if (field != null) {
                // 如果业务中已经加入的返回值，则不予以赋值
                field.setAccessible(true);
                if (!fource && field.get(object) == null) {
                    field.set(object, code);
                    field = ToolRspBaseBo.class.getDeclaredField("respDesc");
                    field.setAccessible(true);
                    field.set(object, desc);
                } else if (fource && field.get(object) == null) {
                    field.set(object, code);
                    field = ToolRspBaseBo.class.getDeclaredField("respDesc");
                    field.setAccessible(true);
                    field.set(object, desc);
                }
            }

        }
    }

    /**
     * 描述 打印业务层请求日志
     * @param pjp
     * @param traceId
     * @return java.lang.String[]
     * @author liugs
     * @date 2021/3/3 16:21:29
     */
    private String[] printBusiAopStartLog(ProceedingJoinPoint pjp, String traceId) throws Throwable {

        Long nowTime = System.currentTimeMillis();
        // 添加edas TraceId
        StringBuilder commonHead = new StringBuilder(traceId + SEPARATOR);
        // 获取目标对象对应的类名
        String className = pjp.getTarget().getClass().getName();
        // 获取目标对象上正在执行的方法名
        String methodString = pjp.getSignature().getName();
        // 要排除的方法
        if (EXCLUDE_METHODS.equals(methodString)) {
            return null;
        }
        // 添加类名.方法名
        commonHead.append(className);
        commonHead.append(CLASS_METHOD_SEPARATOR);
        commonHead.append(methodString);
        String[] infos = new String[8];
        infos[0] = commonHead.toString();
        // 添加开始标识
        infos[1] = START_IDENTIFICATION;
        // 添加开始时间
        infos[2] = nowTime.toString();
        // 添加结束时间
        infos[3] = "";
        // 入参
        Object[] params = pjp.getArgs();
        if (params != null && params.length > 0) {
            if (params.length == 1) {
                // 请求报文
                try {
                    infos[4] = MAPPER.writeValueAsString(params[0]);
                } catch (JsonProcessingException e) {
                    infos[4] = ("请求报文转json出错，改用toString方法" + params[0]).replace("\r\n", "");
                }
            } else {
                StringBuilder paramsStr = new StringBuilder();
                paramsStr.append("[");
                for (Object obj : params) {
                    String objStr = null;
                    if (obj != null) {
                        try {
                            objStr = MAPPER.writeValueAsString(obj);
                        } catch (JsonProcessingException e) {
                            objStr = null;
                        }
                    }
                    paramsStr.append(objStr);
                    paramsStr.append(",");
                }
                paramsStr.setLength(paramsStr.length() - 1);
                paramsStr.append("]");
            }
        }
        // 打印接口服务请求日志
        atomLogger.info(getLogInfoByStrings(infos));
        return infos;
    }

    /**
     * 描述 打印业务层结束日志
     * @param infos
     * @param object
     * @return void
     * @author liugs
     * @date 2021/3/3 16:21:51
     */
    private void printBusiAopEndLog(String[] infos, Object object) {
        if (infos == null) {
            return;
        }
        Long nowTime = System.currentTimeMillis();
        // 修改标识为结束标识
        infos[1] = END_IDENTIFICATION;
        // 添加结束时间
        infos[3] = nowTime.toString();
        if (object != null && object instanceof ToolRspBaseBo) {
            ToolRspBaseBo resp = (ToolRspBaseBo) object;
            infos[4] = resp.getRespCode();
            infos[5] = resp.getRespDesc();
            infos[6] = "用时：" + (Long.parseLong(infos[3]) - Long.parseLong(infos[2])) + "ms";
            infos[7] = object.toString();
        }
        // 打印接口服务结束日志
        atomLogger.info(getLogInfoByStrings(infos));
    }

    /**
     * 描述 生成日志
     * @param infos
     * @return java.lang.String
     * @author liugs
     * @date 2021/3/3 16:22:07
     */
    private String getLogInfoByStrings(String[] infos) {
        StringBuilder stb = new StringBuilder();
        for (String info : infos) {
            if (org.apache.commons.lang3.StringUtils.isBlank(info)) {
                info = "";
            }
            stb.append(info + SEPARATOR);
        }
        if (stb.length() > 1) {
            stb.setLength(stb.length() - 1);
        }
        return stb.toString();
    }

    /**
     * 描述 将Throwable对象的错误堆栈内容形成字符串
     * @param throwable
     * @return java.lang.String
     * @author liugs
     * @date 2021/3/3 16:22:24
     */
    private String getThrowableStrRep(Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        pw.flush();
        LineNumberReader reader = new LineNumberReader(new StringReader(sw.toString()));
        StringBuffer sb = new StringBuffer();
        try {
            String line = reader.readLine();
            while (line != null) {
                sb.append(line + "\r\n");
                line = reader.readLine();
            }
        } catch (IOException ex) {
            sb.append(ex.toString());
        }
        return sb.toString();
    }
}
