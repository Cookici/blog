package com.lrh.blog.chat.listener;

import com.alibaba.fastjson2.JSON;
import com.lrh.blog.chat.pojo.Topic;
import com.lrh.blog.common.domin.Message;
import com.lrh.blog.chat.utils.RedisUtils;
import com.lrh.blog.common.utils.RedisPrefix;
import com.lrh.blog.common.utils.RedisPrefixUtils;
import javafx.print.Collation;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.K;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.*;
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
        log.info("message  ===> {}", message);
        judgeMessage(message);
    }

    /**
     * TODO fileType未实现
     */
    private void judgeMessage(Message message) {
        if (RedisPrefix.ADD_FRIEND.equals(message.getType())) {
            redisUtils.hset(RedisPrefix.ADD_FRIEND + message.getToId(), message.getFromId(), message);
        } else if (RedisPrefix.SINGLE_CHAT.equals(message.getType())) {
            if (Topic.OnLine.equals(message.getStatus())) {
                StringBuilder singleOnline = RedisPrefixUtils.getStringBuilder(message.getFromId(), message.getToId(), RedisPrefix.SINGLE_CHAT_ONLINE);
                redisUtils.lSet(singleOnline.toString(), message);
            } else if (Topic.OffLine.equals(message.getStatus())) {
                StringBuilder stringBuffer = RedisPrefixUtils.getStringBuilder(message.getFromId(), message.getToId(), RedisPrefix.SINGLE_CHAT_OFFLINE);
                redisUtils.lSet(stringBuffer.toString(), message);
            }
        } else {
            String uuid = UUID.randomUUID().toString();
            redisUtils.hset(RedisPrefixUtils.groupMessagePrefix(message.getToId()), uuid, message);
            List<String> userIdList = message.getUserIdList();
            for (String userId : userIdList) {
                redisUtils.hget(RedisPrefixUtils.groupMessagePrefix(message.getToId()), uuid);
                redisUtils.setBit(uuid, Long.parseLong(String.valueOf(redisUtils.hget(RedisPrefixUtils.groupPrefix(message.getToId()), RedisPrefixUtils.groupUserIdPrefix(userId)))), true);
            }
        }
    }


}
