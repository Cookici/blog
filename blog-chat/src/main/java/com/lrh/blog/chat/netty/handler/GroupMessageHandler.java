package com.lrh.blog.chat.netty.handler;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSONObject;
import com.lrh.blog.common.domin.Message;
import com.lrh.blog.common.domin.Message2;
import com.lrh.blog.chat.domain.User;
import com.lrh.blog.chat.pojo.Topic;
import com.lrh.blog.chat.pojo.Type;
import com.lrh.blog.chat.pojo.packet.GroupMessagePacket;
import com.lrh.blog.chat.utils.RabbitMqUtils;
import com.lrh.blog.chat.utils.StatusUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.chat.server.handler
 * @ClassName: CreatGroupHandler
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/7 22:12
 */

@Slf4j
@Service
@ChannelHandler.Sharable
public class GroupMessageHandler extends SimpleChannelInboundHandler<GroupMessagePacket> {

    @Autowired
    private RabbitMqUtils rabbitMqUtils;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessagePacket msg) throws Exception {
        log.info("进入GroupMessageHandler");
        Integer groupId = msg.getToGroupId();
        String fileType = msg.getFileType();
        ChannelGroup channelGroup = StatusUtils.getChannelGroup(groupId);
        log.info(" GroupMessageHandler channelGroup" + channelGroup);
        List<String> userIdList = new ArrayList<>();
        for (Channel channel : channelGroup) {
            User user = StatusUtils.getUser(channel);
            userIdList.add(user.getUserId());
        }
        sendMessage(ctx, msg.getMessage(), userIdList, groupId.toString(), Topic.OnLine, "group");
        if (channelGroup != null) {
            User user = StatusUtils.getUser(ctx.channel());
            ByteBuf byteBuf = getByteBuf(ctx, groupId, msg.getMessage(), user, fileType, userIdList);
            /*
             * 发送方不需要自己再收到消息
             */
            channelGroup.remove(ctx.channel());
            channelGroup.writeAndFlush(new TextWebSocketFrame(byteBuf));
            channelGroup.add(ctx.channel());
        }
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx, Integer groupId, String message,
                               User fromUser, String fileType, List<String> userIdList) {
        ByteBuf byteBuf = ctx.alloc().buffer();
        JSONObject data = new JSONObject();
        data.put("type", Type.GROUP_MESSAGE_RESPONSE);
        data.put("status", 200);
        JSONObject params = new JSONObject();
        params.put("message", message);
        params.put("fileType", fileType);
        params.put("fromUser", fromUser);
        params.put("groupId", groupId);
        Collections.reverse(userIdList);
        params.put("nameList", userIdList);
        data.put("params", params);
        byte[] bytes = data.toJSONString().getBytes(StandardCharsets.UTF_8);
        byteBuf.writeBytes(bytes);
        return byteBuf;
    }

    private void sendMessage(ChannelHandlerContext ctx, String message, List<String> userIdList, String toUser, String status, String type) {
        Message messageMQ = new Message();
        messageMQ.setFromId(StatusUtils.getUser(ctx.channel()).getUserId());
        messageMQ.setToId(toUser);
        messageMQ.setType(type);
        messageMQ.setInfoContent(message);
        messageMQ.setTime(new DateTime().toString());
        messageMQ.setStatus(status);
        messageMQ.setUserIdList(userIdList);
        Message2 message2 = new Message2();
        BeanUtils.copyProperties(messageMQ, message2);
        rabbitMqUtils.messageSend2(message2);
    }


}
