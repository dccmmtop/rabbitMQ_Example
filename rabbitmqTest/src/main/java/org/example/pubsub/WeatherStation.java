package org.example.pubsub;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.example.util.RabbitUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 气象站
 */
public class WeatherStation {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();

        String weather= "{'date': '2023-04-10','location':'郑州','text': '晴，16℃~28℃'}";
        // 第一个参数: 指定交换器
        // 第二个参数: 路由key, 暂时用不到。
        // 第三个参数: 额外信息
        // 第四个参数: 消息内容，字节形式
        // 与工作队列模式不同，这里直接发布消息了，没有指定发布到哪个队列中，这是由RabbitMQ 中的交换器来决定的
        channel.basicPublish("weather_exchange","",null,weather.getBytes());

        weather= "{'date': '2023-04-10','location':'新乡','text': '多云，10℃~26℃'}";
        channel.basicPublish("weather_exchange","",null,weather.getBytes());

        channel.close();
        connection.close();
    }
}
