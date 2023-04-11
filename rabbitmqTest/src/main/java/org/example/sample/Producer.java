package org.example.sample;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.example.util.RabbitUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生产者
 */
public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        // 获得连接
        Connection connection = RabbitUtils.getConnection();
        // 获得 channel
        Channel channel = connection.createChannel();
        String queueName = "hello";
        // 声明队列并创建一个队列
        // 第一个参数： 队列名称
        // 第二个参数: 是否持久化队列, 不持久化队列时，MQ 重启后，队列中的消息会丢失
        // 第三个参数: 是否私有化队列，false 代表所有消费者都可以访问，true 代表只有第一次拥有它的消费者才能访问
        // 第四个参数: 是否自动删除，false 代表连接停掉后不自动删除这个队列
        // 其他额外参数： null
        channel.queueDeclare(queueName,false,false,false, null);
        // 发送消息
        String msg = "你好 dc";
        // 交换机： 简单模式下用不到, 后面的发布订阅模式下用到
        // 队列名称
        // 额外的参数
        // 发送的消息，字节形式
        for (int i = 0; i < 1000000; i++) {
            String m1 = msg + i;
            channel.basicPublish("", queueName,null,m1.getBytes());
//            Thread.sleep(200);
        }
        System.out.println("消息已发送");
        channel.close();
        // 关闭连接
        connection.close();
    }

}
