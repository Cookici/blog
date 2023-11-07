package com.lrh.blog.chat.pojo;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.chat.pojo.packet
 * @ClassName: Commond
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/7 18:02
 */

public interface Type {

    /**
     * 自己发送的消息确认
     */
    Byte SELF_RESPONSE = 0;

    /**
     * 注册请求
     */
    Byte REGISTER = 1;

    /**
     * 私聊消息
     */
    Byte SINGLE_MESSAGE = 2;

    /**
     * 创建群聊
     */
    Byte CREATE_GROUP = 3;

    /**
     * 群聊消息
     */
    Byte GROUP_MESSAGE = 4;

    /**
     * 私聊响应
     */
    Byte SINGLE_MESSAGE_RESPONSE = 5;

    /**
     * 创建群响应
     */
    Byte CREATE_GROUP_RESPONSE = 6;

    /**
     * 接收群消息
     */
    Byte GROUP_MESSAGE_RESPONSE = 7;

}
