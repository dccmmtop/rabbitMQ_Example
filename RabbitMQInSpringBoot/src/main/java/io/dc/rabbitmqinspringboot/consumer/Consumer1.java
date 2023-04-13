//package io.dc.rabbitmqinspringboot.consumer;
//
//import io.dc.rabbitmqinspringboot.config.RabbitMQConfig;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//import org.springframework.amqp.core.Message;
//
//@Component
//public class Consumer1 {
//    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
//    public void listenerQueue(Message message){
//        System.out.println("消费者1接收到消息: "+ message);
//    }
//}
