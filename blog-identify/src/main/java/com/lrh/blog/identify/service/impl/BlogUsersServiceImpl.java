package com.lrh.blog.identify.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lrh.blog.common.entity.BlogUsers;
import com.lrh.blog.common.entity.BlogPhotos;
import com.lrh.blog.identify.mapper.BlogPhotosMapper;
import com.lrh.blog.identify.mapper.BlogUsersMapper;
import com.lrh.blog.identify.service.BlogUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lrh
 * @since 2023-10-21
 */
@Service
public class BlogUsersServiceImpl extends ServiceImpl<BlogUsersMapper, BlogUsers> implements BlogUsersService {

    @Autowired
    private BlogUsersMapper blogUsersMapper;

    @Autowired
    private BlogPhotosMapper blogPhotosMapper;

    @Override
    public BlogUsers selectUserByUsername(String username) {
        return blogUsersMapper.selectOne(new LambdaQueryWrapper<BlogUsers>().eq(BlogUsers::getUserName, username));
    }

    @Override
    public Integer addBlogUsers(BlogUsers blogUsers) {
        return blogUsersMapper.insert(blogUsers);
    }

    @Override
    public List<BlogUsers> selectUsersByIds(List<Long> ids) {
        return baseMapper.selectBatchIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateUrlById(Long id, String photoUrl) {
        Integer i = blogUsersMapper.updateUrlById(id, photoUrl);
        int insert = 0;
        if (i == 1) {
            insert = blogPhotosMapper.insert(new BlogPhotos(null, id, photoUrl));
        }
        return insert;
    }

    @Override
    public int updateBackPhoto(BlogPhotos blogPhotos) {
        BlogUsers blogUsers = baseMapper.selectOne(new LambdaQueryWrapper<BlogUsers>().eq(BlogUsers::getUserId, blogPhotos.getUserId()));
        blogUsers.setUserProfilePhoto(blogPhotos.getPhoto());
        int update = baseMapper.update(blogUsers, new LambdaQueryWrapper<BlogUsers>().eq(BlogUsers::getUserId, blogPhotos.getUserId()));

        return update;
    }


}
