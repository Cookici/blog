package com.lrh.blog.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lrh.blog.article.mapper.BlogArticlesMapper;
import com.lrh.blog.article.mapper.BlogSetArticleLabelMapper;
import com.lrh.blog.article.mapper.BlogSetArticleSortMapper;
import com.lrh.blog.article.mapper.BlogSortsMapper;
import com.lrh.blog.article.service.BlogArticlesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lrh.blog.article.service.ElasticsearchService;
import com.lrh.blog.common.domin.ArticleSearch2;
import com.lrh.blog.common.domin.Message;
import com.lrh.blog.common.entity.BlogArticles;
import com.lrh.blog.common.entity.BlogSetArticleLabel;
import com.lrh.blog.common.entity.BlogSetArticleSort;
import com.lrh.blog.common.entity.BlogSorts;
import com.lrh.blog.common.result.Result;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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

    @Autowired
    private BlogSetArticleLabelMapper blogSetArticleLabelMapper;

    @Autowired
    private ElasticsearchService elasticsearchService;


    @Override
    public IPage<BlogArticles> getAllBlogArticles(String sortId, Page<BlogArticles> page, String keyword) throws IOException {

        if ("original".equals(sortId)) {
            if (keyword.trim().isEmpty()) {
                return baseMapper.selectPage(page, new LambdaQueryWrapper<BlogArticles>().orderByDesc(BlogArticles::getArticleDate));
            }
            List<BlogArticles> blogArticles = getBlogArticles(keyword);
            if (blogArticles == null) {
                return page.setRecords(null);
            }
            return getBlogArticlesIpage(page, blogArticles);
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
        if (keyword.trim().isEmpty()) {
            return getBlogArticlesIpage(page, blogArticles);
        }
        List<BlogArticles> searchList = getBlogArticles(keyword);
        if (searchList == null) {
            return page.setRecords(null);
        }
        blogArticles.retainAll(searchList);
        if (blogArticles.isEmpty()) {
            return page.setRecords(null);
        }
        return getBlogArticlesIpage(page, blogArticles);
    }

    private List<BlogArticles> getBlogArticles(String keyword) throws IOException {
        Result<List<ArticleSearch2>> wantArticle = elasticsearchService.getWantArticle(keyword);
        List<ArticleSearch2> data = wantArticle.getData();
        if (data == null || data.isEmpty()) {
            return null;
        }
        List<Long> idsList = new ArrayList<>();
        for (ArticleSearch2 search : data) {
            idsList.add(search.getId());
        }
        return blogArticlesMapper.selectBatchIds(idsList);
    }

    private static IPage<BlogArticles> getBlogArticlesIpage(Page<BlogArticles> page, List<BlogArticles> blogArticles) {
        long allDateSize = blogArticles.size();
        long current = page.getCurrent();
        long size = page.getSize();
        long maxPage;
        long start;
        long end;
        if ((allDateSize % size == 0)) {
            if (allDateSize >= size) {
                maxPage = allDateSize / size;
            } else {
                maxPage = 1;
            }
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
    @Transactional
    public Integer insertArticle(Long userId, String title, String content, String labelId, String sortId) {
        BlogArticles blogArticle = new BlogArticles(null, userId, title, content, 0L, 0L, LocalDateTime.now(), 0L);
        Integer insertArticle = blogArticlesMapper.insertArticle(blogArticle);
        log.info("blogArticle id : {}", blogArticle.getArticleId());
        Integer insertArticleSort = blogSetArticleSortMapper.insert(new BlogSetArticleSort(blogArticle.getArticleId(), Long.parseLong(sortId)));
        Integer insertArticleLabel = blogSetArticleLabelMapper.insert(new BlogSetArticleLabel(blogArticle.getArticleId(), Long.parseLong(labelId)));
        ArticleSearch2 articleSearch2 = new ArticleSearch2(blogArticle.getArticleId(), title, content);
        log.info("articleSearch2 ---> {}", articleSearch2);
        elasticsearchService.addArticle(articleSearch2);
        return insertArticleSort + insertArticleLabel + insertArticle;
    }


}
