package com.JackGuo.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.util.Date;
/**
 * 消息消费者接收死信队列中的消息
 */
@Component
public class consumerTTL {

    private final static String DEAD_QUEUE_NAME="dead";


    //声明接收消息的死信队列
    @RabbitListener(queues = DEAD_QUEUE_NAME)
    public void receiveDead(Message message, Channel channel){
        String msg=new String(message.getBody());
        System.out.printf("现在时间：%s，接收消息：%s",new Date().toString(),msg);
    }

}
