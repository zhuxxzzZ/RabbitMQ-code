package com.JackGuo.provider;


import com.JackGuo.Utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class providerOne {
    private final static String QUEUE_NAME="WORK";

    public static void main(String[] args) throws Exception {
        RabbitMQUtils rabbitMQUtils=new RabbitMQUtils();
        Channel channel = rabbitMQUtils.getChannelByUtils();
        channel.confirmSelect(); //开启发布确认
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);

        Scanner scanner=new Scanner(System.in);
        while (scanner.hasNext()){
            String message=scanner.next();
            channel.basicPublish("",QUEUE_NAME, MessageProperties.PERSISTENT_BASIC,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("消息发送完毕"+message);
        }

    }
}
