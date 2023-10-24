package com.lrh.blog.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.oss
 * @ClassName: OssMain
 * @Author: 63283
 * @Description:
 * @Date: 2023/10/23 18:45
 */

@SpringBootApplication
@EnableDiscoveryClient
public class Oss8000Main {

    public static void main(String[] args) {

        SpringApplication.run(Oss8000Main.class);

    }

}
