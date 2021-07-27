package com.JackGuo.provider;

import com.JackGuo.Utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;

public class providerLength {
    private static final String NORMAL_EXCHANGES_NAME="normal-exchange";
    private static final String NORMAL_ROUTE_KEY="normalRoute";

    public static void main(String[] args) throws Exception {
        RabbitMQUtils rabbitMQUtils=new RabbitMQUtils();
        Channel channel= rabbitMQUtils.getChannelByUtils();

        for (int i=1;i<11;i++){
            String msg=" "+i;
            channel.basicPublish(NORMAL_EXCHANGES_NAME,NORMAL_ROUTE_KEY,null,msg.getBytes(StandardCharsets.UTF_8));
        }

    }
}
