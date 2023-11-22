package com.lrh.blog.article.service.impl;

import com.lrh.blog.common.entity.BlogCommentLike;
import com.lrh.blog.article.mapper.BlogCommentLikeMapper;
import com.lrh.blog.article.service.BlogCommentLikeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lrh.blog.common.vo.BlogCommentLikeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lrh
 * @since 2023-11-22
 */
@Service
public class BlogCommentLikeServiceImpl extends ServiceImpl<BlogCommentLikeMapper, BlogCommentLike> implements BlogCommentLikeService {


}
