package com.lrh.blog.article.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lrh.blog.article.service.BlogArticlesService;
import com.lrh.blog.article.service.BlogUsersServer;
import com.lrh.blog.common.entity.BlogArticles;
import com.lrh.blog.common.entity.BlogUsers;
import com.lrh.blog.common.result.Result;;
import com.lrh.blog.common.vo.BlogArticlesVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
        Result<List<BlogUsers>> byIds = blogUsersServer.getByIds(ids);

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
    public Result<Integer> createArticle(@PathVariable("userId") Long userId, @RequestBody BlogArticlesVo blogArticlesVo){
        Integer i = blogArticlesService.insertArticle(userId, blogArticlesVo.getTitle(), blogArticlesVo.getContent());
        return Result.ok(i);
    }

    @PostMapping("/addLike/{articleId}")
    public Result<Integer> addLike(@PathVariable("articleId") Long articleId) {
        BlogArticles blogArticles = blogArticlesService.getBaseMapper().selectById(articleId);
        long articleLikeCount = blogArticles.getArticleLikeCount() + 1;
        blogArticles.setArticleLikeCount(articleLikeCount);
        int update = blogArticlesService.getBaseMapper().update(blogArticles, new LambdaQueryWrapper<BlogArticles>().eq(BlogArticles::getArticleId, articleId));
        return Result.ok(update);
    }


    @PostMapping("/addView/{articleId}")
    public Result<Integer> addView(@PathVariable("articleId") Long articleId) {
        BlogArticles blogArticles = blogArticlesService.getBaseMapper().selectById(articleId);
        long articleViews = blogArticles.getArticleViews() + 1;
        blogArticles.setArticleViews(articleViews);
        int update = blogArticlesService.getBaseMapper().update(blogArticles, new LambdaQueryWrapper<BlogArticles>().eq(BlogArticles::getArticleId, articleId));
        return Result.ok(update);
    }

}

