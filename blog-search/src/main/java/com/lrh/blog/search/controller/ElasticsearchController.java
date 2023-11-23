package com.lrh.blog.search.controller;


import com.lrh.blog.common.domin.ArticleSearch2;
import com.lrh.blog.common.result.Result;
import com.lrh.blog.search.pojo.ArticleSearch;
import com.lrh.blog.search.service.ElasticsearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lrh
 * @since 2023-11-19
 */
@Slf4j
@RestController
@RequestMapping("/blog/search")
public class ElasticsearchController {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @GetMapping("/get/want/article")
    public Result<List<ArticleSearch2>> getWantArticle(@RequestParam("item") String item) throws IOException {
        List<ArticleSearch2> articleSearchList = elasticsearchService.queryArticle(item);
        return Result.ok(articleSearchList);
    }


    @PostMapping("/add/article")
    public Result<Void> addArticle(@RequestBody ArticleSearch2 articleSearch2) {
        elasticsearchService.add(articleSearch2);
        return Result.ok();
    }

    @PutMapping("/update/article")
    public Result<Void> updateArticle(@RequestBody ArticleSearch2 articleSearch2) throws IOException {
        elasticsearchService.update(articleSearch2);
        return Result.ok();
    }

    @DeleteMapping ("/delete/article")
    public Result<Void> deleteArticle(@RequestParam("articleId") Long articleId) throws IOException {
        elasticsearchService.delete(articleId);
        return Result.ok();
    }


}

