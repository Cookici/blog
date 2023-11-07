package com.lrh.blog.chat.utils;

import com.lrh.blog.chat.domain.Message2;
import org.aspectj.bridge.Message;
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

    public void messageSend(String topic, Object message){

    }

    public void messageSend2(Message2 message2){
        rabbitTemplate.convertAndSend("chat-exchange","chat",message2);
    }


}
