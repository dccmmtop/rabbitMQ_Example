package org.example.wokkerqueue;

import com.rabbitmq.client.*;
import org.example.util.RabbitUtils;

import java.io.IOException;

/**
 * 消费者
 */
public class Consumer2 {
    public static void main(String[] args) throws IOException {
        // 获取连接
        Connection connection = RabbitUtils.getConnection();
        // 获取chanel
        Channel channel = connection.createChannel();
        // 要绑定的队列, 参数同消费者
        String queueName = "hello";
        channel.queueDeclare(queueName,false,false,false,null);
        // 处理完一个再取一个。公平模式, 默认是轮询机制
//        channel.basicQos(1);
        // 接受并处理消息
        // 队列名称
        // 是否自动确认收到消息, false 代表需要编程手动来确认，这是MQ推荐的做法
        // 用来处理接收到的消息
        channel.basicConsume(queueName, false, new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body);
                System.out.println("消费者2收到消息: " + msg + "。TagId: " + envelope.getDeliveryTag());
                try {
                    // 模拟处理任务的耗时
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                // 第二个参数： false 代表只确认签收当前的消息，true: 代表签收该消费者所有未签收的消息
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        });
        // 不要关闭连接，要持续等待消息的到来
    }
}
