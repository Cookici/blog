package com.lrh.blog.article.controller;


import com.lrh.blog.article.service.BlogArticlesService;
import com.lrh.blog.article.service.BlogSortsService;
import com.lrh.blog.article.service.BlogUsersServer;
import com.lrh.blog.common.entity.BlogSorts;
import com.lrh.blog.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lrh
 * @since 2023-11-16
 */
@RestController
@RequestMapping("/article/sorts")
public class BlogSortsController {

    @Autowired
    private BlogSortsService blogSortsService;

    @GetMapping("/getSortMenu")
    public Result<List<BlogSorts>> getSortMenu() {
        List<BlogSorts> blogSorts = blogSortsService.listWithTree();
        return Result.ok(blogSorts);
    }


}

