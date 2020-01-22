package com.example.demo.example.tx;

import com.example.demo.Utils.RabbitConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * rabbitmq事务模式 tx模式 txSelect() -- channel.txCommit() -- channel.txRollback()
 * @Author zhaojun
 * @create 2020/1/22 15:32
 */
public class TxSend {
    public static String QUEUE_NAME = "tx_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitConnectionUtils.getConnection2();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "tx test";
        try {
            channel.txSelect();
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            channel.txCommit();
        } catch (IOException e) {
            channel.txRollback();
            e.printStackTrace();
        } finally {
            channel.close();
            connection.close();
        }

    }
}
