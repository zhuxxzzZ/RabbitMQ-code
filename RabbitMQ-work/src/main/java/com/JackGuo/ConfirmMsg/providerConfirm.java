package com.JackGuo.ConfirmMsg;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.JackGuo.Utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 发布确认三种方式
 * 单个确认：
 * 批量确认：
 * 异步确认：
 */

public class providerConfirm {

    private final static int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
//        Long time1 = confirmMsgIndividually();
//        System.out.println("单个发布确认"+time1);
//        Long time2=confirmMsgBatch();
//        System.out.println("批量发布确认"+time2);
        Long time3 = confirmMsgAsync();
        System.out.println("异步发布确认" + time3);

    }

    //    单个确认
    public static Long confirmMsgIndividually() throws Exception {
        RabbitMQUtils rabbitMQUtils = new RabbitMQUtils();
        Channel channel = rabbitMQUtils.getChannelByUtils();
        String QUEUE_NAME = UUID.randomUUID().toString(); //生成随机数来代替队列名
        channel.confirmSelect();
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        TimeInterval timer = DateUtil.timer(); //hutools计时器
//        发送信息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String msg = " " + i;
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_BASIC, msg.getBytes(StandardCharsets.UTF_8));
            channel.waitForConfirms();
        }
        return timer.interval();
    }

    // 批量确认
    public static Long confirmMsgBatch() throws Exception {
        RabbitMQUtils rabbitMQUtils = new RabbitMQUtils();
        Channel channel = rabbitMQUtils.getChannelByUtils();
        String QUEUE_NAME = UUID.randomUUID().toString(); //生成随机数来代替队列名
        channel.confirmSelect();
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        TimeInterval timer = DateUtil.timer(); //hutools计时器

//     批量确认消息大小（每发送多少条消息确认一次）
        int Batch = 100;
//        发送信息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String msg = " " + i;
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_BASIC, msg.getBytes(StandardCharsets.UTF_8));
            if (i % Batch == 0) {
                channel.waitForConfirms();
            }
        }


        return timer.interval();
    }

    //    异步批量处理
    public static Long confirmMsgAsync() throws Exception {
        RabbitMQUtils rabbitMQUtils = new RabbitMQUtils();
        Channel channel = rabbitMQUtils.getChannelByUtils();
        String QUEUE_NAME = UUID.randomUUID().toString(); //生成随机数来代替队列名
        channel.confirmSelect();
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        TimeInterval timer = DateUtil.timer(); //hutools计时器

        /**
         * 处理未确认的消息
         * 1,打印所有发送消息（创建一个线程安全有序的哈希表ConcurrentSkipListMap，适用于高并发，来使得序号与消息进行对应）
         * 2，找出所有已经确认消息
         * 3，总消息减去确认消息得到未确认的消息
         */
        ConcurrentSkipListMap<Long,String> outstandingConfirms=new ConcurrentSkipListMap<>();
//        准备消息的监听器，监听哪些消息成功，哪些消息失败
        //成功回调函数
        ConfirmCallback ackCallback = (long deliveryTag, boolean multiple) -> {
            if (multiple){
                ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(deliveryTag);
                confirmed.clear();
            }else {
                outstandingConfirms.remove(deliveryTag);
            }
            System.out.println("已确认的消息" + deliveryTag);
        };
        //失败回调函数        deliveryTag：发送的消息的id标签 multiple：是否批量处理
        ConfirmCallback nackCallback = (long deliveryTag, boolean multiple) -> {
            String s= outstandingConfirms.get(deliveryTag);
            System.out.println("未确认的消息" + deliveryTag+"-----"+s);
        };
        channel.addConfirmListener(ackCallback, nackCallback);
//        发送信息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String msg = " " + i;
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_BASIC, msg.getBytes(StandardCharsets.UTF_8));
            outstandingConfirms.put(channel.getNextPublishSeqNo(),msg); //在确认模式下，返回要发布的下一条消息的序列号。
        }

        return timer.interval();
    }

}
