package org.example.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitUtils {
    private static ConnectionFactory connectionFactory  = new ConnectionFactory();
    static {
        connectionFactory.setHost("127.0.0.1");
        // 默认端口号
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("dc");
        connectionFactory.setPassword("Aa111111");
        // 设置要连接的虚机
        connectionFactory.setVirtualHost("/dc0407");
    }

    public static Connection getConnection(){
        Connection connection = null;
        try {
            connection = connectionFactory.newConnection();
            return connection;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
