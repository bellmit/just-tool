package com.liugs.tool.cacheQueue;

import cn.hutool.core.thread.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * @ClassName DataTester
 * @Description 测试
 * @Author liugs
 * @Date 2022/1/7 15:25
 */
public class DataTester {

    /**
     * 最大可用的CPU核数
     */
    public static final int PROCESSORS = Runtime.getRuntime().availableProcessors();
    /**
     * 线程最大的空闲存活时间，单位为秒
     */
    public static final int KEEP_ALIVE_TIME = 10;
    /**
     * 缓冲队列数
     */
    private static final int QUEUE_CAPACITY = 200;
    /**
     * 线程池名前缀
     */
    private static final String THREAD_NAME_PREFIX = "DATA-FACTORY-";

     private static final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNamePrefix(THREAD_NAME_PREFIX).build();

    public static void main(String[] args) {
        ExecutorService pool = new ThreadPoolExecutor(
                PROCESSORS * 2,
                PROCESSORS * 4,
                KEEP_ALIVE_TIME * 1000,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(QUEUE_CAPACITY),
                threadFactory,
                new ThreadPoolExecutor.CallerRunsPolicy());

        LinkedTransferQueue<String> queue = new LinkedTransferQueue<>();

        DataProducer dataProducer = new DataProducer(queue);
        DataConsumer consumer1 = new DataConsumer(queue, "一");
        DataConsumer consumer2 = new DataConsumer(queue, "二");

        pool.submit(dataProducer);
        pool.submit(consumer1);
        pool.submit(consumer2);
    }
}
