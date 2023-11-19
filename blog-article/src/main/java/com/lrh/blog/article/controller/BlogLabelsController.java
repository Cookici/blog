package com.lrh.blog.article.controller;


import com.lrh.blog.article.service.BlogLabelsService;
import com.lrh.blog.common.entity.BlogLabels;
import com.lrh.blog.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lrh
 * @since 2023-11-16
 */
@RestController
@RequestMapping("/blog/label")
public class BlogLabelsController {

    @Autowired
    private BlogLabelsService blogLabelsService;

    @GetMapping("/getLabels")
    public Result<List<BlogLabels>> getLabelList(){
        List<BlogLabels> list = blogLabelsService.list();
        return Result.ok(list);
    }

}

