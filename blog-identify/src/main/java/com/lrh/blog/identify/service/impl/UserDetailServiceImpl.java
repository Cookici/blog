package com.lrh.blog.identify.service.impl;


import com.lrh.blog.common.entity.BlogUsers;
import com.lrh.blog.identify.entity.CustomUser;
import com.lrh.blog.identify.service.BlogUsersService;
import com.lrh.blog.identify.service.UserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

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
    public Mono<UserDetails> findByUsername(String username) {
        BlogUsers blogUsers = blogUsersService.selectUserByUsername(username);
        if (blogUsers == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        List<GrantedAuthority> list = AuthorityUtils.commaSeparatedStringToAuthorityList(blogUsers.getUserAuthority());
        return Mono.just(new CustomUser(blogUsers,list));

    }
}
