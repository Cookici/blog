package com.lrh.blog.chat.netty.initializer;

import com.lrh.blog.chat.netty.handler.*;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.chat.server.handler
 * @ClassName: WebSocketChannelHandler
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/7 17:35
 */
@Service
@ChannelHandler.Sharable
public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private WebSocketRequestHandler webSocketRequestHandler;

    @Autowired
    private RegisterHandler registerHandler;

    @Autowired
    private AddFriendHandler addFriendHandler;

    @Autowired
    private SingleMessageHandler singleMessageHandler;

    @Autowired
    private CreateGroupHandler createGroupHandler;

    @Autowired
    private GroupMessageHandler groupMessageHandler;

    @Autowired
    private HeartBeatHandler heartBeatHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("httpServerCodec", new HttpServerCodec())
                /*
                 HttpObjectAggregator 因为http在传输过程中是分段的，HttpObjectAggregator可以将多个段聚合起来
                  这就是为什么当浏览器发送大量数据时，会发出多次http请求
                 */
                .addLast("httpObjectAggregator", new HttpObjectAggregator(65536))
                /*
                 HttpContent 压缩
                 */
                .addLast("httpChunkedWriteHandler", new ChunkedWriteHandler())
                /*
                 WebSocketServerProtocolHandler 对应websocket，它的数据是以 帧(frame)形式 传递
                  可以看到 WebSocketFrame 下有六个子类
                  浏览器请求时，ws://localhost:port/XXX 表示请求的资源
                  核心功能是 将http协议升级为ws协议，保持长连接
                 */
                .addLast("idleStateHandler", new IdleStateHandler(30, 30, 60))
                .addLast("webSocketServerProtocolHandler", new WebSocketServerProtocolHandler("/"))
                .addLast("baseHandler", webSocketRequestHandler)
                .addLast("registerHandler", registerHandler)
                .addLast("heartBeatHandler", heartBeatHandler)
                .addLast("addFriendHandler", addFriendHandler)
                .addLast("singleMessageHandler", singleMessageHandler)
                .addLast("createGroupHandler", createGroupHandler)
                .addLast("groupMessageHandler", groupMessageHandler)
                .addLast(ExceptionHandler.INSTANCE);

    }


}
