package com.example.demo.example.tx;

import com.example.demo.Utils.RabbitConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * rabbitmq事务模式 confirm 串行模式
 * @Author zhaojun
 * @create 2020/1/22 15:32
 */
public class ConfirmSend {
    public static String QUEUE_NAME = "confirm_queue";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = RabbitConnectionUtils.getConnection2();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "confirm test";
        channel.confirmSelect();
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        if (channel.waitForConfirms()) {
            System.out.println("confirm send ok");
        } else {
            System.out.println("confirm send failed");
        }
        channel.close();
        connection.close();

    }
}
