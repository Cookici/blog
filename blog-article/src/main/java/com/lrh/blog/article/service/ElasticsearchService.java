package com.lrh.blog.article.service;

import com.lrh.blog.common.domin.ArticleSearch2;
import com.lrh.blog.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.article.service
 * @ClassName: ElasticsearchService
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/20 13:11
 */
@Component
@FeignClient(name = "blog-search")
public interface ElasticsearchService {

    @GetMapping("/blog/search/get/want/article")
    public Result<List<ArticleSearch2>> getWantArticle(@RequestParam("item") String item) throws IOException;

    @PostMapping("/blog/search/add/article")
    public Result<Void> addArticle(@RequestBody ArticleSearch2 articleSearch2);

    @PutMapping("/blog/search/update/article")
    public Result<Void> updateArticle(@RequestBody ArticleSearch2 articleSearch2) throws IOException;

    @DeleteMapping("/blog/search/delete/article")
    public Result<Void> deleteArticle(@RequestParam("articleId") Long articleId) throws IOException;

}
