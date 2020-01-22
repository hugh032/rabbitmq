package com.example.demo.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author zhaojun
 * @create 2020/1/22 17:29
 */
@RestController
public class AmqpController {
    @Autowired
    private final RabbitTemplate rabbitTemplate;

    public AmqpController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    private final static String SUCCESS = "success";

    /**
     * 单点
     * @param msg
     * @return
     */
    @GetMapping("/direct")
    public String direct(String msg){
        rabbitTemplate.convertAndSend("amq.direct", "xudc", msg);
        return SUCCESS;
    }

    @GetMapping("/fanout")
    public String fanout(String msg) {
        //rabbitTemplate.convertAndSend("amq.fanout", "", msg);
        rabbitTemplate.convertAndSend("test_exchange_direct", "error", msg);
        return SUCCESS;
    }

    @GetMapping("/topic")
    public String topic(String msg){
        rabbitTemplate.convertAndSend("amq.topic", "xudc.#", msg);
        return SUCCESS;
    }
}
