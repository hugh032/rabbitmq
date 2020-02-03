package com.example.demo.config;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 死信交换机 队列监听
 *
 * @Author zhaojun
 * @create 2020/1/22 17:31
 */
@Component
public class AmqpListener {
    private Logger logger = LoggerFactory.getLogger(AmqpListener.class);
    /**
     * 订单状态exchange
     */
    public static final String EXCHANGE_ORDERS = "mdd_exchange_orders_state";
    /**
     * 自动取消 routing_key
     */
    public static final String ROUTING_ORDERS_CANCEL = "mdd_routing_key_orders_cancel";
    /**
     * 自动签收 routing_key
     */
    public static final String ROUTING_ORDERS_CONFIRM = "mdd_routing_key_orders_confirm";
    /**
     * 自动完成 routing_key
     */
    public static final String ROUTING_ORDERS_FINISH = "mdd_routing_key_orders_finish";
    /**
     * 未支付订单queue
     */
    public static final String QUEUE_ORDERS_CANCEL = "mdd_queue_orders_cancel";
    /**
     * 自动签收queue
     */
    public static final String QUEUE_ORDERS_CONFIRM = "mdd_queue_orders_confirm";
    /**
     * 自动完成订单queue
     */
    public static final String QUEUE_ORDERS_FINISH = "mdd_queue_orders_finish";

    /**
     * example1 手动创建队列，需要在rabbitmq后台创建queue，否则启动报错
     *
     * @param message
     */
    @RabbitListener(queues = "test_queue_1")
    public void example1(String message) {
        logger.info(message);
    }

    /**
     * example2 自动创建队列
     *
     * @param message
     */
    @RabbitListener(queuesToDeclare = @Queue(value = "test_queue_2", durable = "true"))
    public void example2(String message) {
        logger.info(message);
    }

    /**
     * example3 自动创建队列，并且声明exchange交换机，绑定queue
     *
     * @param message
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "test_queue_3"),
            exchange = @Exchange(value = "test_exchange_1"), key = "test_routing_key_1"
    ))
    public void example3(String message) {
        logger.info(message);
    }

    /**
     * 订单自动取消queue
     *
     * @param message
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = QUEUE_ORDERS_CANCEL),
            exchange = @Exchange(value = EXCHANGE_ORDERS), key = ROUTING_ORDERS_CANCEL
    ))
    public void autoCancel(Message message, Channel channel) {
        try {
            logger.info(ROUTING_ORDERS_CANCEL+ " _消费消息 {}", new String(message.getBody()));
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            //重新入列，会在队列头部，如果一直不成功，会影响正常业务
            //channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            logger.error(ROUTING_ORDERS_CANCEL+ " 消费异常，message：{}", new String(message.getBody()), e);
        }
    }

    /**
     * 订单自动确认queue
     *
     * @param message
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = QUEUE_ORDERS_CONFIRM),
            exchange = @Exchange(value = EXCHANGE_ORDERS), key = ROUTING_ORDERS_CONFIRM
    ))
    public void autoConfirm(Message message, Channel channel) {
        try {
            logger.info(ROUTING_ORDERS_CONFIRM + " 消费消息 {}", new String(message.getBody()));
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            logger.error(ROUTING_ORDERS_CONFIRM + " 消费异常，message：{}", new String(message.getBody()), e);
        }
    }

    /**
     * 订单自动完成queue
     *
     * @param message
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = QUEUE_ORDERS_FINISH),
            exchange = @Exchange(value = EXCHANGE_ORDERS), key = ROUTING_ORDERS_FINISH
    ))
    public void autoFinish(Message message, Channel channel) {
        try {
            logger.info(ROUTING_ORDERS_FINISH + " 消费消息 {}", new String(message.getBody()));
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            logger.error(ROUTING_ORDERS_FINISH + "消费异常，message：{}", new String(message.getBody()), e);
        }
    }


}
