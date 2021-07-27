package com.JackGuo.provider;

import cn.hutool.Hutool;
import cn.hutool.core.thread.ThreadUtil;
import com.JackGuo.Utils.RabbitMQUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;

public class providerTTL {


    private static final String NORMAL_EXCHANGES_NAME="normal-exchange";
    private static final String NORMAL_ROUTE_KEY="normalRoute";

    public static void main(String[] args) throws Exception {
        RabbitMQUtils rabbitMQUtils=new RabbitMQUtils();
        Channel channel= rabbitMQUtils.getChannelByUtils();

//        设置消息TTL（time to live 消息活跃时间）==10s
        AMQP.BasicProperties props=new AMQP.BasicProperties().builder().expiration("10000").build();

        for (int i=1;i<11;i++){
            String msg=" "+i;
            channel.basicPublish(NORMAL_EXCHANGES_NAME,NORMAL_ROUTE_KEY,props,msg.getBytes(StandardCharsets.UTF_8));
        }

    }
}
