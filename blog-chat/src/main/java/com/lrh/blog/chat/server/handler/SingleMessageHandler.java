package com.lrh.blog.chat.server.handler;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson2.JSONObject;
import com.lrh.blog.chat.domain.Message;
import com.lrh.blog.chat.domain.Message2;
import com.lrh.blog.chat.domain.User;
import com.lrh.blog.chat.pojo.Topic;
import com.lrh.blog.chat.pojo.Type;
import com.lrh.blog.chat.pojo.packet.SingleMessagePacket;
import com.lrh.blog.chat.utils.RabbitMqUtils;
import com.lrh.blog.chat.utils.StatusUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.chat.server.handler
 * @ClassName: SingleMessageHandler
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/7 20:26
 */

@Slf4j
@Service
@ChannelHandler.Sharable
public class SingleMessageHandler extends SimpleChannelInboundHandler<SingleMessagePacket> {

    @Autowired
    private RabbitMqUtils rabbitMqUtils;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SingleMessagePacket msg) throws Exception {

        String message = "";
        Channel toUserChannel = StatusUtils.getChannel(msg.getToUserId());

        if (toUserChannel != null && StatusUtils.hasLogin(toUserChannel)) {
            message = msg.getMessage();
            sendMessage(ctx, message, msg.getToUserId(), Topic.OnLine, true);
        } else {
            message = msg.getMessage();
            sendMessage(ctx, message, msg.getToUserId(), Topic.OffLine, true);
            log.info("用户不存在或者未登录");
            return;
        }
        User toUser = StatusUtils.getUser(toUserChannel);
        String fileType = msg.getFileType();
        ByteBuf buf = getByteBuf(ctx, message, toUser, fileType);

        toUserChannel.writeAndFlush(new TextWebSocketFrame(buf));
        log.info(StatusUtils.getUser(ctx.channel()).getUserId() + "发送了消息给" + msg.getToUserId() + "：" + msg.getMessage());


    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx, String message, User toUser, String fileType) {
        ByteBuf byteBuf = ctx.alloc().buffer();
        User fromUser = StatusUtils.getUser(ctx.channel());
        JSONObject data = new JSONObject();
        data.put("type", Type.SINGLE_MESSAGE_RESPONSE);
        data.put("status", 200);
        com.alibaba.fastjson.JSONObject params = new com.alibaba.fastjson.JSONObject();
        params.put("message", message);
        params.put("fileType", fileType);
        params.put("fromUser", fromUser);
        params.put("toUser", toUser);
        data.put("params", params);
        byte[] bytes = data.toJSONString().getBytes(StandardCharsets.UTF_8);
        byteBuf.writeBytes(bytes);
        return byteBuf;
    }

    private void sendMessage(ChannelHandlerContext ctx, String message, String toUserId, String status, boolean type) {
        Message messageMq = new Message();
        messageMq.setFromId(StatusUtils.getUser(ctx.channel()).getUserId());
        messageMq.setToId(toUserId);
        messageMq.setType(type);
        messageMq.setInfoContent(message);
        messageMq.setTime(new DateTime().toString());
        messageMq.setStatus(status);
        //mqUtils.MessageSend(Topic.OnLineTopic,messageMQ);
        Message2 message2 = new Message2();
        BeanUtils.copyProperties(messageMq, message2);
        log.info("走到了mqUtils.MessageSend2");
        rabbitMqUtils.messageSend2(message2);
    }
}
