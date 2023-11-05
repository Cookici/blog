package com.lrh.blog.article.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lrh.blog.common.entity.BlogArticles;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lrh
 * @since 2023-10-31
 */
public interface BlogArticlesService extends IService<BlogArticles> {

    IPage<BlogArticles> getAllBlogArticles(Page<BlogArticles> page);

    List<BlogArticles> getAllBlogArticlesById(Long id);

    Integer insertArticle(Long userId, String title, String content);
}
