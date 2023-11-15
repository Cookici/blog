package com.lrh.blog.chathandler.controller;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lrh.blog.chathandler.service.BlogUsersServer;
import com.lrh.blog.chathandler.utils.RedisUtils;
import com.lrh.blog.common.domin.Message;
import com.lrh.blog.common.entity.BlogGroup;
import com.lrh.blog.chathandler.service.BlogGroupService;
import com.lrh.blog.common.entity.BlogUsers;
import com.lrh.blog.common.result.Result;
import com.lrh.blog.common.utils.RedisPrefixUtils;
import com.lrh.blog.common.vo.BlogGroupVo;
import com.lrh.blog.common.vo.BlogPhotoVo;
import com.lrh.blog.common.vo.GroupPhotosVo;
import com.lrh.blog.common.vo.NoReadGroupMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.chathandler.controller
 * @ClassName: BlogGroupController
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/14 20:14
 */
@Slf4j
@RestController
@RequestMapping("/blog/group")
public class BlogGroupController {

    @Autowired
    private BlogGroupService blogGroupService;

    @Autowired
    private BlogUsersServer blogUsersServer;

    @Autowired
    private RedisUtils redisUtils;


    @PostMapping("/create")
    public Result<Boolean> createGroup(@RequestBody BlogGroupVo blogGroupVo) {
        List<BlogGroup> judge = blogGroupService.list(new LambdaQueryWrapper<BlogGroup>().eq(BlogGroup::getUsersId, JSON.toJSONString(blogGroupVo.getIds())));
        if (!judge.isEmpty()) {
            return Result.ok(false);
        }
        List<BlogGroup> list = new ArrayList<>();
        int i = 0;
        for (Long id : blogGroupVo.getIds()) {
            list.add(new BlogGroup(null, blogGroupVo.getGroupId(), id, blogGroupVo.getGroupName(), JSON.toJSONString(blogGroupVo.getIds())));
            redisUtils.hset(RedisPrefixUtils.groupPrefix(String.valueOf(blogGroupVo.getGroupId())), RedisPrefixUtils.groupUserIdPrefix(String.valueOf(id)), i++);
        }
        boolean result = blogGroupService.saveBatch(list);
        return Result.ok(result);
    }

    @GetMapping("/getGroups/{userId}")
    public Result<Map<String, Object>> getGroupList(@PathVariable("userId") String userId) {
        List<BlogGroup> groups = blogGroupService.list(new LambdaQueryWrapper<BlogGroup>().eq(BlogGroup::getUserId, userId));
        List<BlogPhotoVo> photoUrl = new ArrayList<>();
        List<GroupPhotosVo> groupPhotosList = new ArrayList<>();
        for (BlogGroup group : groups) {
            Result<List<BlogUsers>> blogUsersList = blogUsersServer.getByIds(JSON.parseArray(group.getUsersId(), Long.class));
            for (BlogUsers blogUser : blogUsersList.getData()) {
                photoUrl.add(new BlogPhotoVo(String.valueOf(blogUser.getUserId()), blogUser.getUserProfilePhoto(), blogUser.getUserName()));
            }
            List<BlogPhotoVo> temp = new ArrayList<>(photoUrl);
            photoUrl.clear();
            groupPhotosList.add(new GroupPhotosVo(group, temp));
        }
        Map<String, Object> result = new HashMap<>();
        result.put("groups", groupPhotosList);
        result.put("groupsSize", groups.size());
        return Result.ok(result);
    }


    @GetMapping("/noReadGroupMessage/{userId}")
    public Result<Map<Object, Object>> noReadGroupMessage(@PathVariable("userId") String userId) {
        List<BlogGroup> groupList = blogGroupService.list(new LambdaQueryWrapper<BlogGroup>().eq(BlogGroup::getUserId, userId));
        Map<Object, Object> result = new HashMap<>();
        for (BlogGroup blogGroup : groupList) {
            int sizeForGroupNoReadMessage = 0;
            Object hget = redisUtils.hget(RedisPrefixUtils.groupPrefix(String.valueOf(blogGroup.getGroupId())), RedisPrefixUtils.groupUserIdPrefix(userId));
            Map<Object, Object> hmget = redisUtils.hmget(RedisPrefixUtils.groupMessagePrefix(String.valueOf(blogGroup.getGroupId())));
            for (Map.Entry<Object, Object> map : hmget.entrySet()) {
                if (!redisUtils.getBit(map.getKey(), Long.parseLong(String.valueOf(hget)))) {
                    sizeForGroupNoReadMessage++;
                }
            }
            result.put(String.valueOf(blogGroup.getGroupId()), (long) sizeForGroupNoReadMessage);
        }
        return Result.ok(result);
    }


}
