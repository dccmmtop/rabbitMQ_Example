package org.example.confirm;

import com.rabbitmq.client.*;
import org.example.util.RabbitUtils;

import java.io.IOException;

/**
 * 消息的可靠投递
 */
public class Producer {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();
        // 开启监听模式
        channel.confirmSelect();
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("消息正常投递了");
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("消息投递失败");
            }
        });
        //如果交换器不存在，会直接丢弃，但是在spring中，会回调nack 方法
        channel.basicPublish("weather_routing_exchange","zhengzhou",null,"正常的消息".getBytes());


        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消息被退回");
                System.out.println("replyText: " + replyText);
                System.out.println("exchange: " + exchange);
                System.out.println("routingKey: " + routingKey);
                System.out.println("body: " + new String(body));
            }
        });

        //如果交换器不存在，会直接丢弃，但是在spring中，会回调nack 方法
        channel.basicPublish("weather_routing_exchange","zhengzhou11l",null,"可以到达交换器，但无法投递到队列".getBytes());

    }
}
