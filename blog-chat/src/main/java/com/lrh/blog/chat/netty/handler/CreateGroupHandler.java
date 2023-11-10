package com.lrh.blog.chat.netty.handler;

import com.alibaba.fastjson.JSONObject;
import com.lrh.blog.chat.domain.User;
import com.lrh.blog.chat.pojo.Type;
import com.lrh.blog.chat.pojo.packet.CreateGroupPacket;
import com.lrh.blog.chat.utils.StatusUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.chat.server.handler
 * @ClassName: GreatGroupHandler
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/7 22:42
 */

@Slf4j
@Service
@ChannelHandler.Sharable
public class CreateGroupHandler extends SimpleChannelInboundHandler<CreateGroupPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupPacket msg) throws Exception {
        List<String> userIdList = msg.getUserIdList();
        List<String> nameList = new ArrayList<>();
        ChannelGroup channelGroup = new DefaultChannelGroup(ctx.executor());
        channelGroup.add(ctx.channel());
        for (String userId : userIdList) {
            log.info("遍历用户userId" + userId);
            Channel channel = StatusUtils.getChannel(userId);
            log.info("判断用户channel是否存在===>" + channel);
            if (channel != null) {
                User user = StatusUtils.getUser(channel);
                channelGroup.add(channel);
                nameList.add(user.getUserName());
            }
        }

        /*
          绑定群id和channelGroup的映射
         */
        Integer groupId = msg.getGroupId();
        StatusUtils.bindChannelGroup(groupId,channelGroup);
        ByteBuf byteBuf = getByteBuf(ctx, groupId, nameList);
        channelGroup.writeAndFlush(new TextWebSocketFrame(byteBuf));
    }

    public ByteBuf getByteBuf(ChannelHandlerContext ctx, Integer groupId, List<String> nameList) {
        ByteBuf bytebuf = ctx.alloc().buffer();
        JSONObject data = new JSONObject();
        data.put("type", Type.CREATE_GROUP_RESPONSE);
        data.put("status", 200);
        data.put("groupId", groupId);
        data.put("nameList", nameList);
        System.out.println(data);
        byte []bytes = data.toJSONString().getBytes(StandardCharsets.UTF_8);
        bytebuf.writeBytes(bytes);
        return bytebuf;
    }



}
