package com.lrh.blog.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lrh.blog.article.mapper.BlogArticlesMapper;
import com.lrh.blog.article.mapper.BlogSetArticleSortMapper;
import com.lrh.blog.article.mapper.BlogSortsMapper;
import com.lrh.blog.article.service.BlogArticlesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lrh.blog.common.domin.Message;
import com.lrh.blog.common.entity.BlogArticles;
import com.lrh.blog.common.entity.BlogSetArticleSort;
import com.lrh.blog.common.entity.BlogSorts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lrh
 * @since 2023-10-31
 */
@Slf4j
@Service
public class BlogArticlesServiceImpl extends ServiceImpl<BlogArticlesMapper, BlogArticles> implements BlogArticlesService {

    @Autowired
    private BlogArticlesMapper blogArticlesMapper;

    @Autowired
    private BlogSetArticleSortMapper blogSetArticleSortMapper;


    @Override
    public IPage<BlogArticles> getAllBlogArticles(String sortId, Page<BlogArticles> page) {
        if ("original".equals(sortId)) {
            return baseMapper.selectPage(page, new LambdaQueryWrapper<BlogArticles>().orderByDesc(BlogArticles::getArticleDate));
        }
        List<BlogSetArticleSort> blogSetArticleSorts = blogSetArticleSortMapper.selectList(new LambdaQueryWrapper<BlogSetArticleSort>().eq(BlogSetArticleSort::getSortId, Long.parseLong(sortId)));
        if (blogSetArticleSorts.isEmpty()) {
            return page.setRecords(null);
        }
        List<Long> blogArticleIds = new ArrayList<>();
        for (BlogSetArticleSort blogSetArticleSort : blogSetArticleSorts) {
            blogArticleIds.add(blogSetArticleSort.getArticleId());
        }
        List<BlogArticles> blogArticles = baseMapper.selectBatchIds(blogArticleIds);
        blogArticles.sort(Comparator.comparing(BlogArticles::getArticleDate).reversed());
        long allDateSize = blogArticles.size();
        long current = page.getCurrent();
        long size = page.getSize();
        long maxPage;
        long start;
        long end;
        if (allDateSize % size == 0) {
            maxPage = allDateSize / size;
        } else {
            maxPage = allDateSize / size + 1;
        }
        if (current >= maxPage) {
            current = maxPage;
            start = (current - 1) * size;
            end = allDateSize;
        } else {
            start = (current - 1) * size;
            end = start + size;
        }
        return page.setRecords(blogArticles.subList((int) start, (int) end)).setTotal(blogArticles.size()).setPages(maxPage);
    }

    @Override
    public List<BlogArticles> getAllBlogArticlesById(Long id) {
        return baseMapper.selectList(new LambdaQueryWrapper<BlogArticles>().eq(BlogArticles::getUserId, id));
    }

    @Override
    public Integer insertArticle(Long userId, String title, String content) {
        Integer insert = blogArticlesMapper.insert(userId, title, content, LocalDateTime.now());
        return insert;
    }
}
