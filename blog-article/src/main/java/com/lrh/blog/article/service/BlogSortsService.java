package com.lrh.blog.article.service;

import com.lrh.blog.common.entity.BlogSorts;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lrh
 * @since 2023-11-16
 */
public interface BlogSortsService extends IService<BlogSorts> {

    List<BlogSorts> listWithTree();

}
