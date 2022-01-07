package com.liugs.tool.async;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.LinkedTransferQueue;

/**
 * @ClassName AsyncTaskExcutor
 * @Description 任务执行者
 * @Author liugs
 * @Date 2022/1/6 16:30
 */
@Slf4j
public class AsyncTaskExecutor {

    @Async("toolAsyncExecutor")
    public void execute(AsyncTaskManager asyncTaskManager, LinkedTransferQueue<AsyncQueueTaskBO> taskQueue) {
        AsyncQueueTaskBO taskBO;
        while (true) {
            try {
                taskBO = taskQueue.take();
                log.info("执行任务中：{}", JSON.toJSONString(taskBO));
//                FileTypeConverter.xmlToExcel(taskBO.getSourcePath(), taskBO.getTargetPath());
            } catch (Exception e) {
                log.info("任务执行异常：{}", e.getMessage());
                asyncTaskManager.clearRunning();
            }
            if (taskQueue.isEmpty()) {
                break;
            }
        }
        asyncTaskManager.clearRunning();
        log.info("{}-任务执行完成", Thread.currentThread().getName());
    }
}
