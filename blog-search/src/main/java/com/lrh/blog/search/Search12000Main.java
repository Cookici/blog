package com.lrh.blog.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.search
 * @ClassName: Search12000Main
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/19 19:43
 */
@SpringBootApplication
@EnableDiscoveryClient
public class Search12000Main {
    public static void main(String[] args) {
        SpringApplication.run(Search12000Main.class, args);
    }
}
