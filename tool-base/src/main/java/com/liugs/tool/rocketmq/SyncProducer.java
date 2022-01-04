package com.liugs.tool.rocketmq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * @ClassName SyncProducer
 * @Description
 * @Author liugs
 * @Date 2021/7/8 14:08:09
 */
public class SyncProducer {
    public static void main(String[] args) throws Exception {
        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer("PRODUCER_L");
        // 设置NameServer的地址
        producer.setNamesrvAddr("47.108.24.75:9876");
        //关闭VIP通道
        // 启动Producer实例
        producer.start();
        for (int i = 0; i < 40; i++) {
            // 创建消息，并指定Topic，Tag和消息体
            Message msg = new Message("TopicTest" ,"TagA",
                    ("Hello RocketMQ1212 " + i).getBytes(RemotingHelper.DEFAULT_CHARSET)
            );
            // 发送消息到一个Broker
            producer.sendOneway(msg);
            // 通过sendResult返回消息是否成功送达
//            System.out.printf("%s%n", sendResult);
        }
        // 如果不再发送消息，关闭Producer实例。
        producer.shutdown();
    }
}