package com.lrh.blog.identify.service.impl;


import com.lrh.blog.identify.entity.BlogUsers;
import com.lrh.blog.identify.entity.CustomUser;
import com.lrh.blog.identify.service.BlogUsersService;
import com.lrh.blog.identify.service.UserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ProjectName: SpringSecurity
 * @Package: com.lrh.springsecurity.service.impl
 * @ClassName: UserDetailServiceImpl
 * @Author: 63283
 * @Description:
 * @Date: 2023/4/24 11:17
 */

@Component
@Slf4j
public class UserDetailServiceImpl implements UserDetailService {

    @Autowired
    private BlogUsersService blogUsersService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BlogUsers blogUsers = blogUsersService.selectUserByUsername(username);
        if (blogUsers == null) {
            throw new UsernameNotFoundException("没有此用户");
        }
        List<GrantedAuthority> list = AuthorityUtils.commaSeparatedStringToAuthorityList(blogUsers.getUserAuthority());
        return new CustomUser(blogUsers,list);
    }



}
