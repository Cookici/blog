package com.lrh.blog.search.test;

import com.lrh.blog.search.mapper.ArticleSearchMapper;
import com.lrh.blog.search.entity.BlogArticles;
import com.lrh.blog.search.pojo.ArticleSearch;
import com.lrh.blog.search.service.BlogArticlesService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.search.test
 * @ClassName: input
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/19 20:27
 */
@SpringBootTest
public class InputAndQueryTest {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private BlogArticlesService blogArticlesService;

    @Autowired
    private ArticleSearchMapper articleSearchMapper;

    @Test
    public void inputTest() {
        List<BlogArticles> list = blogArticlesService.list();
        List<ArticleSearch> articleSearchList = new ArrayList<>();
        for (BlogArticles blogArticles : list) {
            ArticleSearch articleSearch = new ArticleSearch();
            articleSearch.setId(blogArticles.getArticleId());
            articleSearch.setArticleTitle(blogArticles.getArticleTitle());
            articleSearch.setArticleContent(blogArticles.getArticleContent());
            articleSearchList.add(articleSearch);
        }
        articleSearchMapper.saveAll(articleSearchList);
    }

    @Test
    public void get() throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("i lik", "articleTitle", "articleContent");
        searchSourceBuilder.query(multiMatchQueryBuilder);
        SearchRequest searchRequest = new SearchRequest("article");
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println("searchRequest ---> " + searchResponse);
    }


}
