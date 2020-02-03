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
 *
 * @Author zhaojun
 * @create 2020/1/23 15:17
 */
@Configuration
public class OrdersMqQueueConfig {
    /**
     * 消息过期后转发的死信队列
     */
    public static final String DEAD_LETTER_QUEUE_KEY = "x-dead-letter-exchange";
    /**
     * 死信队列routing_key
     */
    public static final String DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";
    /**
     * 业务消息exchange
     */
    public static final String DEAD_LETTER_EXCHANGE = "dead_letter_exchange";
    /**
     * 订单自动取消的routing key
     */
    public static final String DEAD_LETTER_CANCEL_KEY = "dead_letter_cancel_key";
    /**
     * 业务消息存放的队列，超时后转发
     * 订单自动取消队列
     */
    public static final String DEAD_LETTER_CANCEL_QUEUE = "dead_letter_cancel_queue";
    /**
     * 订单自动完成的routing key
     */
    public static final String DEAD_LETTER_FINISH_KEY = "dead_letter_finish_key";
    /**
     * 业务消息存放的队列，超时后转发
     * 订单自动完成队列
     */
    public static final String DEAD_LETTER_FINISH_QUEUE = "dead_letter_finish_queue";
    /**
     * 订单自动签收的routing key
     */
    public static final String DEAD_LETTER_CONFIRM_KEY = "dead_letter_confirm_key";
    /**
     * 业务消息存放的队列，超时后转发
     * 订单自动签收队列
     */
    public static final String DEAD_LETTER_CONFIRM_QUEUE = "dead_letter_confirm_queue";
    /**
     * 消息过期时间
     */
    public static final String X_MESSAGE_TTL = "x-message-ttl";
    /**
     * 未支付订单自动取消时间30分钟
     */
    public static final long CANCEL_TIME = 1000 * 60 * 30;
    /**
     * 用户确认收货后7天设置为已完成
     */
    public static final long FINISH_TIME = 1000 * 60 * 60 * 24 * 7;
    /**
     * 出库后24小时配送单自动签收
     */
    public static final long CONFIRM_TIME = 1000 * 60 * 60 * 24;

    /**
     * 声明exchange
     *
     * @return
     */

    @Bean
    public Exchange deadLetterExchange() {
        return ExchangeBuilder.directExchange(DEAD_LETTER_EXCHANGE).durable(true).build();
    }

    /**
     * 绑定订单取消队列queue
     *
     * @return
     */
    @Bean
    public Binding deadLetterCancelBinding() {
        return new Binding(DEAD_LETTER_CANCEL_QUEUE, Binding.DestinationType.QUEUE, DEAD_LETTER_EXCHANGE, DEAD_LETTER_CANCEL_KEY, null);
    }

    /**
     * 绑定订单完成队列queue
     *
     * @return
     */
    @Bean
    public Binding deadLetterFinishBinding() {
        return new Binding(DEAD_LETTER_FINISH_QUEUE, Binding.DestinationType.QUEUE, DEAD_LETTER_EXCHANGE, DEAD_LETTER_FINISH_KEY, null);
    }

    /**
     * 绑定订单签收队列queue
     *
     * @return
     */
    @Bean
    public Binding deadLetterConfirmBinding() {
        return new Binding(DEAD_LETTER_CONFIRM_QUEUE, Binding.DestinationType.QUEUE, DEAD_LETTER_EXCHANGE, DEAD_LETTER_CONFIRM_KEY, null);
    }

    /**
     * 设置队列属性 死信转发 过期时间等
     *
     * @return
     */
    @Bean
    public Queue forwardCancelMsg() {
        Map<String, Object> args = new HashMap<>(3);
        // 出现dead letter之后将dead letter重新发送到指定exchange
        args.put(DEAD_LETTER_QUEUE_KEY, AmqpListener.EXCHANGE_ORDERS);
        // 出现dead letter之后将dead letter重新按照指定的routing-key发送
        args.put(DEAD_LETTER_ROUTING_KEY, AmqpListener.ROUTING_ORDERS_CANCEL);
        // 设置队列消息的过期时间
        args.put(X_MESSAGE_TTL, OrdersMqQueueConfig.CANCEL_TIME);
        // name队列名字  durable是否持久化，true保证消息的不丢失, exclusive是否排他队列，如果一个队列被声明为排他队列，该队列仅对首次申明它的连接可见，并在连接断开时自动删除, autoDelete如果该队列没有任何订阅的消费者的话，该队列是否会被自动删除, arguments参数map
        return new Queue(DEAD_LETTER_CANCEL_QUEUE, true, false, false, args);
    }
    @Bean
    public Queue forwardFinishMsg() {
        Map<String, Object> args = new HashMap<>(3);
        // 出现dead letter之后将dead letter重新发送到指定exchange
        args.put(DEAD_LETTER_QUEUE_KEY, AmqpListener.EXCHANGE_ORDERS);
        // 出现dead letter之后将dead letter重新按照指定的routing-key发送
        args.put(DEAD_LETTER_ROUTING_KEY, AmqpListener.ROUTING_ORDERS_FINISH);
        // 设置队列消息的过期时间
        args.put(X_MESSAGE_TTL, OrdersMqQueueConfig.FINISH_TIME);
        // name队列名字  durable是否持久化，true保证消息的不丢失, exclusive是否排他队列，如果一个队列被声明为排他队列，该队列仅对首次申明它的连接可见，并在连接断开时自动删除, autoDelete如果该队列没有任何订阅的消费者的话，该队列是否会被自动删除, arguments参数map
        return new Queue(DEAD_LETTER_FINISH_QUEUE, true, false, false, args);
    }
    @Bean
    public Queue forwardConfirmMsg() {
        Map<String, Object> args = new HashMap<>(3);
        // 出现dead letter之后将dead letter重新发送到指定exchange
        args.put(DEAD_LETTER_QUEUE_KEY, AmqpListener.EXCHANGE_ORDERS);
        // 出现dead letter之后将dead letter重新按照指定的routing-key发送
        args.put(DEAD_LETTER_ROUTING_KEY, AmqpListener.ROUTING_ORDERS_CONFIRM);
        // 设置队列消息的过期时间
        args.put(X_MESSAGE_TTL, OrdersMqQueueConfig.CONFIRM_TIME);
        // name队列名字  durable是否持久化，true保证消息的不丢失, exclusive是否排他队列，如果一个队列被声明为排他队列，该队列仅对首次申明它的连接可见，并在连接断开时自动删除, autoDelete如果该队列没有任何订阅的消费者的话，该队列是否会被自动删除, arguments参数map
        return new Queue(DEAD_LETTER_CONFIRM_QUEUE, true, false, false, args);
    }
}
