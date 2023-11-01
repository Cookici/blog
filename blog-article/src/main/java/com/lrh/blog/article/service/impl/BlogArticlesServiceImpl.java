package com.lrh.blog.article.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lrh.blog.article.mapper.BlogArticlesMapper;
import com.lrh.blog.article.service.BlogArticlesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lrh.blog.common.entity.BlogArticles;
import org.springframework.stereotype.Service;

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

    @Override
    public IPage<BlogArticles> getAllBlogArticles(Page<BlogArticles> page) {
        return baseMapper.selectPage(page,null);
    }
}
