package com.lrh.blog.chathandler.controller;

import com.lrh.blog.chathandler.utils.RedisUtils;
import com.lrh.blog.common.domin.Message;
import com.lrh.blog.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.chat.controller
 * @ClassName: RedisMessageController
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/9 10:35
 */
@RestController
@RequestMapping("/blog/redis")
public class RedisMessageController {

    @Autowired
    private RedisUtils redisUtils;

    @GetMapping("/get/apply/{userId}")
    public Result<Map<Object, Object>> getApply(@PathVariable Long userId) {
        Map<Object, Object> apply = redisUtils.hmget("add" + userId);
        if(apply != null) {
            Map<Object, Object> result = new HashMap<>();
            result.put("apply", apply);
            result.put("applySize", apply.size());
            return Result.ok(result);
        }
       return Result.ok();
    }




}
