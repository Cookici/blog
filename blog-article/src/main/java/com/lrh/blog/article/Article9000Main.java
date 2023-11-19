package com.lrh.blog.article;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.article
 * @ClassName: com.lrh.blog.article.Article6000Main
 * @Author: 63283
 * @Description:
 * @Date: 2023/10/31 11:01
 */

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class Article9000Main {
    public static void main(String[] args) {
        SpringApplication.run(Article9000Main.class, args);
    }
}
