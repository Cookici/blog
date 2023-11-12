package com.lrh.blog.chathandler.service;

import com.lrh.blog.common.entity.BlogUserFriends;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lrh.blog.common.entity.BlogUsers;
import com.lrh.blog.common.vo.BlogUserFriendsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lrh
 * @since 2023-11-09
 */
public interface BlogUserFriendsService extends IService<BlogUserFriends> {

    Integer addFriend(BlogUserFriendsVo blogUserFriendsVo);


    Integer judgeHaveFriend(Long userId, String friendName);

    Integer rejectApply(BlogUserFriendsVo blogUserFriendsVo);

    List<BlogUsers> getFriendList(Long userId);

    BlogUsers getFriend(Long friendId);
}
