package com.example.demo.Utils;

import com.example.demo.config.RabbitConfig;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author zhaojun
 * @create 2020/1/20 16:00
 */
@Component
public class RabbitConnectionUtils {
    @Autowired
    private RabbitConfig rabbitConfig;
    public Connection getConnection() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(rabbitConfig.getHost());
        connectionFactory.setPort(Integer.parseInt(rabbitConfig.getPort()));
        connectionFactory.setUsername(rabbitConfig.getUsername());
        connectionFactory.setPassword(rabbitConfig.getPassword());
        connectionFactory.setVirtualHost(rabbitConfig.getVirHost());
        return connectionFactory.newConnection();
    }

    public static Connection getConnection2() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("hugh");
        connectionFactory.setPassword("123456");
        connectionFactory.setVirtualHost("/vhost_hugh");
        return connectionFactory.newConnection();
    }
}
