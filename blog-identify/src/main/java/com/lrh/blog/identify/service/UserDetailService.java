package com.lrh.blog.identify.service;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import reactor.core.publisher.Mono;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.identify.service
 * @ClassName: UserDetailService
 * @Author: 63283
 * @Description:
 * @Date: 2023/10/21 12:40
 */

public interface UserDetailService extends ReactiveUserDetailsService {
    @Override
    Mono<UserDetails> findByUsername(String username);
}
