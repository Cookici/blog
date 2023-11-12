package com.lrh.blog.chat.listener;

import com.lrh.blog.chat.pojo.Topic;
import com.lrh.blog.common.domin.Message;
import com.lrh.blog.chat.utils.RedisUtils;
import com.lrh.blog.common.utils.RedisPrefix;
import com.lrh.blog.common.utils.RedisPrefixUtils;
import javafx.print.Collation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.lrh.blog.common.utils.RedisPrefixUtils.getStringBuilder;

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
        if (RedisPrefix.ADD_FRIEND.equals(message.getType())) {
            redisUtils.hset(RedisPrefix.ADD_FRIEND + message.getToId(), message.getFromId(), message);
        } else if (RedisPrefix.SINGLE_CHAT.equals(message.getType())) {
            if (Topic.OnLine.equals(message.getStatus())) {
                StringBuilder singleOnline = RedisPrefixUtils.getStringBuilder(message.getFromId(),message.getToId(), RedisPrefix.SINGLE_CHAT_ONLINE);
                redisUtils.lSet(singleOnline.toString(), message);
            } else if (Topic.OffLine.equals(message.getStatus())) {
                StringBuilder stringBuffer = RedisPrefixUtils.getStringBuilder(message.getFromId(),message.getToId(), RedisPrefix.SINGLE_CHAT_OFFLINE);
                redisUtils.lSet(stringBuffer.toString(), message);
            }
        } else {

        }
    }


}
