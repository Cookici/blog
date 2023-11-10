package com.lrh.blog.chat.netty.handler;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson2.JSONObject;
import com.lrh.blog.common.domin.Message;
import com.lrh.blog.common.domin.Message2;
import com.lrh.blog.chat.domain.User;
import com.lrh.blog.chat.pojo.Topic;
import com.lrh.blog.chat.pojo.Type;
import com.lrh.blog.chat.pojo.packet.AddFriendPacket;
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

import java.nio.charset.StandardCharsets;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.chat.netty.handler
 * @ClassName: AddFriendPakcet
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/8 22:03
 */

@Slf4j
@Service
@ChannelHandler.Sharable
public class AddFriendHandler extends SimpleChannelInboundHandler<AddFriendPacket> {

    @Autowired
    private RabbitMqUtils rabbitMqUtils;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AddFriendPacket msg) throws Exception {
        String message = "";
        String toUserId = msg.getToUserId();
        Channel toUserChannel = StatusUtils.getChannel(toUserId);
        if (toUserChannel != null && StatusUtils.hasLogin(toUserChannel)) {
            message = StatusUtils.getUser(ctx.channel()).getUserName() + "要添加你为好友";
            sendMessage(ctx, message, msg.getToUserId(), Topic.OnLine, "add");
        } else {
            sendMessage(ctx, message, msg.getToUserId(), Topic.OffLine, "add");
            log.info("用户不存在或者未登录");
            return;
        }
        User toUser = StatusUtils.getUser(toUserChannel);
        ByteBuf buf = getByteBuf(ctx, message, toUser);

        toUserChannel.writeAndFlush(new TextWebSocketFrame(buf));
        log.info(StatusUtils.getUser(ctx.channel()).getUserId() + "发送了消息给" + msg.getToUserId() + "：" + message);

    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx, String message, User toUser) {
        ByteBuf byteBuf = ctx.alloc().buffer();
        User fromUser = StatusUtils.getUser(ctx.channel());
        JSONObject data = new JSONObject();
        data.put("type", Type.ADD_FRIEND_RESPONSE);
        data.put("status", 200);
        JSONObject params = new JSONObject();
        params.put("message", message);
        params.put("fromUser", fromUser);
        params.put("toUser", toUser);
        data.put("params", params);
        byte[] bytes = data.toJSONString().getBytes(StandardCharsets.UTF_8);
        byteBuf.writeBytes(bytes);
        return byteBuf;
    }

    private void sendMessage(ChannelHandlerContext ctx, String message, String toUserId, String status, String type) {
        Message messageMq = new Message();
        messageMq.setFromId(StatusUtils.getUser(ctx.channel()).getUserId());
        messageMq.setToId(toUserId);
        messageMq.setType(type);
        messageMq.setInfoContent(message);
        messageMq.setTime(new DateTime().toString());
        messageMq.setStatus(status);
        Message2 message2 = new Message2();
        BeanUtils.copyProperties(messageMq, message2);
        log.info("走到了mqUtils.MessageSend2");
        rabbitMqUtils.messageSend2(message2);
    }


}
