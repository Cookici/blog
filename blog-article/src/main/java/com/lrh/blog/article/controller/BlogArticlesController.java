package com.lrh.blog.article.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lrh.blog.article.service.BlogArticlesService;
import com.lrh.blog.article.service.BlogUsersServer;
import com.lrh.blog.common.entity.BlogArticles;
import com.lrh.blog.common.entity.BlogUsers;
import com.lrh.blog.common.result.Result;
import com.lrh.blog.common.utils.DecodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.util.resources.LocaleData;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        Page<BlogArticles> page = new Page<>(index, 5);
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
        for (BlogUsers articlesForUser : articlesForUsers) {
            articlesForUser.setUserPassword(null);
        }


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


    @GetMapping("/getUserDetail/{id}")
    public Result<Map<String, Object>> getUserByArticleId(@PathVariable("id") Long id) {

        List<BlogArticles> allBlogArticlesById = blogArticlesService.getAllBlogArticlesById(id);
        Long likes = 0L;
        Long views = 0L;
        for (BlogArticles blogArticles : allBlogArticlesById) {
            likes += blogArticles.getArticleLikeCount();
            views += blogArticles.getArticleViews();
        }

        Map<String, Object> map = new HashMap<>();
        map.put("articleList", allBlogArticlesById);
        map.put("articleNumber", allBlogArticlesById.size());
        map.put("like", likes);
        map.put("views", views);

        return Result.ok(map);
    }


    @PostMapping("/create/{userId}")
    public Result<Integer> createArticle(@PathVariable("userId") Long userId,@RequestBody String message) throws UnsupportedEncodingException {
        System.out.println(message);
        String[] strings = DecodeUtils.decodeMessage(message);
        Integer i = blogArticlesService.insertArticle(userId, strings[0], strings[1]);
        return Result.ok(i);
    }


}

