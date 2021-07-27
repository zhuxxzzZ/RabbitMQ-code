package com.JackGuo.consumer;

import cn.hutool.core.thread.ThreadUtil;
import com.JackGuo.Utils.RabbitMQUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class consumerTwo {
    private final static String QUEUE_NAME="WORK";

    public static void main(String[] args) throws Exception {
        RabbitMQUtils rabbitMQUtils=new RabbitMQUtils();
        Channel channel = rabbitMQUtils.getChannelByUtils();
        System.out.println("C2正在接收消息");

        DeliverCallback deliverCallback=(String consumerTag, Delivery message)->{
            ThreadUtil.sleep(10000);
            System.out.println(new String(message.getBody()));

//            DeliveryTag – 来自收到的AMQP.Basic.GetOk或AMQP.Basic.Deliver的标签
//            multiple– true 确认所有消息，包括提供的交付标签； false 仅确认提供的交付标签。
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);

        };
        CancelCallback cancelCallback=(String consumerTag)->{
            System.out.println("消费者1失败");
        };


//        在消费者设置不公平分发
        int prefetchCount=3;
        channel.basicQos(prefetchCount);

        channel.basicConsume(QUEUE_NAME,false,deliverCallback,cancelCallback);


    }

}