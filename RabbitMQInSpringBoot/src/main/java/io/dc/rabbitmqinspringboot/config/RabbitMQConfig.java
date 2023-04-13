package io.dc.rabbitmqinspringboot.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_NAME = "boot_topic_exchange";
    public static final String QUEUE_NAME = "boot_queue";
    // 带有死信的队列
    public static final String QUEUE_NAME_WITH_DEAD = "boot_queue_with_dead";

    // 死信消息从正常队列中移除，通过该交换机进入死信队列
    // 和正常交换机没有差别，只不过被带有死信的队列指定了
    public static final String deadExchange = "dead_exchange";
    // 死信队列,接收死信交换机过来的消息
    public static String deadQueue = "dead_queue";

    // 1. 声明交换机
    @Bean("bootTopicExchange")
    public Exchange bootExchange(){
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(false).build();
    }


    // 2. 声明队列
    @Bean("bootQueue")
    public Queue bootQueue(){
        return QueueBuilder.durable(QUEUE_NAME).build();
    }
    @Bean
    public Binding bindQueueExchange(@Qualifier("bootQueue") Queue queue, @Qualifier("bootTopicExchange") Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("boot.#").noargs();
    }



    // 声明TTL队列
    @Bean("bootQueueTTL")
    public Queue bootQueueTTL(){
        Map<String, Object> arg = new HashMap<>();
        arg.put("x-message-ttl",10000);
        return QueueBuilder.durable("bootQueueTTL").withArguments(arg).build();
    }

    // 将延时队列与交换器进行绑定
    @Bean
    public Binding bindTTLQueueExchange(@Qualifier("bootQueueTTL") Queue queue, @Qualifier("bootTopicExchange") Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("boot.#").noargs();
    }




    // 声明死信交换器,和普通交换器一样
    @Bean("bootDeadExchange")
    public Exchange bootDeadExchange() {
        return ExchangeBuilder.topicExchange(deadExchange).durable(false).build();
    }

    // 声明接收死信的队列
    @Bean("bootDeadQueue")
    public Queue bootDeadQueue(){
        return QueueBuilder.durable(deadQueue).build();
    }
    // 把接收死信的队列与死信交换器绑定
    @Bean
    public Binding bindWithDeadQueueExchange(@Qualifier("bootDeadQueue") Queue queue, @Qualifier("bootDeadExchange") Exchange exchange){

        return BindingBuilder.bind(queue).to(exchange).with("boot.#").noargs();
    }

    //********* 这才是死信队列的重要步骤 ***************
    // 声明带有死信的队列
    @Bean("bootWithDeadQueue")
    public Queue bootWithDeadQueue(){
        Map<String, Object> arg = new HashMap<>();
        // 声明接收死信消息的交换器
        // *****这一步很重要，可以让死信通过 deadExchange 发走****
        arg.put("x-dead-letter-exchange",deadExchange);
        return QueueBuilder.durable(QUEUE_NAME_WITH_DEAD).withArguments(arg).build();
    }

    // 将带有死信的队列绑定到交换机上
    @Bean
    public Binding bindDeadQueueExchange(@Qualifier("bootWithDeadQueue") Queue queue, @Qualifier("bootTopicExchange") Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("boot.#").noargs();
    }


    // 自定义连接工厂, 自由程度高,本次演示没用到
    @Bean("customConnectionFactory")
    public ConnectionFactory connectionFactory(
            @Value("${spring.rabbitmq.host}") String host,
            @Value("${spring.rabbitmq.port}") int port,
            @Value("${spring.rabbitmq.username}") String username,
            @Value("${spring.rabbitmq.password}") String password,
            @Value("${spring.rabbitmq.virtual-host}") String vhost
    ){
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);
        factory.setVirtualHost(vhost);

        return factory;
    }

    // 自定义消息监听器工厂, 自由程度高
    @Bean(name = "customListenFactory")
    public SimpleRabbitListenerContainerFactory listenerContainerFactory(@Qualifier("customConnectionFactory") ConnectionFactory connectionFactory){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        // 设置手动签收
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        // 设置预处理数量，同时没有确认的消息不能超过N个,起到消费者限流的作用
        factory.setPrefetchCount(5);
        return factory;
    }
}
