package com.lrh.blog.article.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lrh.blog.article.service.*;
import com.lrh.blog.article.utils.JudgeRightUtils;
import com.lrh.blog.article.utils.JwtUtils;
import com.lrh.blog.common.entity.*;
import com.lrh.blog.common.result.Result;;
import com.lrh.blog.common.result.ResultCodeEnum;
import com.lrh.blog.common.vo.BlogArticlesLikeVo;
import com.lrh.blog.common.vo.BlogArticlesVo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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

    @Autowired
    private BlogLabelsService blogLabelsService;

    @Autowired
    private BlogSetArticleLabelService blogSetArticleLabelService;

    @Autowired
    private BlogSortsService blogSortsService;

    @Autowired
    private BlogSetArticleSortService blogSetArticleSortService;

    @Autowired
    private JudgeRightUtils judgeRightUtils;

    @GetMapping("/getAll/{sortName}/{index}")
    public Result<Map<String, Object>> getAllBlogArticles(@PathVariable("sortName") String sortName, @PathVariable Integer index, @RequestParam("item") String keyword) throws IOException {
        Map<String, Object> map = new HashMap<>();
        Page<BlogArticles> page = new Page<>(index, 5);
        IPage<BlogArticles> allBlogArticles = blogArticlesService.getAllBlogArticles(sortName, page, keyword);
        List<BlogArticles> records = allBlogArticles.getRecords();
        if (records == null) {
            map.put("data", null);
            map.put("total", 0);
            map.put("pageAll", 0);
            return Result.ok(map);
        }
        List<Long> ids = new ArrayList<>();
        records.forEach(item -> ids.add(item.getUserId()));
        Result<List<BlogUsers>> byIds = blogUsersServer.getByIds(ids);

        List<BlogUsers> articlesForUsers = byIds.getData();
        for (BlogUsers articlesForUser : articlesForUsers) {
            articlesForUser.setUserPassword(null);
            articlesForUser.setUserTelephoneNumber(null);
        }


        for (BlogArticles article : records) {
            if (ids.contains(article.getUserId())) {
                BlogUsers blogUsers = articlesForUsers.stream()
                        .filter(user -> user.getUserId().equals(article.getUserId())).findFirst().get();
                article.setBlogUsers(blogUsers);
            }
            BlogSetArticleLabel blogSetArticleLabel = blogSetArticleLabelService.getOne(new LambdaQueryWrapper<BlogSetArticleLabel>().eq(BlogSetArticleLabel::getArticleId, article.getArticleId()));
            Long labelId = blogSetArticleLabel.getLabelId();
            BlogLabels blogLabels = blogLabelsService.getOne(new LambdaQueryWrapper<BlogLabels>().eq(BlogLabels::getLabelId, labelId));
            BlogSetArticleSort blogSetArticleSort = blogSetArticleSortService.getOne(new LambdaQueryWrapper<BlogSetArticleSort>().eq(BlogSetArticleSort::getArticleId, article.getArticleId()));
            Long sortId = blogSetArticleSort.getSortId();
            BlogSorts blogSorts = blogSortsService.getOne(new LambdaQueryWrapper<BlogSorts>().eq(BlogSorts::getSortId, sortId));
            article.setBlogLabels(blogLabels);
            article.setBlogSorts(blogSorts);
        }


        map.put("data", allBlogArticles.getRecords());
        map.put("total", allBlogArticles.getTotal());
        map.put("pageAll", allBlogArticles.getPages());

        return Result.ok(map);
    }


    @GetMapping("/getUserDetail/{id}")
    public Result<Map<String, Object>> getUserByArticleId(@PathVariable("id") Long id) {

        List<BlogArticles> allBlogArticlesById = blogArticlesService.getAllBlogArticlesById(id);
        allBlogArticlesById.sort(Comparator.comparing(BlogArticles::getArticleDate).reversed());
        Result<BlogUsers> userById = blogUsersServer.getUserById(id);
        BlogUsers blogUsers = userById.getData();
        Long likes = 0L;
        Long views = 0L;
        for (BlogArticles blogArticles : allBlogArticlesById) {
            likes += blogArticles.getArticleLikeCount();
            views += blogArticles.getArticleViews();
            blogArticles.setBlogUsers(blogUsers);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("user", blogUsers);
        map.put("articleList", allBlogArticlesById);
        map.put("articleNumber", allBlogArticlesById.size());
        map.put("like", likes);
        map.put("views", views);

        return Result.ok(map);
    }


    @PostMapping("/create/{userId}/{userName}")
    public Result<Integer> createArticle(@PathVariable("userId") Long userId, @RequestBody BlogArticlesVo blogArticlesVo, @PathVariable String userName,HttpServletRequest httpServletRequest) {
        if (!judgeRightUtils.judgeRight(userName, httpServletRequest)) {
            return Result.fail(0).message("没有权限").code(ResultCodeEnum.NO_RIGHT.getCode());
        }
        Integer i = blogArticlesService.insertArticle(userId, blogArticlesVo.getTitle(), blogArticlesVo.getContent(), blogArticlesVo.getLabelId(), blogArticlesVo.getSortId());
        return Result.ok(i);
    }

    @PutMapping("/addLike")
    public Result<Integer> addLike(@RequestBody BlogArticlesLikeVo blogArticlesLikeVo) {
        Integer i = blogArticlesService.addLike(blogArticlesLikeVo);
        return Result.ok(i);
    }


    @PostMapping("/addView/{articleId}")
    public Result<Integer> addView(@PathVariable("articleId") Long articleId) {
        BlogArticles blogArticles = blogArticlesService.getBaseMapper().selectById(articleId);
        long articleViews = blogArticles.getArticleViews() + 1;
        blogArticles.setArticleViews(articleViews);
        int update = blogArticlesService.getBaseMapper().update(blogArticles, new LambdaQueryWrapper<BlogArticles>().eq(BlogArticles::getArticleId, articleId));
        return Result.ok(update);
    }

    @GetMapping("/hotArticle")
    public Result<List<BlogArticles>> getHotArticle() {
        List<BlogArticles> blogArticlesList = blogArticlesService.list(new LambdaQueryWrapper<BlogArticles>().orderByDesc(BlogArticles::getArticleLikeCount).last("limit 5"));
        List<Long> ids = new ArrayList<>();
        for (BlogArticles blogArticles : blogArticlesList) {
            ids.add(blogArticles.getUserId());
        }
        Result<List<BlogUsers>> byIds = blogUsersServer.getByIds(ids);
        List<BlogUsers> blogUsersList = byIds.getData();
        for (BlogUsers blogUsers : blogUsersList) {
            blogUsers.setUserPassword(null);
            blogUsers.setUserTelephoneNumber(null);
        }

        for (BlogArticles article : blogArticlesList) {
            if (ids.contains(article.getUserId())) {
                BlogUsers blogUsers = blogUsersList.stream()
                        .filter(user -> user.getUserId().equals(article.getUserId())).findFirst().get();
                article.setBlogUsers(blogUsers);
            }
        }
        return Result.ok(blogArticlesList);
    }


    @GetMapping("/get/labelAndSort/{articleId}")
    public Result<Map<String, Object>> getLabelAndSort(@PathVariable("articleId") Long articleId) {
        Map<String, Object> result = blogArticlesService.getLabelAndSort(articleId);
        return Result.ok(result);
    }

    @PutMapping("/update/{articleId}/{userName}")
    public Result<Integer> updateArticle(@RequestBody BlogArticlesVo blogArticlesVo, @PathVariable("articleId") Long articleId, @PathVariable String userName, HttpServletRequest httpServletRequest) {
        if (!judgeRightUtils.judgeRight(userName, httpServletRequest)) {
            return Result.fail(0).message("没有权限").code(ResultCodeEnum.NO_RIGHT.getCode());
        }
        Integer i = blogArticlesService.updateArticle(blogArticlesVo, articleId);
        return Result.ok(i);
    }

    @DeleteMapping("/delete/{articleId}/{userName}")
    public Result<Integer> deleteArticle(@PathVariable("articleId") Long articleId, HttpServletRequest httpServletRequest, @PathVariable String userName) {

        if (!judgeRightUtils.judgeRight(userName, httpServletRequest)) {
            return Result.fail(0).message("没有权限").code(ResultCodeEnum.NO_RIGHT.getCode());
        }

        Integer removeById = blogArticlesService.removeArticleById(articleId);
        return Result.ok(removeById);
    }


}

