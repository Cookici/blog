package com.lrh.blog.article.service.impl;


import com.lrh.blog.article.mapper.BlogArticlesMapper;
import com.lrh.blog.article.mapper.BlogSetArticleSortMapper;
import com.lrh.blog.article.service.BlogUsersServer;
import com.lrh.blog.common.entity.BlogSorts;
import com.lrh.blog.article.mapper.BlogSortsMapper;
import com.lrh.blog.article.service.BlogSortsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lrh
 * @since 2023-11-16
 */
@Service
public class BlogSortsServiceImpl extends ServiceImpl<BlogSortsMapper, BlogSorts> implements BlogSortsService {

    @Override
    public List<BlogSorts> listWithTree() {

        List<BlogSorts> blogSortsList = baseMapper.selectList(null);

        return blogSortsList.stream().filter(blogSorts -> blogSorts.getParentSortId() == 0)
                .peek(menu -> menu.setChildren(getChildrenForCurrentMenu(menu, blogSortsList)))
                .collect(Collectors.toList());
    }

    private List<BlogSorts> getChildrenForCurrentMenu(BlogSorts root, List<BlogSorts> all) {


        return all
                .stream()
                .filter(blogSorts -> blogSorts.getParentSortId().longValue() == root.getSortId().longValue())
                .peek(menu -> menu.setChildren(getChildrenForCurrentMenu(menu, all)))
                .collect(Collectors.toList());
    }



}
