package com.liugs.tool.cacheQueue;

import com.liugs.tool.base.Console;

import java.util.concurrent.LinkedTransferQueue;

/**
 * @ClassName DateProducer
 * @Description 数据生产
 * @Author liugs
 * @Date 2022/1/7 10:54
 */
public class DataProducer implements Runnable{

    private final LinkedTransferQueue<String> queue;

    public DataProducer(LinkedTransferQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        String message = "MESSAGE_";
        Integer num = 0;
        while (true) {
            num ++;
            Console.show(Thread.currentThread().getName() + "-消息入列：" + message + num);
            queue.put(message + num);
            if (num > 100) {
                break;
            }
        }
        Console.show("消息生产结束");
    }
}
