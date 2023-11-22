package com.lrh.blog.article.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lrh.blog.article.service.BlogArticlesLikeService;
import com.lrh.blog.common.entity.BlogArticlesLike;
import com.lrh.blog.common.result.Result;
import com.lrh.blog.common.vo.BlogArticlesLikeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lrh
 * @since 2023-11-22
 */
@RestController
@RequestMapping("/blog/articles-like")
public class BlogArticlesLikeController {

    @Autowired
    private BlogArticlesLikeService blogArticlesLikeService;

    @GetMapping("/getLikeList/{userId}")
    public Result<List<Long>> getLikes(@PathVariable("userId") Long userId) {
        List<BlogArticlesLike> list = blogArticlesLikeService.list(new LambdaQueryWrapper<BlogArticlesLike>().eq(BlogArticlesLike::getUserId, userId));
        if (list.isEmpty()) {
            return Result.ok(new ArrayList<>());
        }
        List<Long> ids = new ArrayList<>();
        for (BlogArticlesLike blogArticlesLike : list) {
            ids.add(blogArticlesLike.getArticleId());
        }
        return Result.ok(ids);
    }

}

