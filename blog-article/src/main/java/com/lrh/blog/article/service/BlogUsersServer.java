package com.lrh.blog.article.service;

import com.lrh.blog.common.entity.BlogUsers;
import com.lrh.blog.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.article.service
 * @ClassName: BlogUsersServer
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/1 16:30
 */

@Component
@FeignClient(name = "blog-identify")
public interface BlogUsersServer {

    @GetMapping("/blog/identify/getByIds")
    Result<List<BlogUsers>> getByIds(@RequestParam("ids")List<Long> ids);

}
