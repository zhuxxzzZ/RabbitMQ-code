package com.JackGuo.controller;

import org.springframework.amqp.core.Correlation;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class providerOne {

    private final static String CONFIRM_EXCHANGES_NAME = "confirm_exchange";
    private final static String CONFIRM_ROUTING_KEY = "confirm_key";

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @RequestMapping("/send/{message}/{id}")
    public void sendMsg(@PathVariable String message, @PathVariable String id) {

        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(id);

        MessagePostProcessor messagePostProcessor = (Message msg) -> {

            System.out.println("消息发送成功" + msg);
            return msg;
        };
//        生产者发送消息设置优先级
//        for (int i=10;i>1;i--){
//            int finalI = i;
//            rabbitTemplate.convertAndSend(CONFIRM_EXCHANGES_NAME, CONFIRM_ROUTING_KEY, (Object) message+" "+i,
//                    msg->{
//                    msg.getMessageProperties().setPriority(finalI);
//                    return msg;
//                    });
//        }

        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGES_NAME, CONFIRM_ROUTING_KEY, (Object) message, messagePostProcessor, correlationData);
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGES_NAME, CONFIRM_ROUTING_KEY + "1",
                (Object) message + "1", messagePostProcessor, correlationData);
    }
}
