package com.JakGuo.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConsumerOne {
    private static final String QUEUE_NAME="HELLO";
    public static void main(String[] args) throws IOException, TimeoutException {
        //        创建一个链接RabbitMQ的工厂（工厂模式）
        ConnectionFactory factory=new ConnectionFactory();
        //        工厂ip，就是我们linux上部署的RabbitMQ，在这里进行引用
        factory.setHost("192.168.121.128");
        factory.setUsername("admin");
        factory.setPassword("admin");
//        创建链接
        Connection connection = factory.newConnection();
//        增加一个信道Channel
        Channel channel=connection.createChannel();
//       消费者接收消息
//        queue – 队列的名称
//        autoAck – 如果服务器应该考虑消息一旦发送就确认为true自动； 如果服务器应该期待明确的确认，则为 false手动
//        参数 - 消费的一组参数
//        DeliverCallback – 传递消息时的回调
//        cancelCallback – 消费者取消时的回调
//        成功回调使用lambda进行实现
        DeliverCallback deliverCallback=(consumerTag,message) ->{
            System.out.println("消息为："+new String(message.getBody()));
        };
        //       失败返回使用lambda进行实现
        CancelCallback cancelCallback=(consumerTag)->{
            System.out.println("接收消息失败");
        };
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
        System.out.println("消息接收完毕");

    }
}
