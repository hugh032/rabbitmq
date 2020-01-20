package com.example.demo.example.simple;

import com.example.demo.Utils.RabbitConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Simple queue
 * 生产者 消费者一一对应
 * |production|------|Queue|--------|consumer|
 *
 * @Author zhaojun
 * @create 2020/1/20 16:02
 */
public class SimpleQueueSend {
    public static String SIMPLE_QUEUE_NAME = "simple_queue";
    public static void main(String[] args) throws IOException, TimeoutException {
        /*1.建立连接*/
        Connection connection = RabbitConnectionUtils.getConnection2();
        /*2.创建通道channel*/
        Channel channel = connection.createChannel();
        /*3.声明队列*/
        channel.queueDeclare(SIMPLE_QUEUE_NAME,false,false,false,null);
        /*4.发送消息到queue*/
        String message = "hello simple queue";
        channel.basicPublish("", SIMPLE_QUEUE_NAME, null, message.getBytes());
        System.out.println(SIMPLE_QUEUE_NAME+"发送消息:"+message);
        channel.close();
        connection.close();
    }
}
