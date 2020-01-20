package com.example.demo;

import com.example.demo.Utils.RabbitConnectionUtils;
import com.rabbitmq.client.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Simple queue
 * 生产者 消费者一一对应
 * |production|------|Queue|--------|consumer|
 *
 * @Author zhaojun
 * @create 2020/1/20 16:12
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SimpleQueueTest {
    public static String SIMPLE_QUEUE_NAME = "simple_queue";
    @Autowired
    private RabbitConnectionUtils rabbitConnectionUtils;

    /**
     * 测试发送消息
     * @throws IOException
     * @throws TimeoutException
     */
    @Test
    public void testProduction() throws IOException, TimeoutException {
        /*1.建立连接*/
        Connection connection = rabbitConnectionUtils.getConnection();
        /*2.创建通道channel*/
        Channel channel = connection.createChannel();
        /*3.声明队列*/
        channel.queueDeclare(SIMPLE_QUEUE_NAME,false,false,false,null);
        /*4.发送消息到queue*/
        String message = "hello simple queue";
        channel.basicPublish("", SIMPLE_QUEUE_NAME, null, message.getBytes());
        channel.close();
        connection.close();
    }

    /**
     * 测试接收消息
     * @throws IOException
     * @throws TimeoutException
     */
    @Test
    public void testConsumer() throws IOException, TimeoutException {
        /*1.建立连接*/
        Connection connection = rabbitConnectionUtils.getConnection();
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
