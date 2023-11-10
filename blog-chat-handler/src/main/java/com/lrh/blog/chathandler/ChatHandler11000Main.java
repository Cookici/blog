package com.lrh.blog.chathandler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.chathandler
 * @ClassName: ChatHanlder11000Main
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/9 11:10
 */

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ChatHandler11000Main {
    public static void main(String[] args) {
        SpringApplication.run(ChatHandler11000Main.class,args);
    }
}
