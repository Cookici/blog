package com.lrh.blog.common.utils;

import com.lrh.blog.common.domin.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.common.utils
 * @ClassName: RedisPrefixUtils
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/12 21:48
 */

public class RedisPrefixUtils {

    private RedisPrefixUtils() {

    }

    public static StringBuilder getStringBuilder(String fromId, String toId, String type) {
        StringBuilder stringBuffer = new StringBuilder(type);
        List<Long> list = new ArrayList<>();
        Collections.addAll(list, Long.valueOf(fromId), Long.valueOf(toId));
        list = list.stream().sorted().collect(Collectors.toList());
        for (Long id : list) {
            stringBuffer.append("-").append(id);
        }
        return stringBuffer;
    }


    public static String groupPrefix(String groupId) {
        StringBuilder stringBuffer = new StringBuilder();
        StringBuilder groupPrefix = stringBuffer.append("group").append("-").append(groupId);
        return groupPrefix.toString();
    }

    public static String groupMessagePrefix(String groupId) {
        StringBuilder stringBuffer = new StringBuilder();
        StringBuilder groupMessagePrefix = stringBuffer.append("message").append("-").append(groupId);
        return groupMessagePrefix.toString();
    }

    public static String groupUserIdPrefix(String userId) {
        StringBuilder stringBuffer = new StringBuilder();
        StringBuilder groupUserIdPrefix = stringBuffer.append("userId:").append(userId);
        return groupUserIdPrefix.toString();
    }


    public static String groupHistoryMessagePrefix(String groupId) {
        StringBuilder stringBuffer = new StringBuilder();
        StringBuilder groupHistoryMessagePrefix = stringBuffer.append("historyMessage").append("-").append("group").append(groupId);
        return groupHistoryMessagePrefix.toString();
    }


}
