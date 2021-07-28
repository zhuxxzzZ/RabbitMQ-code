package com.JackGuo.config;

import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component//1注入步骤
public class MyCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    /**
     * @param correlationData 保存回调信息的ID和相关信息
     * @param ack             交换机是否接收到消息
     * @param cause           引起消息发送失败的原因，成功则为null
     */
    @Autowired
    private RabbitTemplate rabbitTemplate;//2

    @PostConstruct  //3注入最后一步，在其他注入成功后才能成功
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }


    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : " ";
        if (ack) {
            System.out.println("交换机收到消息ID为：" + id);
        } else {
            System.out.println("交换机没有收到消息，原因为：" + cause);
        }

    }


    /**
     * 实现回退接口
     *
     * @param returned 当消息没有达到目的地时将消息回退给生产者
     */
    @Override
    public void returnedMessage(ReturnedMessage returned) {
        System.out.println("消息被回退了消息为：" + returned.getMessage() + "处理的交换机为：" + returned.getExchange()
                + "路由为：" + returned.getRoutingKey());
        System.out.println();

    }
}
