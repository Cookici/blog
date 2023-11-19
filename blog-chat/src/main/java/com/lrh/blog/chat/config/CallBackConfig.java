package com.lrh.blog.chat.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.chat.config
 * @ClassName: CallBackConfig
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/19 11:55
 */
@Slf4j
@Component
public class CallBackConfig implements RabbitTemplate.ConfirmCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    private void init() {
        rabbitTemplate.setConfirmCallback(this);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("交换机已经收到了ID为：{}的消息", id);
        } else {
            log.info("交换机还没有收到ID为：{}的消息，原因为：{}", id, cause);
        }
    }
}
