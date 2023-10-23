package com.lrh.blog.identify.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.identify.service
 * @ClassName: UserDetailService
 * @Author: 63283
 * @Description:
 * @Date: 2023/10/21 12:40
 */

public interface UserDetailService extends UserDetailsService {
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

}
