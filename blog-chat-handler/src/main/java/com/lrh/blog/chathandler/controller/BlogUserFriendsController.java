package com.lrh.blog.chathandler.controller;


import com.lrh.blog.chathandler.utils.RedisUtils;
import com.lrh.blog.common.entity.BlogUserFriends;
import com.lrh.blog.chathandler.service.BlogUserFriendsService;
import com.lrh.blog.common.result.Result;
import com.lrh.blog.common.vo.BlogUserFriendsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lrh
 * @since 2023-11-09
 */
@RestController
@RequestMapping("/blog/chat")
public class BlogUserFriendsController {

    @Autowired
    private BlogUserFriendsService blogUserFriendsService;

    @Autowired
    private RedisUtils redisUtils;

    @PostMapping("/agree")
    public Result<Integer> agree(@RequestBody BlogUserFriendsVo blogUserFriendsVo) {
        blogUserFriendsService.addFriend(blogUserFriendsVo);
        redisUtils.hdel("add" + blogUserFriendsVo.getUserId(), blogUserFriendsVo.getFriendId());
        return Result.ok();
    }

}

