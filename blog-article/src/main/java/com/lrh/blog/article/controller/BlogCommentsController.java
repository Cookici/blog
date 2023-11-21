package com.lrh.blog.article.controller;


import com.lrh.blog.article.service.BlogCommentsService;
import com.lrh.blog.common.domin.BlogComments2;
import com.lrh.blog.common.entity.BlogComments;
import com.lrh.blog.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lrh
 * @since 2023-11-21
 */
@Slf4j
@RestController
@RequestMapping("/blog/comments")
public class BlogCommentsController {

    @Autowired
    private BlogCommentsService blogCommentsService;

    @GetMapping("/getAllComments/{articleId}")
    public Result<List<BlogComments2>> getCommentsTree(@PathVariable("articleId") Long articleId) {
        List<BlogComments2> tree = blogCommentsService.getCommentsTree(articleId);
        return Result.ok(tree);
    }

    @PostMapping("/add/{articleId}")
    public Result<Boolean> addComments(@RequestBody BlogComments2 blogComments2, @PathVariable("articleId") Long articleId) {
        log.info("blogComments2 ---> {}", blogComments2);
        BlogComments blogComments = new BlogComments();
        blogComments.setCommentId(null);
        blogComments.setUserId(Long.valueOf(blogComments2.getUid()));
        blogComments.setArticleId(articleId);
        blogComments.setCommentLikeCount(blogComments2.getLikes());
        blogComments.setCommentDate(blogComments2.getCreateTime());
        blogComments.setCommentContent(blogComments2.getContent());
        blogComments.setCommentContentImg(blogComments2.getContentImg());
        if (blogComments2.getParentId() == null) {
            blogComments.setParentCommentId(0L);
        } else {
            blogComments.setParentCommentId(Long.valueOf(blogComments2.getParentId()));
        }
        boolean save = blogCommentsService.save(blogComments);
        return Result.ok(save);
    }

    @GetMapping("/total")
    public Result<Long> commentsSize() {
        List<BlogComments> list = blogCommentsService.list();
        return Result.ok((long) list.size());
    }

}

