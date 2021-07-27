package com.JackGuo.consumer;

import com.JackGuo.Utils.RabbitMQUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.io.IOException;

public class consumerPublishOne {

    private static final String EXCHANGES_NAME="publish";
    private static final String ROUTE_KEY="TEST";
    public static void main(String[] args) throws IOException {
        RabbitMQUtils rabbitMQUtils=new RabbitMQUtils();
        Channel channel= rabbitMQUtils.getChannelByUtils();

        channel.exchangeDeclare(EXCHANGES_NAME,"fanout");
        String queue = channel.queueDeclare().getQueue();
        channel.queueBind(queue ,EXCHANGES_NAME,ROUTE_KEY);

        DeliverCallback deliverCallback=(String consumerTag, Delivery message)->{
            System.out.println(new String(message.getBody()));
        };
        CancelCallback cancelCallback=(String consumerTag) ->{
            System.out.println("失败");
        };
        System.out.println("消费者1接收消息");
        channel.basicConsume(queue ,true,deliverCallback,cancelCallback);
    }
}
