package com.example.demo.example.work;

import com.example.demo.Utils.RabbitConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author zhaojun
 * @create 2020/1/20 18:12
 */
public class WorkQueueRead1 {
    public static String WORK_QUEUE_NAME = "work_queue";
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitConnectionUtils.getConnection2();
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
