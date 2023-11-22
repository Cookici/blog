package com.lrh.blog.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lrh.blog.common.entity.BlogArticles;

import java.util.List;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.chat.utils
 * @ClassName: PageUtils
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/22 16:27
 */

public class PageUtils<T> {

    public IPage<T> getBlogArticlesIpage(Page<T> page, List<T> tList) {
        long allDateSize = tList.size();
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
        return page.setRecords(tList.subList((int) start, (int) end)).setTotal(tList.size()).setPages(maxPage);
    }
}
