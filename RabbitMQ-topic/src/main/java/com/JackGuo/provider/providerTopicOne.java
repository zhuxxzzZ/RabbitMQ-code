package com.JackGuo.provider;

import com.JackGuo.Utils.RabbitMQUtils;
import com.JackGuo.consumer.consumerTopicOne;
import com.rabbitmq.client.Channel;

import java.util.Scanner;


public class providerTopicOne {

    private static final String EXCHANGES_NAME = "Topic";
    private static final String ROUTE_KEY1="info.topic";
    private static final String ROUTE_KEY2="error.topic";
    public static void main(String[] args) throws Exception {
        RabbitMQUtils rabbitMQUtils = new RabbitMQUtils();
        Channel channel = rabbitMQUtils.getChannelByUtils();
        channel.exchangeDeclare(EXCHANGES_NAME, "topic");

//        发送消息
        Scanner scanner = new Scanner(System.in);
        System.out.println("发送消息。。。");
        while (scanner.hasNext()) {
            String msg = scanner.next();
            channel.basicPublish(EXCHANGES_NAME, ROUTE_KEY2, null, msg.getBytes());
            System.out.println("发送消息成功" + msg);
        }

    }


}
