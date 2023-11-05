package com.lrh.blog.identify.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lrh.blog.common.entity.BlogPhotos;
import com.lrh.blog.common.entity.BlogUsers;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lrh
 * @since 2023-10-21
 */
public interface BlogUsersService extends IService<BlogUsers> {

    /**
     * SpringSecurity登录获取User
     * @param username 用户名
     * @return 用户
     */
    BlogUsers selectUserByUsername(String username);

    Integer addBlogUsers(BlogUsers blogUsers);

    List<BlogUsers> selectUsersByIds(List<Long> ids);

    int updateUrlById(Long id, String photoUrl);

    int updateBackPhoto(BlogPhotos blogPhotos);
}
