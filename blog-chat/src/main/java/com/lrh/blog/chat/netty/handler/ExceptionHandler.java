package com.lrh.blog.chat.netty.handler;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.chat.server.handler
 * @ClassName: ExceptionHandler
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/7 23:01
 */
@Slf4j
@ChannelHandler.Sharable
public class ExceptionHandler extends ChannelDuplexHandler {
    public static ExceptionHandler INSTANCE = new ExceptionHandler();

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        if (cause instanceof RuntimeException) {

            log.info("出现错误.... ===> {}", cause.getMessage());

            ByteBuf byteBuf = ctx.alloc().buffer();
            JSONObject data = new JSONObject();

            data.put("type", 500);
            data.put("status", 500);

            byteBuf.writeBytes(data.toJSONString().getBytes());

            ctx.channel().writeAndFlush(new TextWebSocketFrame(byteBuf));

        }
    }
}
