package com.lrh.blog.chathandler.controller;

import com.lrh.blog.chathandler.service.BlogUserFriendsService;
import com.lrh.blog.chathandler.utils.RedisUtils;
import com.lrh.blog.common.domin.Message;
import com.lrh.blog.common.entity.BlogUsers;
import com.lrh.blog.common.result.Result;
import com.lrh.blog.common.utils.RedisPrefix;
import com.lrh.blog.common.utils.RedisPrefixUtils;
import com.lrh.blog.common.vo.BlogUserFriendsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @Autowired
    private BlogUserFriendsService blogUserFriendsService;

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
        redisUtils.del(offLineString.toString());
        return Result.ok(onLine);
    }

    @GetMapping("/getOffline/{userId}")
    public Result<Map<Long, Integer>> getOffline(@PathVariable("userId") String userId) {

        List<BlogUsers> friendList = blogUserFriendsService.getFriendList(Long.valueOf(userId));
        List<Long> friendListId = new ArrayList<>();
        for (BlogUsers friend : friendList) {
            friendListId.add(friend.getUserId());
        }
        Map<Long, Integer> result = new HashMap<>();

        for (Long id : friendListId) {
            StringBuilder offLineString = RedisPrefixUtils.getStringBuilder(userId, String.valueOf(id), RedisPrefix.SINGLE_CHAT_OFFLINE);
            List<Object> objects = redisUtils.lGet(offLineString.toString(), 0, -1);
            result.put(id, objects.size());
        }

        return Result.ok(result);
    }

    @GetMapping("/redPoint/add/{userId}/{friendId}")
    public Result<Map<Long, Integer>> addRedPoint(@PathVariable("userId") String userId, @PathVariable("friendId") String friendId) {
        StringBuilder stringBuilder = RedisPrefixUtils.getStringBuilder(userId, friendId, RedisPrefix.RED_POINT);
        if (redisUtils.hasKey(stringBuilder.toString())) {
            redisUtils.incr(stringBuilder.toString(), 1);
        } else {
            redisUtils.set(stringBuilder.toString(), 1);
        }

        List<BlogUsers> friendList = blogUserFriendsService.getFriendList(Long.valueOf(userId));
        List<Long> friendListId = new ArrayList<>();
        for (BlogUsers friend : friendList) {
            friendListId.add(friend.getUserId());
        }
        Map<Long, Integer> result = new HashMap<>();

        for (Long id : friendListId) {
            StringBuilder redPoint = RedisPrefixUtils.getStringBuilder(userId, String.valueOf(id), RedisPrefix.RED_POINT);
            Object object = redisUtils.get(redPoint.toString());
            result.put(id, (Integer) object);
        }
        return Result.ok(result);
    }

    @GetMapping("/redPoint/exit/{userId}")
    public Result<Map<Long,Integer>> exitRedPoint(@PathVariable("userId") String userId){
        List<BlogUsers> friendList = blogUserFriendsService.getFriendList(Long.valueOf(userId));
        List<Long> friendListId = new ArrayList<>();
        for (BlogUsers friend : friendList) {
            friendListId.add(friend.getUserId());
        }
        Map<Long, Integer> result = new HashMap<>();

        for (Long id : friendListId) {
            StringBuilder redPoint = RedisPrefixUtils.getStringBuilder(userId, String.valueOf(id), RedisPrefix.RED_POINT);
            Object object = redisUtils.get(redPoint.toString());
            result.put(id, (Integer) object);
        }
        return Result.ok(result);
    }

    @PutMapping("/redPoint/clear")
    public Result<String> clearRedPoint(@RequestBody BlogUserFriendsVo blogUserFriendsVo) {
        StringBuilder stringBuilder = RedisPrefixUtils.getStringBuilder(String.valueOf(blogUserFriendsVo.getUserId()), String.valueOf(blogUserFriendsVo.getFriendId()), RedisPrefix.RED_POINT);
        redisUtils.del(stringBuilder.toString());
        return Result.ok("clear");
    }


}
