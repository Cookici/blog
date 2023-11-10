package com.lrh.blog.chat.netty.handler;

import com.alibaba.fastjson2.JSONObject;
import com.lrh.blog.chat.pojo.Type;
import com.lrh.blog.chat.pojo.packet.HeartBeatPacket;
import com.lrh.blog.chat.utils.StatusUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.chat.netty.handler
 * @ClassName: HeartBeatHandler
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/10 16:03
 */

@Slf4j
@Service
@ChannelHandler.Sharable
public class HeartBeatHandler extends SimpleChannelInboundHandler<HeartBeatPacket> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartBeatPacket msg) throws Exception {
        String message = "服务器pong";
        ByteBuf buf = getByteBuf(ctx, message);
        Channel channel = StatusUtils.getChannel(msg.getUserId());
        channel.writeAndFlush(new TextWebSocketFrame(buf));
        log.info("客户端:{}", msg.getMessage());
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx, String message) {
        ByteBuf byteBuf = ctx.alloc().buffer();
        JSONObject data = new JSONObject();
        data.put("type", Type.HEART_BEAT);
        data.put("status", 200);
        JSONObject params = new JSONObject();
        params.put("message", message);
        data.put("params", params);
        byte[] bytes = data.toJSONString().getBytes(StandardCharsets.UTF_8);
        byteBuf.writeBytes(bytes);
        return byteBuf;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("ctx={},evt={}", ctx, evt);
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                log.info("客户端读空闲");
            } else if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                log.info("客户端写空闲");
            } else if (idleStateEvent.state() == IdleState.ALL_IDLE) {
                log.info("客户端读写空闲");
                StatusUtils.clearChannel(StatusUtils.getUser(ctx.channel()), ctx.channel());
                ctx.channel().close();
            }
        }
        super.userEventTriggered(ctx, evt);
    }

}
