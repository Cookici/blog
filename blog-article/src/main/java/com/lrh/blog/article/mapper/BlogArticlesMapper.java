package com.lrh.blog.article.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lrh.blog.common.entity.BlogArticles;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lrh
 * @since 2023-10-31
 */
@Mapper
public interface BlogArticlesMapper extends BaseMapper<BlogArticles> {

    @Update("insert into blog.blog_articles values (null, #{userId}, #{articleTitle}, #{articleContent}, 0, 0, #{articleDate}, 0, 0)")
    Integer insert(@Param("userId") Long userId, @Param("articleTitle") String title, @Param("articleContent") String content, @Param("articleDate") LocalDateTime localDateTime);
}
