package com.lrh.blog.identify.service;

import com.lrh.blog.identify.entity.BlogUsers;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

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

}
