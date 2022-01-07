package com.liugs.tool.cacheQueue;

import com.liugs.tool.base.Console;

import java.util.concurrent.LinkedTransferQueue;

/**
 * @ClassName DataConsumer
 * @Description 数据消费者
 * @Author liugs
 * @Date 2022/1/7 14:22
 */
public class DataConsumer implements Runnable {

    private final LinkedTransferQueue<String> queue;

    private final String name;

    public DataConsumer(LinkedTransferQueue<String> queue, String name) {
        this.queue = queue;
        this.name = name;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object data = queue.take();
                Console.show(Thread.currentThread().getName() + "-消费者[" + name + "]：" + data);
//                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
