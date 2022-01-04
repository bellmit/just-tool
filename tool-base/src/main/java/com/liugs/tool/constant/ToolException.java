package com.liugs.tool.constant;

/**
 * @ClassName ToolException
 * @Description
 * @Author liugs
 * @Date 2020/7/16 10:56:18
 */
public class ToolException extends RuntimeException {

    private String msgCode;
    private String[] args;

    public String getMsgCode() {
        return this.msgCode;
    }

    public String[] getArgs() {
        return this.args;
    }

    public String getMsgInfo() {
        return this.getMessage();
    }

    public ToolException(String msgId, String message) {
        super(message);
        this.msgCode = msgId;
    }

    public ToolException(String msgId, String message, Throwable cause) {
        super(message, cause);
        this.args = new String[1];
        this.args[0] = message;
        this.msgCode = msgId;
    }
}
