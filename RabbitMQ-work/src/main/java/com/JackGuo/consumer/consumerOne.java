package com.JackGuo.consumer;

import com.JackGuo.Utils.RabbitMQUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class consumerOne {

    private final static String QUEUE_NAME="WORK";

    public static void main(String[] args) throws Exception {
        RabbitMQUtils rabbitMQUtils=new RabbitMQUtils();
        Channel channel = rabbitMQUtils.getChannelByUtils();

        DeliverCallback deliverCallback=(String consumerTag, Delivery message)->{
            System.out.println(new String(message.getBody()));
        };
        CancelCallback cancelCallback=(String consumerTag)->{
            System.out.println("消费者1失败");
        };
        System.out.println("C1正在接收消息");
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);



    }
}
