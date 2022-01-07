package com.liugs.tool.async;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName AsyncQueueTaskBO
 * @Description 异步任务对象
 * @Author liugs
 * @Date 2022/1/6 15:37
 */
@Data
public class AsyncQueueTaskBO implements Serializable {

    private static final long serialVersionUID = -697641029083251301L;

    private String sourcePath;

    private String targetPath;
}
