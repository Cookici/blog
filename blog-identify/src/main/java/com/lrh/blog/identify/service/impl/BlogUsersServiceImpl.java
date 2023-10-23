package com.lrh.blog.identify.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lrh.blog.identify.entity.BlogUsers;
import com.lrh.blog.identify.mapper.BlogUsersMapper;
import com.lrh.blog.identify.service.BlogUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lrh
 * @since 2023-10-21
 */
@Service
public class BlogUsersServiceImpl extends ServiceImpl<BlogUsersMapper, BlogUsers> implements BlogUsersService {

    @Autowired
    private BlogUsersMapper blogUsersMapper;

    @Override
    public BlogUsers selectUserByUsername(String username) {
        return blogUsersMapper.selectOne(new LambdaQueryWrapper<BlogUsers>().eq(BlogUsers::getUserName, username));
    }




}
