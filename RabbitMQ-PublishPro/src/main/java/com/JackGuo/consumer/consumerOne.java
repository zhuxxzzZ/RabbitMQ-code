package com.JackGuo.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class consumerOne {


    private final static String CONFIRM_QUEUE_NAME="confirm_queue";

    @RabbitListener(queues =CONFIRM_QUEUE_NAME )
    public void getMsg(Message message, Channel channel){
        String msg=new String(message.getBody());
        System.out.println("收到消息"+msg);
    }
}
