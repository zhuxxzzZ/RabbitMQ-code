package com.JackGuo.provider;

import com.JackGuo.Utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;


public class providerPublishOne {

    private static final String EXCHANGES_NAME = "publish";
    private static final String ROUTE_KEY = "TEST";

    public static void main(String[] args) throws Exception {
        RabbitMQUtils rabbitMQUtils = new RabbitMQUtils();
        Channel channel = rabbitMQUtils.getChannelByUtils();
        channel.exchangeDeclare(EXCHANGES_NAME, "fanout");

//        发送消息
        Scanner scanner = new Scanner(System.in);
        System.out.println("发送消息。。。");
        while (scanner.hasNext()) {
            String msg = scanner.next();
            channel.basicPublish(EXCHANGES_NAME, ROUTE_KEY, null, msg.getBytes());
            System.out.println("发送消息成功" + msg);
        }

    }


}
