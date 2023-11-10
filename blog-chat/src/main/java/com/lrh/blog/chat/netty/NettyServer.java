package com.lrh.blog.chat.netty;

import com.lrh.blog.chat.netty.initializer.WebSocketChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.chat.server
 * @ClassName: NettyServer
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/7 17:30
 */

@ChannelHandler.Sharable
@Component
public class NettyServer {


     private final EventLoopGroup boosGroup = new NioEventLoopGroup(1);

     private final EventLoopGroup workGroup = new NioEventLoopGroup();


     @Autowired
     private WebSocketChannelInitializer webSocketChannelHandler;

     public void run(){

         try{
             ServerBootstrap bootstrap = new ServerBootstrap();
             bootstrap.group(boosGroup,workGroup)
                     .option(ChannelOption.SO_BACKLOG,1024)
                     .channel(NioServerSocketChannel.class)
                     .childHandler(webSocketChannelHandler);
             System.out.println("服务器启动.....");
             Channel ch = bootstrap.bind(10001).sync().channel();
             ch.closeFuture().sync();
         }catch (Exception e){
             e.printStackTrace();
         }finally {
             boosGroup.shutdownGracefully();
             workGroup.shutdownGracefully();
         }


     }



}
