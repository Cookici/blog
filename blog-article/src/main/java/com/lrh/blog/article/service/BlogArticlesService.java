package com.lrh.blog.article.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lrh.blog.common.entity.BlogArticles;
import com.lrh.blog.common.vo.BlogArticlesLikeVo;
import com.lrh.blog.common.vo.BlogArticlesVo;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author lrh
 * @since 2023-10-31
 */
public interface BlogArticlesService extends IService<BlogArticles> {

    IPage<BlogArticles> getAllBlogArticles(String sortId, Page<BlogArticles> page,String keyword) throws IOException;

    List<BlogArticles> getAllBlogArticlesById(Long id);

    Integer insertArticle(Long userId, String title, String content,String labelId,String sortId);

    Integer addLike(BlogArticlesLikeVo blogArticlesLikeVo);

    Map<String, Object> getLabelAndSort(Long articleId);

    Integer updateArticle(BlogArticlesVo blogArticlesVo, Long articleId);

    Integer removeArticleById(Long articleId);
}
