package com.JackGuo.consumer;


import com.JackGuo.Utils.RabbitMQUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class consumerRouteTwo {

    private static final String EXCHANGES_NAME="Route";
    private static final String ROUTE_KEY="error";
    private static final String QUEUE_NAME="disk";

    public static void main(String[] args) throws Exception {
        RabbitMQUtils rabbitMQUtils=new RabbitMQUtils();
        Channel channel=rabbitMQUtils.getChannelByUtils();
        channel.exchangeDeclare(EXCHANGES_NAME,"direct");
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        channel.queueBind(QUEUE_NAME,EXCHANGES_NAME,ROUTE_KEY);
        DeliverCallback deliverCallback=(String consumerTag, Delivery message)->{
            System.out.println(new String(message.getBody()));
        };
        CancelCallback cancelCallback=(String consumerTag)->{
            System.out.println("false");
        };
        System.out.println("disk接收消息");
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }


}
