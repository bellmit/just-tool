package com.liugs.tool.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ClassName ToolThreadPollManager
 * @Description 线程池管理
 * @Author liugs
 * @Date 2021/2/28 11:28:13
 */
@EnableAsync
@Configuration
public class ToolThreadPollManager {

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
    private static final String THREAD_NAME_PREFIX = "PAY-ASYNC-";

    @Bean("toolAsyncExecutor")
    public Executor payAsyncExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程数
        executor.setCorePoolSize(PROCESSORS * 2);
        //最大线程数
        executor.setMaxPoolSize(PROCESSORS * 4);
        //缓冲队列
        executor.setQueueCapacity(QUEUE_CAPACITY);
        //允许线程的空闲时间
        executor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
        //线程池名的前缀
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);
        // 线程池对拒绝任务的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住
        executor.setAwaitTerminationSeconds(120);
        // 初始化
        executor.initialize();
        return executor;
    }

   /* private final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNamePrefix(THREAD_NAME_PREFIX +"%d").build();

    @Bean("payAsyncExecutor")
    public Executor payAsyncExecutor() {
        ExecutorService pool = new ThreadPoolExecutor(
                PROCESSORS * 2,
                PROCESSORS * 4,
                KEEP_ALIVE_TIME * 1000,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(QUEUE_CAPACITY),
                threadFactory,
                new ThreadPoolExecutor.CallerRunsPolicy());
        return pool;
    }*/
}
