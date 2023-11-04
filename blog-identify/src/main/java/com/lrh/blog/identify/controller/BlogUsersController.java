package com.lrh.blog.identify.controller;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.lrh.blog.common.entity.BlogUsers;
import com.lrh.blog.common.result.Result;
import com.lrh.blog.identify.mapper.BlogUsersMapper;
import com.lrh.blog.identify.service.BlogUsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
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
    public Result<BlogUsers> getBlogUser(@PathVariable("username") String username) {
        BlogUsers blogUsers = blogUsersService.selectUserByUsername(username);
        blogUsers.setUserPassword(null);
        return Result.ok(blogUsers);
    }


    @GetMapping("/getByIds")
    public Result<List<BlogUsers>> getByIds(Long[] ids) {
        List<BlogUsers> blogUsers = blogUsersService.selectUsersByIds(Arrays.asList(ids));
        return Result.ok(blogUsers);
    }

    @PutMapping("/updateUserPhoto")
    public Result<Integer> updateUserPhoto(@RequestBody String message) throws UnsupportedEncodingException {

        String decode = URLDecoder.decode(message, String.valueOf(StandardCharsets.UTF_8));
        System.out.println(decode);
        String string = decode.substring(1, decode.length() - 1);
        String[] split = string.split("&");
        String id = split[0].substring(split[0].indexOf("=") + 1);
        String photoUrl = split[1].substring(split[1].indexOf("=") + 1);
        Integer i = blogUsersService.updateUrlById(Long.valueOf(id), photoUrl);
        return Result.ok(i);
    }


}

