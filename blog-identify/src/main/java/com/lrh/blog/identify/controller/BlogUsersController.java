package com.lrh.blog.identify.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lrh.blog.common.entity.BlogPhotos;
import com.lrh.blog.common.entity.BlogUsers;
import com.lrh.blog.common.result.Result;
import com.lrh.blog.common.vo.BlogPhotoVo;
import com.lrh.blog.identify.service.BlogUsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
        Duration duration = Duration.between(blogUsers.getUserRegistrationTime(), LocalDateTime.now());
        System.out.println(duration.toDays());
        long level = duration.toDays() / 10;
        blogUsers.setUserLevel(Math.toIntExact(level));
        blogUsersService.update(blogUsers, new LambdaQueryWrapper<BlogUsers>().eq(BlogUsers::getUserName, username));
        blogUsers.setUserPassword(null);
        return Result.ok(blogUsers);
    }

    @GetMapping("/get")
    public Result<BlogUsers> getByUserName(@RequestParam String username) {
        BlogUsers blogUsers = blogUsersService.getOne(new LambdaQueryWrapper<BlogUsers>().eq(BlogUsers::getUserName,username));
        return Result.ok(blogUsers);
    }

    @GetMapping("/getById")
    public Result<BlogUsers> getUserById(@RequestParam Long id) {
        BlogUsers blogUsers = blogUsersService.getById(id);
        return Result.ok(blogUsers);
    }


    @GetMapping("/getByIds")
    public Result<List<BlogUsers>> getByIds(@RequestParam("ids") List<Long> ids) {
        List<BlogUsers> blogUsers = blogUsersService.selectUsersByIds(ids);
        return Result.ok(blogUsers);
    }

    @GetMapping("/getByUserName/{userName}")
    public Result<BlogUsers> getById(@PathVariable String userName) {
        BlogUsers blogUsers = blogUsersService.getOne(new LambdaQueryWrapper<BlogUsers>().eq(BlogUsers::getUserName, userName));
        if (blogUsers != null) {
            return Result.ok(blogUsers);
        }
        return Result.fail();
    }

    @PutMapping("/updateUserPhoto")
    public Result<Integer> updateUserPhoto(@RequestBody BlogPhotoVo blogPhotoVo) {
        int i = blogUsersService.updateUrlById(Long.valueOf(blogPhotoVo.getId()), blogPhotoVo.getPhotoUrl());
        return Result.ok(i);
    }

    @PutMapping("/goBackPhoto")
    public Result<Integer> goBackPhoto(@RequestBody BlogPhotoVo blogPhotoVo) {
        BlogPhotos blogPhotos = new BlogPhotos(null, Long.valueOf(blogPhotoVo.getId()), blogPhotoVo.getPhotoUrl());
        int i = blogUsersService.updateBackPhoto(blogPhotos);
        return Result.ok(i);
    }


}

