package com.example.demo.example.routing;

import com.example.demo.Utils.RabbitConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author zhaojun
 * @create 2020/1/22 13:46
 */
public class RoutingRead1 {
    public static String ROUTING_QUEUE_NAME_ERROR = "routing_error";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitConnectionUtils.getConnection2();
        Channel channel = connection.createChannel();
        channel.queueDeclare(ROUTING_QUEUE_NAME_ERROR, false, false, false, null);
        channel.queueBind(ROUTING_QUEUE_NAME_ERROR, RoutingSend.EXCHANGE_ROUTING_NAME, RoutingSend.ROUTING_KEY_ERROE);
        channel.basicQos(1);
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "utf-8");
                System.out.println("routing key: error read message ==" + message);
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
        channel.basicConsume(ROUTING_QUEUE_NAME_ERROR, false, consumer);
    }
}
