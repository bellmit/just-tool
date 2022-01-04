package com.liugs.tool.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName ToolBusinessException
 * @Description 自定义异常
 * @Author liugs
 * @Date 2021/3/3 16:13:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ToolBusinessException extends RuntimeException {

    private static final long serialVersionUID = -1814155355569144196L;

    private String msgCode;
    private String[] args;

    public ToolBusinessException(String msgId, String message) {
        super(message);
        this.msgCode = msgId;
    }

    public ToolBusinessException(String msgId, String message, Throwable cause) {
        super(message, cause);
        this.args = new String[1];
        this.args[0] = message;
        this.msgCode = msgId;
    }
}
