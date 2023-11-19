package com.lrh.blog.chat.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

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

    @Value("backup-exchange")
    private String backupExchange;

    @Value("backup-queue")
    private String backupQueue;

    @Value("warn-queue")
    private String warnQueue;


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
        Map<String, Object> arguments = new HashMap<>(1);
        arguments.put("alternate-exchange",backupExchange);
        return new TopicExchange(exchange, true, false,arguments);
    }


    /**
     *  绑定队列 交换机 和 key
     */
    @Bean
    public Binding binding(){
        return new Binding(queue, Binding.DestinationType.QUEUE, exchange, routingKey, null);
    }

    /**
     * 备份交换机
     */
    @Bean
    public FanoutExchange backupExchange(){
        return new FanoutExchange(backupExchange,true, false);
    }

    /**
     * 备份交换机队列
     */
    @Bean
    public Queue backQueue(){
        return new Queue(backupQueue, true, false, false);
    }


    /**
     * 备份交换机警告队列
     */
    @Bean
    public Queue warningQueue(){
        return new Queue(warnQueue,true, false, false);
    }


    /**
     * 备份绑定
     */
    @Bean
    public Binding backupBinding(){
        return new Binding(backupQueue, Binding.DestinationType.QUEUE, backupExchange, null, null);
    }


    /**
     * 备份警告绑定
     */
    @Bean
    public Binding warnBinding(){
        return new Binding(warnQueue, Binding.DestinationType.QUEUE, backupExchange, null, null);
    }

}
