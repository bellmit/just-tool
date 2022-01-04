package com.liugs.tool.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.liugs.tool.ability.bo.RpcMockServiceReqBo;
import com.liugs.tool.ability.bo.RpcMockServiceRspBo;
import com.liugs.tool.constants.RespConstants;
import com.liugs.tool.exception.ToolBusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

/**
 * @ClassName RpcMockAbilityService
 * @Description RPC调用服务
 * @Author liugs
 * @Date 2021/7/2 9:54:18
 */
@Slf4j
public class RpcMockService {
    /**
     * rpc泛化调用controller路径，默认使用的迪易采标准版的应用层的controller：RpcController
     */
    @Value("${uoc.mock.rpc.url:http://59.110.230.30:10130/deploy/federate/noauth/rpc}")
    private String rpcCallUrl;

    private static final String SUCCESS_CODE = "0";

    /**
     * 描述
     * @param interClassName 接口类名
     * @param methodName     方法名
     * @param data           接口数据
     * @param returnClass    返回类型
     * @return T
     * @author liugs
     * @date 2021/7/2 9:59:14
     */
    public <T> T rpcMockData(String interClassName, String methodName, Object data, Class<T> returnClass) {
        validateArg(interClassName, methodName, data, returnClass);

        RpcMockServiceReqBo remoteReqBo = new RpcMockServiceReqBo();
        remoteReqBo.setInterClass(interClassName);
        remoteReqBo.setMethod(methodName);
        remoteReqBo.setData(data);
        String jsonStr = JSON.toJSONString(remoteReqBo);

        log.info("rpc mock调用入参：" + jsonStr);
        String body = HttpRequest.post(rpcCallUrl).body(jsonStr).timeout(60000).execute().body();
        RpcMockServiceRspBo remoteRspBo = JSON.parseObject(body, RpcMockServiceRspBo.class);

        String code = remoteRspBo.getCode();

        log.info("rpc mock调用出参：" + remoteRspBo.getData());

        if (SUCCESS_CODE.equals(code)) {
            T rspObj = JSON.parseObject(remoteRspBo.getData(), returnClass);
            log.info("rpc mock调用出参：" + JSON.toJSONString(rspObj));
            return rspObj;
        } else {
            log.error( "RPC接口调用返回失败信息：" + remoteRspBo.getMessage()+",出参：" + JSON.toJSONString(remoteRspBo));
            throw new ToolBusinessException(RespConstants.RESP_CODE_FAILED, "RPC接口调用返回失败信息：" + remoteRspBo.getMessage());
        }

    }

    /**
     * 描述 入参校验
     * @param interClassName        接口类名
     * @param methodName            方法名
     * @param data                  接口数据
     * @param returnClass           返回类型
     * @return void
     * @author liugs
     * @date 2021/7/2 9:59:29
     */
    private void validateArg(String interClassName, String methodName, Object data, Class returnClass) {
        if (ObjectUtil.isEmpty(interClassName)) {
            throw new ToolBusinessException(RespConstants.RESP_CODE_FAILED, "[interClassName]不能为空");
        }
        if (ObjectUtil.isEmpty(methodName)) {
            throw new ToolBusinessException(RespConstants.RESP_CODE_FAILED, "[methodName]不能为空");
        }
        if (ObjectUtil.isEmpty(data)) {
            throw new ToolBusinessException(RespConstants.RESP_CODE_FAILED, "[data]不能为空");
        }
        if (ObjectUtil.isEmpty(returnClass)) {
            throw new ToolBusinessException(RespConstants.RESP_CODE_FAILED, "[returnClass]不能为空");
        }
    }
}