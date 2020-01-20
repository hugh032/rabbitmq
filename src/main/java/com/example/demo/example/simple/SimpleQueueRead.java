package com.example.demo.example.simple;

import com.example.demo.Utils.RabbitConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author zhaojun
 * @create 2020/1/20 16:02
 */
public class SimpleQueueRead {
    public static String SIMPLE_QUEUE_NAME = "simple_queue";
    public static void main(String[] args) throws IOException, TimeoutException {
        /*1.建立连接*/
        Connection connection = RabbitConnectionUtils.getConnection2();
        /*2.创建通道channel*/
        Channel channel = connection.createChannel();
        /*3.声明队列*/
        channel.queueDeclare(SIMPLE_QUEUE_NAME, false, false, false, null);
        /*4.监听消费*/
        DefaultConsumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body,"utf-8");
                System.out.println("consumer message ===="+message);
            }
        };
        //监听
        channel.basicConsume(SIMPLE_QUEUE_NAME, true, consumer);
    }
}
