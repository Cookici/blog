package com.lrh.blog.identify.controller;


import com.lrh.blog.common.entity.BlogUsers;
import com.lrh.blog.common.result.Result;
import com.lrh.blog.identify.service.BlogUsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lrh
 * @since 2023-10-21
 */
@RestController
@RequestMapping("/blog/identify")
public class BlogUsersController {

    @Autowired
    private BlogUsersService blogUsersService;


    @PostMapping("/register")
    public Result<Integer> register(@RequestBody BlogUsers blogUsers, HttpServletRequest request) {
        blogUsers.setUserIp(request.getRemoteHost());
        LocalDate localDate = LocalDate.now();
        Integer age = Math.toIntExact(ChronoUnit.YEARS.between(blogUsers.getUserBirthday(), localDate));
        blogUsers.setUserPassword(new BCryptPasswordEncoder().encode(blogUsers.getUserPassword()));
        blogUsers.setUserAge(age);
        blogUsers.setUserRegistrationTime(LocalDateTime.now());
        blogUsers.setUserLevel(0);
        blogUsers.setUserAuthority("user");
        Integer i = blogUsersService.addBlogUsers(blogUsers);
        return Result.ok(i);
    }

    @GetMapping("/get/{username}")
    public Result<BlogUsers> getBlogUser(@PathVariable("username") String username){
        BlogUsers blogUsers = blogUsersService.selectUserByUsername(username);
        blogUsers.setUserPassword(null);
        return Result.ok(blogUsers);
    }


    @GetMapping("/getByIds")
    public Result<List<BlogUsers>> getByIds(Long[] ids){
        List<BlogUsers> blogUsers = blogUsersService.selectUsersByIds(Arrays.asList(ids));
        return Result.ok(blogUsers);
    }


}

