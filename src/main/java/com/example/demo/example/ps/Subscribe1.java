package com.example.demo.example.ps;

import com.example.demo.Utils.RabbitConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author zhaojun
 * @create 2020/1/21 17:38
 */
public class Subscribe1 {
    public static String PUB_QUEUE_NAME_EMAIL = "publish_queue_email";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitConnectionUtils.getConnection2();
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(PUB_QUEUE_NAME_EMAIL, false, false, false, null);
        channel.queueBind(PUB_QUEUE_NAME_EMAIL, Publish.EXCHANGE_NAME, "");
        channel.basicQos(1);
        DefaultConsumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body,"utf-8");
                System.out.println("pub_email_consumer message ===="+message);
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
        channel.basicConsume(PUB_QUEUE_NAME_EMAIL, false, consumer);

    }
}
