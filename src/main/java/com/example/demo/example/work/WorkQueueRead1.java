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
        /*
        公平分发
        默认情况下，消息队列不管消费者是否处理完毕消息，
        都会继续发送下一条消息,如果两个消费者消费速度不一样，慢的会积压一些消息慢慢处理 配置basicQos 会使慢的消费者上一条消息没处理完不会接收
        新的消息，达到能者多劳的效果
        */
        channel.basicQos(1);
        DefaultConsumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body,"utf-8");
                System.out.println("consumer[1] message ===="+message);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }

            }
        };
        channel.basicConsume(WORK_QUEUE_NAME, false, consumer);
    }
}
