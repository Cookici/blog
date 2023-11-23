package com.lrh.blog.search.service;

import com.lrh.blog.common.domin.ArticleSearch2;
import com.lrh.blog.search.pojo.ArticleSearch;

import java.io.IOException;
import java.util.List;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.search.service
 * @ClassName: ElasticsearchService
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/20 12:14
 */

public interface ElasticsearchService {

    List<ArticleSearch2> queryArticle(String item) throws IOException;

    void add(ArticleSearch2 articleSearch2);

    void update(ArticleSearch2 articleSearch2) throws IOException;

    void delete(Long id) throws IOException;
}
