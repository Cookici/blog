package com.lrh.blog.chathandler.controller;


import com.lrh.blog.common.entity.BlogUserFriends;
import com.lrh.blog.chathandler.service.BlogUserFriendsService;
import com.lrh.blog.common.result.Result;
import com.lrh.blog.common.vo.BlogUserFriendsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping("/agree")
    public Result<Integer> agree(@RequestBody BlogUserFriendsVo blogUserFriendsVo) {
        Integer i = blogUserFriendsService.addFriend(blogUserFriendsVo);
        return Result.ok(i);
    }

    @GetMapping("/{userId}/{friendName}")
    public Result<Integer> judgeHaveFriend(@PathVariable("userId") Long userId, @PathVariable("friendName") String friendName){
        Integer judge = blogUserFriendsService.judgeHaveFriend(userId, friendName);
        return Result.ok(judge);
    }

    @PutMapping("/reject")
    public Result<Integer> reject(@RequestBody BlogUserFriendsVo blogUserFriendsVo) {
        Integer i = blogUserFriendsService.rejectApply(blogUserFriendsVo);
        return Result.ok(i);
    }



    }

