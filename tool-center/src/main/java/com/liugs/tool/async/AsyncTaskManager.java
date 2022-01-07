package com.liugs.tool.async;

import com.alibaba.fastjson.JSON;
import com.liugs.tool.base.Console;
import org.springframework.scheduling.annotation.EnableAsync;
import java.util.Vector;
import java.util.concurrent.LinkedTransferQueue;

/**
 * @ClassName AsyncTaskManager
 * @Description 异步任务管理
 * @Author liugs
 * @Date 2022/1/6 16:04
 */
@EnableAsync
public class AsyncTaskManager {

    private boolean running = false;

    private Vector<AsyncTaskExecutor> executorVector;

    /** 任务队列 */
    private static LinkedTransferQueue<AsyncQueueTaskBO> taskQueue;

    /** 单例 懒汉式 （双检锁/双重校验锁）*/
    private volatile static AsyncTaskManager instance;
    private AsyncTaskManager() {
        executorVector = new Vector<>();
        taskQueue = new LinkedTransferQueue<>();
    }
    public static AsyncTaskManager getInstance() {
        if (instance == null) {
            synchronized(AsyncTaskManager.class) {
                if (instance == null) {
                    instance = new AsyncTaskManager();
                }
            }
        }
        return instance;
    }

    public synchronized void addExecutor(AsyncTaskExecutor executor) {
        if (executor == null) {
            throw new NullPointerException();
        }
        if (!executorVector.contains(executor)) {
            executorVector.add(executor);
        }
    }


    /** 添加任务 */
    public void recordTaskAndTriggerExecutor(AsyncQueueTaskBO taskBO) {
        taskQueue.add(taskBO);
        Console.show("任务入列：" + JSON.toJSONString(taskBO));
        triggerExecutor();
    }


    private void triggerExecutor() {
        // 执行器
        Object[] executors;
        synchronized (this) {
            // 如果执行器在执行，返回
            if (running) {
                return;
            }
            executors = executorVector.toArray();
        }
        // 触发所有注册的执行器
        for (int i = executors.length - 1; i>=0; i--) {
            ((AsyncTaskExecutor)executors[i]).execute(this, taskQueue);
        }
        setRunning();
    }

    protected synchronized void clearRunning() {
        running = false;
    }

    protected synchronized void setRunning() {
        running = true;
    }
}