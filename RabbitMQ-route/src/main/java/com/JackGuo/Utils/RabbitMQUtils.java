package com.JackGuo.Utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQUtils {

    public Connection getConnectionByUtils(){
        ConnectionFactory factory=new ConnectionFactory();
//        工厂ip，就是我们linux上部署的RabbitMQ，在这里进行引用
        factory.setHost("192.168.121.128");
        factory.setUsername("admin");
        factory.setPassword("admin");
//        创建链接
        Connection connection = null;
        try {
            connection = factory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        return connection;
    }
        public Channel getChannelByUtils(){

            Channel channel =null;
//        增加一个信道Channel
            try {
                channel=getConnectionByUtils().createChannel();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return channel;
        }
}
