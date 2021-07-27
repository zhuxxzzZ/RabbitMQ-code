package com.JackGuo.config;


import org.springframework.amqp.core.*;
import org.springframework.beans.PropertyBatchUpdateException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * 此配置类用于声明交换机和队列
 */
@Configuration
public class TTLQueueConfig {


    //    声明两个交换机
    private final static String X_EXCHANGES_NAME = "X";
    private final static String Y_EXCHANGES_NAME = "Y";

    //    声明三个队列
    private final static String A_QUEUE_NAME = "A";
    private final static String B_QUEUE_NAME = "B";
    private final static String C_QUEUE_NAME = "C";
    private final static String DEAD_QUEUE_NAME = "dead";

    //  Routing key
    private final static String NORMAL_ROUTE_KEY1 = "A_KEY";
    private final static String NORMAL_ROUTE_KEY2 = "B_KEY";
    private final static String NORMAL_ROUTE_KEY3 = "C_KEY";
    private final static String DEAD_ROUTE_KEY = "DEAD_KEY";

    @Bean("xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(X_EXCHANGES_NAME);
    }

    @Bean("yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(Y_EXCHANGES_NAME);
    }

    //创建队列
    @Bean("queueA")
    public Queue queueA() {
        HashMap<String, Object> map = new HashMap<>();
//        设置死信交换机
        map.put("x-dead-letter-exchange", Y_EXCHANGES_NAME);
//        设置死信队列route-key
        map.put("x-dead-letter-routing-key", DEAD_ROUTE_KEY);
//        设置TTL活跃时间==10s
        map.put("x-message-ttl", 10000);
        return QueueBuilder.durable(A_QUEUE_NAME).withArguments(map).build();
    }

    @Bean("queueB")
    public Queue queueB() {
        HashMap<String, Object> map = new HashMap<>();
//        设置死信交换机
        map.put("x-dead-letter-exchange", Y_EXCHANGES_NAME);
//        设置死信队列route-key
        map.put("x-dead-letter-routing-key", DEAD_ROUTE_KEY);
//        设置TTL活跃时间==40s
        map.put("x-message-ttl", 40000);
        return QueueBuilder.durable(B_QUEUE_NAME).withArguments(map).build();
    }

    @Bean("queueC")
    public Queue queueC() {
        HashMap<String, Object> map = new HashMap<>();
//        设置死信交换机
        map.put("x-dead-letter-exchange", Y_EXCHANGES_NAME);
//        设置死信队列route-key
        map.put("x-dead-letter-routing-key", DEAD_ROUTE_KEY);
        return QueueBuilder.durable(C_QUEUE_NAME).withArguments(map).build();
    }

    @Bean("queueDead")
    public Queue queueDead() {
        return QueueBuilder.durable(DEAD_QUEUE_NAME).build();
    }

    //    队列和交换机进行绑定
    @Bean
    public Binding queueABanding(@Qualifier("queueA") Queue queueA,
                                 @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueA).to(xExchange).with(NORMAL_ROUTE_KEY1);
    }

    @Bean
    public Binding queueBBanding(@Qualifier("queueB") Queue queueB,
                                 @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueB).to(xExchange).with(NORMAL_ROUTE_KEY2);
    }

    @Bean
    public Binding queueCBanding(@Qualifier("queueC") Queue queueC,
                                 @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueC).to(xExchange).with(NORMAL_ROUTE_KEY3);
    }

    @Bean
    public Binding queueDeadBanding(@Qualifier("queueDead") Queue queueDead,
                                    @Qualifier("yExchange") DirectExchange yExchange) {
        return BindingBuilder.bind(queueDead).to(yExchange).with(DEAD_ROUTE_KEY);
    }


}
