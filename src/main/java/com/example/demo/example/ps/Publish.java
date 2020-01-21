package com.example.demo.example.ps;


import com.example.demo.Utils.RabbitConnectionUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 订阅模式
 *               |----|Q1|----|C1|
 * |P|----|E|----|
 *               |----|Q2|----|C2|
 * @Author zhaojun
 * @create 2020/1/21 17:19
 */
public class Publish {
    public static String EXCHANGE_NAME = "test_exchange_fanout";
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitConnectionUtils.getConnection2();
        Channel channel = connection.createChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        String message = "hello exchange!";
        channel.basicPublish(EXCHANGE_NAME,"", null, message.getBytes());
        channel.close();
        connection.close();
    }

}
