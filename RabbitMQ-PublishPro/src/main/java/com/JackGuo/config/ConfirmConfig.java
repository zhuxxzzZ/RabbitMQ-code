package com.JackGuo.config;

import org.omg.CORBA.TRANSACTION_MODE;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class ConfirmConfig {

    private final static String CONFIRM_EXCHANGES_NAME = "confirm_exchange";
    private final static String CONFIRM_QUEUE_NAME = "confirm_queue";
    private final static String CONFIRM_ROUTING_KEY = "confirm_key";


    //备份交换机
    private final static String BACKUP_EXCHANGE_NAME = "backup_exchange";
    private final static String BACKUP_QUEUE_NORMAL = "back_queue";
    private final static String BACKUP_QUEUE_WARNING = "warning_queue";

    @Bean("confirmExchange")
    public DirectExchange confirmExchange() {
        //进行转发
        HashMap<String, Object> map = new HashMap<>();
        map.put("alternate-exchange", BACKUP_EXCHANGE_NAME);
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGES_NAME).durable(true).withArguments(map).build();
    }

    @Bean("backupExchange")
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }


    @Bean("confirmQueue")
    public Queue confirmQueue() {
        HashMap<String, Object> map = new HashMap<>();
//        map.put("x-max-priority",10); //设置队列优先级为10
//        map.put("x-queue-mode","lazy"); //设置队列为惰性队列
       return QueueBuilder.durable(CONFIRM_QUEUE_NAME).withArguments(map).build();
    }

    @Bean("backupQueue")
    public Queue backupQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE_NORMAL).build();
    }

    @Bean("warningQueue")
    public Queue warningQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE_WARNING).build();
    }

    //    绑定
    @Bean
    public Binding queueBindingExchange(@Qualifier("confirmExchange") DirectExchange confirmExchange
            , @Qualifier("confirmQueue") Queue confirmQueue) {
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(CONFIRM_ROUTING_KEY);

    }

    @Bean
    public Binding backupQueueBindingExchange(@Qualifier("backupExchange") FanoutExchange backupExchange
            , @Qualifier("backupQueue") Queue backupQueue) {
        return BindingBuilder.bind(backupQueue).to(backupExchange);

    }

    @Bean
    public Binding warningQueueBindingExchange(@Qualifier("backupExchange") FanoutExchange backupExchange
            , @Qualifier("warningQueue") Queue warningQueue) {
        return BindingBuilder.bind(warningQueue).to(backupExchange);

    }

}
