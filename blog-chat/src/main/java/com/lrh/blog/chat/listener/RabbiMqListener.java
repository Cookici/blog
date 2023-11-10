package com.lrh.blog.chat.listener;

import com.lrh.blog.common.domin.Message;
import com.lrh.blog.chat.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.chat.listener
 * @ClassName: RabbiMqListener
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/8 16:51
 */

@Slf4j
@Component
public class RabbiMqListener {

    @Autowired
    private RedisUtils redisUtils;

    @RabbitListener(queues = "chat.queue")
    public void getMessage(Message message) {
        System.out.println(message);
        judgeMessage(message);
    }

    private void judgeMessage(Message message) {
        if ("add".equals(message.getType())) {
            redisUtils.hset("add" + message.getToId(), message.getFromId(), message);
        }
    }

}
