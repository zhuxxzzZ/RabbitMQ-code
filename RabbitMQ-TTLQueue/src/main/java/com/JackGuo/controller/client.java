package com.JackGuo.controller;


import cn.hutool.core.thread.ThreadUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 消息生产者：       发送延迟消息
 *
 */
@RestController
public class client {
    //    声明两个交换机
    private  final static String X_EXCHANGES_NAME="X";
    //  Routing key
    private final static String NORMAL_ROUTE_KEY1="A_KEY";
    private final static String NORMAL_ROUTE_KEY2="B_KEY";
    private final static String NORMAL_ROUTE_KEY3="C_KEY";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("/send/{message}")
    public void sendMessage(@PathVariable String message){
        System.out.printf("当前时间：%s,发送消息：%s",new Date().toString(),message);

        rabbitTemplate.convertAndSend(X_EXCHANGES_NAME,NORMAL_ROUTE_KEY1,"消息来自TTL为10s的队列"+message);
        rabbitTemplate.convertAndSend(X_EXCHANGES_NAME,NORMAL_ROUTE_KEY2,"消息来自TTL为40s的队列"+message);
    }

//    从生产者来将消息TLL进行设置
    @RequestMapping("/sendOptimize/{message}/{TTL}")
    public void sendMessageOptimize(@PathVariable String message,@PathVariable String TTL){
        System.out.printf("当前时间：%s,发送的消息为：%s ,发送消息TTL为：%s",new Date().toString(),message,TTL);
        rabbitTemplate.convertAndSend(X_EXCHANGES_NAME,NORMAL_ROUTE_KEY3,"发送的消息为："+message,msg->{
            msg.getMessageProperties().setExpiration(TTL);
            return msg;
        });
    }


}
