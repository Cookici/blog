package com.lrh.blog.article.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lrh.blog.article.service.BlogArticlesService;
import com.lrh.blog.article.service.BlogUsersServer;
import com.lrh.blog.common.entity.BlogArticles;
import com.lrh.blog.common.entity.BlogUsers;
import com.lrh.blog.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lrh
 * @since 2023-10-31
 */
@RestController
@RequestMapping("/blog/articles")
public class BlogArticlesController {

    @Autowired
    private BlogArticlesService blogArticlesService;

    @Autowired
    private BlogUsersServer blogUsersServer;

    @GetMapping("/getAll/{index}")
    public Result<Map<String, Object>> getAllBlogArticles(@PathVariable Integer index) {
        Page<BlogArticles> page = new Page<>(index, 2);
        IPage<BlogArticles> allBlogArticles = blogArticlesService.getAllBlogArticles(page);
        List<BlogArticles> records = allBlogArticles.getRecords();
        List<Long> ids = new ArrayList<>();
        records.forEach(item -> ids.add(item.getUserId()));
        Long[] idsFormat = new Long[ids.size()];
        for (int i = 0; i < idsFormat.length; i++) {
            idsFormat[i] = ids.get(i);
        }
        Result<List<BlogUsers>> byIds = blogUsersServer.getByIds(idsFormat);

        List<BlogUsers> articlesForUsers = byIds.getData();


        for (BlogArticles article : records) {
            if (ids.contains(article.getUserId())) {
                BlogUsers blogUsers = articlesForUsers.stream()
                        .filter(user -> user.getUserId().equals(article.getUserId())).findFirst().get();
                article.setBlogUsers(blogUsers);
            }
        }

        Map<String, Object> map = new HashMap<>();

        map.put("data", allBlogArticles.getRecords());
        map.put("total", allBlogArticles.getTotal());
        map.put("pageAll", allBlogArticles.getPages());

        return Result.ok(map);
    }

}

