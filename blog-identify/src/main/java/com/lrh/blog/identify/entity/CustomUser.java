package com.lrh.blog.identify.entity;

import com.lrh.blog.common.entity.BlogUsers;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.identify.entity
 * @ClassName: CustomUser
 * @Author: 63283
 * @Description:
 * @Date: 2023/10/21 12:36
 */

public class CustomUser extends User {

    private BlogUsers blogUsers;

    public CustomUser(BlogUsers blogUsers, Collection<? extends GrantedAuthority> authorities) {
        super(blogUsers.getUserName(), blogUsers.getUserPassword(), authorities);
        this.blogUsers = blogUsers;
    }

    public BlogUsers getBlogUser() {
        return blogUsers;
    }

    public void settUser(BlogUsers blogUsers) {
        this.blogUsers = blogUsers;
    }

}
