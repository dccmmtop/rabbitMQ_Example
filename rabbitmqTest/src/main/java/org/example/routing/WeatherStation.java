package org.example.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.example.util.RabbitUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * 气象站
 */
public class WeatherStation {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();

        List<Weather> weatherList = new ArrayList<>();
        weatherList.add(new Weather("zhengzhou","郑州晴，16℃~28℃"));
        weatherList.add(new Weather("xinxiang","新乡多云，10℃~26℃"));

        for (Weather weather : weatherList) {
            // 指定了路由key， 路由key的格式是地区
            channel.basicPublish("weather_routing_exchange",weather.getKey(),null,weather.getMsg().getBytes());
        }

        channel.close();
        connection.close();
    }
}

class Weather{
    private String key;
    private String msg;

    public Weather(String key, String msg) {
        this.key = key;
        this.msg = msg;
    }

    public String getKey() {
        return key;
    }

    public String getMsg() {
        return msg;
    }
}
