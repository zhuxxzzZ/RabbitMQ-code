package com.JackGuo.consumer;


import com.JackGuo.Utils.RabbitMQUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class consumerTopicTwo {

    private static final String EXCHANGES_NAME="Topic";
    private static final String QUEUE_NAME="disk";

    public static void main(String[] args) throws Exception {
        RabbitMQUtils rabbitMQUtils=new RabbitMQUtils();
        Channel channel=rabbitMQUtils.getChannelByUtils();
        channel.exchangeDeclare(EXCHANGES_NAME,"topic");
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        channel.queueBind(QUEUE_NAME,EXCHANGES_NAME,"error.#");
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
