package com.lrh.blog.identify.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lrh.blog.common.result.Result;
import com.lrh.blog.common.entity.BlogPhotos;
import com.lrh.blog.identify.service.BlogPhotosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lrh
 * @since 2023-11-05
 */
@RestController
@RequestMapping("/blog/photo")
public class BlogPhotosController {


    @Autowired
    private BlogPhotosService blogPhotosService;


    @GetMapping("/getAllPhotos/{userId}")
    public Result<List<BlogPhotos>> getPhotoList(@PathVariable("userId") Long userId) {
        List<BlogPhotos> blogPhotos = blogPhotosService.getBaseMapper().selectList(new LambdaQueryWrapper<BlogPhotos>().eq(BlogPhotos::getUserId,userId));
        return Result.ok(blogPhotos);
    }


}

