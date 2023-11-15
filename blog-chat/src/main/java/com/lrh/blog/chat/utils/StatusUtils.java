package com.lrh.blog.chat.utils;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.chat.utils
 * @ClassName: StatusUtils
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/7 19:33
 */


import com.lrh.blog.chat.domain.User;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import lombok.extern.slf4j.Slf4j;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 退出后的操作
 */
@Slf4j
public class StatusUtils {

    /**
     * UserId 映射 连接 channel
     */
    public static Map<String, Channel> userIdChannelMap = new ConcurrentHashMap<>();

    /**
     * groupId ----> channelGroup 群聊id和群聊ChannelGroup映射
     */
    private static Map<Integer, ChannelGroup> groupIdChannelGroupMap
            = new ConcurrentHashMap<>();


    public static void bindChannel(User user, Channel channel) {
        userIdChannelMap.put(user.getUserId(), channel);
        log.info(user.getUserId() + "上线服务器...");
        log.info("总channel:{}", userIdChannelMap);
        channel.attr(Attributes.STATUS).set(user);
    }


    /**
     * channel.attr(Attributes.STATUS).set(xx)可以设置为null hasLogin然后取出来判断是否为null
     *
     * @param user
     * @param channel
     */
    public static void clearChannel(User user, Channel channel) {
        userIdChannelMap.remove(user.getUserId());
        log.info(user.userId + "已经退出...");
        channel.attr(Attributes.STATUS).remove();
    }

    public static boolean hasLogin(Channel channel) {
        return channel.hasAttr(Attributes.STATUS);
    }

    public static User getUser(Channel channel) {
        return channel.attr(Attributes.STATUS).get();
    }

    public static Channel getChannel(String userId) {
        return userIdChannelMap.get(userId);
    }

    /**
     * 绑定群聊Id 群聊channelGroup
     *
     * @param groupId
     * @param channelGroup
     */
    public static void bindChannelGroup(Integer groupId, ChannelGroup channelGroup) {
        groupIdChannelGroupMap.put(groupId, channelGroup);
        log.info("channelGroup:{}", channelGroup);
        for (Channel channel : channelGroup) {
            log.info("bindChannelGroup channel ====> {}", channel);
        }
        log.info("channelGroupSize:{}", channelGroup.size());
        for (Map.Entry<Integer, ChannelGroup> entry : groupIdChannelGroupMap.entrySet()) {
            log.info("groupId ===> {},channelGroup ===> {}", entry.getKey(), entry.getValue());
        }
    }

    public static ChannelGroup getChannelGroup(Integer groupId) {
        return groupIdChannelGroupMap.get(groupId);
    }

    public static Map<String, Channel> getAllOnlineChannel() {
        return userIdChannelMap;
    }

}
