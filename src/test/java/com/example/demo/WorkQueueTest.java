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
 * Work queue 一个生产者对应多个消费者
 *
 *                                 --- |consumer1|
 *    |production|-----|Queue|----
 *                                 --- |consumer2|
 *
 * @Author zhaojun
 * @create 2020/1/20 17:28
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class WorkQueueTest {
    public static String WORK_QUEUE_NAME = "work_queue";
    @Autowired
    private RabbitConnectionUtils rabbitConnectionUtils;
    @Test
    public void testProduction() throws IOException, TimeoutException {
        Connection connection = rabbitConnectionUtils.getConnection();
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

    @Test
    public void testConsumer() throws IOException, TimeoutException {
        Connection connection = rabbitConnectionUtils.getConnection();
        /*1号消费者*/
        Channel channel = connection.createChannel();
        channel.queueDeclare(WORK_QUEUE_NAME, false, false, false, null);
        DefaultConsumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body,"utf-8");
                System.out.println("consumer[1] message ===="+message);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        channel.basicConsume(WORK_QUEUE_NAME, true, consumer);
    }
}
