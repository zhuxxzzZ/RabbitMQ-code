package com.JackGuo.consumer;

import com.JackGuo.Utils.RabbitMQUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.util.HashMap;


public class consumerNormal {
    //声明两个交换机正常交换机和死信交换机
    private static final String NORMAL_EXCHANGES_NAME="normal-exchange";
    private static final String DEAD_EXCHANGES_NAME="dead-exchange";
    //声明两个队列正常队列和死信队列
    private static final String NORMAL_QUEUE_NAME="normal-queue";
    private static final String DEAD_QUEUE_NAME="dead-queue";

    //声明两个route-key
    private static final String NORMAL_ROUTE_KEY="normalRoute";
    private static final String DEAD_ROUTE_KEY="deadRoute";

    public static void main(String[] args) throws Exception {
        RabbitMQUtils rabbitMQUtils=new RabbitMQUtils();
        Channel channel = rabbitMQUtils.getChannelByUtils();
//      声明正常交换机和死信交换机
        channel.exchangeDeclare(NORMAL_EXCHANGES_NAME,"direct");
        channel.exchangeDeclare(DEAD_EXCHANGES_NAME,"direct");

        HashMap<String,Object> map=new HashMap<>();
//        设置转发的死信交换机
        map.put("x-dead-letter-exchange",DEAD_EXCHANGES_NAME);
//        设置转发的死信队列的RouteKey
        map.put("x-dead-letter-routing-key",DEAD_ROUTE_KEY);
//          设置正常队列的长度限制，超过将进入死信队列】
//        map.put("x-max-length",6);


//        声明正常队列和死信队列
        channel.queueDeclare(NORMAL_QUEUE_NAME,false,false,false,map);
        channel.queueDeclare(DEAD_QUEUE_NAME,false,false,false,null);

//      绑定正常队列和死信队列
        channel.queueBind(NORMAL_QUEUE_NAME,NORMAL_EXCHANGES_NAME,NORMAL_ROUTE_KEY);
        channel.queueBind(DEAD_QUEUE_NAME,DEAD_EXCHANGES_NAME,DEAD_ROUTE_KEY);

        DeliverCallback deliverCallback=(String consumerTag, Delivery message)->{
            String msg=new String(message.getBody());
            if (msg.equals("a5")){
//             拒绝消息，false表示将消息不放回当前队列中，，true则将消息返回当前队列
                channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
            }else {
                System.out.println(new String(message.getBody()));
                channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
            }
        };
        CancelCallback cancelCallback=(String consumerTag)->{
            System.out.println("失败");
        };
        System.out.println("正常队列正在接收消息");
        channel.basicConsume(NORMAL_QUEUE_NAME,false,deliverCallback,cancelCallback);

    }



}
