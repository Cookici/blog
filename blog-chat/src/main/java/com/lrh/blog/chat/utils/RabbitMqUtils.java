package com.lrh.blog.chat.utils;

import cn.hutool.core.lang.UUID;
import com.lrh.blog.common.domin.Message2;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.chat.utils
 * @ClassName: RabbitMqUtills
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/7 20:27
 */

@Component
public class RabbitMqUtils {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void messageSend2(Message2 message2) {
        message2.setId(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("chat-exchange", "chat", message2, new CorrelationData(message2.getId()));
    }


}
