package io.dc.rabbitmqinspringboot;

import io.dc.rabbitmqinspringboot.config.RabbitMQConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class RabbitMqInSpringBootApplicationTests {

    @Autowired
    RabbitTemplate rabbitTemplate;


    @Test
    public void sendMsg() {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME,"boot.dc","你好 rabbitMQ");
    }

    @Test
    public void testConfirm(){
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean b, String s) {
                System.out.println("执行了confirm 方法...");
                if(b){
                    System.out.println("发送成功");
                }else{
                    System.out.println("发送失败: " + s);
                }
            }
        });

        // 正常
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME,"boot.dc","你好 rabbitMQ");
        // 错误的交换器。会打印发送失败
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME + "111","boot.dc","你好 rabbitMQ");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testReturn(){
        // 只有设置true，消息无法到达队列时，才会退回给生产者
       rabbitTemplate.setMandatory(true);
       rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
           /**
            *
            * @param message the returned message.
            * @param replyCode the reply code.
            * @param replyText the reply text.
            * @param exchange the exchange.
            * @param routingKey the routing key.
            */
           @Override
           public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
               System.out.println("消息退回了~");
               System.out.println("message: " + message.toString());
               System.out.println("replyCode: " + replyCode);
               System.out.println("replyText: " + replyText);
               System.out.println("exchange: " + exchange);
               System.out.println("routingKey: " + routingKey);
           }
       });
        // 正常
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME,"boot.dc","你好 rabbitMQ");
        // 错误的路由key。消息会被退回
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME,"1111_boot.dc","你好 rabbitMQ");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
