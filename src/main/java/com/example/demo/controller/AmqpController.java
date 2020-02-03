package com.example.demo.controller;

import com.example.demo.config.OrdersMqQueueConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/dead/{msg}")
    public String deadTest(@PathVariable(name = "msg") String msg, @RequestParam(name="type") String type) throws JsonProcessingException {
        //为每条消息设置过期时间Per-Message TTL，也可以对queue设置 Queue TTL，都设置取偏小的值
        MessagePostProcessor messagePostProcessor = message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            messageProperties.setMessageId(UUID.randomUUID().toString().replaceAll("-", ""));
            messageProperties.setContentEncoding("utf-8");
            //超时时间10秒
            //messageProperties.setExpiration(String.valueOf(1000 * 10));
            return message;
        };
        Map<String, Object> dataMap = new HashMap<>(16);
        dataMap.put("msg", msg);
        ObjectMapper mapper = new ObjectMapper();
        String messJson = mapper.writeValueAsString(dataMap);
        if(type.equals("cancel")){
            rabbitTemplate.convertAndSend(OrdersMqQueueConfig.DEAD_LETTER_EXCHANGE, OrdersMqQueueConfig.DEAD_LETTER_CANCEL_KEY, messJson, messagePostProcessor);
        } else if (type.equals("finish")) {
            rabbitTemplate.convertAndSend(OrdersMqQueueConfig.DEAD_LETTER_EXCHANGE, OrdersMqQueueConfig.DEAD_LETTER_FINISH_KEY, messJson, messagePostProcessor);
        } else {
            rabbitTemplate.convertAndSend(OrdersMqQueueConfig.DEAD_LETTER_EXCHANGE, OrdersMqQueueConfig.DEAD_LETTER_CONFIRM_KEY, messJson, messagePostProcessor);
        }
        return SUCCESS;
    }
}
