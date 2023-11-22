package com.lrh.blog.article.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lrh.blog.article.mapper.BlogCommentLikeMapper;
import com.lrh.blog.article.service.BlogArticlesLikeService;
import com.lrh.blog.article.service.BlogCommentLikeService;
import com.lrh.blog.common.entity.BlogCommentLike;
import com.lrh.blog.common.result.Result;
import com.lrh.blog.common.vo.BlogCommentLikeVo;
import com.sun.org.apache.xpath.internal.operations.Bool;
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
@RequestMapping("/blog/comment-like")
public class BlogCommentLikeController {

    @Autowired
    private BlogCommentLikeService blogCommentLikeService;

    @GetMapping("/getCommentList/{userId}")
    private Result<List<Long>> getComments(@PathVariable("userId") Long userId) {
        List<BlogCommentLike> blogCommentLikes = blogCommentLikeService.list(new LambdaQueryWrapper<BlogCommentLike>().eq(BlogCommentLike::getUserId, userId));
        if (blogCommentLikes.isEmpty()) {
            return Result.ok(new ArrayList<>());
        }
        List<Long> commentIds = new ArrayList<>();
        for (BlogCommentLike blogCommentLike : blogCommentLikes) {
            commentIds.add(blogCommentLike.getCommentId());
        }
        return Result.ok(commentIds);
    }


}

