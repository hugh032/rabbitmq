package com.example.demo;

import com.example.demo.config.RabbitConfig;
import com.example.demo.entity.Message;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@SpringBootTest
@RunWith(SpringRunner.class)
class DemoApplicationTests {
    @Autowired
    private RabbitConfig config;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Test
    void contextLoads() {
        System.out.println(config.getHost());
    }

    @Test
    public void test_send() {
    }
}
