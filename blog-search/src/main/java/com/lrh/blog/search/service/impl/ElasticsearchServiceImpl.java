package com.lrh.blog.search.service.impl;


import com.alibaba.fastjson2.JSON;
import com.lrh.blog.common.domin.ArticleSearch2;
import com.lrh.blog.search.pojo.ArticleSearch;
import com.lrh.blog.search.service.ElasticsearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.search.service.impl
 * @ClassName: ElasticsearchServiceImpl
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/20 12:14
 */
@Slf4j
@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public List<ArticleSearch2> queryArticle(String item) throws IOException {
        List<ArticleSearch2> articleSearchList = new ArrayList<>();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(item, "articleTitle", "articleContent");
        searchSourceBuilder.query(multiMatchQueryBuilder);
        SearchRequest searchRequest = new SearchRequest("article");
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            Map<String, Object> map = hit.getSourceAsMap();
            ArticleSearch articleSearch = JSON.parseObject(JSON.toJSONString(map), ArticleSearch.class);
            ArticleSearch2 articleSearch2 = new ArticleSearch2();
            articleSearch2.setId(articleSearch.getId());
            articleSearch2.setArticleTitle(articleSearch.getArticleTitle());
            articleSearch2.setArticleContent(articleSearch.getArticleContent());
            articleSearchList.add(articleSearch2);
        }
        return articleSearchList;
    }

    @Override
    public void add(ArticleSearch2 articleSearch2) {
        ArticleSearch articleSearch = new ArticleSearch();
        articleSearch.setId(articleSearch2.getId());
        articleSearch.setArticleTitle(articleSearch2.getArticleTitle());
        articleSearch.setArticleContent(articleSearch2.getArticleContent());
        IndexRequest indexRequest = new IndexRequest("article");
        indexRequest.source(JSON.toJSONString(articleSearch), XContentType.JSON);
        indexRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        indexRequest.create(false);
        restHighLevelClient.indexAsync(indexRequest, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {
                ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
                if (shardInfo.getFailed() > 0) {
                    for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                        log.error("将id为：{}", indexRequest.id());
                        log.error("数据存入ES时存在失败的分片，原因为：" + failure.getCause());
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                log.error("{}:存储es时异常，数据信息为", indexRequest.id(), e);
            }
        });
    }

    @Override
    public void update(ArticleSearch2 articleSearch2) throws IOException {
        UpdateByQueryRequest updateByQueryRequest = new UpdateByQueryRequest();
        updateByQueryRequest.setConflicts("proceed");
        updateByQueryRequest.setRefresh(true);
        updateByQueryRequest.indices("article");
        updateByQueryRequest.setQuery(new TermQueryBuilder("id", articleSearch2.getId()));
        Map<String, Object> map = new HashMap<>();
        map.put("articleTitle", articleSearch2.getArticleTitle());
        map.put("articleContent", articleSearch2.getArticleContent());
        updateByQueryRequest.setScript(new Script(ScriptType.INLINE, "painless",
                "ctx._source.articleTitle ='" + articleSearch2.getArticleTitle() + "';" + "ctx._source.articleContent='" + articleSearch2.getArticleContent() + "';", Collections.emptyMap()));
        restHighLevelClient.updateByQuery(updateByQueryRequest, RequestOptions.DEFAULT);
    }

    @Override
    public void delete(Long id) throws IOException {
        DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest("article");
        deleteByQueryRequest.setQuery(QueryBuilders.termQuery("id", id));
        restHighLevelClient.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
    }
}
