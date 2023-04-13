//package io.dc.rabbitmqinspringboot.consumer;
//
//import com.rabbitmq.client.Channel;
//import io.dc.rabbitmqinspringboot.config.RabbitMQConfig;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
//import org.springframework.stereotype.Component;
//
//@Component
//public class Consumer2 {
////    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME, containerFactory = "customListenFactory")
//    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
//    public void onMessage(Message message, Channel channel) throws Exception {
//        System.out.println("消费者2收到消息: " + new String(message.getBody()));
//        long deliveryTag = message.getMessageProperties().getDeliveryTag();
//        System.out.println("消息Id: " + deliveryTag);
//        Thread.sleep(2000);
//        // 进行消息签收
//        channel.basicAck(deliveryTag, true);
//
//        // 拒绝签收
//        // 最后一个参数： 是否重回队列
////        channel.basicNack(deliveryTag,false, false);
//    }
//}
