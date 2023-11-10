package com.lrh.blog.chat.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.chat.config
 * @ClassName: MqConfig
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/8 11:21
 */

@Configuration
public class MqConfig {

    @Value("chat-exchange")
    private String exchange;

    @Value("chat.queue")
    private String queue;

    @Value("chat")
    private String routingKey;

    /**
     * 创建一个队列
     */
    @Bean
    public Queue queue(){
        return new Queue(queue, true, false, false);
    }

    /**
     *  创建一个交换机
     */
    @Bean
    public Exchange exchange(){
        return new TopicExchange(exchange, true, false);
    }
    //创建一个队列

    /**
     *  绑定队列 交换机 和 key
     */
    @Bean
    public Binding binding(){
        return new Binding(queue, Binding.DestinationType.QUEUE, exchange, routingKey, null);
    }

}
