package com.JakGuo.provider;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * 生产者：发送消息
 */
public class ProducerOne {
//    设置队列名称1
    private final static String QUEUE_NAME="HELLO" ;
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
//           生成一个队列
// 队列声明  queueDeclare(): queue – 队列的名称
//持久 durable - 如果我们声明一个持久队列，则为true（该队列将在服务器重启后继续存在）
//独占 exclusive – 如果我们声明独占队列（仅限于此连接），则为 true可以共享
//autoDelete – 如果我们声明一个自动删除队列，则为 true（服务器将在不再使用时将其删除）
//参数 - 队列的其他属性（构造参数）
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        channel.queueDeclare().getQueue();
//        交换机 - 将消息发布到的交换机
//        routingKey – 路由密钥
//        props - 消息的其他属性 - 路由标头等
//        body – 消息正文
        String message="Hello,JackGuo";
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes(StandardCharsets.UTF_8));
        System.out.println("消息发送完毕");
        channel.close();
        connection.close();

    }

}
