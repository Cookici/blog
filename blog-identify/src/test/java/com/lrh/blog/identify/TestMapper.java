package com.lrh.blog.identify;

import com.lrh.blog.identify.entity.BlogUsers;
import com.lrh.blog.identify.service.BlogUsersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.identify.users
 * @ClassName: TestMapper
 * @Author: 63283
 * @Description:
 * @Date: 2023/10/21 11:54
 */

@SpringBootTest
public class TestMapper {

    @Autowired
    private BlogUsersService blogUsersService;

    @Test
    public void getAllUser(){
        BlogUsers blogUsers = blogUsersService.selectUserByUsername("admin");
        System.out.println(blogUsers);
    }


}
