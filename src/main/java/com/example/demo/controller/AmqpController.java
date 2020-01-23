package com.example.demo.controller;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
     *
     * @param msg
     * @return
     */
    @GetMapping("/direct")
    public String direct(String msg) {
        rabbitTemplate.convertAndSend("amq.direct", "xudc", msg);
        return SUCCESS;
    }

    @GetMapping("/fanout")
    public String fanout(String msg) {
        //rabbitTemplate.convertAndSend("test_exchange_direct", "error", msg);
        return SUCCESS;
    }

    @GetMapping("/topic")
    public String topic(String msg) {
        rabbitTemplate.convertAndSend("amq.topic", "xudc.#", msg);
        return SUCCESS;
    }

    @GetMapping("/dead")
    public String deadTest(String msg) {
        MessagePostProcessor messagePostProcessor = message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            messageProperties.setMessageId(UUID.randomUUID().toString().replaceAll("-", ""));
            messageProperties.setContentEncoding("utf-8");
            //超时时间10秒
            messageProperties.setExpiration(String.valueOf(1000 * 10));
            return message;
        };
        Map<String, Object> dataMap = new HashMap<>(16);
        dataMap.put("msg", "我是传递的消息");
        rabbitTemplate.convertAndSend("DEAD_LETTER_EXCHANGE", "DEAD_LETTER_KEY", dataMap, messagePostProcessor);
        return SUCCESS;
    }
}
