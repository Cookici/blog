package com.lrh.blog.chathandler.controller;

import com.lrh.blog.chathandler.utils.RedisUtils;
import com.lrh.blog.common.domin.Message;
import com.lrh.blog.common.result.Result;
import com.lrh.blog.common.utils.RedisPrefix;
import com.lrh.blog.common.utils.RedisPrefixUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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
        if (apply != null) {
            Map<Object, Object> result = new HashMap<>();
            result.put("apply", apply);
            result.put("applySize", apply.size());
            return Result.ok(result);
        }
        return Result.ok();
    }

    @GetMapping("/getFriendSingleMessage/{userId}/{friendId}")
    public Result<List<Object>> getSingleMessage(@PathVariable("userId") String userId, @PathVariable("friendId") String friendId) {
        StringBuilder onLineString = RedisPrefixUtils.getStringBuilder(userId, friendId, RedisPrefix.SINGLE_CHAT_ONLINE);
        StringBuilder offLineString = RedisPrefixUtils.getStringBuilder(userId, friendId, RedisPrefix.SINGLE_CHAT_OFFLINE);
        List<Object> onLine = redisUtils.lGet(onLineString.toString(), 0, -1);
        List<Object> offLine = redisUtils.lGet(offLineString.toString(), 0, -1);
        onLine.addAll(offLine);
        for (Object object : offLine) {
            redisUtils.lSet(onLineString.toString(), object);
        }
        Map<Object, Object> result = new HashMap<>();
        result.put("allMessage", onLine);
        result.put("noReadSize", offLine.size());
        redisUtils.del(offLineString.toString());
        return Result.ok(onLine);
    }


}
