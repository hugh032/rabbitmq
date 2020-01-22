package com.example.demo.example.routing;

import com.example.demo.Utils.RabbitConnectionUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author zhaojun
 * @create 2020/1/22 13:25
 */
public class RoutingSend {
    public static String EXCHANGE_ROUTING_NAME = "test_exchange_direct";
    public static String ROUTING_KEY_ERROE = "error";
    public static String ROUTING_KEY_NORMAL = "normal";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitConnectionUtils.getConnection2();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_ROUTING_NAME, BuiltinExchangeType.DIRECT);
        String message = "hello routing ";
        channel.basicPublish(EXCHANGE_ROUTING_NAME, ROUTING_KEY_ERROE, null, message.getBytes());
        channel.basicPublish(EXCHANGE_ROUTING_NAME, ROUTING_KEY_NORMAL, null, message.getBytes());
        System.out.println("routing send:" + message);
        channel.close();
        connection.close();

    }
}
