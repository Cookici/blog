package com.lrh.blog.article.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lrh.blog.common.entity.BlogArticles;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author lrh
 * @since 2023-10-31
 */
@Mapper
public interface BlogArticlesMapper extends BaseMapper<BlogArticles> {

    Integer insertArticle(@Param("blogArticles") BlogArticles blogArticles);
}
