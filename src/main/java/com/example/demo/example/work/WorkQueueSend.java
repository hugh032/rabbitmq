package com.example.demo.example.work;

import com.example.demo.Utils.RabbitConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Work queue 一个生产者对应多个消费者
 *
 *                                 --- |consumer1|
 *    |production|-----|Queue|----
 *                                 --- |consumer2|
 * @Author zhaojun
 * @create 2020/1/20 18:11
 */
public class WorkQueueSend {
    public static String WORK_QUEUE_NAME = "work_queue";
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitConnectionUtils.getConnection2();
        Channel channel = connection.createChannel();
        channel.queueDeclare(WORK_QUEUE_NAME,false,false,false,null);
        for (int i = 0; i <10 ; i++) {
            String message = "男嘉宾_"+i;
            System.out.println("work queue send..."+message);
            channel.basicPublish("", WORK_QUEUE_NAME, null, message.getBytes());
        }
        channel.close();
        connection.close();
    }
}
