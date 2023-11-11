package com.lrh.blog.chathandler.service;

import com.lrh.blog.common.entity.BlogUsers;
import com.lrh.blog.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.chathandler.service
 * @ClassName: BlogUsersServer
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/10 22:43
 */

@Component
@FeignClient(name = "blog-identify")
public interface BlogUsersServer {

    @GetMapping("/blog/identify/getById")
    public Result<BlogUsers> getUserById(@RequestParam Long id);

    @GetMapping("/blog/identify/get")
    public Result<BlogUsers> getByUserName(@RequestParam String username);

}