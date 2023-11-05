package com.lrh.blog.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lrh.blog.article.mapper.BlogArticlesMapper;
import com.lrh.blog.article.service.BlogArticlesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lrh.blog.common.entity.BlogArticles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lrh
 * @since 2023-10-31
 */
@Service
public class BlogArticlesServiceImpl extends ServiceImpl<BlogArticlesMapper, BlogArticles> implements BlogArticlesService {

    @Autowired
    private BlogArticlesMapper blogArticlesMapper;

    @Override
    public IPage<BlogArticles> getAllBlogArticles(Page<BlogArticles> page) {
        return baseMapper.selectPage(page,new LambdaQueryWrapper<BlogArticles>().orderByDesc(BlogArticles::getArticleDate));
    }

    @Override
    public List<BlogArticles> getAllBlogArticlesById(Long id) {
        return baseMapper.selectList(new LambdaQueryWrapper<BlogArticles>().eq(BlogArticles::getUserId,id));
    }

    @Override
    public Integer insertArticle(Long userId, String title, String content) {
        Integer insert = blogArticlesMapper.insert(userId, title, content, LocalDateTime.now());
        return insert;
    }
}
