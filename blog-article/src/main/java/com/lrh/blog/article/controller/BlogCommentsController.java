package com.lrh.blog.article.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lrh.blog.article.service.BlogCommentsService;
import com.lrh.blog.common.domin.BlogComments2;
import com.lrh.blog.common.domin.Reply;
import com.lrh.blog.common.entity.BlogArticles;
import com.lrh.blog.common.entity.BlogCommentLike;
import com.lrh.blog.common.entity.BlogComments;
import com.lrh.blog.common.result.Result;
import com.lrh.blog.common.utils.PageUtils;
import com.lrh.blog.common.vo.BlogCommentLikeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/getAllComments/{articleId}/{pageNum}")
    public Result<Map<String, Object>> getCommentsTree(@PathVariable("articleId") Long articleId, @PathVariable("pageNum") Integer pageNum) {
        Map<String, Object> map = new HashMap<>();
        Page<BlogComments2> page = new Page<>(pageNum, 5);
        PageUtils<BlogComments2> pageUtils = new PageUtils<>();
        List<BlogComments2> tree = blogCommentsService.getCommentsTree(articleId);
        IPage<BlogComments2> blogComments2Ipage = pageUtils.getBlogArticlesIpage(page, tree);
        map.put("comments",blogComments2Ipage.getRecords());
        map.put("total",blogComments2Ipage.getTotal());
        map.put("pageAll",blogComments2Ipage.getPages());

        return Result.ok(map);
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

    @GetMapping("/lastId")
    public Result<Long> commentsSize() {
        Long lastId = blogCommentsService.lastId();
        return Result.ok(lastId);
    }

    @PutMapping("/addLike")
    private Result<Integer> addCommentLike(@RequestBody BlogCommentLikeVo blogCommentLikeVo) {
        Integer i = blogCommentsService.addLike(blogCommentLikeVo);
        return Result.ok(i);
    }

    @GetMapping("/page/{parentId}")
    public Result<Reply> pageList(
            @PathVariable("parentId") Long parentId
    ) {
        List<BlogComments2> allBlogArticles = blogCommentsService.getPageList(parentId);
        Reply reply = new Reply(allBlogArticles);
        return Result.ok(reply);
    }


}

