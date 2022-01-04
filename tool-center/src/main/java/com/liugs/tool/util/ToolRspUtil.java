package com.liugs.tool.util;

import com.liugs.tool.constants.RespConstants;
import com.liugs.tool.constants.ToolRspBaseBo;
import com.liugs.tool.exception.ToolBusinessException;

/**
 * @ClassName ToolRspUtil
 * @Description 出参工具类
 * @Author liugs
 * @Date 2021/3/3 17:29:38
 */
public class ToolRspUtil {

    /**
     * 描述 获取出参对象
     * @param code
     * @param desc
     * @param tClass
     * @return T
     * @author liugs
     * @date 2021/3/3 17:33:59
     */
    public static <T extends ToolRspBaseBo> T getRspBo(String code, String desc, Class<T> tClass) {
        try {
            T t = tClass.newInstance();
            t.setRespCode(code);
            t.setRespDesc(desc);
            return t;
        } catch (Exception e) {
            throw new ToolBusinessException("ToolRspUtil生成出参对象异常", e.getMessage());
        }
    }

    /**
     * 描述 获取成功出参对象
     * @param tClass
     * @return T
     * @author liugs
     * @date 2021/3/3 17:34:21
     */
    public static <T extends ToolRspBaseBo> T getSuccessRspBo(Class<T> tClass) {
        return getRspBo(RespConstants.RESP_CODE_SUCCESS, RespConstants.RESP_DESC_SUCCESS, tClass);
    }
}
