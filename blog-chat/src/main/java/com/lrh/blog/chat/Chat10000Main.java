package com.lrh.blog.chat;

import com.lrh.blog.chat.netty.NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.chat
 * @ClassName: Chat10000Main
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/7 17:19
 */
@EnableDiscoveryClient
@SpringBootApplication
public class Chat10000Main implements CommandLineRunner {

    @Autowired
    private NettyServer nettyServer;

    public static void main(String[] args) {
        SpringApplication.run(Chat10000Main.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        nettyServer.run();
    }
}
