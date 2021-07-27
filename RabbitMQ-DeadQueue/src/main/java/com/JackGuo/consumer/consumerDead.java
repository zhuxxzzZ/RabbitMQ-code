package com.JackGuo.consumer;

import com.JackGuo.Utils.RabbitMQUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;


public class consumerDead {

    private static final String DEAD_QUEUE_NAME="dead-queue";

    public static void main(String[] args) throws Exception {

        RabbitMQUtils rabbitMQUtils=new RabbitMQUtils();
        Channel channel = rabbitMQUtils.getChannelByUtils();

        DeliverCallback deliverCallback=(String consumerTag, Delivery message)->{
            System.out.println("死信队列进行处理："+new String(message.getBody()));
        };

        CancelCallback cancelCallback=(String consumerTag)->{
            System.out.println("失败");
        };

        channel.basicConsume(DEAD_QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
