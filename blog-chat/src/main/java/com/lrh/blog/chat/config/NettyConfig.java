package com.lrh.blog.chat.config;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import javax.annotation.Resource;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.chat.config
 * @ClassName: NettyConfig
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/7 18:25
 */

@Resource
public class NettyConfig {

    /**
     *     储存每个客户端接入进来的channel对象
     */
    public static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

}
