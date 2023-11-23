package com.lrh.blog.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lrh.blog.article.mapper.*;
import com.lrh.blog.article.service.BlogArticlesLikeService;
import com.lrh.blog.article.service.BlogArticlesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lrh.blog.article.service.BlogSortsService;
import com.lrh.blog.article.service.ElasticsearchService;
import com.lrh.blog.common.domin.ArticleSearch2;
import com.lrh.blog.common.domin.Message;
import com.lrh.blog.common.entity.*;
import com.lrh.blog.common.result.Result;
import com.lrh.blog.common.utils.PageUtils;
import com.lrh.blog.common.vo.BlogArticlesLikeVo;
import com.lrh.blog.common.vo.BlogArticlesVo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

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
    private BlogSortsService blogSortsService;

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private BlogArticlesLikeMapper blogArticlesLikeMapper;


    @Override
    public IPage<BlogArticles> getAllBlogArticles(String sortId, Page<BlogArticles> page, String keyword) throws IOException {
        PageUtils<BlogArticles> pageUtils = new PageUtils<>();
        if ("original".equals(sortId)) {
            if (keyword.trim().isEmpty()) {
                return baseMapper.selectPage(page, new LambdaQueryWrapper<BlogArticles>().orderByDesc(BlogArticles::getArticleDate));
            }
            List<BlogArticles> blogArticles = getBlogArticles(keyword);
            if (blogArticles == null) {
                return page.setRecords(null);
            }
            return pageUtils.getBlogArticlesIpage(page, blogArticles);
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
            return pageUtils.getBlogArticlesIpage(page, blogArticles);
        }
        List<BlogArticles> searchList = getBlogArticles(keyword);
        if (searchList == null) {
            return page.setRecords(null);
        }
        blogArticles.retainAll(searchList);
        if (blogArticles.isEmpty()) {
            return page.setRecords(null);
        }
        return pageUtils.getBlogArticlesIpage(page, blogArticles);
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

    @Override
    @Transactional
    public Integer addLike(BlogArticlesLikeVo blogArticlesLikeVo) {
        BlogArticles blogArticles = blogArticlesMapper.selectById(blogArticlesLikeVo.getArticleId());
        long articleLikeCount = 0;
        if (blogArticles.getArticleLikeCount() == null) {
            articleLikeCount += 1;
        } else {
            articleLikeCount = blogArticles.getArticleLikeCount() + 1;
        }
        blogArticles.setArticleLikeCount(articleLikeCount);
        int update = blogArticlesMapper.update(blogArticles, new LambdaQueryWrapper<BlogArticles>().eq(BlogArticles::getArticleId, blogArticlesLikeVo.getArticleId()));
        LocalDateTime now = LocalDateTime.now();
        int insert = blogArticlesLikeMapper.insert(new BlogArticlesLike(null, blogArticlesLikeVo.getArticleId(), blogArticlesLikeVo.getUserId(), now, now));
        return update + insert;
    }

    @Override
    public Map<String, Object> getLabelAndSort(Long articleId) {
        BlogSetArticleLabel blogSetArticleLabel = blogSetArticleLabelMapper.selectOne(new LambdaQueryWrapper<BlogSetArticleLabel>().eq(BlogSetArticleLabel::getArticleId, articleId));
        BlogSetArticleSort blogSetArticleSort = blogSetArticleSortMapper.selectOne(new LambdaQueryWrapper<BlogSetArticleSort>().eq(BlogSetArticleSort::getArticleId, articleId));
        BlogSorts blogSorts = blogSortsService.getOne(new LambdaQueryWrapper<BlogSorts>().eq(BlogSorts::getSortId, blogSetArticleSort.getSortId()));
        Map<String, Object> map = new HashMap<>();
        map.put("labelId", blogSetArticleLabel.getLabelId());
        map.put("sortId", blogSetArticleSort.getSortId());
        map.put("sortParentId", blogSorts.getParentSortId());
        return map;
    }

    @Override
    @Transactional
    public Integer updateArticle(BlogArticlesVo blogArticlesVo, Long articleId) {
        BlogArticles blogArticles = blogArticlesMapper.selectById(articleId);
        blogArticles.setArticleDate(LocalDateTime.now());
        blogArticles.setArticleTitle(blogArticlesVo.getTitle());
        blogArticles.setArticleContent(blogArticlesVo.getContent());
        int update = blogArticlesMapper.update(blogArticles, new LambdaQueryWrapper<BlogArticles>().eq(BlogArticles::getArticleId, articleId));

        BlogSetArticleLabel blogSetArticleLabel = blogSetArticleLabelMapper.selectOne(new LambdaQueryWrapper<BlogSetArticleLabel>().eq(BlogSetArticleLabel::getArticleId, articleId));
        blogSetArticleLabel.setLabelId(Long.valueOf(blogArticlesVo.getLabelId()));
        int update1 = blogSetArticleLabelMapper.update(blogSetArticleLabel, new LambdaQueryWrapper<BlogSetArticleLabel>().eq(BlogSetArticleLabel::getArticleId, articleId));

        BlogSetArticleSort blogSetArticleSort = blogSetArticleSortMapper.selectOne(new LambdaQueryWrapper<BlogSetArticleSort>().eq(BlogSetArticleSort::getArticleId, articleId));
        blogSetArticleSort.setSortId(Long.valueOf(blogArticlesVo.getSortId()));
        int update2 = blogSetArticleSortMapper.update(blogSetArticleSort, new LambdaQueryWrapper<BlogSetArticleSort>().eq(BlogSetArticleSort::getArticleId, articleId));
        try {
            elasticsearchService.updateArticle(new ArticleSearch2(articleId, blogArticlesVo.getTitle(), blogArticles.getArticleContent()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return update + update1 + update2;
    }

    @Override
    @Transactional
    public Integer removeArticleById(Long articleId) {
        int i = baseMapper.deleteById(articleId);
        try {
            elasticsearchService.deleteArticle(articleId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return i;
    }


}
