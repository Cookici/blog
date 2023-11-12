package com.lrh.blog.common.utils;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.common.utils
 * @ClassName: RedisPrefix
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/12 22:04
 */

public interface RedisPrefix {
    String ADD_FRIEND = "add";

    String SINGLE_CHAT = "single";
    String SINGLE_CHAT_ONLINE = "singleOnline";

    String SINGLE_CHAT_OFFLINE = "singleOffline";

}
