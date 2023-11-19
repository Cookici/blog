package com.lrh.blog.search.mapper;

import com.lrh.blog.search.pojo.ArticleSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.search.dao
 * @ClassName: BlogArticleDao
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/19 20:01
 */
@Repository
public interface ArticleSearchMapper extends ElasticsearchRepository<ArticleSearch,Long> {

}
