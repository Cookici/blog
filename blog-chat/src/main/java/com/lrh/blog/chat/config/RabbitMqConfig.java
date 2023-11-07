package com.lrh.blog.chat.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.common.config
 * @ClassName: RabbitMqConfig
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/7 20:33
 */

@Configuration
public class RabbitMqConfig {
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
