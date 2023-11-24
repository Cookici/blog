package com.lrh.blog.chathandler.controller;


import com.lrh.blog.chathandler.utils.JudgeRightUtils;
import com.lrh.blog.common.entity.BlogUserFriends;
import com.lrh.blog.chathandler.service.BlogUserFriendsService;
import com.lrh.blog.common.entity.BlogUsers;
import com.lrh.blog.common.result.Result;
import com.lrh.blog.common.result.ResultCodeEnum;
import com.lrh.blog.common.vo.BlogUserFriendsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    private JudgeRightUtils judgeRightUtils;

    @PostMapping("/agree/{userName}")
    public Result<Integer> agree(@RequestBody BlogUserFriendsVo blogUserFriendsVo, @PathVariable String userName, HttpServletRequest httpServletRequest) {
        if (!judgeRightUtils.judgeRight(userName, httpServletRequest)) {
            return Result.fail(0).message("没有权限").code(ResultCodeEnum.NO_RIGHT.getCode());
        }
        Integer i = blogUserFriendsService.addFriend(blogUserFriendsVo);
        return Result.ok(i);
    }

    @GetMapping("/{userId}/{friendName}")
    public Result<Integer> judgeHaveFriend(@PathVariable("userId") Long userId, @PathVariable("friendName") String friendName) {
        Integer judge = blogUserFriendsService.judgeHaveFriend(userId, friendName);
        return Result.ok(judge);
    }

    @PutMapping("/reject/{userName}")
    public Result<Integer> reject(@RequestBody BlogUserFriendsVo blogUserFriendsVo, @PathVariable String userName,HttpServletRequest httpServletRequest) {
        if (!judgeRightUtils.judgeRight(userName, httpServletRequest)) {
            return Result.fail(0).message("没有权限").code(ResultCodeEnum.NO_RIGHT.getCode());
        }
        Integer i = blogUserFriendsService.rejectApply(blogUserFriendsVo);
        return Result.ok(i);
    }

    @GetMapping("/friendList/{userId}")
    public Result<List<BlogUsers>> getFriendsByIds(@PathVariable("userId") Long userId) {
        List<BlogUsers> blogUsersList = blogUserFriendsService.getFriendList(userId);
        return Result.ok(blogUsersList);
    }

    @GetMapping("/getFriend/{friendId}")
    public Result<BlogUsers> getFriendById(@PathVariable("friendId") Long friendId) {
        BlogUsers blogUsers = blogUserFriendsService.getFriend(friendId);
        blogUsers.setUserPassword(null);
        blogUsers.setUserTelephoneNumber(null);
        return Result.ok(blogUsers);
    }

    @DeleteMapping("/delete/{userName}")
    public Result<Integer> deleteFriend(@RequestBody BlogUserFriendsVo blogUserFriendsVo, @PathVariable String userName, HttpServletRequest httpServletRequest) {
        if (!judgeRightUtils.judgeRight(userName, httpServletRequest)) {
            return Result.fail(0).message("没有权限").code(ResultCodeEnum.NO_RIGHT.getCode());
        }
        Integer i = blogUserFriendsService.deleteFriend(blogUserFriendsVo);
        return Result.ok(i);
    }


}

