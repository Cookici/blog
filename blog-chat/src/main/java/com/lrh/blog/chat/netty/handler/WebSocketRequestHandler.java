package com.lrh.blog.chat.netty.handler;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.lrh.blog.chat.config.NettyConfig;
import com.lrh.blog.chat.domain.User;
import com.lrh.blog.chat.pojo.AbstractPacket;
import com.lrh.blog.chat.pojo.Type;
import com.lrh.blog.chat.pojo.packet.*;
import com.lrh.blog.chat.utils.StatusUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.chat.server.handler
 * @ClassName: WebSocketRequestHandler
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/7 17:52
 */

@Slf4j
@Service
@ChannelHandler.Sharable
public class WebSocketRequestHandler extends SimpleChannelInboundHandler<WebSocketFrame> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
        handleWebSocketFrame(ctx, msg);
    }


    /**
     * 处理客户端向服务器发送数据的业务逻辑
     *
     * @param ctx
     * @param msg
     */
    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame msg) {
        if (msg instanceof CloseWebSocketFrame) {
            ctx.channel().close();
        }

        //判断是否是ping消息
        if (msg instanceof PingWebSocketFrame) {
            ctx.channel().write(new PingWebSocketFrame(msg.content().retain()));
        }

        TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame) msg;
        ByteBuf byteBuf = textWebSocketFrame.content();
        String content = byteBuf.toString(StandardCharsets.UTF_8);
        log.info("客户端发送的消息：" + content);

        JSONObject jsonObject = JSONObject.parseObject(content);
        Byte type = jsonObject.getByte("type");
        JSONObject params = jsonObject.getJSONObject("params");

        AbstractPacket packet = null;


        switch (type) {

            case 1:
                //注测user 到 channel的映射
                RegisterPacket registerPacket = new RegisterPacket();
                User user = JSON.parseObject(params.toJSONString(), User.class);
                registerPacket.setUser(user);
                packet = registerPacket;
                break;
            case 2:
                //单聊
                SingleMessagePacket messageRequestPacket = new SingleMessagePacket();
                messageRequestPacket.setMessage(params.getString("message"));
                messageRequestPacket.setToUserId(params.getString("toMessageId"));
                messageRequestPacket.setFileType(params.getString("fileType"));
                packet = messageRequestPacket;
                break;
            case 3:
                //群聊创建
                CreateGroupPacket createGroupPacket = new CreateGroupPacket();
                String userListStr = params.getString("userIdList");
                log.info(userListStr);
                List<String> userIdList = Arrays.asList(userListStr.split(","));
                log.info(userIdList.toString());
                createGroupPacket.setUserIdList(userIdList);
                createGroupPacket.setGroupId(params.getInteger("groupId"));
                packet = createGroupPacket;
                break;
            case 4:
                //群聊消息
                GroupMessagePacket groupMessageRequestPacket = new GroupMessagePacket();
                groupMessageRequestPacket.setMessage(params.getString("message"));
                groupMessageRequestPacket.setToGroupId(params.getInteger("toMessageId"));
                groupMessageRequestPacket.setFileType(params.getString("fileType"));
                packet = groupMessageRequestPacket;
                break;
            case 5:
                //添加好友
                AddFriendPacket addFriendPacket = new AddFriendPacket();
                addFriendPacket.setToUserId(params.getString("toMessageId"));
                packet = addFriendPacket;
                break;
            case 6:
                //心跳检测
                HeartBeatPacket heartBeatPacket = new HeartBeatPacket();
                heartBeatPacket.setUserId(params.getString("userId"));
                heartBeatPacket.setMessage(params.getString("message"));
                packet = heartBeatPacket;
            default:
                break;
        }


        //返回应答消息
        //获取客户端向服务端发送的消息
        if (type.equals(Type.SINGLE_MESSAGE) || type.equals(Type.GROUP_MESSAGE)) {
            ByteBuf buf = getByteBuf(ctx, params.getString("message"));
            TextWebSocketFrame tws = new TextWebSocketFrame(buf);
            ctx.writeAndFlush(tws);
        }

        //传给下一个处理器   这里对数据的处理知识一种简单的封装
        ctx.fireChannelRead(packet);
    }

    /**
     * 客户端与服务端创建连接
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelActive ==> id:{}", ctx.channel().id());
        NettyConfig.group.add(ctx.channel());
    }

    /**
     * 客户端与服务端断开连接
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelInactive ==> id:{}", ctx.channel().id());
        StatusUtils.clearChannel(StatusUtils.getUser(ctx.channel()), ctx.channel());
        NettyConfig.group.remove(ctx.channel());
    }

    /**
     * 接收结束之后 read相对于服务端
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }


    /**
     * 出现异常时调用
     */

    public ByteBuf getByteBuf(ChannelHandlerContext ctx, String message) {
        ByteBuf byteBuf = ctx.alloc().buffer();
        User user = StatusUtils.getUser(ctx.channel());
        JSONObject data = new JSONObject();
        data.put("type", Type.SELF_RESPONSE);
        data.put("status", 200);
        JSONObject params = new JSONObject();
        params.put("user", user);
        params.put("message", message);
        params.put("date", new Date().toString());
        data.put("params", params);
        byte[] bytes = data.toJSONString().getBytes(StandardCharsets.UTF_8);
        byteBuf.writeBytes(bytes);
        return byteBuf;
    }


}
