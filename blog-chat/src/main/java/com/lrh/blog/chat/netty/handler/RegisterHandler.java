package com.lrh.blog.chat.netty.handler;

import com.lrh.blog.chat.domain.User;
import com.lrh.blog.chat.pojo.Status;
import com.lrh.blog.chat.pojo.packet.RegisterPacket;
import com.lrh.blog.chat.utils.StatusUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.chat.server.handler
 * @ClassName: RegisterHandler
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/7 20:09
 */
@Slf4j
@Service
@ChannelHandler.Sharable
public class RegisterHandler extends SimpleChannelInboundHandler<RegisterPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RegisterPacket msg) throws Exception {
        User user = msg.getUser();
        if ((Status.LOGIN).equals(user.getLoginStatus())) {
            StatusUtils.bindChannel(user, ctx.channel());
            if (StatusUtils.hasLogin(ctx.channel())) {
                log.info(user.getUserName() + "已经在服务器有资源了");
            }
        }

        if((Status.NO_LOGIN).equals(user.getLoginStatus())){
            StatusUtils.clearChannel(user,ctx.channel());
            System.out.println(StatusUtils.getAllOnlineChannel());
        }
    }





}
