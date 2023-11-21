package com.lrh.blog.article.service;

import com.lrh.blog.common.domin.BlogComments2;
import com.lrh.blog.common.entity.BlogComments;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lrh
 * @since 2023-11-21
 */

public interface BlogCommentsService extends IService<BlogComments> {

    List<BlogComments2> getCommentsTree(Long articleId);
}
