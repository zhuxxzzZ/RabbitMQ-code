package com.JackGuo.consumer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class consumerTwo {
    private final static String BACKUP_QUEUE_WARNING = "warning_queue";

    @RabbitListener(queues =BACKUP_QUEUE_WARNING)
    public void getMsg(Message message){
        String msg=new String(message.getBody());
        System.out.println("报警队列收到消息"+msg);
    }
}
