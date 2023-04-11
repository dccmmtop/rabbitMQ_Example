package org.example.pubsub;

import com.rabbitmq.client.*;
import org.example.util.RabbitUtils;

import java.io.IOException;

/**
 * 墨迹APP
 */
public class MoJiApp {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();
        String queueName = "moji";
        // 声明一个队列
        channel.queueDeclare(queueName,false,false,false, null);
        // 绑定队列与交换器的关系
        channel.queueBind(queueName,"weather_exchange","");
        channel.basicQos(1);
        // 监听消息
        channel.basicConsume(queueName,false, new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("墨迹APP 收到天气信息: " + new String(body));
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        });

    }
}
