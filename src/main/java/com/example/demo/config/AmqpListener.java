package com.example.demo.config;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Author zhaojun
 * @create 2020/1/22 17:31
 */
@Component
public class AmqpListener {

    @RabbitListener(queues = "tx_queue")
    public void receive1(String message) {
        System.err.println("tx_queue -- receive1接收到消息：" + message);
    }

//    @RabbitListener(queues = "xudc.book")
//    public void receive2(String message) {
//        System.err.println("xudc.book -- receive2接收到消息：" + message);
//    }

//    @RabbitListener(queues = "andy")
//    public void receive3(String message) {
//        System.err.println("andy -- receive3接收到消息：" + message);
//    }
}
