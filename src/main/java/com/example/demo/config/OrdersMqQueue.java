package com.example.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单消息队列绑定死信队列
 * @Author hugh
 * @create 2020/1/23 15:17
 */
@Configuration
public class OrdersMqQueue {
    /**
     * 消息过期后转发的死信队列
     */
    private static final String DEAD_LETTER_QUEUE_KEY = "x-dead-letter-exchange";
    /**
     * 死信队列routing_key
     */
    private static final String  DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";

    /**
     *
     * @return
     */

    @Bean
    public Exchange deadLetterExchange() {
        return ExchangeBuilder.directExchange("DEAD_LETTER_EXCHANGE").durable(true).build();
    }
    @Bean
    public Queue forwardMsg(){
        Map<String, Object> args = new HashMap<>(2);
        // 出现dead letter之后将dead letter重新发送到指定exchange
        args.put(DEAD_LETTER_QUEUE_KEY, AmqpListener.EXCHANGE_ORDERS);
        // 出现dead letter之后将dead letter重新按照指定的routing-key发送
        args.put(DEAD_LETTER_ROUTING_KEY, AmqpListener.ROUTING_ORDERS_CANCEL);
        // name队列名字  durable是否持久化，true保证消息的不丢失, exclusive是否排他队列，如果一个队列被声明为排他队列，该队列仅对首次申明它的连接可见，并在连接断开时自动删除, autoDelete如果该队列没有任何订阅的消费者的话，该队列是否会被自动删除, arguments参数map
        return new Queue("DEAD_LETTER_QUEUE", true, false, false, args);
    }
    @Bean
    public Binding deadLetterBinding() {
        return new Binding("DEAD_LETTER_QUEUE", Binding.DestinationType.QUEUE, "DEAD_LETTER_EXCHANGE", "DEAD_LETTER_KEY", null);
    }
}
