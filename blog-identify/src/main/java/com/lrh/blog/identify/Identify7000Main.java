package com.lrh.blog.identify;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.identify
 * @ClassName: IdentifyMain
 * @Author: 63283
 * @Description:
 * @Date: 2023/10/21 13:29
 */

@SpringBootApplication
@EnableDiscoveryClient
public class Identify7000Main {

    public static void main(String[] args) {

        SpringApplication.run(Identify7000Main.class, args);

    }

}
