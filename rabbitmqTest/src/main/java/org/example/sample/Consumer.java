package org.example.sample;

import com.rabbitmq.client.*;
import org.example.util.RabbitUtils;

import java.io.IOException;

/**
 * 消费者
 */
public class Consumer {
    public static void main(String[] args) throws IOException {
        // 获取连接
        Connection connection = RabbitUtils.getConnection();
        // 获取chanel
        Channel channel = connection.createChannel();
        // 要绑定的队列, 参数同消费者
        String queueName = "hello";
        channel.queueDeclare(queueName,false,false,false,null);
        // 接受并处理消息
        // 队列名称
        // 是否自动确认收到消息, false 代表需要编程手动来确认，这是MQ推荐的做法
        // 用来处理接收到的消息，是 DefaultConsumer 的实现类
        channel.basicConsume(queueName,false,new Reciver(channel));
        // 不要关闭连接，要持续等待消息的到来
    }
}

class Reciver extends DefaultConsumer {

    private Channel channel;
    public Reciver(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        String msg = new String(body);
        System.out.println("消费者收到消息: " + msg);
        System.out.println("消息的TagId: " + envelope.getDeliveryTag());
        // 第二个参数： false 代表只确认签收当前的消息，true: 代表签收该消费者所有未签收的消息
        channel.basicAck(envelope.getDeliveryTag(),false);
    }
}
